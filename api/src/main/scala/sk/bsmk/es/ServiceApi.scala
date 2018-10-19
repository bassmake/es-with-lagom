package sk.bsmk.es

import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import sk.bsmk.es.dto.{CreateCustomerRequest, CustomerDetailResponse}

trait ServiceApi extends Service {

  def receiveMessage()
    : ServiceCall[CreateCustomerRequest, CustomerDetailResponse]

  override def descriptor: Descriptor = {
    import Service._

    named("service")
      .withCalls(
        call(receiveMessage())
      )
      .withAutoAcl(true)

  }

}
