package sk.bsmk.es.lagom.sk.bsmk.es.lagom.app

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{Matchers, OptionValues, WordSpec}
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.time.{Millis, Seconds, Span}
import sk.bsmk.es.lagom.api.LoyaltyService
import sk.bsmk.es.lagom.app.LoyaltyApplication
import sk.bsmk.es.lagom.external.{MonetaryTransaction, MonetaryTransactionService, MonetaryTransactionServiceStub}

class LoyaltyApplicationSpec
    extends WordSpec
    with Matchers
    with Eventually
    with ScalaFutures
    with OptionValues
    with TypeCheckedTripleEquals {

  var monetaryTransactionsStub: ProducerStub[MonetaryTransaction] = _

  private val server: ServiceTest.TestServer[LoyaltyApplication with LocalServiceLocator] =
    ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra()) { ctx =>
      new LoyaltyApplication(ctx) with LocalServiceLocator {

        val stubFactory = new ProducerStubFactory(actorSystem, materializer)

        monetaryTransactionsStub =
          stubFactory.producer[MonetaryTransaction](MonetaryTransactionService.MonetaryTransactionsTopic)

        override val monetaryTransactionService: MonetaryTransactionService =
          new MonetaryTransactionServiceStub(monetaryTransactionsStub)
      }
    }

  private val client: LoyaltyService = server.serviceClient.implement[LoyaltyService]

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(
    timeout = Span(2, Seconds),
    interval = Span(150, Millis)
  )

  "The AnotherService" should {
    "publish updates on greetings message" in {

      val username = "Alice"

      monetaryTransactionsStub.send(
        MonetaryTransaction(
          username,
          3.4,
          "EUR"
        )
      )

      eventually {
        whenReady(client.customerDetail(username).invoke()) { detail =>
          detail.points should be(34)
        }
      }
    }
  }

}
