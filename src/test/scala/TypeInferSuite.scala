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
  test("unify: monomorphic types") {
    val l = TypeCon("A")
    val r = TypeCon("A")
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: monomorphic type with type var") {
    val l = TypeCon("B")
    val r = TypeVar("a")
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: type var with monomorphic type") {
    val l = TypeVar("a")
    val r = TypeCon("B")
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: higer kinded types") {
    val l = TypeCon("A", List(TypeVar("a")))
    val r = TypeCon("A", List(TypeVar("a")))
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: higer kinded types with different TypeVars") {
    val l = TypeCon("A", List(TypeVar("a")))
    val r = TypeCon("A", List(TypeVar("b")))
    // substitute a -> b or b -> a
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: monomorphic arrows") {
    val l = Arrow(TypeCon("A"), TypeCon("B"))
    val r = Arrow(TypeCon("A"), TypeCon("B"))
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: polymorphic arrows") {
    val l = Arrow(TypeVar("a"), TypeVar("b"))
    val r = Arrow(TypeVar("c"), TypeVar("d"))
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: monomorphic arrow with polymorphic one -- A -> B <=> A -> c") {
    val l = Arrow(TypeCon("A"), TypeCon("B"))
    val r = Arrow(TypeCon("A"), TypeVar("c"))
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
  }
  test("unify: monomorphic arrow with polymorphic one -- a -> B <=> C -> d") {
    val l = Arrow(TypeVar("a"), TypeCon("B"))
    val r = Arrow(TypeCon("C"), TypeVar("d"))
    val s = unify(l, r, Subst.empty)
    val (l0, r0) = (s(l), s(r))
    assertEquals(l0, r0)
    assertEquals(l0, Arrow(TypeCon("C"), TypeCon("B")))
  }
