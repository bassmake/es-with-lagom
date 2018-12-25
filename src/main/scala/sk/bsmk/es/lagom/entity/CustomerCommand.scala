package sk.bsmk.es.lagom.entity

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import play.api.libs.json.{Format, Json}
import sk.bsmk.es.lagom.api.CustomerDetail
import sk.bsmk.es.lagom.producer.Transaction

sealed trait CustomerCommand[R] extends ReplyType[R]

final case class ProcessTransaction(transaction: Transaction)
    extends CustomerCommand[Done]

object ProcessTransaction {
  implicit val format: Format[ProcessTransaction] = Json.format
}

final case class SetTier(newTier: Tier) extends CustomerCommand[CustomerDetail]

object SetTier {
  implicit val format: Format[SetTier] = Json.format
}

case object GetDetail extends CustomerCommand[CustomerDetail] {
  implicit val format: Format[GetDetail.type] =
    JsonSerializer.emptySingletonFormat(GetDetail)
}
