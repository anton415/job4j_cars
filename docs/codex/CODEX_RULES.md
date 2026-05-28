# CODEX_RULES: implementation guardrails for job4j_cars

Use this file as the system/context instruction when asking Codex to implement tasks.

## Main rule

Implement a job4j training web app for car sale posts with Spring Boot, Thymeleaf and Hibernate. Do not use Spring Data.

## Minimal-code rule

This is an educational project. For every task, write the minimum code needed to satisfy the current task and its acceptance criteria.

- Prefer direct, readable course-style code over generic frameworks, reusable abstractions and future-proofing.
- Do not add unused classes, methods, templates, configuration, DTOs, interfaces, mappers, validators, utilities or tests "just in case".
- Treat optional items as "do not implement yet" unless the current task explicitly needs them.
- If a simple implementation leaves a known gap outside the current task, document it as a remaining risk instead of solving a later task early.

## Forbidden

- No `spring-boot-starter-data-jpa` if it introduces Spring Data repositories.
- No `JpaRepository`.
- No `CrudRepository`.
- No `PagingAndSortingRepository`.
- No `@Repository` interfaces implemented by Spring Data.
- No `@EnableJpaRepositories`.
- No generated repository methods such as `findBy...` from Spring Data.
- Do not replace Hibernate `SessionFactory` with `EntityManager` unless the existing course template already requires it. The target architecture is `SessionFactory` injected into repositories.

## Required architecture

Packages should be close to this structure:

```text
ru.job4j.cars
  config
  controller
  model
  repository
  service
```

Layer rules:

- Controllers know about HTTP, sessions, forms, model attributes and redirects.
- Services know about business rules.
- Repositories know about Hibernate and database queries.
- Models must not contain persistence logic.
- Controllers must not open Hibernate sessions.
- Services must not build HTML or read request parameters directly.

## SessionFactory rule

Create a Spring bean similar to:

```java
@Bean(destroyMethod = "close")
public SessionFactory sessionFactory() {
    return new Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();
}
```

Adjust the exact code to the current project and Hibernate version.

Repositories must receive it through constructor injection:

```java
@Component
public class PostRepository {
    private final SessionFactory sf;

    public PostRepository(SessionFactory sf) {
        this.sf = sf;
    }
}
```

## Transaction rule

Use explicit transactions in repositories. Keep each method atomic.

Recommended pattern:

```java
try (Session session = sf.openSession()) {
    Transaction tx = session.beginTransaction();
    try {
        // operation
        tx.commit();
    } catch (Exception e) {
        tx.rollback();
        throw e;
    }
}
```

Do not hide all errors by returning empty values. Return `Optional.empty()` only for legitimate not-found cases.

## Entity loading rule

When a controller renders posts, `User` and `Car` must already be loaded. Use `join fetch` in `PostRepository.findAll()` and `findById()` to avoid lazy-loading errors in Thymeleaf.

Example:

```java
select distinct p from Post p
join fetch p.user
join fetch p.car
order by p.created desc
```

## Photo rule

Keep photo upload simple:

- save files under local `uploads/` or `files/`;
- store path/filename in `Post.photoPath`;
- add upload directory to `.gitignore`;
- do not commit user-uploaded photos.

## Security rule

This is not a full security project. Use simple HTTP session authentication unless Spring Security is already configured and does not conflict with the course.

Required rule: only the post author can change sale status.

## Task workflow for Codex

For each task:

1. Read `docs/codex/SPEC.md`.
2. Read `docs/codex/CODEX_RULES.md`.
3. Read the current repository code before editing.
4. Implement only the current task and only the code required by its acceptance criteria.
5. Do not do unrelated refactoring.
6. Do not add optional or future-task functionality.
7. Run or at least preserve `mvn test` compatibility.
8. Summarize changed files and any risks.
