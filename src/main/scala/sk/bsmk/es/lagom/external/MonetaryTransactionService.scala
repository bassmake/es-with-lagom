package sk.bsmk.es.lagom.external

import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service}

object MonetaryTransactionService {
  val MonetaryTransactionsTopic = "transactions"
}

trait MonetaryTransactionService extends Service {

  import MonetaryTransactionService._

  override def descriptor: Descriptor = {
    import Service._
    named("loyalty")
      .withTopics(
        topic(MonetaryTransactionsTopic, monetaryTransactionsTopic())
      )
      .withAutoAcl(true)
  }

  def monetaryTransactionsTopic(): Topic[MonetaryTransaction]

}
