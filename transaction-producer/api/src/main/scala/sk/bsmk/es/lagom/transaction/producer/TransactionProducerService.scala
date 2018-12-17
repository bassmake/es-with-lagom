package sk.bsmk.es.lagom.transaction.producer

import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{
  KafkaProperties,
  PartitionKeyStrategy
}
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service}

object TransactionProducerService {
  val TOPIC_NAME = "transactions"
}

trait TransactionProducerService extends Service {
  override final def descriptor: Descriptor = {
    import Service._

    named("transaction-producer")
      .withTopics(
        topic(TransactionProducerService.TOPIC_NAME, transactionsTopic())
          .addProperty(
            KafkaProperties.partitionKeyStrategy,
            PartitionKeyStrategy[TransactionMessage](_.customerId.toString)
          )
      )
  }

  def transactionsTopic(): Topic[TransactionMessage]

}
