package sk.bsmk.es.lagom.api

import play.api.libs.json.{Format, Json}
import sk.bsmk.es.lagom.entity.Tier

final case class CustomerDetail(username: String, points: Int, tier: Tier)

object CustomerDetail {

  implicit val format: Format[CustomerDetail] = Json.format

}
