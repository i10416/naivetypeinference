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

  test("create new instance using empty env") {
    import TypeInfer.Env
    given Env = Env.empty
    val ts0 = TypeScheme(TypeVar("a"))
    val ts1 = TypeScheme(Set(TypeVar("a")), TypeVar("a"))
    assertEquals(ts0.toString, ts1.toString)
    val ts2 = TypeScheme(Arrow(TypeVar("a"), TypeVar("b")))
    val ts3 = TypeScheme(
      Set(TypeVar("a"), TypeVar("b")),
      Arrow(TypeVar("a"), TypeVar("b"))
    )
    assertEquals(ts2, ts3)
  }

  test("create new instance using env") {
    import TypeInfer.Env
    given Env = Env(
      ("a", TypeScheme(Set(TypeVar("b"), TypeVar("c")), TypeVar("a")))
    )
    val ts0 = TypeScheme(TypeVar("a"))
    val ts1 = TypeScheme(Set.empty, TypeVar("a"))
    assertEquals(ts0.toString, ts1.toString)
    val ts2 = TypeScheme(Arrow(TypeVar("a"), TypeVar("b")))
    val ts3 = TypeScheme(
      Set(TypeVar("b")),
      Arrow(TypeVar("a"), TypeVar("b"))
    )
    assertEquals(ts2, ts3)
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
