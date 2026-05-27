# Task 01 — Audit current repository and remove Spring Data risk

## Goal

Make the project safe to continue without Spring Data. The mentor may reject the project if Spring Data is used.

## Instructions for Codex

1. Inspect `pom.xml`, application classes, repositories and configuration.
2. Find any usage of:
   - `spring-boot-starter-data-jpa`;
   - `JpaRepository`;
   - `CrudRepository`;
   - `PagingAndSortingRepository`;
   - `@EnableJpaRepositories`;
   - Spring Data repository interfaces.
3. Remove or replace these usages.
4. Preserve required dependencies for Spring Boot, Thymeleaf, Hibernate, PostgreSQL/H2 if currently used, Liquibase and tests.
5. Do not implement business features yet.

## Acceptance criteria

- Search by `JpaRepository`, `CrudRepository`, `PagingAndSortingRepository`, `EnableJpaRepositories` returns no production usage.
- Project still compiles or the remaining compile errors are only caused by missing future classes explicitly listed in the summary.
- No user-facing behavior is changed in this task.

## Suggested Codex prompt

Implement Task 01 from `docs/codex/tasks/01_audit_baseline.md`. Read `docs/codex/SPEC.md` and `docs/codex/CODEX_RULES.md` first. Only audit and remove Spring Data risk. Do not implement car sale functionality yet. After changes, summarize files changed and remaining risks.
