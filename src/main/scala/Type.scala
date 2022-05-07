package naivetypeinference

enum Type:
  override def toString: String = this match
    case TypeVar(name)   => name
    case Arrow(arg, ret) => s"($arg -> $ret)"
    case TypeCon(k, ts)  => s"$k[${ts.mkString(",")}]"

  /** 型変数をあらわす. たとえば Int, Bool, Char がこの 'インスタンス'
    */
  case TypeVar(name: String)

  /** 関数をあらわす. e.g. f: a => b
    */
  case Arrow(arg: Type, ret: Type)

  /** 型コンストラクタをあらわす. e.g. Tpe[A,B,C,...]
    */
  case TypeCon(k: String, ts: List[Type] = Nil)
object Type:
  def from(typeScheme: TypeScheme): Type = typeScheme match
    case TypeScheme(ts, tpe) =>
      // ここであらわれる TypeInfer.newTypeVar は未解決の型
      ts.foldLeft(Subst.empty)(_.extend(_, TypeInfer.newTypeVar()))(tpe)
  // 型の構造をたどって含まれるすべての TypeVar を返す.
  extension (t: Type)
    def collect: Set[TypeVar] = t match
      case tv @ TypeVar(_) => Set(tv)
      case Arrow(arg, ret) => arg.collect.toSet union ret.collect.toSet
      case TypeCon(_, ts) =>
        ts.foldLeft(Set.empty)((acc, tpe) => acc union tpe.collect)
