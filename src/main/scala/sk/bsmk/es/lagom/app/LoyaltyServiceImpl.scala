package sk.bsmk.es.lagom.app

import java.time.Clock
import java.util.UUID

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.typesafe.scalalogging.LazyLogging
import sk.bsmk.es.lagom.api.{CustomerDetail, LoyaltyService}
import sk.bsmk.es.lagom.entity.{AddPointsFromTransaction, CustomerEntity, GetCustomerState, PointTransaction, Tier}
import sk.bsmk.es.lagom.external.{MonetaryTransaction, MonetaryTransactionService}

import scala.concurrent.Future
import scala.concurrent.duration._

class LoyaltyServiceImpl(
    entityRegistry: PersistentEntityRegistry,
    monetaryTransactionService: MonetaryTransactionService
) extends LoyaltyService
    with LazyLogging {

  private val clock = Clock.systemUTC()

  monetaryTransactionService
    .monetaryTransactionsTopic()
    .subscribe
    .atLeastOnce(Flow[MonetaryTransaction].mapAsync(1) { transaction =>
      logger.info(s"Received $transaction")

      val pointTransaction = PointTransaction(
        UUID.randomUUID(),
        transaction.customerId,
        (transaction.amount * 10).intValue(),
        clock.instant()
      )

      logger.info(s"Created $pointTransaction")

      val command = AddPointsFromTransaction(pointTransaction)
      entityRegistry
        .refFor[CustomerEntity](transaction.customerId)
        .ask(command)

    })

  override def customers: ServiceCall[NotUsed, Source[CustomerDetail, NotUsed]] = ServiceCall { _ =>
    val detail = CustomerDetail(
      "customer-one",
      100,
      Tier.None
    )
    Future.successful(
      Source
        .tick(1.second, 1.second, detail)
        .mapMaterializedValue(_ => NotUsed)
    )
  }

  override def customerDetail(customerId: String): ServiceCall[NotUsed, CustomerDetail] = ServiceCall { _ =>
    entityRegistry
      .refFor[CustomerEntity](customerId)
      .ask(GetCustomerState)

  }

}
