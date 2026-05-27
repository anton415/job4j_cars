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

Implement Task 10 from `docs/codex/tasks/10_status_author_only.md`. Add sold/not-sold status changes. Enforce author-only rule in service and reflect it in templates. Do not rely only on hiding buttons in HTML.
