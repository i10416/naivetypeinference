# naive type inferernce

Keywords

- Hindley Miler Type System
- TaPL
- 型付きラムダ計算
- Haskell


次のような関数を考える.

```
fun(a) {
  fun(f) {
    if a
    then 0
    else f(1)
  }
}
```

この関数は、`if <bool> then expr0 else expr1` ,`fun(f): f:F -> Int` や `f(_) => Int` などの制約をまとめた連立方程式を解くことで
`fun(a) { fun(f) { if a then 0 else f(1) } } : Bool → (Int → Int) → Int` というように型が推論される.

参考記事: https://www.klab.com/jp/blog/tech/2015/1047569315.html
実装例: https://github.com/kakkun61/TaPL/tree/master/Chapter22Reconstruction


- Hindley/Milner型チェックの中心的な操作は unify

## Links

Scala による HM型推論の実装(Scala by Examples) より: https://w.atwiki.jp/tmiya/pages/78.html
Scala による HM型推論の実装 その２: http://dysphoria.net/2009/06/28/hindley-milner-type-inference-in-scala/
