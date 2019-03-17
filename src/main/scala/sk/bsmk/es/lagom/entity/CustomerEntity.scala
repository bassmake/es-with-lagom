package sk.bsmk.es.lagom.entity

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.typesafe.scalalogging.LazyLogging
import sk.bsmk.es.lagom.api.CustomerDetail
import scala.collection.mutable

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
        case (PointsAdded(pointsAdded), state) =>
          state.copy(
            customer = state.customer.copy(
              points = state.customer.points + pointsAdded
            )
          )
        case (TransactionPerformed(transaction), state) =>
          state.copy(
            transactions = state.transactions :+ transaction
          )
        case (TierChanged(newTier), state) =>
          state.copy(customer = state.customer.copy(tier = newTier))
      }
      .onCommand[AddPointsFromTransaction, Done] {



        case (AddPointsFromTransaction(transaction), ctx, state) =>
          val events = mutable.Buffer[CustomerEvent]()

          if (state.transactions.isEmpty) {
            val event = CustomerCreated(transaction.createdAt)
            events  += event
          }

          events += PointsAdded(transaction.value)
          events += TransactionPerformed(transaction)

          val newTier = Tier.compute(state.customer.points + transaction.value)
          if (newTier != state.customer.tier) {
            events += TierChanged(newTier)
          }

          logger.info(s"Events to store: $events")

          ctx.thenPersistAll(events: _*)(() => ctx.reply(Done))
      }
      .onReadOnlyCommand[GetDetail.type, CustomerDetail] {
        case (GetDetail, ctx, state) => ctx.reply(detail(state))
      }

  private def detail(state: State) = CustomerDetail(
    username = entityId,
    points = state.customer.points,
    tier = state.customer.tier
  )

}
