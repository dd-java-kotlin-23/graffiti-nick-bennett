---
name: create-session-snapshot
description: Create a durable, agent-neutral Markdown snapshot of the current work session inside this project. Use when the requester asks to create, take, save, or commit a session snapshot, checkpoint, handoff, or resumable context record.
---

# Create a Session Snapshot

Create a concise, portable record that another person or compatible agent can
use to resume the work.

## Follow the Project Protocol

Locate and read every applicable `AGENTS.md` before changing the project.

Follow the root `AGENTS.md` **Session Snapshots** section as the authoritative
specification for the snapshot location, filename, content, safety rules, and
commit procedure. Do not substitute a fallback format or location.

## Execute the Workflow

1. Gather the session objective, completed and remaining work, decisions,
   relevant files, verification results, working-tree state, blockers, and next
   actions.
2. Collect the Git metadata and status required by `AGENTS.md`.
3. Synthesize the context rather than copying the conversation transcript.
4. Review the snapshot for sensitive information and portability.
5. Commit only the snapshot file using the path-limited procedure in
   `AGENTS.md`; leave unrelated changes untouched.
6. Verify the commit file list and final working-tree state.
7. Report the snapshot path, commit hash, and whether unrelated changes remain.

If the project protocol cannot be followed safely, stop and report the exact
conflict rather than broadening the operation.
