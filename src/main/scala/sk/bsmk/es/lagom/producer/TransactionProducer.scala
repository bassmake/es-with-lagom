package sk.bsmk.es.lagom.producer

import java.time.Instant
import java.util.UUID

import akka.NotUsed
import akka.actor.Cancellable
import akka.stream.scaladsl.Source

import scala.concurrent.duration._

object TransactionProducer {

  val producer: Source[Transaction, Cancellable] = Source
    .tick(1.second, 1.second, NotUsed)
    .map(_ => generateTransaction())

  private def generateTransaction(): Transaction =
    Transaction(
      UUID.randomUUID(),
      "Alice",
      100,
      Instant.now()
    )

}
