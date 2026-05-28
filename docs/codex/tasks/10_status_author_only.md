# Task 10 — Implement sold/not sold status change with author-only rule

## Goal

Allow only the post author to change whether the car is sold.

## Instructions for Codex

1. Add or complete route: `POST /posts/{id}/status`.
2. Read current user from session.
3. If user is not authenticated, redirect to login.
4. Call `PostService.changeSoldStatus(postId, sold, currentUser)`.
5. If user is not the author, do not change status. Show an error or redirect with a message.
6. Update templates to show status change controls only to the author.

## Acceptance criteria

- Author can mark post as sold.
- Author can mark post as available again.
- Another logged-in user cannot change status.
- Anonymous user cannot change status.
- Business rule is enforced in service, not only hidden in UI.

## Suggested Codex prompt

Implement Task 10 from `docs/codex/tasks/10_status_author_only.md`. Add sold/not-sold status changes. Enforce author-only rule in service and reflect it in templates. Do not rely only on hiding buttons in HTML. After that mark task as done.

## Status

Done on 2026-05-28.

## Verification

- `mvn test` passes: 13 tests, 0 failures, 0 errors.
- `rg -n "spring-boot-starter-data-jpa|JpaRepository|CrudRepository|PagingAndSortingRepository|EnableJpaRepositories|org\\.springframework\\.data|@Repository" pom.xml src/main src/test -S` returns no matches.
- H2-backed runtime check with `mvn spring-boot:test-run` on port 18084:
  - author `POST /posts/{id}/status` with `sold=true` marks the post as sold;
  - another logged-in user cannot change the status and sees the author-only message;
  - anonymous `POST /posts/{id}/status` redirects to `/users/login`;
  - author `POST /posts/{id}/status` with `sold=false` marks the post available again.

## Remaining risks

- No known remaining risks for this task.
