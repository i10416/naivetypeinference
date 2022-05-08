package naivetypeinference
import Type.*

// a chain of type mappings
// probably corresponding to https://github.com/nojima/typed-lambda/blob/master/src/Type.hs
abstract class Subst extends (Type => Type):
  def lookup(x: TypeVar): Type

  override def apply(t: Type): Type = t match
    case tv @ TypeVar(_) =>
      val u = lookup(tv)
      if (t == u) t else apply(u)
    case Arrow(t0, t1) =>
      Arrow(apply(t0), apply(t1))
    case TypeCon(k, ts) =>
      TypeCon(k, ts map apply)

  def extend(t0: TypeVar, t1: Type) = new Subst:
    def lookup(t: TypeVar): Type = if (t0 == t) t1 else Subst.this.lookup(t)
object Subst:
  val empty = new Subst:
    def lookup(t: TypeVar): Type = t
