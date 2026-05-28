# Task 05 — Implement services and business rules

## Goal

Create service layer that hides repository details and enforces business rules.

## Instructions for Codex

1. Implement `UserService`, `CarService`, `PostService`.
2. Use constructor injection of repositories.
3. Implement duplicate email check during registration.
4. Implement login by email/password.
5. Implement post creation with current user assigned as author.
6. Implement status change rule: only post author can change `sold`.
7. Keep controllers out of this task unless compile fixes are needed.

## Acceptance criteria

- Services compile.
- Duplicate email registration returns failure result, preferably `Optional.empty()` or boolean depending on existing style.
- Invalid login returns empty result.
- Anonymous user cannot be passed silently as author.
- Author-only status change rule is enforced in service.

## Suggested Codex prompt

Implement Task 05 from `docs/codex/tasks/05_services.md`. Add service layer for users, cars and posts. Enforce registration/login and author-only sold-status business rules. Do not use Spring Data. Keep changes focused.

## Status

Done on 2026-05-28.

## Verification

- `mvn test` passes: 13 tests, 0 failures, 0 errors.
- `rg -n "spring-boot-starter-data-jpa|JpaRepository|CrudRepository|PagingAndSortingRepository|EnableJpaRepositories|org\\.springframework\\.data|@Repository" pom.xml src/main src/test -S` returns no matches.

## Remaining risks

- Controllers are not wired to services yet; registration/login pages, session handling, and HTTP status changes remain for later tasks.
