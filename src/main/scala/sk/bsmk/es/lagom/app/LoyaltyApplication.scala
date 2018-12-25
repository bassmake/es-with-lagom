package sk.bsmk.es.lagom.app

import com.lightbend.lagom.scaladsl.server.{
  LagomApplication,
  LagomApplicationContext,
  LagomServer
}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

abstract class LoyaltyApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with AhcWSComponents {

  override lazy val lagomServer: LagomServer =
    serverFor[LoyaltyService](wire[LoyaltyServiceImpl])

}
