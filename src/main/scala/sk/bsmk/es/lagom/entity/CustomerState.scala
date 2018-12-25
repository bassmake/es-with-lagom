package sk.bsmk.es.lagom.entity

import play.api.libs.json.{Format, Json}
import sk.bsmk.es.lagom.producer.Transaction

final case class CustomerState(customer: Customer,
                               transactions: Seq[Transaction])

object CustomerState {

  implicit val format: Format[CustomerState] = Json.format

  val Init = CustomerState(Customer.Init, Seq.empty)

}
