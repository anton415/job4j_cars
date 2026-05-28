# Task 07 — Implement post list and detail pages

## Goal

Create the main user-facing pages for viewing car sale posts.

## Instructions for Codex

1. Add `PostController` routes:
   - `GET /` redirecting to `/posts` or rendering the list directly;
   - `GET /posts`;
   - `GET /posts/{id}`.
2. Add templates:
   - `posts/list.html`;
   - `posts/detail.html`.
3. Show required post fields from `SPEC.md`.
4. Show create button/link.
5. Show status as available/sold.
6. Show status change button only for the author; the actual status-changing action will be completed in a later task if not yet implemented.

## Acceptance criteria

- Anonymous user can view all posts.
- Anonymous user can open post detail page.
- List page shows car data, author and status.
- Detail page shows full description.
- No lazy-loading errors in templates.

## Suggested Codex prompt

Implement Task 07 from `docs/codex/tasks/07_post_list_detail.md`. Add post list and detail pages using Thymeleaf. Make sure repositories/services load `User` and `Car` data before rendering. Keep create/status actions minimal if later tasks are not complete yet.

## Status

- [x] Done

## Verification

- `mvn test` — passed, 13 tests.
- Runtime check with H2-backed app on port 8081:
  - `GET /` returns redirect to `/posts`;
  - `GET /posts` renders the post list page for an anonymous user.

## Remaining risks

- `/posts/create` is only linked for authenticated users; the create form is still Task 08.
- Author-only status buttons are visible but disabled; the status-changing action is still Task 10.
- Photo paths are rendered as image sources when present; upload/static serving is still Task 09.
