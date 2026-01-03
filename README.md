[BookTripApi.drawio](https://github.com/user-attachments/files/24414063/BookTripApi.drawio)## Book Trip API

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

[Uploading BookTripApi<mxfile host="app.diagrams.net" agent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" version="29.2.9">
  <diagram name="Page-1" id="IW6h3HyrusvtPFg6DJsz">
    <mxGraphModel dx="719" dy="1532" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="850" pageHeight="1100" math="0" shadow="0">
      <root>
        <mxCell id="0" />
        <mxCell id="1" parent="0" />
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-8" edge="1" parent="1" source="rr6c9T-Vos0qCI4vx9Np-1" style="edgeStyle=none;curved=1;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;fontSize=10;startSize=8;endSize=8;" target="rr6c9T-Vos0qCI4vx9Np-4" value="">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-1" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="Jmeter" vertex="1">
          <mxGeometry height="35" width="80" x="60" y="25" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-4" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="bookTripApi" vertex="1">
          <mxGeometry height="35" width="90" x="170" y="25" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-9" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="searchFlights" vertex="1">
          <mxGeometry height="35" width="90" x="470" y="-20" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-10" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="reserve" vertex="1">
          <mxGeometry height="35" width="90" x="320" y="-20" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-18" edge="1" parent="1" source="rr6c9T-Vos0qCI4vx9Np-4" style="endArrow=none;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;exitX=1;exitY=0.714;exitDx=0;exitDy=0;exitPerimeter=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="400" y="30" as="sourcePoint" />
            <mxPoint x="290" y="50" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-19" edge="1" parent="1" style="endArrow=none;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="290" y="50" as="sourcePoint" />
            <mxPoint x="290" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-22" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="290" as="sourcePoint" />
            <mxPoint x="320" y="0.25" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-23" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.579;entryDx=0;entryDy=0;entryPerimeter=0;" target="rr6c9T-Vos0qCI4vx9Np-9" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="410" as="sourcePoint" />
            <mxPoint x="440" y="0.25" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-24" edge="1" parent="1" source="rr6c9T-Vos0qCI4vx9Np-9" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;exitX=0.998;exitY=0.535;exitDx=0;exitDy=0;exitPerimeter=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="610" y="-1" as="sourcePoint" />
            <mxPoint x="640" y="-0.75" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-27" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="External&amp;nbsp;&lt;div&gt;Services&lt;/div&gt;&lt;div&gt;Jar&lt;/div&gt;&lt;div&gt;Module&lt;/div&gt;" vertex="1">
          <mxGeometry height="302.5" width="70" x="640" y="-32.5" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-28" edge="1" parent="1" style="endArrow=none;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="290" y="50" as="sourcePoint" />
            <mxPoint x="290" y="170" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-29" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="getTripPlan" vertex="1">
          <mxGeometry height="35" width="90" x="320" y="150" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-30" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <Array as="points">
              <mxPoint x="290" y="170" />
            </Array>
            <mxPoint x="290" y="170" as="sourcePoint" />
            <mxPoint x="320" y="170.25" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-31" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <Array as="points">
              <mxPoint x="410" y="167.33" />
            </Array>
            <mxPoint x="410" y="167.33" as="sourcePoint" />
            <mxPoint x="440" y="167.58" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-32" edge="1" parent="1" style="endArrow=none;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="440" y="80" as="sourcePoint" />
            <mxPoint x="440" y="240" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-33" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <Array as="points" />
            <mxPoint x="440" y="80" as="sourcePoint" />
            <mxPoint x="470" y="80.25" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-34" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <Array as="points" />
            <mxPoint x="440" y="120" as="sourcePoint" />
            <mxPoint x="470" y="120.25" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-35" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="Accomodation Service" vertex="1">
          <mxGeometry height="35" width="90" x="470" y="60" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-36" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="Events&lt;div&gt;Service&lt;/div&gt;" vertex="1">
          <mxGeometry height="35" width="90" x="470" y="100" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-37" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="&lt;font&gt;Recommendations&lt;/font&gt;&lt;div&gt;Service&lt;/div&gt;" vertex="1">
          <mxGeometry height="35" width="90" x="470" y="140" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-38" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="Transportation&lt;div&gt;Service&lt;/div&gt;" vertex="1">
          <mxGeometry height="35" width="90" x="470" y="180" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-39" parent="1" style="rounded=0;whiteSpace=wrap;html=1;fontSize=10;" value="Weather&lt;div&gt;Service&lt;/div&gt;" vertex="1">
          <mxGeometry height="35" width="90" x="470" y="220" as="geometry" />
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-41" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <Array as="points" />
            <mxPoint x="440" y="157.32999999999998" as="sourcePoint" />
            <mxPoint x="470" y="157.57999999999998" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-42" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <Array as="points" />
            <mxPoint x="440" y="197.32999999999998" as="sourcePoint" />
            <mxPoint x="470" y="197.57999999999998" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-43" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <Array as="points" />
            <mxPoint x="440" y="240" as="sourcePoint" />
            <mxPoint x="470" y="240.25" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-44" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;exitX=0.998;exitY=0.535;exitDx=0;exitDy=0;exitPerimeter=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="560" y="77.33" as="sourcePoint" />
            <mxPoint x="640" y="77.58" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-45" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;exitX=0.998;exitY=0.535;exitDx=0;exitDy=0;exitPerimeter=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="560" y="118.58" as="sourcePoint" />
            <mxPoint x="640" y="118.83" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-46" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;exitX=0.998;exitY=0.535;exitDx=0;exitDy=0;exitPerimeter=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="560" y="157.32999999999998" as="sourcePoint" />
            <mxPoint x="640" y="157.57999999999998" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-47" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;exitX=0.998;exitY=0.535;exitDx=0;exitDy=0;exitPerimeter=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="560" y="197.33" as="sourcePoint" />
            <mxPoint x="640" y="197.58" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="rr6c9T-Vos0qCI4vx9Np-48" edge="1" parent="1" style="endArrow=classic;html=1;rounded=0;fontSize=10;startSize=8;endSize=8;curved=1;entryX=0;entryY=0.5;entryDx=0;entryDy=0;exitX=0.998;exitY=0.535;exitDx=0;exitDy=0;exitPerimeter=0;" value="">
          <mxGeometry height="50" relative="1" width="50" as="geometry">
            <mxPoint x="560" y="237.32999999999998" as="sourcePoint" />
            <mxPoint x="640" y="237.57999999999998" as="targetPoint" />
          </mxGeometry>
        </mxCell>
      </root>
    </mxGraphModel>
  </diagram>
</mxfile>
.drawio…]()


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
