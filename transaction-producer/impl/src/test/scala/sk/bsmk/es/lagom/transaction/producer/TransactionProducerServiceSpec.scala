package sk.bsmk.es.lagom.transaction.producer

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, Matchers}

class TransactionProducerServiceSpec extends AsyncWordSpec with Matchers {

  "The Transaction Producer" should {

    "say hello" in ServiceTest.withServer(ServiceTest.defaultSetup) { ctx =>
      new TransactionProducerApplication(ctx) with LocalServiceLocator
    } { server =>
      val client = server.serviceClient.implement[TransactionProducerService]

      client.produceTransaction().invoke().map { response =>
        response.value should be > 0
      }
    }

  }

}
