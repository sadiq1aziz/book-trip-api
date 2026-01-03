## Book Trip API

A Spring Boot backend service designed to empirically evaluate the performance characteristics of **Java platform threads vs virtual threads (Project Loom)** under high-concurrency, I/O-bound workloads.

This project uses a simplified trip reservation API as a test harness where each incoming request fans out to multiple downstream service calls (via a mocked stub). By isolating threading behavior from business complexity and external dependencies, it measures the direct impact of thread model choice on throughput, latency distribution (particularly p95/p99), and resource utilization under load. The goal is to demonstrate when and why virtual threads provide measurable advantages over traditional platform threads in concurrent, blocking I/O scenarios.

## Table of Contents

- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Threading Model](#threading-model)
- [Load Testing Strategy](#load-testing-strategy)
- [Performance Results](#performance-results)
- [JMeter Test Results](#jmeter-test-results)
- [Key Observations](#key-observations)
- [External Downstream Stub](#external-downstream-stub)
- [Repository Structure](#repository-structure)
- [Running the Tests](#running-the-tests)
- [Learnings](#learnings)
- [Author](#author)

## Architecture

<img width="1237" height="607" alt="booktripapi" src="https://github.com/user-attachments/assets/283e70a5-b169-47d6-81a1-24556cc86ab9" />


- Each request triggers downstream calls
- Downstream dependency is mocked to isolate threading behavior
- No database — latency driven by I/O simulation

## Tech Stack

- Java 21+
- Spring Boot
- Virtual Threads (Loom)
- Apache JMeter 5.6.3
- Maven

## Threading Model

The application runs in two modes:

**Platform Threads (Traditional)**
- Backed by OS threads
- Limited scalability under blocking I/O

**Virtual Threads (Loom)**
- Lightweight JVM-managed threads
- Efficient parking/unparking during blocking calls

Thread mode is configurable at runtime.

## Load Testing Strategy

**Test Configuration:**
- Concurrent users: 300
- Ramp-up: 300 seconds
- Duration: 360 seconds
- Multiple downstream calls per request

**Metrics Observed:**
- Throughput (requests/sec)
- Average latency
- p90 / p95 / p99 latency
- Error rate

## Performance Results

### Platform Threads

| Metric | Value |
|--------|-------|
| Throughput | ~143 req/sec |
| Avg Latency | ~1219 ms |
| p95 | ~1873 ms |
| p99 | ~1913 ms |

### Virtual Threads

| Metric | Value |
|--------|-------|
| Throughput | ~173 req/sec |
| Avg Latency | ~1008 ms |
| p95 | ~1013 ms |
| p99 | ~1017 ms |

## JMeter Test Results

Visual comparison of threading models under load:

### Response Time Over Time
Latency trends during test execution for both platform and virtual threads.

### Transactions Per Second
Throughput comparison under sustained concurrent load.

### Aggregate Reports
Detailed breakdown of percentiles (p50, p90, p95, p99) and error rates.

*Screenshots available in `/docs/jmeter-results/`*

## Key Observations

**Virtual threads delivered:**
- ~20% higher throughput
- Significantly tighter p95/p99 latency

**Platform threads showed:**
- Tail latency inflation under load

**Thread dumps confirmed:**
- Virtual threads mostly parked during I/O
- No thread starvation or deadlocks observed

## External Downstream Stub

This project depends on an external JAR (`downstream-stub.jar`) that simulates downstream services.

**Why it's external:**
- Acts as a test double with deterministic responses
- Intentionally not committed to version control

**Location (local only):**
```
/external/downstream-stub.jar
```

⚠️ The application will not start without this JAR.

## Repository Structure

```
book-trip-api/
 ├─ src/
 ├─ pom.xml
 ├─ README.md
 ├─ .gitignore
 ├─ docs/
 │   └─ jmeter-results/
 └─ external/              # local stub JAR (not committed)
```

## Running the Tests

Execute JMeter in non-GUI mode:

```bash
jmeter -n -t trip-reserve.jmx -l results.jtl
```

Result files (`.jtl`) are excluded from Git.

## Learnings

- Throughput alone is misleading — tail latency matters
- Virtual threads excel when requests block on I/O
- Thread dumps are essential to validate threading behavior

## Author

**Sadiq Aziz**  
[GitHub](https://github.com/sadiq1aziz)
