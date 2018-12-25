package sk.bsmk.es.lagom.entity

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import sk.bsmk.es.lagom.api.CustomerDetail

class CustomerEntity extends PersistentEntity {

  override type Event = CustomerEvent
  override type Command = CustomerCommand[_]
  override type State = CustomerState
  override def initialState: CustomerState = CustomerState.Init

  override def behavior: Behavior =
    Actions()
      .onEvent {
        case (FirstTransactionReceived(_), state) =>
          state
        case (PointsAdded(pointsAdded, transaction), state) =>
          state.copy(
            customer = state.customer.copy(
              points = state.customer.points + pointsAdded
            ),
            transactions = state.transactions :+ transaction
          )
        case (TierChanged(newTier), state) =>
          state.copy(customer = state.customer.copy(tier = newTier))
      }
      .onReadOnlyCommand[GetDetail.type, CustomerDetail] {
        case (GetDetail, ctx, state) => ctx.reply(detail(state))
      }
      .onCommand[ProcessTransaction, Done] {
        case (ProcessTransaction(transaction), ctx, state) =>
          val firsTransactionEvent =
            state.transactions.headOption.fold(Option(FirstTransactionReceived(transaction.createdAt)))(_ =>
              Option.empty[FirstTransactionReceived])

          val pointsAdded = Some(PointsAdded(transaction.value, transaction))
          val tierChanged =
            Tier.compute(state.customer.points + transaction.value) match {
              case state.customer.tier => None
              case newTier @ _         => Some(TierChanged(newTier))
            }

          val events = Seq(firsTransactionEvent, pointsAdded, tierChanged).flatten
          ctx.thenPersistAll(events: _*)(() => ctx.reply(Done))
      }
      .onCommand[SetTier, CustomerDetail] {
        case (SetTier(newTier), ctx, state) if newTier == state.customer.tier =>
          ctx.commandFailed(new IllegalArgumentException("Tier already set"))
          ctx.done
        case (SetTier(newTier), ctx, state) =>
          val event = TierChanged(newTier)
          ctx.thenPersist(event) { _ =>
            ctx.reply(detail(state))
          }
      }

  private def detail(state: State) = CustomerDetail(
    name = entityId,
    points = state.customer.points,
    tier = state.customer.tier
  )

}
