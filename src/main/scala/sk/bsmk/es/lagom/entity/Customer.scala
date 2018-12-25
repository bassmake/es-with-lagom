package sk.bsmk.es.lagom.entity

import play.api.libs.json.{Format, Json}

final case class Customer(points: Int, tier: Tier)

object Customer {

  implicit val format: Format[Customer] = Json.format

  val Init = Customer(0, Tier.None)

}
