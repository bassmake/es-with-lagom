package sk.bsmk.es.lagom.transaction.producer
import com.lightbend.lagom.scaladsl.api.broker.Topic

class TransactionProducerServiceImpl extends TransactionProducerService {

  override def transactionsTopic(): Topic[TransactionMessage] = ???

}
