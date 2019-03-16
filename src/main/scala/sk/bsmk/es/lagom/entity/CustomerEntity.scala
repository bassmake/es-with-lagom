package sk.bsmk.es.lagom.entity

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.typesafe.scalalogging.LazyLogging
import sk.bsmk.es.lagom.api.CustomerDetail

class CustomerEntity extends PersistentEntity with LazyLogging {

  override type Event = CustomerEvent
  override type Command = CustomerCommand[_]
  override type State = CustomerState
  override def initialState: CustomerState = CustomerState.Init

  override def behavior: Behavior =
    Actions()
      .onEvent {
        case (CustomerCreated(_), state) =>
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
      .onCommand[AddPointsFromTransaction, Done] {
        case (AddPointsFromTransaction(transaction), ctx, state) =>
          val firsTransactionEvent =
            state.transactions.headOption.fold(Option(CustomerCreated(transaction.createdAt)))(_ =>
              Option.empty[CustomerCreated])

          val pointsAdded = Some(PointsAdded(transaction.value, transaction))
          val tierChanged =
            Tier.compute(state.customer.points + transaction.value) match {
              case state.customer.tier => None
              case newTier @ _         => Some(TierChanged(newTier))
            }

          val events = Seq(firsTransactionEvent, pointsAdded, tierChanged).toList.flatten

          logger.info(s"Events to store: $events")

          ctx.thenPersistAll(events: _*)(() => ctx.reply(Done))
      }

  private def detail(state: State) = CustomerDetail(
    username = entityId,
    points = state.customer.points,
    tier = state.customer.tier
  )

}
