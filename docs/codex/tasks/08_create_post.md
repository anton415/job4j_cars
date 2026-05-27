# Task 08 — Implement create post flow with car fields

## Goal

Allow authenticated users to create car sale posts with car characteristics.

## Instructions for Codex

1. Add routes:
   - `GET /posts/create`;
   - `POST /posts/create`.
2. Add `posts/create.html` form.
3. Form must collect:
   - title;
   - description;
   - price;
   - brand;
   - model;
   - year;
   - body type;
   - engine type;
   - transmission;
   - mileage.
4. If user is not logged in, redirect to login page.
5. Create `Car`, then create `Post` linked to the current user and car.
6. Default status must be `sold = false`.
7. Add basic validation and user-friendly errors.

## Acceptance criteria

- Authenticated user can create post.
- Anonymous user cannot create post.
- Created post appears on the main page.
- Car fields appear on list/detail pages.
- No Spring Data is used.

## Suggested Codex prompt

Implement Task 08 from `docs/codex/tasks/08_create_post.md`. Add authenticated create-post flow with car fields. Use service layer and manual Hibernate repositories. Keep validation simple and readable.
