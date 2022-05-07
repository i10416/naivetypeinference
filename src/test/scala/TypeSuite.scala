package naivetypeinference

class TypeSuite extends munit.FunSuite:
  import Type.*
  test("show") {
    assertEquals(TypeVar("a").toString, "a")
    assertEquals(Arrow(TypeVar("a"), TypeVar("b")).toString, "(a -> b)")
    assertEquals(TypeCon("A", Nil).toString, "A[]")
    assertEquals(TypeCon("A", TypeVar("a") :: Nil).toString, "A[a]")
    assertEquals(
      TypeCon("A", TypeVar("a") :: TypeVar("b") :: Nil).toString,
      "A[a,b]"
    )
  }
  test("eq") {
    assertEquals(TypeVar("a"), TypeVar("a"))
    assertEquals(
      Arrow(TypeVar("a"), TypeVar("b")),
      Arrow(TypeVar("a"), TypeVar("b"))
    )
    assertEquals(TypeCon("A", Nil), TypeCon("A", Nil))
  }
  test("collect") {
    assertEquals(TypeVar("a").collect, Set[TypeVar](TypeVar("a")))
    assertEquals(
      Arrow(TypeVar("a"), TypeVar("b")).collect,
      Set[TypeVar](TypeVar("a"), TypeVar("b"))
    )
    assertEquals(
      TypeCon("a", List(TypeVar("b"), TypeVar("c"))).collect,
      Set[TypeVar](TypeVar("b"), TypeVar("c"))
    )
  }
