package ch.ethz.acl.passera.unsigned

class UInt(val intValue: Int) extends AnyVal with Serializable with SmallUInt[UInt] {
  override def toUInt = this
  override def intRep = intValue
}

object UInt {
  def MinValue = UInt(0)
  def MaxValue = UInt(~0)

  def apply(x: Int) = new UInt(x)
  def unapply(x: UInt) = Some(x.intValue)
}

