package sk.bsmk.es.lagom.app

import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Flow, Source}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.typesafe.scalalogging.LazyLogging
import sk.bsmk.es.lagom.api.{CustomerDetail, LoyaltyService}
import sk.bsmk.es.lagom.entity.Tier
import sk.bsmk.es.lagom.external.{MonetaryTransaction, MonetaryTransactionService}

import scala.concurrent.Future
import scala.concurrent.duration._

class LoyaltyServiceImpl(monetaryTransactionService: MonetaryTransactionService)
    extends LoyaltyService
    with LazyLogging {

  monetaryTransactionService
    .monetaryTransactionsTopic()
    .subscribe
    .atLeastOnce(Flow[MonetaryTransaction].map { transaction =>
      logger.info(s"Received $transaction")
      Done
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

  override def customerDetail(id: String): ServiceCall[NotUsed, CustomerDetail] = ServiceCall { _ =>
    val detail = CustomerDetail(
      id,
      100,
      Tier.None
    )
    Future.successful(detail)
  }

}
