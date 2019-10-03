package sk.bsmk.es.lagom.entity

import play.api.libs.json.{Format, Json}

final case class CustomerState(customer: Customer, transactions: Seq[PointTransaction])

object CustomerState {

  implicit val format: Format[CustomerState] = Json.format

  val Init = CustomerState(Customer.Init, Seq.empty)

}
