package sk.bsmk.es.lagom.entity

import java.time.Instant
import java.util.UUID

import play.api.libs.json.{Format, Json}

final case class PointTransaction(
    transactionId: UUID,
    customerId: String,
    value: Int,
    createdAt: Instant
)

object PointTransaction {
  implicit val format: Format[PointTransaction] = Json.format
}
