package sk.bsmk.es.lagom.transaction.producer

import java.util.UUID

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class TransactionProducerEntity extends PersistentEntity {

  override type Command = TransactionProducerCommand
  override type Event = TransactionProducerEvent
  override type State = Map[UUID, TransactionData]

  override def initialState: Map[UUID, TransactionData] = Map.empty

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
