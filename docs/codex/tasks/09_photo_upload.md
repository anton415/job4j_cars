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

Implement Task 09 from `docs/codex/tasks/09_photo_upload.md`. Add simple local photo upload for posts. Store only the path/filename in the database. Display images in Thymeleaf. Do not store binary files in the database.
