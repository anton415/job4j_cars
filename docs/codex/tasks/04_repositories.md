# Task 04 — Implement manual Hibernate repositories

## Goal

Implement `UserRepository`, `CarRepository` and `PostRepository` using injected `SessionFactory`.

## Instructions for Codex

1. Create repositories in `repository` package or adapt existing package name consistently.
2. Implement:
   - `UserRepository`;
   - `CarRepository`;
   - `PostRepository`.
3. Use constructor injection of `SessionFactory`.
4. Use explicit Hibernate sessions and transactions.
5. For post reads, use `join fetch` for `user` and `car`.
6. Return `Optional<T>` for not-found queries.
7. Do not use Spring Data.

## Required methods

See `SPEC.md`, section 7.

## Acceptance criteria

- Repositories compile.
- Repositories use `SessionFactory`, not Spring Data.
- `PostRepository.findAll()` returns posts ordered by newest first.
- `PostRepository.findById()` loads author and car.

## Suggested Codex prompt

Implement Task 04 from `docs/codex/tasks/04_repositories.md`. Create manual Hibernate repositories for users, cars and posts. Use injected `SessionFactory`, explicit transactions and `join fetch` for post views. Do not add service/controller logic except wiring fixes.
