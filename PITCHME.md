# Event Sourcing and CQRS with Lagom

---

### Event sourcing

- Every change is stored as an event
- Command is applied and Events are published
- Easy to create new read models (projections)
- Data for audit out of the box
- Easier to reason about in complex systems
- Higher complexity compared to CRUD in init phases

+++

TODO: Some nice ES diagram with core components

---

### CQRS

- Command Query Responsibility Segregation

+++

![cqrs](https://martinfowler.com/bliki/images/cqrs/cqrs.png)

---

## Lagom

- Open source framework for building reactive microservice systems in Java or Scala. |
- BUT |
- It has also nice persistence model based on ES and CQRS |

---

### Events

---

### Commands

---

### PersistentEntity

---

### Services

---

### Processor

---

### Projections