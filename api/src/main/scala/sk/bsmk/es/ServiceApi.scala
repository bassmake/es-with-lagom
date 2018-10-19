package sk.bsmk.es

import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait ServiceApi extends Service {

  def receiveMessage(): ServiceCall[String, String]

  override def descriptor: Descriptor = {
    import Service._

    named("service")
      .withCalls(
        call(receiveMessage())
      ).withAutoAcl(true)


  }
}
