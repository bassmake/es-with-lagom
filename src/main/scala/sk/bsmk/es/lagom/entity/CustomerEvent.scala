package sk.bsmk.es.lagom.entity

import com.lightbend.lagom.scaladsl.persistence.{
  AggregateEvent,
  AggregateEventShards,
  AggregateEventTag,
  AggregateEventTagger
}
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import play.api.libs.json.{Format, Json}

sealed trait CustomerEvent extends AggregateEvent[CustomerEvent] {
  override def aggregateTag: AggregateEventTagger[CustomerEvent] =
    CustomerEvent.Tag
}

object CustomerEvent {
  val NumShards = 20
  val Tag: AggregateEventShards[CustomerEvent] =
    AggregateEventTag.sharded[CustomerEvent](NumShards)
}

case object CustomerCreated extends CustomerEvent {
  implicit val format: Format[CustomerCreated.type] =
    JsonSerializer.emptySingletonFormat(CustomerCreated)
}

final case class PointsAdded(added: Int) extends CustomerEvent

object PointsAdded {
  implicit val format: Format[PointsAdded] = Json.format
}

final case class TierChanged(newTier: Tier) extends CustomerEvent

object TierChanged {
  implicit val format: Format[TierChanged] = Json.format
}
