package sk.bsmk.es.dto

import play.api.libs.json.{Format, Json}

final case class CreateCustomerRequest(name: String)

object CreateCustomerRequest {

  implicit val format: Format[CreateCustomerRequest] = Json.format

}
