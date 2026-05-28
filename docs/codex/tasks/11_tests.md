# Task 11 — Add tests

## Goal

Add practical tests without overengineering the training project.

## Instructions for Codex

1. Inspect existing test setup.
2. Add repository tests if test database configuration exists.
3. Add service tests for:
   - duplicate email registration;
   - successful/failed login;
   - author-only status change.
4. Add controller smoke tests only if the project already has Spring MVC test setup.
5. Keep tests compatible with the current course stack.

## Acceptance criteria

- `mvn test` passes.
- Critical business rules are covered.
- Tests do not require external manual steps beyond configured test database if the project already uses one.

## Suggested Codex prompt

Implement Task 11 from `docs/codex/tasks/11_tests.md`. Add practical tests for repositories/services/controllers where the current project setup supports it. Prioritize service rules. Keep tests simple and make `mvn test` pass. After that mark task as done.

## Status

Done on 2026-05-28.

## Verification

- `mvn test` passes: 20 tests, 0 failures, 0 errors.
- `rg -n "spring-boot-starter-data-jpa|JpaRepository|CrudRepository|PagingAndSortingRepository|EnableJpaRepositories|org\\.springframework\\.data|@Repository" pom.xml src/main src/test -S` returns no matches.

## Remaining risks

- Controller smoke tests were not added because the project did not already have a Spring MVC test setup.
