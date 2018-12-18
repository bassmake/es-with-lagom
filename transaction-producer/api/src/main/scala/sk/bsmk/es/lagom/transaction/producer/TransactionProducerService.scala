package sk.bsmk.es.lagom.transaction.producer

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{
  KafkaProperties,
  PartitionKeyStrategy
}
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

object TransactionProducerService {
  val TOPIC_NAME = "transactions"
}

trait TransactionProducerService extends Service {
  override final def descriptor: Descriptor = {
    import Service._

    named("transaction-producer")
      .withCalls(
        pathCall("/random-transaction", produceTransaction _),
        pathCall("/tick-transactions", tickTransactions _)
      )
      .withTopics(
        topic(TransactionProducerService.TOPIC_NAME, transactionsTopic())
          .addProperty(
            KafkaProperties.partitionKeyStrategy,
            PartitionKeyStrategy[TransactionData](_.customerId.toString)
          )
      )
      .withAutoAcl(true)
  }

  def produceTransaction(): ServiceCall[NotUsed, TransactionData]

  def tickTransactions(
      interval: Int): ServiceCall[NotUsed, Source[TransactionData, NotUsed]]

  def transactionsTopic(): Topic[TransactionData]

}
