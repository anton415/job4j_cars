# Task 09 — Implement photo upload and image display

## Goal

Allow users to attach a photo to a car sale post and display it in list/detail pages.

## Instructions for Codex

1. Configure multipart upload if not already configured.
2. Add `MultipartFile` handling to create-post controller method.
3. Save uploaded files to a local directory, for example `uploads/`.
4. Generate unique filenames to avoid overwriting existing files.
5. Store relative path or filename in `Post.photoPath`.
6. Serve images through Spring MVC static resource mapping or a controller endpoint.
7. Update list/detail templates to show image if present.
8. Add upload directory to `.gitignore`, keeping optional `.gitkeep`.

## Acceptance criteria

- Post can be created without photo.
- Post can be created with photo.
- Uploaded photo is visible on list/detail pages.
- Uploaded files are not committed to Git.
- Invalid/empty upload does not break post creation.

## Suggested Codex prompt

Implement Task 09 from `docs/codex/tasks/09_photo_upload.md`. Add simple local photo upload for posts. Store only the path/filename in the database. Display images in Thymeleaf. Do not store binary files in the database. After that mark task as done.

## Status

Done on 2026-05-28.

## Verification

- `mvn test` passes: 13 tests, 0 failures, 0 errors.
- `rg -n "spring-boot-starter-data-jpa|JpaRepository|CrudRepository|PagingAndSortingRepository|EnableJpaRepositories|org\\.springframework\\.data|@Repository" pom.xml src/main src/test -S` returns no matches.
- H2-backed runtime check with `mvn spring-boot:test-run` on port 18083:
  - authenticated multipart `POST /posts/create` with `photo` creates a post and redirects to detail;
  - detail page renders an `/uploads/...` image URL;
  - `GET /uploads/...` returns `200 image/png`;
  - authenticated multipart `POST /posts/create` without `photo` also creates a post.

## Remaining risks

- Uploaded files are stored on the local filesystem only; there is no cleanup if database creation fails after a file is saved.
- Status-changing actions are still disabled and remain Task 10.
