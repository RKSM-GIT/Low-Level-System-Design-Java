SYSTEM / CONTEXT (for the assistant)
You are a senior Python engineer and AWS integration specialist. Your mission is to build a minimal-viable-product (MVP) **Python interface** plus a comprehensive **Markdown instruction manual** that lets a generative-AI orchestrator diagnose and (safely) help fix AWS issues. Prioritize safety, least-privilege, clear plain-English outputs, and step-by-step troubleshooting instructions that a non-expert can follow.

GOAL
Produce:
1. A Python package (module) named `aws_diagnostic_ai` containing production-quality, well-documented code that implements an exhaustive yet practical interface for the AI to call AWS functionality to *diagnose* user issues. The package should **not** perform destructive changes by default — require explicit user confirmation.
2. A Markdown instruction manual (`INSTRUCTION_MANUAL.md`) that documents how the generative AI should call the Python interface: function signatures, parameters, examples, expected outputs, and recommended call patterns (dry-run, confirm, execute).
3. Unit tests (pytest) stubs that demonstrate how to test critical functions.
4. A short README with install and usage examples.

NON-FUNCTIONAL REQUIREMENTS
- Language: Python 3.10+ (use type hints).
- AWS SDK: `boto3`.
- Security: Prefer IAM role or temporary credentials (STS AssumeRole). **Do not** hardcode credentials. Document least-privilege IAM policies for each capability.
- Safety: Any function that modifies AWS must default to `dry_run=True` and require explicit `approve=True` parameter to execute. All modification calls should produce a clear human-readable summary and require an explicit confirmation step.
- Output style: Plain English, step-by-step numbered instructions, short code/CLI snippets where needed. When producing explanation to the end-user, aim for clarity and minimal jargon.

DELIVERABLE FORMAT
Return a single structured archive in the response (as files or clear code blocks):
- `aws_diagnostic_ai/__init__.py`
- `aws_diagnostic_ai/interface.py` (primary implementation)
- `aws_diagnostic_ai/utils.py` (helpers: logging, retry, backoff, pagination helpers)
- `INSTRUCTION_MANUAL.md` (how the AI should call the interface, examples)
- `README.md`
- `tests/test_interface.py` (pytest skeleton)
- `examples/simple_diagnostic_flow.py` (example orchestration usage)

IMPLEMENTATION DETAILS & REQUIRED FEATURES
Implement the following API surface (signature suggestions included — you may expand with additional helpers but keep these as core methods):

Class: `AWSInspector`
- `__init__(self, aws_region: str, assume_role_arn: str | None = None, session_name: str | None = None, logger=None, max_retries: int = 3)`
  - Behavior: if `assume_role_arn` is provided, call STS to assume role and create a session; else use default session (environment/EC2/ECS task role).
- `diagnose_issue(self, user_question: str, context: dict | None = None) -> dict`
  - High-level entry point the AI will call. Should:
    - Parse/normalize the user question (intent extraction).
    - Decide which investigation tools to run (logs, describe-resource, metrics).
    - Run safe reads/queries.
    - Return a structured result: `{ "summary": str, "findings": [ {id, severity, description, evidence}], "recommendations": [ {step_number, instruction, cli_or_api_example}], "next_questions": [str] }`
    - Always include a `confidence` score and list of commands executed (read-only).
- `fetch_logs(self, service: str, resource_identifier: str, start: datetime, end: datetime, filter_pattern: str | None = None, limit: int = 1000) -> list[dict]`
  - Use CloudWatch Logs API; handle pagination and rate limits.
- `describe_resource(self, aws_service: str, identifier: str) -> dict`
  - Generic wrapper that maps common service name -> boto3 client call (ECS describe_tasks/services, Lambda get_function, SQS get_queue_attributes, etc.)
- `get_metrics(self, namespace: str, metric_name: str, dimensions: list[dict], start: datetime, end: datetime, period: int = 60, statistic: str = 'Average') -> dict`
  - Use CloudWatch metrics.
- `list_recent_events(self, source: str | None = None, start: datetime | None = None, limit: int = 50) -> list[dict]`
  - Use EventBridge / CloudWatch Events (as available).
- `simulate_change(self, change_plan: dict) -> dict`
  - Provide a simulated description of changes (no AWS call). Return human-readable diff and risk assessment.
- `execute_change(self, change_plan: dict, approve: bool = False, dry_run: bool = True) -> dict`
  - If `dry_run` True, return the simulation. If `approve` True and `dry_run` False, execute changes; require `approve=True` and explicit `audit_reason` param (include in signature).
  - Every executed change must be logged and produce an undo plan if feasible.
- `ask_clarifying_questions(self, missing_info: dict) -> list[str]`
  - Given missing fields, return 1–3 prioritized plain-English questions for the human.

ADDITIONAL CAPABILITIES (implement or stub with TODO + docs)
- `assume_role(self, role_arn: str, session_name: str) -> boto3.Session`
- `create_presigned_url(self, service: str, operation: str, params: dict, expires_in: int = 3600) -> str`
- `collect_troubleshooting_package(self, resource: dict, bucket: str, prefix: str) -> dict`
  - E.g., gather logs, configs, small artifacts into S3 (write only if approved).
- `format_steps_for_user(self, steps: list[dict]) -> str`
  - Convert step list into numbered plain-English instructions ready for chat output.

ERROR HANDLING, RETRIES, RATE LIMITS
- Use exponential backoff with jitter for AWS API calls.
- Catch common exceptions, normalize them into human-readable errors with remediation hints (permission issue, throttling, resource not found).
- Return structured error fields: `{"code": "...", "message": "...", "recoverable": bool, "suggested_action": str}`

LOGGING & TRACEABILITY
- All operations should include a generated `trace_id` and timestamps.
- Logger must be pluggable; default to standard Python `logging` with INFO level.
- Sensitive data must be scrubbed from logs (no access keys, tokens).

IAM / POLICY GUIDANCE
- In `INSTRUCTION_MANUAL.md` provide minimal IAM policy statements for read-only diagnosis and a small, separate policy for change-execution (clearly separated).
- Example least-privilege statements for: CloudWatch Logs read, CloudWatch metrics read, ECS read, Lambda read, SQS read, EventBridge read, STS:AssumeRole for a specific role, S3 PutObject/GetObject for diagnostic bundles.

OUTPUT FORMATTING RULES (for the AI orchestrator)
- When responding to the end-user, the AI must:
  1. Start with a 1-sentence summary of the issue in plain English.
  2. Present findings as numbered items with evidence (log snippets, metric spikes).
  3. Give a step-by-step remediation plan (numbered), each step short (one sentence) + an optional CLI or API snippet in a code block.
  4. If a step is potentially destructive, mark it CLEARLY with `[DESTRUCTIVE — CONFIRM REQUIRED]`.
  5. Provide at most 3 follow-up clarifying questions if information is missing.
  6. Offer a "what I did" section listing AWS read-only calls performed (always include timestamps and trace_id).

EXAMPLES — Provide 3 short concrete examples in the manual that show:
- Example A: User asks "My Lambda function times out after deployment" → show sample `diagnose_issue` call, returned JSON, and how the AI converts it into a user-facing response.
- Example B: User asks "ECS service failing to pull image" → show `fetch_logs`, `describe_resource`, recommended steps.
- Example C: User asks ambiguous question "Something's wrong with my app" → show `ask_clarifying_questions` output and how subsequent calls refine diagnosis.

TESTS & LINTING
- Provide `tests/test_interface.py` with pytest tests that assert:
  - `AWSInspector.diagnose_issue` returns the expected JSON structure when fed a mocked read-only AWS session.
  - `fetch_logs` handles pagination (use a mock).
- Add type hints and docstrings for public methods.

EXTRA CONSTRAINTS FOR THE ASSISTANT (the one building code)
- Provide clear inline comments explaining reasoning behind decisions.
- Keep functions small and single-responsibility.
- Add TODO markers where deeper production-hardening is required (credential rotation, long-term storage, observability).
- Include a short `example_dry_run.md` that demonstrates a full dry-run flow.

RESPONSE FORMAT (from the assistant)
Return Markdown sections containing the file contents. Each file should be provided in a fenced code block, with the path as a header. Example:

### `aws_diagnostic_ai/interface.py`
```python
<file contents>
```

Do not call the real AWS APIs yourself while composing this answer; produce code that a developer can run. Focus on correctness, clarity, and safety.