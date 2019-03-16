package sk.bsmk.es.lagom.entity

import java.time.Instant

import com.lightbend.lagom.scaladsl.persistence.{
  AggregateEvent,
  AggregateEventShards,
  AggregateEventTag,
  AggregateEventTagger
}
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

final case class CustomerCreated(createdAt: Instant) extends CustomerEvent

object CustomerCreated {
  implicit val format: Format[CustomerCreated] = Json.format
}

final case class PointsAdded(added: Int, transaction: Transaction) extends CustomerEvent

object PointsAdded {
  implicit val format: Format[PointsAdded] = Json.format
}

final case class TierChanged(newTier: Tier) extends CustomerEvent

object TierChanged {
  implicit val format: Format[TierChanged] = Json.format
}
