You are working in the job4j_cars repository.

Before coding:
1. Read docs/codex/CODEX_RULES.md.
2. Read docs/codex/SPEC.md.
3. Read docs/codex/BACKLOG.md.
4. Read the selected task file.

Selected task:
docs/codex/tasks/01_audit_baseline.md

Rules:
- Do not use Spring Data.
- Do not add JpaRepository, CrudRepository, @Repository from Spring Data, or derived query methods.
- Use Hibernate SessionFactory injected into repository classes.
- Keep three layers: controller, service, repository.
- Prefer small, reviewable changes.
- Do not jump to later tasks.
- If the task is blocked, explain the blocker and propose the smallest next fix.

After coding:
- Run mvn test or mvn package if possible.
- Summarize changed files.
- Update the selected task file with status, verification, and remaining risks.