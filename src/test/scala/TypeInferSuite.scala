package naivetypeinference

class TypeInferSuite extends munit.FunSuite:
  import TypeInfer.*
  import Type.*
  test("newTypeVar()") {
    reset()
    assertEquals(newTypeVar(), TypeVar("a1"))
    assertEquals(newTypeVar(), TypeVar("a2"))
    assertEquals(newTypeVar(), TypeVar("a3"))
    assertEquals(newTypeVar(), TypeVar("a4"))
    reset()
  }
  test("Env: collect") {
    val env0 =
      Env("a" -> TypeScheme(Set(TypeVar("b"), TypeVar("c")), TypeVar("a")))
    assertEquals(env0.collect, Set[TypeVar](TypeVar("a")))
    val env1 = Env(
      "a" -> TypeScheme(Set(TypeVar("b"), TypeVar("c")), TypeVar("a")),
      "b" -> TypeScheme(Set(TypeVar("f"), TypeVar("e")), TypeVar("d"))
    )
    assertEquals(env1.collect, Set[TypeVar](TypeVar("a"), TypeVar("d")))
  }
  test("unify: same type vars") {
    val (l, r) = (TypeVar("a"), TypeVar("a"))
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: different type vars") {
    val (l, r) = (TypeVar("a"), TypeVar("b"))
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: arrow") {
    val l = Arrow(TypeVar("a"), TypeVar("b"))
    val r = Arrow(TypeVar("c"), TypeVar("d"))
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: type var with arrow") {
    val l = TypeVar("a")
    val r = Arrow(TypeVar("b"), TypeVar("c"))
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: arrow with type var") {
    val l = Arrow(TypeVar("b"), TypeVar("c"))
    val r = TypeVar("a")
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
