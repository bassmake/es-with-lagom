package sk.bsmk.es.lagom.entity

import enumeratum.{Enum, EnumEntry, PlayJsonEnum}

import scala.collection.immutable

sealed trait Tier extends EnumEntry {
  val threshold: Int
}

object Tier extends Enum[Tier] with PlayJsonEnum[Tier] {

  override def values: immutable.IndexedSeq[Tier] = findValues

  case object None extends Tier {
    override val threshold = 0
  }
  case object Silver extends Tier {
    override val threshold = 100
  }
  case object Gold extends Tier {
    override val threshold = 1000
  }

  def compute(points: Int): Tier =
    if (points >= Gold.threshold) {
      Gold
    } else if (points >= Silver.threshold) {
      Silver
    } else {
      None
    }

}
