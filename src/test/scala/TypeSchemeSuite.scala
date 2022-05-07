package naivetypeinference

class TypeSchemeSuite extends munit.FunSuite:
  import Type.*
  test("create new instance") {
    val ts = TypeScheme(Set(TypeVar("a"), TypeVar("b")), TypeVar("a"))
    assertEquals(ts.ts, Set[TypeVar](TypeVar("a"), TypeVar("b")))
    assertEquals(ts.tpe, TypeVar("a"))
    val tpe = Type.from(ts)
    assertEquals(tpe.toString, "a1")
  }
  test("create new instance for free type var") {
    val ts = TypeScheme(Set(TypeVar("a"), TypeVar("b")), TypeVar("c"))
    val tpe = Type.from(ts)
    assertEquals(tpe.toString, "c")
  }

  test("collect") {
    val ts0 = TypeScheme(Set.empty, TypeVar("a"))
    val ts1 = TypeScheme(Set(TypeVar("a")), TypeVar("a"))
    val ts2 = TypeScheme(Set(TypeVar("a")), Arrow(TypeVar("a"), TypeVar("b")))
    val ts3 = TypeScheme(Set(TypeVar("a")), Arrow(TypeVar("a"), TypeVar("a")))
    assertEquals(ts0.collect, Set[TypeVar](TypeVar("a")))
    assertEquals(ts2.collect, Set[TypeVar](TypeVar("b")))
    assertEquals(ts3.collect, Set.empty)
  }
