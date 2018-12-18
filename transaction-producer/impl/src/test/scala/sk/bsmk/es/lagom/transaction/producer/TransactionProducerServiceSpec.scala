package sk.bsmk.es.lagom.transaction.producer

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.testkit.scaladsl.TestSink
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

import scala.concurrent.duration._

class TransactionProducerServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private lazy val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
  ) { ctx =>
    new TransactionProducerApplication(ctx) with LocalServiceLocator
  }

  private lazy val client = server.serviceClient.implement[TransactionProducerService]

  private implicit lazy val materializer: Materializer = server.materializer
  private implicit lazy val system: ActorSystem = server.actorSystem

  "The Transaction Producer" should {

    "return produced transaction" in {
      client.produceTransaction().invoke().map { response =>
        response.value should be > 0
      }
    }

    "tick transaction via stream" in {
      val interval = 100
      client.tickTransactions(interval).invoke().map { output =>
        val probe = output.runWith(TestSink.probe)
        probe.requestNext((2 * interval).milliseconds).value should be > 0
      }
    }

    "publish transactions" in {
      val source = client.transactionsTopic().subscribe.atMostOnceSource
      source.runWith(TestSink.probe)
          .request(1)
          .expectNext.value should be > 0
    }

  }

  override protected def beforeAll(): Unit = server

  override protected def afterAll(): Unit = server.stop()

}
