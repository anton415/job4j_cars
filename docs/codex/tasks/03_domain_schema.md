# Task 03 — Implement domain model and database schema

## Goal

Create the core model classes and database migrations for users, cars and posts.

## Instructions for Codex

1. Create or adjust model classes:
   - `User`;
   - `Car`;
   - `Post`.
2. Add fields described in `SPEC.md`.
3. Add Hibernate annotations for table and relationship mapping.
4. Create Liquibase SQL migration scripts for:
   - `users`;
   - `cars`;
   - `posts`.
5. Add constraints:
   - unique user email;
   - required post title;
   - required car brand/model/year/body/engine/transmission;
   - `sold` default false.
6. If using enums, map them as strings.

## Acceptance criteria

- Hibernate mappings are valid.
- Database schema supports required UI and business flows.
- `Post` has relationships to `User` and `Car`.
- `Post` can store optional photo path.
- No Spring Data is introduced.

## Suggested Codex prompt

Implement Task 03 from `docs/codex/tasks/03_domain_schema.md`. Add domain models and Liquibase migrations for `User`, `Car`, and `Post`. Use Hibernate annotations and manual SessionFactory-compatible mappings. Do not implement repositories yet except for compile fixes if necessary.

## Status

Done on 2026-05-28.

## Verification

- `mvn clean test` passes: 17 tests, 0 failures, 0 errors.
- `rg -n "spring-boot-starter-data-jpa|JpaRepository|CrudRepository|PagingAndSortingRepository|EnableJpaRepositories|org\\.springframework\\.data" pom.xml src/main src/test -S` returns no matches.

## Remaining risks

- Existing `UserRepository`, `CarRepository`, and `PostRepository` were only adjusted enough to compile and run against the new mappings. Task 04 still needs to implement the required repository API from `SPEC.md`.
