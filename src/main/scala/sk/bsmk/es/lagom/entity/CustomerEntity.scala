package sk.bsmk.es.lagom.entity

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class CustomerEntity extends PersistentEntity {

  override type Event = CustomerEvent
  override type Command = CustomerCommand[_]
  override type State = CustomerState
  override def initialState: CustomerState = CustomerState.Init

  override def behavior: Behavior = ???

}
