# naive type inferernce

次のような構文とプリミティブ型をもつ簡易的なプログラムについて型推論を実装する.

```scala
enum Term:
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
```

```scala
object Predefined:
  // ...
  val boolType = TypeCon("Bool", Nil)
  val intType = TypeCon("Int", Nil)
  // ...
```

## 型推論の概要
例えば、`a + b` という式が与えられたとき、`+`は `Int => Int => Int` であるから `a :Int`,`b: Int` が推論できる.

静的型付け言語ではこのように型を書かなくても変数の型を推論できるとうれしい.

より一般的な関数についてもこのように型を推論したい.

次の関数を考える.

```
fun(a) {
  fun(f) {
    if a
    then 0
    else f(1)
  }
}
```

関数の抽象構文木を解析すれば、`fun(a): typeOf(a) =>typeOf(fun(f))`(`fun(a)`の返り値は`fun(f)`に`f` を適用したときの値の型となる)、 `if <bool> then expr0:<t> else expr1:<t>`(`if`は`bool`をとる, `then` 節と `else` 節が返す型は一致する), などの制約を得られる.


制約から `fun(a) { fun(f) { if a then 0 else f(1) } } : Bool → (Int → Int) → Int` というように型が推論される.

人間であれば順番に考えれば型推論ができる. 

1. `fun(a)`の型はわからないが、 `fun(a)` の引数 `a` は `if` の引数になっているので `a: Bool` が推測できるから `Bool => _` と推測できる. 
2. `if` 式の `then` 節で `0: Int` を返しているので `else` 節の `f` は `_ => Int` と推定できる. 
3. また、 `f` は `1:Int` を受け取っているので `f: Int => _` と推定できる. 
4.  2,3 から `f: Int => Int` と推定できる. (このように仮置きした型を埋める処理が `unify`)
5.  `fun(f)`は`(Int => Int) => Int` と推定できる.
6.  `fun(a)` は `Bool => (Int => Int) => Int` と推定できる.

これを整理すると次の一連の処理になる.

1. 構文木を下る過程では型が未知なので型を適当な変数(e.g. `a`)で仮置きする. また、型についての制約を記録する.
2. 1 で置いた仮定と制約に矛盾しないような型を `a` に代入する.

この処理(連立方程式)を計算機に解かせることで型を推論できる.

参考記事: https://www.klab.com/jp/blog/tech/2015/1047569315.html
実装例: https://github.com/kakkun61/TaPL/tree/master/Chapter22Reconstruction

## Point
- HM型システムを使えば、型に関するヒントがなくても、ラムダ式だけから適切な型が推論できる
- OCaml や Haskell の Hindley/Milner 型推論は関数の引数が多相にならないという制約の下で型推論を行っている.
  - この制約が無いと型推論できない. 型推論の制約については [計算機に推論できる型、できない型
](https://www.wantedly.com/companies/wantedly/post_articles/349494)が詳しい.
- Hindley/Milner 型チェックの中心的な操作は unify 関数
- unify は `unify(a,b)` の `a,b` が `a == b` を満たすような代入 `Subst` を返す
  - `Subst` は `Type => Type` の関数


## Links
### Basic
- Scala による HM型推論の実装(Scala by Examples) より: https://w.atwiki.jp/tmiya/pages/78.html
- Scala による HM型推論の実装 その２: http://dysphoria.net/2009/06/28/hindley-milner-type-inference-in-scala/

### Advanced
- https://github.com/nojima/typed-lambda: HM 型推論を備えた簡易的な言語の実装(Haskell)
- https://www.fos.kuis.kyoto-u.ac.jp/~igarashi/class/isle4-06w/text/miniml011.html: 京大の情報学部の講義資料(OCaml)

## Keywords

- Hindley Miler Type System
- TaPL
- 型付きラムダ計算
- Haskell
