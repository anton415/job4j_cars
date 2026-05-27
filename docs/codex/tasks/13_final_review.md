# Task 13 — Final review against job4j Web requirements

## Goal

Perform final cleanup before submitting the project.

## Instructions for Codex

1. Search for forbidden Spring Data usages again.
2. Check package structure and layer boundaries.
3. Check migrations and model mappings.
4. Check README completeness.
5. Run tests if possible.
6. Check that anonymous/authenticated/author-only flows work logically.
7. Remove dead code, unused imports and accidental debug output.
8. Do not perform large rewrites unless required for correctness.

## Acceptance criteria

- No Spring Data usage.
- Three-layer architecture is visible.
- Required repositories exist.
- Main page, create page, auth flow, photo flow and status flow are implemented.
- README is submission-ready.
- `mvn test` passes or all remaining failures are explicitly explained.

## Suggested Codex prompt

Implement Task 13 from `docs/codex/tasks/13_final_review.md`. Perform a final mentor-readiness review and small cleanup only. Verify the no-Spring-Data rule, layer boundaries, README, migrations and tests. Summarize remaining risks honestly.
