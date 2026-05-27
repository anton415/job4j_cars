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
