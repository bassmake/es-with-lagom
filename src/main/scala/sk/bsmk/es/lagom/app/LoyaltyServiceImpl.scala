package sk.bsmk.es.lagom.app

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.ServiceCall
import sk.bsmk.es.lagom.api.CustomerDetail
import sk.bsmk.es.lagom.entity.Tier

import scala.concurrent.Future
import scala.concurrent.duration._

class LoyaltyServiceImpl extends LoyaltyService {

  override def customers: ServiceCall[NotUsed, Source[CustomerDetail, NotUsed]] = ServiceCall { _ =>
    val detail = CustomerDetail(
      "customer-one",
      100,
      Tier.None
    )
    Future.successful(
      Source
        .tick(1.second, 1.second, detail)
        .mapMaterializedValue(_ => NotUsed)
    )
  }

  override def customerDetail(id: String): ServiceCall[NotUsed, CustomerDetail] = ServiceCall { _ =>
    val detail = CustomerDetail(
      id,
      100,
      Tier.None
    )
    Future.successful(detail)
  }

}
