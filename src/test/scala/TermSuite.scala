package naivetypeinference

class TermSuite extends munit.FunSuite:
  import Term.*
  test("show"){
    assertEquals(Id("a").toString,"a")
    assertEquals(Lambda("a",Id("b")).toString,"λ a . b")
    assertEquals(Apply(Id("a"),Id("b")).toString,"(a b)")
    assertEquals(Let(("a"),Id("b"),Id("c")).toString,"let a = b in c")
  }
  test("eq"){
    assertEquals(Id("a"),Id("a"))
    assertEquals(Lambda("a",Id("b")),Lambda("a",Id("b")))
    assertEquals(Apply(Id("a"),Id("b")),Apply(Id("a"),Id("b")))
    assertEquals(Let("a",Id("b"),Id("c")),Let("a",Id("b"),Id("c")))
  }
  test("neq"){
    assert(Id("a")!=Id("a0"))
    assert(Lambda("a",Id("b"))!=Lambda("a",Id("b0")))
    assert(Apply(Id("a"),Id("b"))!=Apply(Id("a"),Id("b0")))
    assert(Let("a",Id("b"),Id("c"))!=Let("a",Id("b"),Id("c0")))
  }
    