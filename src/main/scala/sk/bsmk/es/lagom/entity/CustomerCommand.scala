package sk.bsmk.es.lagom.entity

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import play.api.libs.json.{Format, Json}
import sk.bsmk.es.lagom.api.CustomerDetail

sealed trait CustomerCommand[R] extends ReplyType[R]

final case class AddPointsFromTransaction(transaction: Transaction) extends CustomerCommand[Done]

object AddPointsFromTransaction {
  implicit val format: Format[AddPointsFromTransaction] = Json.format
}

case object GetDetail extends CustomerCommand[CustomerDetail] {
  implicit val format: Format[GetDetail.type] =
    JsonSerializer.emptySingletonFormat(GetDetail)
}
