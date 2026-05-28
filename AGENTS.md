You are working in the job4j_cars repository.

Before coding:
1. Read docs/codex/CODEX_RULES.md.
2. Read docs/codex/SPEC.md.
3. Read docs/codex/BACKLOG.md.
4. Read the selected task file if one is explicitly set for the current request.

Rules:
- Do not use Spring Data.
- Do not add JpaRepository, CrudRepository, @Repository from Spring Data, or derived query methods.
- Use Hibernate SessionFactory injected into repository classes.
- Keep three layers: controller, service, repository.
- Write the minimum code needed for the current task and its acceptance criteria.
- This is an educational project: prefer direct course-style solutions over generic frameworks, reusable abstractions, and future-proofing.
- Do not add unused classes, methods, templates, configuration, DTOs, interfaces, mappers, validators, or tests "just in case".
- Prefer small, reviewable changes.
- Do not jump to later tasks.
- If the task is blocked, explain the blocker and propose the smallest next fix.

After coding:
- Run mvn test or mvn package if possible. For documentation-only changes, Maven verification is optional; state if it was skipped.
- Summarize changed files.
- Update the selected task file with status, verification, and remaining risks only when a selected task file was explicitly set.
