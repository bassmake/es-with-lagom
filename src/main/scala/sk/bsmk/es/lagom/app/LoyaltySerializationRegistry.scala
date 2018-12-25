package sk.bsmk.es.lagom.app

import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry

object LoyaltySerializationRegistry extends JsonSerializerRegistry {
  override def serializers: List[Nothing] = List.empty
}
