package sk.bsmk.es.lagom.external

import play.api.libs.json.{Format, Json}

final case class MonetaryTransaction(
    username: String,
    amount: BigDecimal,
    currency: String
)

object MonetaryTransaction {
  implicit val format: Format[MonetaryTransaction] = Json.format
}
