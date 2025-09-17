You are a senior Java backend + systems engineer. Design and implement a collaborative terminal-based text editor in Java, where multiple users connect to a server, join a room, and edit a single shared document in real time.

⸻

Goals & Priorities
	1.	Terminal interface: text-based editor using a simple Java library (e.g., Lanterna or raw ANSI escape codes).
	2.	Collaboration: multiple clients connect to the server, edits sync in real time.
	3.	Architecture:
	•	Server: manages rooms, canonical documents, edit sequencing, broadcast of changes.
	•	Client (Terminal UI): connects via sockets/WebSocket, shows document, sends keystroke or line edits, shows updates from others.
	4.	Conflict resolution: start with central sequencing server model (server assigns global order to edits). Explain OT vs CRDT and give migration notes.
	5.	Features (MVP scope):
	•	Join/create room by name
	•	Edit document (line-based or character-based, whichever is simpler for MVP)
	•	Show who’s online (presence list)
	•	Remote edits appear live
	•	Save document on server side
	6.	Non-goals (MVP): advanced permissions, encryption, syntax highlighting, mouse support.

⸻

Deliverables
	1.	Architecture overview: textual diagram + explanation.
	2.	File layout: project structure (Maven/Gradle).
	3.	Protocol spec: JSON message schema (join, op, ack, presence, syncRequest).
	4.	Server code: Java-based (Spring Boot with WebSockets, or plain Java sockets). Handles rooms, documents, sequencing, persistence.
	5.	Client code: Java terminal client (Lanterna preferred, or raw ANSI escape if simpler). Supports editing, shows updates, presence.
	6.	Example run:
	•	Start server
	•	Start 2 terminal clients
	•	Both type edits → edits appear live
	•	Save document
	7.	Worked concurrency example: two clients editing same line, show how server resolves sequence.
	8.	Unit tests: for sequencing logic & basic client-server communication.
	9.	Run instructions: build commands, how to start server + client, how to simulate multiple users.
	10.	Discussion: why central sequencing first, how to migrate to CRDT, pitfalls (latency, reconnection, terminal redraw performance).

⸻

Constraints & Assumptions
	•	Must be terminal-only (no GUI).
	•	Document is plain text (UTF-8).
	•	Single server → many clients (LAN friendly).
	•	Minimal dependencies: Lanterna (for terminal UI) or ANSI escapes; JSON (Jackson/Gson).
	•	Keep code runnable with Maven/Gradle defaults.

⸻

Required Output Format
	•	Section 1: Architecture diagram (ASCII text ok).
	•	Section 2: File layout + build/run commands.
	•	Section 3: Protocol spec (with JSON examples).
	•	Section 4: Full Java server code with comments.
	•	Section 5: Full Java client code (terminal editor) with comments.
	•	Section 6: Example concurrency scenario explained with sample JSON traffic.
	•	Section 7: Tests (sequencing + client-server).
	•	Section 8: Step-by-step run instructions.
	•	Section 9: Design rationale & future improvements.

⸻

Tone & Depth
	•	Authoritative but practical.
	•	Provide runnable, well-commented Java code.
	•	Include small worked example: two clients editing the same line, showing how conflicts are resolved.
	•	Keep explanations concise, focusing on “how to run it” + “why this design.”