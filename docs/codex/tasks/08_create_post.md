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

## Status

Done on 2026-05-28.

## Verification

- `mvn test` passes: 13 tests, 0 failures, 0 errors.
- `rg -n "spring-boot-starter-data-jpa|JpaRepository|CrudRepository|PagingAndSortingRepository|EnableJpaRepositories|org\\.springframework\\.data|@Repository" pom.xml src/main src/test -S` returns no matches.
- H2-backed runtime check with `mvn spring-boot:test-run` on port 18082:
  - anonymous `GET /posts/create` redirects to `/users/login`;
  - authenticated `GET /posts/create` renders the create form;
  - authenticated `POST /posts/create` creates a post and redirects to `/posts/1`;
  - `/posts` and `/posts/1` render the created car fields.

## Remaining risks

- Photo upload is not implemented yet; it remains Task 09.
- Status-changing actions are still disabled and remain Task 10.
