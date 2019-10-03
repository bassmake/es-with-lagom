package sk.bsmk.es.lagom.entity

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import org.scalatest.{BeforeAndAfterAll, Matchers, OptionValues, WordSpec}
import sk.bsmk.es.lagom.app.LoyaltySerializationRegistry

abstract class CustomerEntitySpec extends WordSpec with Matchers with BeforeAndAfterAll with OptionValues {

  private val system = ActorSystem("test", JsonSerializerRegistry.actorSystemSetupFor(LoyaltySerializationRegistry))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val customerName = "customer-zero"

  private def withDriver[T](
      block: PersistentEntityTestDriver[CustomerCommand[_], CustomerEvent, CustomerState] => T
  ): T = {
    val driver = new PersistentEntityTestDriver(system, new CustomerEntity, customerName)
    try {
      block(driver)
    } finally {
      driver.getAllIssues shouldBe empty
      ()
    }
  }

}
