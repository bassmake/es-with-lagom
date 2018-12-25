package sk.bsmk.es.lagom.entity

import play.api.libs.json.{Format, Json}

final case class CustomerDetail(name: String, points: Long, tier: Tier)

object CustomerDetail {

  implicit val format: Format[CustomerDetail] = Json.format

}
