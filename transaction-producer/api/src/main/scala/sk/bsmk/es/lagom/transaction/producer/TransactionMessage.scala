package sk.bsmk.es.lagom.transaction.producer

import java.time.Instant
import java.util.UUID

import play.api.libs.json.{Format, Json}

final case class TransactionMessage(
    transactionId: UUID,
    groupId: UUID,
    customerId: UUID,
    value: TransactionValue,
    createdAt: Instant
)

object TransactionMessage {
  implicit val format: Format[TransactionMessage] = Json.format
}
