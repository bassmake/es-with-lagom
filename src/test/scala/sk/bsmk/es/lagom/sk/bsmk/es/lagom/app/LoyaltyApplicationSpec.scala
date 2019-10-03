package sk.bsmk.es.lagom.sk.bsmk.es.lagom.app

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{Matchers, OptionValues, WordSpec}
import org.scalatest.concurrent.{Eventually, ScalaFutures}
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

  "The AnotherService" should {
    "publish updates on greetings message" in ServiceTest.withServer(ServiceTest.defaultSetup.withCassandra()) { ctx =>
      new LoyaltyApplication(ctx) with LocalServiceLocator {

        val stubFactory = new ProducerStubFactory(actorSystem, materializer)

        monetaryTransactionsStub =
          stubFactory.producer[MonetaryTransaction](MonetaryTransactionService.MonetaryTransactionsTopic)

        override val monetaryTransactionService: MonetaryTransactionService =
          new MonetaryTransactionServiceStub(monetaryTransactionsStub)
      }
    } { server =>
      monetaryTransactionsStub.send(
        MonetaryTransaction(
          "someone",
          3.4,
          "EUR"
        ))

      eventually {
        1 should ===(2)
      }

    }
  }

}
