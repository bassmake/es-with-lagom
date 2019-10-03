package sk.bsmk.es.lagom.external

import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.testkit.ProducerStub

class MonetaryTransactionServiceStub(stub: ProducerStub[MonetaryTransaction]) extends MonetaryTransactionService {

  override def monetaryTransactionsTopic(): Topic[MonetaryTransaction] = stub.topic

}
