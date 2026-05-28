# BACKLOG: job4j_cars implementation plan for Codex

## Milestone 0 — Audit and baseline

- [x] Task 01: Audit current repository and remove Spring Data risk.
- [x] Task 02: Align Maven, Hibernate configuration, Liquibase and `SessionFactory` bean.

## Milestone 1 — Domain and database

- [x] Task 03: Create database schema and Hibernate model mappings.
- [x] Task 04: Implement repositories with manual Hibernate `SessionFactory`.

## Milestone 2 — Business layer

- [x] Task 05: Implement services and business rules.

## Milestone 3 — User flows

- [x] Task 06: Implement registration, login, logout and session handling.

## Milestone 4 — Post flows

- [x] Task 07: Implement post list and detail pages.
- [x] Task 08: Implement create post flow with car fields.
- [ ] Task 09: Implement photo upload and image display.
- [ ] Task 10: Implement sold/not sold status change with author-only rule.

## Milestone 5 — Quality and delivery

- [ ] Task 11: Add tests for repositories/services/controllers where practical.
- [ ] Task 12: Update README with description, launch instructions and screenshots.
- [ ] Task 13: Final review against job4j Web requirements.
