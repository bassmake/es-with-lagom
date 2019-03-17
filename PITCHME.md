# Event Sourcing and CQRS with Lagom

---

### Event sourcing

- Every change is stored as an event
- Command is applied and Events are published
- Easy to create new read models (projections)
- Data for audit out of the box
- Easier to reason about in complex systems
- Higher complexity compared to CRUD in init phases
- very similar to version control systems

+++

TODO: Some nice ES diagram with core components

![Event sourcing simplified](https://g.gravizo.com/svg?
    @startuml
    Command -> Entity: ask to do something
    Entity -> Entity: validates, creates events\n(not stored yet)
    Entity -> Journal: stores events
    Entity -> Command: response
    Entity -> Journal: ask for not applied events
    Entity -> Entity: events are applied and state is changed
    @enduml
)

---

## Lagom

- Open source framework for building reactive microservice systems in Java or Scala. |
- BUT |
- It has also nice persistence model based on ES and CQRS |

---

### Events

- Describe what happened in the system
- Persisted
- Changing state
- No validations when applying
- Cannot be changed!
- System must be able to load all events from the beginning (migrations)
- Snapshots - state persisted at some time for efficiency

+++

### Events in Lagom

```scala
sealed trait CustomerEvent extends AggregateEvent[CustomerEvent] {
  override def aggregateTag: AggregateEventTagger[CustomerEvent] 
    = CustomerEvent.Tag
}

object CustomerEvent {
  val NumShards = 20
  val Tag: AggregateEventShards[CustomerEvent]
    = AggregateEventTag.sharded[CustomerEvent](NumShards)
}
```
@[1](extend `AggregateEvent` trait)
@[2,7](aggregateTag)
@[6](number of shards)

---

### Commands

- What should be done 
- Input to the system
- Validations are applied while processing
- After success events are stored
- State is not changed!

+++

### Commands in Lagom 

```scala
sealed trait CustomerCommand[R] extends ReplyType[R]
```

@[1](extend `ReplyType` trait where R specifies Reply)

---

### Entity and state

- 

+++

### Entity in Lagom

```scala
class CustomerEntity extends PersistentEntity {
  override type Event = CustomerEvent
  override type Command = CustomerCommand[_]
  override type State = CustomerState
  override def initialState: CustomerState = CustomerState.Init
```

@[1](extend `PersistentEntity` trait)
@[2](define type of events)
@[3](define type of commands)
@[4](define type of state)
@[5](initial state, could be Option if needed)

+++

### Behaviour in entity

```scala

```

---

### Services

---

### Processor

---

### Projections