package sk.bsmk.es.lagom.entity
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

import scala.collection.immutable.Seq

object CustomerSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[AddPointsFromTransaction],
    JsonSerializer[AddPointsFromTransaction],
    JsonSerializer[CustomerCreated],
    JsonSerializer[PointsAdded],
    JsonSerializer[TransactionPerformed],
    JsonSerializer[TierChanged]
  )
}
