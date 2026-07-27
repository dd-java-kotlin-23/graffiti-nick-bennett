# Repository Guidelines

## Core Principles

- Do only what the current request asks; when the correct scope is unclear, ask.
- Prefer the smallest change that satisfies the request.
- **Never report a result you did not observe.**
- **Never take an irreversible action without an explicit instruction to do so in the current prompt.**
- The work must remain something the requester can explain.

### Precedence and conflicts

- Where instructions conflict, the order of authority is: the current prompt; the assignment or lesson instructions in the project documentation; this file; any agent-specific instruction file (`CLAUDE.md`, `GEMINI.md`, `.junie/AGENTS.md`, `.github/copilot-instructions.md`) or skill definition; your own defaults.
- Agent-specific files and skill definitions must not restate the rules in this file; they describe only how to carry them out. Where they conflict with this file, this file wins.
- If two sources conflict and the conflict cannot be resolved by the order above, stop and report it rather than silently choosing one.

## Git Workflow

- Before making any changes, or simply when requested by me to do so, check for unstaged or uncommitted changes.
- If unstaged or uncommitted changes are already present, inspect them with a diff before committing them.
- Commit pre-existing unstaged or uncommitted changes before making your own edits.
- For commits that capture pre-existing changes, use `Commit by {agent}: ` (where `{agent}` is replaced by your name) followed by a short summary line.
- Keep the first line to no more than 72 characters.
- After a blank line, include a summary based on the diff, proportionate to the change: a single sentence for a small or self-evident change, up to 1-2 paragraphs where the change spans several files or a decision needs explaining.
- After making changes in response to a prompt, commit those changes.
- For commits that capture changes made in response to a prompt, use `Changes by {agent}: ` (where `{agent}` is replaced by your name) followed by a short summary line.
- Keep the first line to no more than 72 characters.
- After a blank line, include a summary proportionate to the change in the same way, covering why the change was made and not only what changed.
- After another blank line, include `Prompt: ` followed immediately on the same line by the prompt that led to the change.
- The above commit message formats are minimal requirements; if your internal instructions require the use of trailers (e.g., `Co-authored-by`), add them in addition to the specified content.

### Session Snapshot Exception

- Session snapshot files may be created and committed without first committing unrelated existing changes.
- A session snapshot commit must contain only the newly created snapshot file.
- Existing staged and unstaged changes must remain untouched.

## Irreversible and Destructive Operations

Do not perform any of the following unless the current prompt explicitly and specifically asks for it:

- Rewrite history: `git commit --amend`, `git rebase`, `git reset --hard`, any
  `git filter-*` command, or any force-push.
- Push to a remote, or create, merge, or close a pull request.
- Delete or rename a branch; drop or clear a stash.
- Change remotes, credentials, or repository settings.
- Delete or overwrite a file you did not create in this session, or run `git clean`.
- Stage or commit with `git add -A`, `git add .`, or `git commit -a`; always stage and commit by explicit pathspec.

Never rewrite a commit that has already been pushed. If asked to do so, explain the consequences and propose a corrective commit instead; proceed only if the requester confirms.

Prefer additive recovery: correct a mistake with a new commit rather than by rewriting or discarding work. If a task appears to require one of the operations above, explain why and wait for an instruction.

## Protected Paths and the Assignment Contract

### Protected paths

Do not modify, move, or delete any of the following:

- Build and toolchain files: `build.gradle.kts`, `settings.gradle.kts`,
  `gradle.properties`, `gradle/**`, `gradlew`, `gradlew.bat`.
- Repository metadata: `LICENSE`, `README.md`, `.gitignore`.
- Agent instructions: `AGENTS.md`, `AGENTS.override.md`, and `AGENT.md` in any directory, not only the repository root; `CLAUDE.md`; `GEMINI.md`; `.github/copilot-instructions.md`; `.github/instructions/**`.
- Agent configuration and capability grants: `.agents/**`, `.claude/**`, `.codex/**`, `.copilot/**`, `.gemini/**`, `.junie/**`, `.mcp.json`, `.github/agents/**`, `.github/chatmodes/**`, `.github/prompts/**`, `.github/workflows/**`.
- Agent context-exclusion files: `.aiexclude`, `.aiignore`, `.geminiignore`.
- Any other file that instructs, configures, or grants capabilities to an AI coding agent, whether or not it appears above. Apply two tests in order: first, whether the file sits under a directory named for an agent vendor
  (`.agents/`, `.claude/`, `.codex/`, `.copilot/`, `.gemini/`, `.junie/`) or has a name containing `agent`, `claude`, `codex`, `copilot`, `gemini`, or
  `junie`; then, for anything else, whether its purpose is to direct an agent.
    The lists above are illustrative, not exhaustive.
- Any file the assignment identifies as provided, fixed, or instructor-supplied.

### Assignment contract

- Where the assignment specifies a declaration, do not change any aspect of it — name, modifiers, type parameters, parameters, return type, or thrown types.
- Do not modify, delete, disable, or weaken a provided test in order to make it pass; correct the implementation instead. If a test itself appears to be incorrect, say so and stop.
- Do not add, remove, or change dependencies or their versions. The dependency set is supplied by the shared version catalog.

### Explicit-ask exception

Any rule in this section may be set aside when the current prompt explicitly and specifically asks for the change, naming the file, declaration, test, or dependency involved.

- A general instruction — for example, "make the tests pass", "fix the build", or "do whatever it takes" — is **not** an explicit ask.
- Before proceeding, state which rule is being set aside and what the change affects.
- Note the exception in the body of the resulting commit message.

## Verification and Honesty

- Verify with the project's own checks, and report both the command you ran and its actual outcome.
- **Do not state that code compiles, that tests pass, or that the program runs unless you observed it in this session.** Report it as unverified instead.
- Report failures with the relevant output; do not summarize a failure into a success.
- Do not hardcode expected values, special-case test inputs, or stub a member in order to satisfy a check.
- If you are blocked, say so and stop. Do not silently substitute a partial or placeholder solution; if you deliver one, say that is what it is.
- Distinguish verified facts from assumptions and recommendations in every report, not only in session snapshots.

## Change Discipline

A diff that mixes the intended change with incidental churn cannot be reviewed usefully, and an unreviewable history defeats the purpose of the commit conventions above.

- Make the smallest coherent change that satisfies the request, and keep one logical change per commit.
- Do not reformat, reorganize, or refactor code you were not asked to change.
- Do not rewrite an entire file when an edit will do. Preserve the existing formatting, line endings, and character encoding.
- Match the conventions of the surrounding code and documentation.
- Do not add comments that narrate the change; that belongs in the commit message.
- Do not create files — scripts, notes, configuration — that were not requested.

## Session Snapshots

A *session snapshot* captures the durable context of a work session — the goal, what was done, decisions, current state, and how to resume — as a single Markdown
file in the project. Any compatible agent or assistant, when asked to "create a snapshot" (or similar), must follow this section.

For the purposes of a snapshot, a "session" begins with a user's prompt that initiates a new goal or task. The agent should use the conversation history from that point forward to generate the snapshot content.

### Location and filename

- Write snapshots to `snapshots/` in the project root.
- Name each file `snapshot-YYYYMMDD-HHMMSS[-+]xxxx-<slug>.md`, where the timestamp is the local date and time with offset, and `<slug>` is a short, kebab-case summary of the session's
  focus (for example, `snapshot-20260724-143005-0600-add-scoring-command.md`).
- The slug should be 2–5 words and based on the initial objective of the session.
- Never overwrite an existing snapshot.

### Content

Synthesize the durable context of the work session. Do not copy or embed a full conversation transcript. Use these sections in order, fill every section, and write "None" where a section does not apply.

1.  **Title** — `# Session Snapshot: <short description>`. The description should be a one-sentence summary of the work performed.
2.  **Metadata** — `### Metadata` followed by the date/time (RFC 3339 format, using a space separator between date and time, and including the time offset), current branch, and current HEAD commit (7-character short hash). The producing tool or agent may be recorded when known, but the snapshot must not depend on a particular agent or model.
3.  **Objective** — what the session set out to accomplish, based on the initial user prompt. This may be augmented if the session's goal was refined or shifted.
4.  **Work done** — a concise summary of what was actually done.
5.  **Key decisions & rationale** — choices made and why. This should explain the reasoning behind implementation choices, referencing project context, files, and user requests.
6.  **Current state** — what is working and what is still in progress.
7.  **Files touched** — repository-relative paths of modified files (excluding the snapshot file itself), with a one-line note each.
8.  **Key files read** — up to 50 of the most important files read during the session, with a one-line note each, using the agent's assessment to select those most relevant to the work.
9.  **Verification** — tests or checks performed, their results, and anything not yet verified.
10. **Working-tree state** — a concise status summary, including whether unrelated changes were already present, and listing all file names normally included in the output from `git status`.
11. **Open questions / blockers** — anything unresolved.
12. **Next steps** — concrete, actionable points to resume from.
13. **Key commands & references** — commands, URLs, or docs worth keeping.

### Safety and portability

- Write plain Markdown that does not depend on a particular agent, model, client, IDE, operating system, or proprietary session format.
- Do not include full conversation transcripts, secrets, credentials, authentication headers, private keys, or secret environment-variable values.
- Use repository-relative paths.
- Distinguish verified facts from assumptions and recommendations.
- Record commands and verification results, but omit large logs and full diffs unless indispensable.
- Review the finished snapshot for accidental sensitive information before saving or committing it.

### Committing (Git repositories only)

Before committing, run `git rev-parse --is-inside-work-tree`. If it fails (not a Git repository), report the written file path and stop — do not attempt a commit.

The rules in this subsection replace the standard commit-message rules under Git Workflow for session snapshot commits.

If it succeeds, commit **only** the snapshot file, using a pathspec commit so that any other staged or unstaged changes remain untouched (see the Session Snapshot Exception above):

```
git add -- snapshots/<file>
git commit -m "Snapshot: <summary>" -m "<description>" --trailer "Co-authored-by: <agent> <email>" -- snapshots/<file>
```

- Replace `<summary>` with a short description (one sentence) of the work performed, and keep the first line to no more than 72 characters.
- Replace `<description>` with a 1–2 sentence description of the session's outcome.
- Replace `<agent>` and `<email>` with the name and email of the agent creating the snapshot. A trailer **must** be used for session snapshot commits.
- Options such as `-m` must come **before** the `--`; everything after `--` is treated as a file path, not an option.
- These commands are identical on Windows, macOS, and Linux; do not introduce a shell script or any Git Bash dependency.
- Verify that the resulting commit contains only the snapshot and that pre-existing staged and unstaged changes remain present and unchanged.
