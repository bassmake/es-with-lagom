package sk.bsmk.es.lagom.transaction.producer

import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.Environment
import play.api.libs.ws.ahc.AhcWSComponents

import scala.concurrent.ExecutionContext

trait TransactionProducerComponents
    extends LagomServerComponents
    with CassandraPersistenceComponents {

  override implicit def executionContext: ExecutionContext
  override implicit def materializer: Materializer

  override def environment: Environment

  override lazy val lagomServer: LagomServer =
    serverFor[TransactionProducerService](wire[TransactionProducerServiceImpl])

  override lazy val jsonSerializerRegistry
    : TransactionProducerSerializationRegistry.type =
    TransactionProducerSerializationRegistry

}

abstract class TransactionProducerApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with TransactionProducerComponents
    with AhcWSComponents
    with LagomKafkaComponents {}

class TransactionProducerApplicationLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TransactionProducerApplication(context) with LagomDevModeComponents
}
