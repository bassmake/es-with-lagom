package sk.bsmk.es.lagom.transaction.producer

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.duration._
import scala.concurrent.Future

class TransactionProducerServiceImpl(
    entityRegistry: PersistentEntityRegistry
) extends TransactionProducerService {

  override def produceTransaction(): ServiceCall[NotUsed, TransactionData] =
    ServiceCall { _ =>
      Future.successful(TransactionData.random())
    }

  override def tickTransactions(
      intervalMs: Int): ServiceCall[NotUsed, Source[TransactionData, NotUsed]] =
    ServiceCall { _ =>
      Future.successful(
        Source
          .tick(
            intervalMs.milliseconds,
            intervalMs.milliseconds,
            TransactionData.random()
          )
          .mapMaterializedValue(_ => NotUsed))
    }

  override def transactionsTopic(): Topic[TransactionData] = {
    TopicProducer.singleStreamWithOffset { fromOffset =>
      entityRegistry
        .eventStream(TransactionProducerEvent.TransactionProducerEventTag,
                     fromOffset)
        .map(element =>
          element.event match {
            case TransactionProduced(data) => (data, element.offset)
        })
    }
  }

}
