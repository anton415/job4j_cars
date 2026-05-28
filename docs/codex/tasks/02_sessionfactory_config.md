# Task 02 — Configure Hibernate SessionFactory and migrations

## Goal

Create or fix project configuration so repositories can use a manually defined Hibernate `SessionFactory` bean.

## Instructions for Codex

1. Inspect current application entry point and resource configuration.
2. Add a Spring bean for `SessionFactory` if missing.
3. Ensure Hibernate configuration includes all mapped model classes or package scanning supported by the chosen Hibernate configuration approach.
4. Ensure database settings are consistent across:
   - `hibernate.cfg.xml` or equivalent;
   - Liquibase config;
   - application properties if used.
5. Keep the setup close to `job4j_todo` style.

## Acceptance criteria

- `SessionFactory` is a Spring bean.
- Repositories can receive `SessionFactory` via constructor injection.
- Liquibase changelog path is clear.
- `mvn test` is not broken by configuration changes.

## Suggested Codex prompt

Implement Task 02 from `docs/codex/tasks/02_sessionfactory_config.md`. Preserve the no-Spring-Data rule. Configure manual Hibernate `SessionFactory` as a Spring bean and align migration/config files. Keep changes minimal and explain any version-specific decisions.

## Status

Done on 2026-05-28.

## Verification

- `mvn test` passes: 23 tests, 0 failures, 0 errors.
- `rg -n "spring-boot-starter-data-jpa|JpaRepository|CrudRepository|PagingAndSortingRepository|EnableJpaRepositories|org\\.springframework\\.data" pom.xml src/main src/test -S` returns no matches.

## Remaining risks

- No known Task 02 blockers. Development config uses PostgreSQL; test config uses H2 through `src/test/resources/application.properties` and `hibernate-test.cfg.xml`.
