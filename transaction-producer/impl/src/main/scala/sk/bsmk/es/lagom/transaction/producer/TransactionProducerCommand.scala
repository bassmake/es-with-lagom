package sk.bsmk.es.lagom.transaction.producer

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

sealed trait TransactionProducerCommand

final case class ProduceTransaction(transaction: TransactionData)
    extends TransactionProducerCommand
    with ReplyType[Done]

object ProduceTransaction {
  implicit val format: Format[ProduceTransaction] = Json.format
}
