# Task 06 — Implement registration, login, logout and session handling

## Goal

Add minimal session-based authentication required for creating posts and changing post status.

## Instructions for Codex

1. Add `UserController`.
2. Add routes:
   - `GET /users/register`;
   - `POST /users/register`;
   - `GET /users/login`;
   - `POST /users/login`;
   - `GET /users/logout` or `POST /users/logout`.
3. Store authenticated user in `HttpSession`, for example under key `user`.
4. Add login/register Thymeleaf templates.
5. Add navigation links that reflect authentication state.
6. Do not implement Spring Security unless already required by the current project.

## Acceptance criteria

- User can register.
- User can login.
- User can logout.
- Current user is visible to templates where needed.
- Duplicate email is handled with a clear message.

## Suggested Codex prompt

Implement Task 06 from `docs/codex/tasks/06_user_auth.md`. Add minimal session-based user registration/login/logout with Thymeleaf views. Use existing services. Keep anonymous users able to view posts later. Do not introduce Spring Security unless strictly necessary.

## Status

Done on 2026-05-28.

## Verification

- `mvn test` passes: 13 tests, 0 failures, 0 errors.
- `rg -n "spring-boot-starter-data-jpa|JpaRepository|CrudRepository|PagingAndSortingRepository|EnableJpaRepositories|org\\.springframework\\.data|@Repository" pom.xml src/main src/test -S` returns no matches.
- Temporary H2 web run on port 18080 verified `GET /`, `GET /users/register`, `GET /users/login`, registration, duplicate email message, login, invalid login message, and logout.

## Remaining risks

- Post list/detail pages, create-post authorization redirects, and author-only HTTP status change routes remain for later tasks.
