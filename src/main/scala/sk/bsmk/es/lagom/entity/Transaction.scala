package sk.bsmk.es.lagom.entity

import java.time.Instant
import java.util.UUID

import play.api.libs.json.{Format, Json}

final case class Transaction(
    transactionId: UUID,
    customerId: String,
    value: Int,
    createdAt: Instant
)

object Transaction {
  implicit val format: Format[Transaction] = Json.format
}
