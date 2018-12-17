package sk.bsmk.es.lagom.transaction.producer

import play.api.libs.json.{Format, Json}

final case class TransactionValue(value: Double, currency: String)

object TransactionValue {
  implicit val format: Format[TransactionValue] = Json.format
}
