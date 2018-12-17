package sk.bsmk.es.lagom.transaction.producer

import com.lightbend.lagom.scaladsl.playjson.{
  JsonSerializer,
  JsonSerializerRegistry
}

object TransactionProducerSerializationRegistry extends JsonSerializerRegistry {

  override def serializers = List(
    JsonSerializer[TransactionProduced],
    JsonSerializer[ProduceTransaction]
  )

}
