package sk.bsmk.es.lagom.transaction.producer

import java.time.Instant
import java.util.UUID

import play.api.libs.json.{Format, Json}

final case class TransactionData(
    transactionId: UUID,
    customerId: UUID,
    value: Int,
    createdAt: Instant
)

object TransactionData {
  implicit val format: Format[TransactionData] = Json.format

  def random(): TransactionData = TransactionData(
    transactionId = UUID.randomUUID(),
    customerId = UUID.randomUUID(),
    value = (System.currentTimeMillis() % 100).intValue() + 20,
    createdAt = Instant.now()
  )

}
