package sk.bsmk.es.lagom.transaction.producer

import com.lightbend.lagom.scaladsl.persistence.{
  AggregateEvent,
  AggregateEventTag,
  AggregateEventTagger
}
import play.api.libs.json.{Format, Json}

sealed trait TransactionProducerEvent
    extends AggregateEvent[TransactionProducerEvent] {
  override def aggregateTag: AggregateEventTagger[TransactionProducerEvent] =
    TransactionProducerEvent.TransactionProducerEventTag
}

object TransactionProducerEvent {
  val TransactionProducerEventTag: AggregateEventTag[TransactionProducerEvent] =
    AggregateEventTag[TransactionProducerEvent]
}

final case class TransactionProduced(transaction: TransactionData)
    extends TransactionProducerEvent

object TransactionProduced {
  implicit val format: Format[TransactionProduced] = Json.format
}
