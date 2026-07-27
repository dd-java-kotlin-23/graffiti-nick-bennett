---
name: snapshot
description: >-
  Capture the critical context of the current agent session as a markdown file
  in the project and, if it is a Git repository, commit that file in isolation.
  Use whenever the user asks to "create a snapshot", "snapshot this session",
  "capture context", "save session context", or invokes /snapshot.
---

# Session Snapshot

Create a session snapshot: a single Markdown file that captures the critical
context of this session so the work can be resumed later.

## Follow the Project Protocol

`AGENTS.md` in the repository root is the authoritative specification for the
snapshot location, filename, required content, safety rules, and commit
procedure. Read its **"Session Snapshots"** section and follow it exactly. Do
not substitute a fallback format or location.

This skill adds only the Claude-specific mechanics for carrying that out. If
anything here conflicts with `AGENTS.md`, `AGENTS.md` wins.

## Procedure

1. **Read `AGENTS.md`** and note the required sections, the target path and
   filename pattern, and the commit procedure.

2. **Synthesize the context.** There is no transcript to copy — write the
   snapshot yourself from the session.

3. **Gather metadata.** Use Bash to run the read-only Git commands `AGENTS.md`
   requires, and record the date/time, your agent name (Claude), and the model.

4. **Write the file** with the Write tool, at the path and filename `AGENTS.md`
   specifies.

5. **Commit — Git repositories only.** Confirm this is a Git repository, then use
   Bash to run the path-limited commit procedure in `AGENTS.md` verbatim,
   including its option-ordering caveat.

6. **Verify** that the commit contains only the snapshot file and that
   pre-existing staged and unstaged changes remain present and unchanged.

7. **Report** the snapshot path, the commit hash, and whether unrelated changes
   remain.
