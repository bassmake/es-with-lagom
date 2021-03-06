package sk.bsmk.es.lagom.app

import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaClientComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomServer}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents
import sk.bsmk.es.lagom.api.LoyaltyService
import sk.bsmk.es.lagom.entity.{CustomerEntity, CustomerSerializerRegistry}
import sk.bsmk.es.lagom.external.MonetaryTransactionService

abstract class LoyaltyApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with AhcWSComponents
    with LagomKafkaClientComponents
    with CassandraPersistenceComponents {

  override lazy val lagomServer: LagomServer =
    serverFor[LoyaltyService](wire[LoyaltyServiceImpl])

  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = CustomerSerializerRegistry

  persistentEntityRegistry.register(wire[CustomerEntity])

  val monetaryTransactionService: MonetaryTransactionService = serviceClient.implement[MonetaryTransactionService]

}
