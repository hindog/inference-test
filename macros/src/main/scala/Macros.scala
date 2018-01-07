import scala.reflect.runtime.universe._
import scala.reflect.macros._
import scala.language.implicitConversions
import scala.collection._
import scala.reflect.macros.blackbox

/**
  * Created by Aaron Hiniker (ahiniker@atomtickets.com) 
  * 1/7/18
  * Copyright (c) Atom Tickets, LLC
  */
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

  /** Attempt 2:
    *
    * def liftValueType2: T0 => M0
    */
  def liftValueType2[T0: c.WeakTypeTag, M0: c.WeakTypeTag]: c.Expr[T0 => M0] = {
    val from = weakTypeOf[T0]
    val to = weakTypeOf[M0]

    c.echo(c.enclosingPosition, s"T0: $from, M0: $to")

    c.Expr[T0 => M0](q"(value: $from) => ${to.typeSymbol.companion}.apply(value)")
  }

}
