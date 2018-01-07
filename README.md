## Problem ##

Define a generic value class trait using:
```scala
trait ValueType extends Any { this: AnyVal =>
  type T
  def value: T
}
```

And a concrete implementation of that class using:
```scala
case class AccountId(value: UUID) extends AnyVal with ValueType { type T = UUID }
```

I'm trying to write a macro to generate an implicit `lift` function from the underlying type to the value class type (eg: `UUID => AccountId`):

```scala
implicit def liftValueType1[T0, M0 <: ValueType { type T = T0 }](value: T0): M0 = macro Macros.liftValueType1[T0, M0]
```

Macro def looks like this:

```scala
class Macros(val c: blackbox.Context) {
  import c.universe._

  /** Attempt 1:
    * 
    *  def liftValueType1(value: T0): M0
    */
  def liftValueType1[T0: c.WeakTypeTag, M0: c.WeakTypeTag](value: c.Expr[T0]): c.Expr[M0] = {
    val from = weakTypeOf[T0]
    val to = weakTypeOf[M0]

    c.echo(c.enclosingPosition, s"T0: $from, M0: $to")

    c.Expr[M0](q"${to.typeSymbol.companion}.apply($value)")
  }

  ...
}
  
```

I get this compile error (can't infer types?):

```
Information:(31, 24) ValueType.liftValueType1 is not a valid implicit value for java.util.UUID => AccountId because:
hasMatchingSymbol reported error: type mismatch;
 found   : (value: Any)Nothing
 required: java.util.UUID => AccountId
  val conv = implicitly[UUID => AccountId]
```
 