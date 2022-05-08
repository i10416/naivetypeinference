package naivetypeinference

enum Term:
  override def toString: String = this match
    case Id(name)           => name
    case Lambda(name, t)    => s"λ $name . $t"
    case Apply(a, b)        => s"($a $b)"
    case Let(id, dfn, body) => s"let $id = $dfn in $body"

  /** 変数(identifier)をあらわす.
    */
  case Id(name: String)

  /** λ抽象をあらわす.
    */
  case Lambda(name: String, t: Term)

  /** 関数適用をあらわす.
    */
  case Apply(fn: Term, arg: Term)

  /** Let 式をあらわす.
    */
  case Let(x: String, defn: Term, body: Term)
