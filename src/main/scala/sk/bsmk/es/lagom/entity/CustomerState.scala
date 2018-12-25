package sk.bsmk.es.lagom.entity

import play.api.libs.json.{Format, Json}

final case class CustomerState(points: Int, tier: Tier)

object CustomerState {
  implicit val format: Format[CustomerState] = Json.format

  val Init = CustomerState(0, Tier.None)
}
