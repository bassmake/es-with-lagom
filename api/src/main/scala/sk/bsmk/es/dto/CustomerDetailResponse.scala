package sk.bsmk.es.dto

import play.api.libs.json.{Format, Json}

final case class CustomerDetailResponse(name: String)

object CustomerDetailResponse {

  implicit val format: Format[CustomerDetailResponse] = Json.format

}
