package naivetypeinference
import Type.*

// a chain of type mappings
// probably corresponding to https://github.com/nojima/typed-lambda/blob/master/src/Type.hs
abstract class Subst extends (Type => Type):
  def lookup(x: TypeVar): Type

  // resolve t
  // e.g. subst.extend(t0,t1) creates t0 to t1 mapping. Hence, subst.apply(t0) returns t1
  // In the same way, subst.extend(t0,t1);subst.extend(t1,t2),...,subst.extend(t_{k-1},t_k);subst.apply(t0) returns t_k

  //  let env = env.Add("x", new TypeScheme(["a"; "b"], Arrow(Tyvar("a"),Tyvar("c"))))
  // これは すべての a,b について a -> c となるような c が存在する、とかいう話?
  // とすれば let e = e.Add("a", new TypeScheme(["a"; "b"], Tyvar("c"))) は、a, b が与えられれば 一意にきまる型 c が存在するというニュアンスかしら
  //  で、let e = e.Add("b", new TypeScheme(["a"; "b"], Arrow(Tyvar("c"),Tyvar("a")))) は、a,bが与えられたとき 適当な c をとれば、
  // c -> a が決まるような c が存在する、という話かしら

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
