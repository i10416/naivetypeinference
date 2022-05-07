package naivetypeinference
import Type.*

// ∀t0∀t1,...,. f(t0,t1,...)
// ts は ∀* の * の部分の宣言, tpe は f(t0,t1,...) に対応する
// data TypeScheme = ForAll [Variable] Type
// 例: TypeScheme(a::b::Nil,Arrow(a, b)) ≒ ∀a∀b. a -> b
// 例: Int は TypeScheme(Nil,TypeVar("Int")) : 独立して Int という型が存在する. : ∀ Nothing,∃ Int.
case class TypeScheme(ts: Set[TypeVar], tpe: Type) {
  override def toString: String = s"∀ ${ts.mkString(", ")}. $tpe"
}
object TypeScheme:
  def apply(tpe: Type)(using env: TypeInfer.Env): TypeScheme =
    // すでに解決済みの型の場合は 独立した tpe を、そうでなければ ∀? に依存した tpe の TypeSchema を宣言する
    TypeScheme(tpe.collect diff env.collect, tpe)
  extension (ts: TypeScheme)
    def collect: Set[TypeVar] =
      ts.tpe.collect diff ts.ts
