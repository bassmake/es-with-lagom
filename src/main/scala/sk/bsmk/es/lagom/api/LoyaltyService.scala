package sk.bsmk.es.lagom.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait LoyaltyService extends Service {
  override def descriptor: Descriptor = {
    import Service._
    named("loyalty")
      .withCalls(
        pathCall("/customers/:id", customerDetail _),
        pathCall("/customer-changes", customers)
      )
      .withAutoAcl(true)
  }

  def customers: ServiceCall[NotUsed, Source[CustomerDetail, NotUsed]]

  def customerDetail(customerId: String): ServiceCall[NotUsed, CustomerDetail]

}
