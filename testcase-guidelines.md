# Spring Boot Unit Testing Guidelines
> Standards and pragmatic rules for an AI (or engineer) to follow when writing **unit tests** for a Spring Boot project.
> Tests must be idiomatic Java, follow Clean Code, use **JUnit 5** and **Mockito**, prefer `@MethodSource` parameterized tests, and aim for **100% line & branch coverage**.

---

## Table of contents
1. [Purpose & Scope](#purpose--scope)  
2. [High-level principles](#high-level-principles)  
3. [Testing strategy & the pyramid](#testing-strategy--the-pyramid)  
4. [Test types & when to use them](#test-types--when-to-use-them)  
5. [Standards & conventions](#standards--conventions)  
6. [Test organization & packaging](#test-organization--packaging)  
7. [Unit test rules (must follow)](#unit-test-rules-must-follow)  
8. [Mocking rules & Mockito patterns](#mocking-rules--mockito-patterns)  
9. [Parameterized tests (MethodSource) — best practices & examples](#parameterized-tests-methodsource---best-practices--examples)  
10. [Edge & branch coverage checklist](#edge--branch-coverage-checklist)  
11. [Spring-specific patterns (controllers, repositories, slices)](#spring-specific-patterns-controllers-repositories-slices)  
12. [Avoid flaky tests — async & timing](#avoid-flaky-tests---async--timing)  
13. [Test data & builders / fixtures](#test-data--builders--fixtures)  
14. [CI, coverage enforcement, and tools (JaCoCo, surefire)](#ci-coverage-enforcement-and-tools-jacoco-surefire)  
15. [Refactoring for testability](#refactoring-for-testability)  
16. [Review / PR checklist for tests](#review--pr-checklist-for-tests)  
17. [Appendix: templates & sample test classes](#appendix-templates--sample-test-classes)

---

## Purpose & Scope
**Goal:** Provide deterministic, fast, idiomatic unit tests for Spring Boot services/controllers/repositories that:
- Use **JUnit 5** and **Mockito**.
- Test each **public method in isolation**.
- Mock every function/dependency called from that method (use `spy` only when unavoidable).
- Prefer parameterized tests (`@MethodSource`) to cover multiple input / branch combinations.
- Aim for **100% line and branch coverage** (practical guidance to achieve this).

This doc covers standards and examples for unit tests (with notes on slice/integration tests).

---

## High-level principles
- **One assertion intent per test** (one scenario). Multiple asserts OK when they verify the same logical outcome.
- **Arrange / Act / Assert (AAA)** structure in every test.
- **Readable names**: `methodName_givenCondition_expectedOutcome` or `shouldDoX_whenY`.
- **Fast & deterministic**: tests should run in milliseconds and never rely on real networks or current time.
- **Prefer composition over spying**: extensive spying often signals poor design. Consider refactoring.
- **Test behaviors, not implementation** — however, to reach 100% branch coverage you must explicitly cover branches; still keep tests meaningful.

---

## Testing strategy & the pyramid
- **Unit tests** (fast, many) — primary focus here.
- **Integration/slice tests** (fewer) — verify wiring and contracts, use `@WebMvcTest`, `@DataJpaTest`, or `@SpringBootTest` as needed.
- **End-to-end tests** (CI pipeline) — use Testcontainers if you must test DB/queues.

Aim to have 80–90% tests as unit tests (but your coverage target can be stricter).

---

## Test types & when to use them
- **Unit test**: test class X in isolation. Mock dependencies.
- **Service slice unit**: test service with repository mocked.
- **Controller slice**: use `@WebMvcTest` + `MockMvc` or `WebTestClient`, mock service layer with `@MockBean`.
- **Repository tests**: `@DataJpaTest` (in-memory or Testcontainers).
- **Integration tests**: `@SpringBootTest` for cross-cutting concerns; keep these minimal.

---

## Standards & conventions

**Naming**
- Test class: `ClassUnderTestTest` (e.g., `OrderServiceTest`).
- Test methods: `shouldDoX_whenY`, or `methodName_givenX_thenY`.

**Structure**
- `@ExtendWith(MockitoExtension.class)` for pure unit tests.
- Use `@Mock` and `@InjectMocks` for dependencies.
- Keep tests below ~200 lines; split when necessary.

**Clean code**
- Small helper methods for repetitive setup (`buildOrder()`, `sampleUser()`).
- Use `TestDataBuilder` pattern for complex objects.
- Avoid magic literals — use constants.

---

## Test organization & packaging
- Mirror production package structure under `src/test/java`.
- One test class per production class.
- Keep helper/test util classes in `test` scope only (e.g., `test.util`, `test.builder`).

---

## Unit test rules (must follow)
1. **Every public method must have tests**: happy path(s), edge cases, exception paths, and all internal branches exercised.
2. **Isolation**: The unit under test (class A) should not call real collaborators. All collaborators invoked by the public method must be **mocked**.
   - If the method calls another public method of the same class and you want to stub that call, prefer **refactor** to extract into collaborator. If not feasible, use `spy` and `doReturn(...)` pattern sparingly.
3. **No external resources**: network, file system, DB, or environment variables must be mocked or simulated.
4. **Parametrization**: Prefer `@ParameterizedTest` + `@MethodSource` for combinatorial/branch coverage.
5. **Assertions**: Use `org.junit.jupiter.api.Assertions` or a fluent library (AssertJ) where available. Keep assertions expressive.
6. **Test side-effects**: Always verify state change or interactions with `verify(...)` and `ArgumentCaptor` where appropriate.
7. **Exception tests**: Use `assertThrows()` and verify interactions and messages if relevant.
8. **Test duration**: Unit tests should be fast. If a test takes >200ms, investigate.

---

## Mocking rules & Mockito patterns

**Base setup**
```java
@ExtendWith(MockitoExtension.class)
class MyServiceTest {
    @Mock private Dependency dependency;
    @InjectMocks private MyService myService;
}
```

When to mock vs spy
	•	Mock: external collaborators, repositories, REST clients, lambdas or other classes.
	•	Spy: only when you must override behavior of the class under test (rare). Prefer extracting the behavior into a collaborator to mock instead.

Common patterns
	•	when(collab.call(arg)).thenReturn(result);
	•	doThrow(new Exception()).when(collab).voidMethod(arg); — for void methods.
	•	doReturn(value).when(spy).method(args); — to stub internal calls on spies.
	•	ArgumentCaptor<T> to capture arguments and assert.
	•	verify(collab).method(arg) and verifyNoMoreInteractions(collab) to ensure no extra calls.
	•	verify(collab, times(1)).method(arg) for exact invocation counts.

Static & final methods
	•	Avoid testing/depending on static or final methods. If unavoidable, use Mockito inline mock maker for static mocking (try (MockedStatic<Utils> ms = mockStatic(Utils.class)) { ... }) — document and justify this use.

⸻

Parameterized tests (MethodSource)

Guideline: Use @ParameterizedTest + @MethodSource where multiple input combinations exist or where you must cover branches systematically.

Example provider
static Stream<Arguments> createOrderProvider() {
    return Stream.of(
        Arguments.of(validSmallOrder(), 1L),
        Arguments.of(validLargeOrder(), 100L)
    );
}

Example parameterized test
@ParameterizedTest
@MethodSource("createOrderProvider")
void createOrder_happyPath_persists(OrderRequest input, long expectedTotal) {
    when(paymentService.charge(any())).thenReturn(true);
    when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    
    Order result = orderService.createOrder(input);
    
    assertNotNull(result);
    assertEquals(expectedTotal, result.getTotal());
    verify(paymentService).charge(any());
}

Best practices
	•	Give providers descriptive names.
	•	Use Arguments.of(...) and complex objects built via builders for clarity.
	•	Keep datasets small but comprehensive — use combinatorial coverage where necessary but avoid explosion.

⸻

Edge & branch coverage checklist

For every public method, create a small matrix of test cases that ensures the following branches are exercised:
	•	Null / invalid inputs.
	•	Empty collections / single-item / many-items.
	•	if true and false.
	•	switch — each case.
	•	Loops: zero iterations, one iteration, N iterations.
	•	Exceptions thrown by dependencies (verify method handles or propagates correctly).
	•	Boundaries: min, max, off-by-one, overflow-like scenarios.
	•	Concurrency: race conditions (if applicable).
	•	Transactional behavior: success and rollback scenarios.
	•	Feature flags or environment variances.

Template (per method)
	•	Happy path(s)
	•	Invalid input(s)
	•	Dependency throws — assert propagation/handling
	•	Each boolean branch
	•	Boundary values

Review / PR checklist for tests

For each PR that adds/changes production code:
	•	A test exists for every changed/added public method.
	•	Tests exercise all logical branches and exceptions.
	•	No external I/O used in unit tests.
	•	Tests are deterministic and fast.
	•	CI has passing JaCoCo coverage rules (or a reasoned exception noted).
	•	Mocks used responsibly — no overuse of spy unless documented.