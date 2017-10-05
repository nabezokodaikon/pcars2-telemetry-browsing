package ch.ethz.acl.passera.unsigned

case class UShort(override val shortValue: Short) extends AnyVal with Serializable with SmallUInt[UShort] {
  override def intValue = shortValue & 0xffff
}

object UShort {
  def MinValue = UShort(0)
  def MaxValue = UShort(~0)
}
