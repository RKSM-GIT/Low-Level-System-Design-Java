(For building a comprehensive boto3-based AWS interface + instruction manual — no generative logic, no tests)

⸻

SYSTEM / CONTEXT

You are a senior AWS systems engineer and Python SDK developer.
Your task: Build a Python interface (library) that exposes safe, structured, and exhaustive wrappers around AWS services using boto3, plus a detailed Markdown instruction manual describing how an AI agent should use it.

This program does not use generative AI.
It acts as a low-level operational layer — a bridge between an AI control system and AWS infrastructure.

The downstream AI agent will receive both the Python interface and the instruction manual and will use them to:
	•	Query and manage AWS resources,
	•	Collect and return diagnostic information,
	•	Execute safe, controlled actions in multiple AWS regions and Availability Zones.

⸻

OBJECTIVE

Deliver exactly two artifacts:
	1.	A Python interface package — aws_interface/
	2.	A Markdown instruction manual — INSTRUCTION_MANUAL.md

⸻

OUTPUT FORMAT

Return your results as Markdown sections with filenames in headers and fenced code blocks:
### `aws_interface/interface.py`
```python
# File content
```

Files required:
	•	aws_interface/__init__.py
	•	aws_interface/interface.py
	•	aws_interface/utils.py
	•	INSTRUCTION_MANUAL.md
	•	README.md

⸻

DESIGN GOALS

✅ Scope
	•	Provide exhaustive, modular coverage of AWS operations via boto3 (read and control capabilities).
	•	Include helpers for:
	•	Authentication and session management (multi-region, multi-AZ)
	•	Resource inspection (ECS, EC2, Lambda, S3, CloudWatch, IAM, RDS, EventBridge, SQS, SNS, ECR, etc.)
	•	Log fetching and metric retrieval
	•	Resource creation, modification, and deletion (with safety flags)
	•	Handle rate limiting, pagination, and cross-region retries transparently.
	•	Offer a consistent structured output format across all methods.

🚫 Out of Scope
	•	No chat logic, prompt parsing, or generative text reasoning.
	•	No automated tests or pytest structure.
	•	No frontend or orchestration.

⸻

FUNCTIONAL REQUIREMENTS

Core Class: AWSInterface

Main entry point for all AWS interactions.

Constructor
def __init__(self, regions: list[str] | None = None, profile: str | None = None, assume_role_arn: str | None = None)

Establishes a boto3 multi-region session pool.
	•	If assume_role_arn is provided, assumes the specified role in each region.
	•	Stores session clients per service/region pair.
	•	Exposes safe getters for each service.

Required Capabilities

1. Session & Metadata
	•	list_regions()
	•	list_availability_zones(region: str)
	•	get_caller_identity()

2. Resource Discovery
	•	list_ec2_instances(region: str)
	•	list_lambda_functions(region: str)
	•	list_ecs_services(region: str)
	•	list_iam_roles(region: str)
	•	list_s3_buckets()
	•	list_rds_instances(region: str)
	•	list_ecr_repositories(region: str)
	•	list_sqs_queues(region: str)
	•	list_eventbridge_rules(region: str)

3. Describe / Inspect
	•	describe_instance(instance_id: str, region: str)
	•	describe_lambda(function_name: str, region: str)
	•	describe_ecs_service(cluster_name: str, service_name: str, region: str)
	•	get_logs(log_group: str, region: str, start_time: datetime, end_time: datetime, filter_pattern: str | None = None)

4. Metrics & Monitoring
	•	get_metric_statistics(namespace: str, metric_name: str, dimensions: list[dict], region: str, start_time, end_time, period=300, statistic='Average')

5. Control (Safe Modification Layer)

All modification methods must default to dry_run=True and require explicit confirm=True for real execution.
	•	stop_instance(instance_id: str, region: str, dry_run=True, confirm=False)
	•	start_instance(instance_id: str, region: str, dry_run=True, confirm=False)
	•	update_lambda_config(function_name: str, region: str, config_changes: dict, dry_run=True, confirm=False)
	•	delete_sqs_queue(queue_url: str, region: str, dry_run=True, confirm=False)
Each should return a dictionary:
{
  "action": "start_instance",
  "resource": "i-1234",
  "region": "us-east-1",
  "dry_run": True,
  "status": "simulated" | "executed",
  "message": "Instance start simulated. Confirm=True required to execute."
}

6. Utility Features
	•	Logging with timestamps and region context
	•	Retry and rate-limit handling
	•	Structured error normalization ({"code": "Throttling", "message": "...", "suggested_action": "retry"})

⸻

MULTI-REGION AND AVAILABILITY ZONE SUPPORT
	•	Support dynamic region enumeration from ec2.describe_regions()
	•	For each region, support ec2.describe_availability_zones()
	•	Maintain per-region clients (EC2, ECS, CloudWatch, Lambda, etc.) in a dictionary cache
	•	Allow switching default region dynamically
	•	Ensure all API methods accept a region argument

Example:

interface = AWSInterface(regions=["us-east-1", "us-west-2"])
for region in interface.regions:
    print(interface.list_availability_zones(region))

CHECKPOINT TASK PLAN

Each phase is a clear development checkpoint with verification steps.

🧩 CHECKPOINT 1 — Setup & Session Management

Goal: Initialize the interface with multi-region sessions.
	•	Create base AWSInterface class.
	•	Implement session pooling for multiple regions.
	•	Verify get_caller_identity() and list_regions().

Verification:
iface = AWSInterface(regions=["us-east-1", "us-west-2"])
print(iface.get_caller_identity())
print(iface.list_regions())

🧩 CHECKPOINT 2 — Resource Discovery

Goal: Implement list methods for EC2, ECS, Lambda, etc.
Verification:
iface.list_ec2_instances("us-east-1")
iface.list_lambda_functions("us-east-1")

🧩 CHECKPOINT 3 — Resource Description

Goal: Add describe/detail calls for common AWS resources.
Verification:
iface.describe_lambda("my-function", "us-east-1")
iface.describe_instance("i-xxxx", "us-east-1")

🧩 CHECKPOINT 4 — Logs & Metrics

Goal: Add CloudWatch log and metric retrieval.
Verification:
iface.get_logs("my-log-group", "us-west-2", start_time, end_time)
iface.get_metric_statistics("AWS/Lambda", "Duration", dims, "us-east-1", start, end)

🧩 CHECKPOINT 5 — Safe Control Operations

Goal: Implement dry-run controls (start/stop, config updates, deletes).
Verification:
iface.stop_instance("i-xxxx", "us-west-2", dry_run=True)

🧩 CHECKPOINT 6 — Documentation & Manual

Goal: Write INSTRUCTION_MANUAL.md and README.md.
	•	Include detailed usage guide for the AI agent.
	•	Provide example commands and explanations per function.
	•	Include IAM least-privilege policy examples for safe usage.

⸻

INSTRUCTION MANUAL REQUIREMENTS

Sections
	1.	Overview – Purpose of the interface, intended users (AI agents).
	2.	Setup Guide – AWS credential and region setup.
	3.	Method Reference – Function list, parameters, and examples.
	4.	Safety Guidelines – Confirm flags, dry-run behavior, region-specific warnings.
	5.	Multi-Region Usage Examples – Cross-region operations and concurrency tips.
	6.	IAM Policy Examples – For read-only and limited-control use.
	7.	Error Handling & Logging – How to interpret returned errors.

⸻

TECHNICAL REQUIREMENTS
	•	Python ≥ 3.10
	•	Libraries: boto3, botocore, logging, datetime, concurrent.futures
	•	Thread-safe design for multi-region concurrency (read operations).
	•	Clean docstrings, type hints, structured responses.
	•	No credential hardcoding; use environment variables or IAM roles.

⸻

COMPLETION CRITERIA

✅ Must deliver:
	•	Fully functional aws_interface Python package (with boto3-based multi-region logic).
	•	Detailed INSTRUCTION_MANUAL.md explaining every capability.
	•	README.md with install instructions and minimal usage.
	•	No generative AI logic, no tests, no orchestration — only interface + manual.

⸻

RESPONSE FORMAT

Provide Markdown sections for:
	•	aws_interface/__init__.py
	•	aws_interface/interface.py
	•	aws_interface/utils.py
	•	INSTRUCTION_MANUAL.md
	•	README.md

⸻

END OF PROMPT
