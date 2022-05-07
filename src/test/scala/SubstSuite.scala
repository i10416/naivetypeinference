package naivetypeinference

class SubstSuite extends munit.FunSuite:
  import Type.*
  test(
    "lookup: given a type variable supposed not to be substituted, it returns the type variable itself."
  ) {
    val subst0 = Subst.empty
    assertEquals(subst0.lookup(TypeVar("a")), TypeVar("a"))
    // assertEquals(subst0.lookup(TypeVar("a")),TypeVar("a"))
  }

  test("lookup: substitute a to b type") {
    val subst0 = Subst.empty
    val subst1 = subst0.extend(TypeVar("a"), TypeVar("b"))
    assertEquals(subst0.lookup(TypeVar("a")), TypeVar("a"))
    assertEquals(subst0.lookup(TypeVar("b")), TypeVar("b"))
    assertEquals(subst1.lookup(TypeVar("a")), TypeVar("b"))
    assertEquals(subst1.lookup(TypeVar("b")), TypeVar("b"))
    assert(subst0 != subst1)
  }

  test("lookup: substitute type variables a to c via a to b and b to c") {
    // extend b -> c, then a -> b
    val tpe0 = Subst.empty
      .extend(TypeVar("b"), TypeVar("c")) // map b -> c
      .extend(TypeVar("a"), TypeVar("b")) // map a -> b
      .apply(TypeVar("a"))
    // extend a -> b, then b -> c
    val tpe1 = Subst.empty
      .extend(TypeVar("a"), TypeVar("b")) // map a -> b
      .extend(TypeVar("b"), TypeVar("c")) // map b -> c
      .apply(TypeVar("a"))
    assertEquals(tpe0, TypeVar("c"))
    assertEquals(tpe0, TypeVar("c"))
  }

  test("lookup: substitute type variables a to (b -> c)") {
    // extend b -> c, then a -> b
    val tpe0 = Subst.empty
      .extend(TypeVar("a"), Arrow(TypeVar("b"), TypeVar("c"))) // map b -> c
    // extend a -> b, then b -> c
    assertEquals(tpe0.lookup(TypeVar("a")), Arrow(TypeVar("b"), TypeVar("c")))
    assertEquals(tpe0.apply(TypeVar("a")), Arrow(TypeVar("b"), TypeVar("c")))
  }
  test("lookup: substitute type variables a to (c -> c) using b -> c") {
    // extend b -> c, then a -> b
    val tpe0 = Subst.empty
      .extend(TypeVar("b"), TypeVar("c"))
      .extend(
        TypeVar("a"),
        Arrow(TypeVar("b"), TypeVar("c"))
      ) // map a -> (b -> c)
    // extend a -> b, then b -> c
    assertEquals(tpe0.lookup(TypeVar("a")), Arrow(TypeVar("b"), TypeVar("c")))
    assertEquals(tpe0.apply(TypeVar("a")), Arrow(TypeVar("c"), TypeVar("c")))
    val tpe1 = tpe0.extend(TypeVar("d"), TypeVar("a"))
    assertEquals(tpe1.apply(TypeVar("d")), Arrow(TypeVar("c"), TypeVar("c")))
    val tpe2 = tpe0.extend(TypeVar("c"), TypeVar("d"))
    assertEquals(tpe2.apply(TypeVar("a")), Arrow(TypeVar("d"), TypeVar("d")))
  }
  test("lookup: substitute type variables a to mono-morphic") {
    // extend b -> c, then a -> b
    val tpe0 = Subst.empty
      .extend(TypeVar("a"), TypeCon("B"))
    // extend a -> b, then b -> c
    assertEquals(tpe0.apply(TypeVar("a")), TypeCon("B"))
  }
  test("lookup: substitute type variables a to polymorphic") {
    // extend b -> c, then a -> b
    val tpe0 = Subst.empty
      .extend(TypeVar("a"), TypeCon("B", List(TypeVar("c"), TypeVar("d"))))
    // extend a -> b, then b -> c
    assertEquals(
      tpe0.apply(TypeVar("a")),
      TypeCon("B", List(TypeVar("c"), TypeVar("d")))
    )
  }
