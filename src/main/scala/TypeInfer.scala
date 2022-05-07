package naivetypeinference

import scala.util.control.NonFatal
// Point: HM型システムを使えば、型に関するヒントがなくても、ラムダ式だけから適切な型が推論できる
// 木構造を潜って不明な型を仮置きし、登りながら不明な型を解決していく
object TypeInfer:
  import Type.*
  opaque type Env = String Map TypeScheme
  object Env:
    val empty: Env = Map.empty
    def apply(mapping: (String, TypeScheme)*): Env =
      Map.apply(mapping: _*)
    extension (env: Env)
      def lookup(s: String): Option[TypeScheme] = env.get(s)
      def collect: Set[TypeVar] =
        env.foldLeft(Set.empty) { case (acc, (_, tpe)) =>
          acc union tpe.collect
        }

  object Predefined:
    val boolType = TypeCon("Bool", Nil)
    val intType = TypeCon("Int", Nil)
    def listOf(t: Type) = TypeCon("List", t :: Nil)
    def gen(t: Type) = TypeScheme(t)
    private val a = TypeInfer.newTypeVar()
    given Env = Env(
      "true" -> TypeScheme(boolType)(using Env.empty),
      "false" -> TypeScheme(boolType)(using Env.empty),
      // bool -> a -> (a -> a)
      "if" -> TypeScheme(Arrow(boolType, Arrow(a, Arrow(a, a))))(using
        Env.empty
      ),
      // -> int
      "zero" -> TypeScheme(intType)(using Env.empty),
      // int -> int
      "succ" -> TypeScheme(Arrow(intType, intType))(using Env.empty),
      "nil" -> TypeScheme(listOf(a))(using Env.empty),
      // a -> [a] -> [a]
      "cons" -> TypeScheme(Arrow(a, Arrow(listOf(a), listOf(a))))(using
        Env.empty
      ),
      // [a] -> bool
      "isEmpty" -> TypeScheme(Arrow(listOf(a), boolType))(using Env.empty),
      // [a] -> a
      "head" -> TypeScheme(Arrow(listOf(a), a))(using Env.empty),
      // [a] -> [a]
      "tail" -> TypeScheme(Arrow(listOf(a), listOf(a)))(using Env.empty),
      "fix" -> TypeScheme(Arrow(Arrow(a, a), a))(using Env.empty)
    )

  import Predefined.*
  private var n: Int = 0
  def reset() = n = 0
  def newTypeVar(): Type =
    n += 1
    TypeVar(s"a$n")

  def typeOf(expr: Term): Env ?=> Type =
    val a = newTypeVar()
    infer(expr, a, Subst.empty).apply(a)
  /*t == u を満たすような Subst を返す*/
  def unify(t: Type, u: Type, s: Subst): Subst =
    (s(t), s(u)) match
      case (TypeVar(name0), TypeVar(name1)) if name0 == name1 => s
      case (st @ TypeVar(_), su @ _) => s.extend(st, su)
      case (_, TypeVar(_))           => unify(u, t, s)
      case (Arrow(st0, st1), Arrow(su0, su1)) =>
        unify(su0, st0, unify(su1, st1, s))
      // case TypeCon(kts,tst),TypeCon(ktu,tus) =>
      case _ => throw new TypeError(s"cannot unify $t with $u")

  def infer(expr: Term, t: Type, s: Subst): Env ?=> Subst =
    import Term.*
    expr match
      case Id(name) =>
        import Env.*
        summon[Env]
          .lookup(name)
          .fold(throw new TypeError(s"undefined: $name"))(???)
        ???
      case Lambda(arg, ret)  => ???
      case Apply(f, args)    => ???
      case Let(id, lhs, rhs) => ???

  case class TypeError(msg: String) extends Exception(msg)
