# Task 13 — Final review against job4j Web requirements

## Goal

Perform final cleanup before submitting the project.

## Instructions for Codex

1. Search for forbidden Spring Data usages again.
2. Check package structure and layer boundaries.
3. Check migrations and model mappings.
4. Check README completeness.
5. Run tests if possible.
6. Check that anonymous/authenticated/author-only flows work logically.
7. Remove dead code, unused imports and accidental debug output.
8. Do not perform large rewrites unless required for correctness.

## Acceptance criteria

- No Spring Data usage.
- Three-layer architecture is visible.
- Required repositories exist.
- Main page, create page, auth flow, photo flow and status flow are implemented.
- README is submission-ready.
- `mvn test` passes or all remaining failures are explicitly explained.

## Suggested Codex prompt

Implement Task 13 from `docs/codex/tasks/13_final_review.md`. Perform a final mentor-readiness review and small cleanup only. Verify the no-Spring-Data rule, layer boundaries, README, migrations and tests. Summarize remaining risks honestly. After that mark task as done.

## Status

Done on 2026-05-28.

## Verification

- Forbidden Spring Data search passed: no `spring-boot-starter-data-jpa`, `JpaRepository`, `CrudRepository`, `PagingAndSortingRepository`, `@EnableJpaRepositories`, `org.springframework.data` or Spring Data `@Repository` usage in `pom.xml`, `src/main` or `src/test`.
- Three layers are present: controllers handle HTTP/session/forms, services hold business rules, repositories use Hibernate `SessionFactory` and explicit transactions.
- Required repositories exist: `UserRepository`, `CarRepository`, `PostRepository`.
- Migrations and mappings were checked for `users`, `cars` and `posts`; Liquibase changelog includes all three SQL scripts.
- README describes features, stack, no-Spring-Data architecture, launch instructions, migrations and screenshots. Referenced screenshot files exist under `images/`.
- Main page, create page, auth flow, photo flow and author-only status flow are implemented logically in controllers, services and templates.
- `mvn test` passes: 20 tests, 0 failures, 0 errors.

## Remaining risks

- Passwords are stored in plain text by design for the educational scope; this is documented in README.
- Create-post flow saves the uploaded file and car before the post. A later database failure could leave an unused upload or car row; not changed to avoid a broader transaction rewrite during final cleanup.
