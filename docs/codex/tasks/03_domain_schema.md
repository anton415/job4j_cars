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
