package sk.bsmk.es.lagom.app
import java.time.Instant
import java.util.UUID

import akka.NotUsed
import akka.actor.Cancellable
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.scalalogging.LazyLogging
import sk.bsmk.es.lagom.entity.{AddPointsFromTransaction, CustomerEntity, Transaction}

import scala.concurrent.duration._
import scala.util.Random
import TransactionProcessing.producer
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

class TransactionProcessing(entityRegistry: PersistentEntityRegistry)(implicit val mat: Materializer)
    extends LazyLogging {

  producer
    .map(trx => {
      logger.info(s"Processing $trx")

      val command = AddPointsFromTransaction(trx)
      entityRegistry
        .refFor[CustomerEntity](trx.customerId)
        .ask(command)
    })
    .runWith(Sink.ignore)

}

object TransactionProcessing {
  private val producer: Source[Transaction, Cancellable] = Source
    .tick(0.seconds, 1.second, NotUsed)
    .map(_ => generateTransaction())

  private def generateTransaction(): Transaction =
    Transaction(
      UUID.randomUUID(),
      chooseUsername(),
      transactionValue(),
      Instant.now()
    )

  private def chooseUsername(): String = {
    usernames(Random.nextInt(usernames.size))
  }

  private val usernames = List("Alice")
  private def transactionValue(): Int = 10

//  private val usernames = List("Alice", "Bob", "Charlie", "Dave")
//  private def transactionValue(): Int = Random.nextInt(10) + 10

}
