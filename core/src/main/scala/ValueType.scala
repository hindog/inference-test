
import scala.collection._

import java.util.UUID
import scala.language.experimental.macros

trait ValueType extends Any { this: AnyVal =>
  type T
  def value: T
}

object ValueType {
  type Aux[T0] = ValueType { type T = T0 }


/* Attempt 1:  def liftValueType1(value: T0): M0 */
  implicit def liftValueType1[T0, M0 <: ValueType { type T = T0 }](value: T0): M0 = macro Macros.liftValueType1[T0, M0]

/* Attempt 2:  def liftValueType2: T0 => M0 */

  //implicit def liftValueType2[T0, M0 <: ValueType { type T = T0 }]: T0 => M0 = macro Macros.liftValueType2[T0, M0]

/* Explicit definition (works, of course) */

  //implicit def lift3(value: UUID): AccountId = AccountId(value)
}

case class AccountId(value: UUID) extends AnyVal with ValueType { type T = UUID }

object TestApp extends App {
  val conv = implicitly[UUID => AccountId]
  println(conv(UUID.randomUUID()))
}