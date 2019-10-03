package sk.bsmk.es.lagom.app

import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import sk.bsmk.es.lagom.api.LoyaltyService

class LoyaltyApplicationLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LoyaltyApplication(context) with LagomDevModeComponents

  override def describeService: Option[Descriptor] =
    Some(readDescriptor[LoyaltyService])
}
