package sk.bsmk.es.lagom.transaction.producer

import java.util.UUID

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

class TransactionProducerEntity extends PersistentEntity {
  override type Command = TransactionMessageProducerCommand
  override type Event = TransactionMessageProducerEvent
  override type State = Map[UUID, TransactionMessage]

  override def initialState: Map[UUID, TransactionMessage] = Map.empty

  override def behavior: Behavior =
    Actions()
      .onEvent {
        case (TransactionProduced(trx), state) =>
          state + (trx.transactionId -> trx)
      }
      .onCommand[ProduceTransaction, Done] {
        case (ProduceTransaction(trx), ctx, state) =>
          if (state.contains(trx.transactionId)) {
            ctx.commandFailed(
              new IllegalArgumentException(
                s"Transaction with id=${trx.transactionId} already produced"))
            ctx.done
          } else {
            ctx.thenPersist(TransactionProduced(trx))(_ => ctx.reply(Done))
          }
      }
}

trait TransactionMessageProducerEvent

final case class TransactionProduced(transaction: TransactionMessage)
    extends TransactionMessageProducerEvent

object TransactionProduced {
  implicit val format: Format[TransactionProduced] = Json.format
}

trait TransactionMessageProducerCommand

final case class ProduceTransaction(transaction: TransactionMessage)
    extends TransactionMessageProducerCommand
    with ReplyType[Done]

object ProduceTransaction {
  implicit val format: Format[ProduceTransaction] = Json.format
}
