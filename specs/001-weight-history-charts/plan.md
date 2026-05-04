# Implementation Plan: Weight History & Progress Charts

**Branch**: `001-weight-history-charts` | **Date**: 2026-05-04 | **Spec**: [spec.md](spec.md)
**Input**: Feature specification from `/specs/001-weight-history-charts/spec.md`

## Summary

Add a `WeightRecord` entity to track pet weight measurements over time, with a chronological list view and a visual line chart on the pet's profile page. The implementation follows the existing three-layer architecture (Presentation → Service → Repository), adds all three persistence implementations (JPA, JDBC, Spring Data JPA), and renders the chart using a WebJar-declared charting library (Chart.js).

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Framework 7.0.6, Hibernate ORM 7.3.0.Final, Spring Data JPA 2025.1.2, Hibernate Validator 9.1.0.Final, Bootstrap 5.3.8 (WebJar), Chart.js (WebJar — to be added)  
**Storage**: H2 (default in-memory), MySQL, PostgreSQL — all via Maven profiles; JPA/JDBC/Spring Data JPA switchable via Spring profile  
**Testing**: JUnit Jupiter 6.0.2, Mockito 5.23.0, AssertJ 3.27.7, Spring Test 7.0.6  
**Target Platform**: Servlet container (Jetty 11+ / Tomcat 11+), WAR packaging  
**Project Type**: Web application (Spring MVC + JSP)  
**Performance Goals**: Chart renders within 2 seconds of page load (SC-003); list displays up to 100 records without pagination issues (SC-002)  
**Constraints**: No Spring Boot; XML configuration is the default wiring mechanism per Constitution Principle II. The main branch uses XML config; Java Config (`PetclinicInitializer`) is used only on the `javaconfig` branch. JavaScript dependencies MUST be WebJars in `pom.xml`; no checked-in static JS files  
**Scale/Scope**: Per-pet weight history; up to 100+ records per pet; single-unit (kg) only

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Notes |
|-----------|--------|-------|
| **I. Three-Layer Architecture** | PASS | `WeightRecord` flows: Controller → ClinicService → WeightRecordRepository. No controller-to-repository direct calls. |
| **II. Spring Framework XML/Java Config** | ⚠️ VERIFY | Constitution requires XML as default on main branch. Confirm active branch: if `main`, wiring must use XML config; if `javaconfig` branch, Java Config via `PetclinicInitializer` is permitted. No Spring Boot dependencies introduced. |
| **III. Pluggable Persistence Layer** | PASS | `WeightRecordRepository` interface + three implementations: `JpaWeightRecordRepositoryImpl`, `JdbcWeightRecordRepositoryImpl`, `SpringDataWeightRecordRepository`. |
| **IV. Test Coverage** | PASS | Unit tests for service-layer logic; integration tests for all three persistence implementations against H2. |
| **V. Simplicity and Minimal Dependencies** | PASS | Chart.js added as a WebJar in `pom.xml` (not a checked-in static file). No other new dependencies needed. |

**Post-design re-check**: Pending Phase 1 completion.

## Project Structure

### Documentation (this feature)

```text
specs/001-weight-history-charts/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```text
src/main/java/org/springframework/samples/petclinic/
├── model/
│   └── WeightRecord.java                          # NEW: weight measurement entity
├── repository/
│   ├── WeightRecordRepository.java                # NEW: repository interface
│   ├── jdbc/
│   │   └── JdbcWeightRecordRepositoryImpl.java    # NEW: JDBC implementation
│   ├── jpa/
│   │   └── JpaWeightRecordRepositoryImpl.java     # NEW: JPA implementation
│   └── springdatajpa/
│       └── SpringDataWeightRecordRepository.java  # NEW: Spring Data JPA implementation
├── service/
│   ├── ClinicService.java                         # MODIFIED: add weight record methods
│   └── ClinicServiceImpl.java                     # MODIFIED: implement weight record methods
└── web/
    └── WeightRecordController.java                # NEW: MVC controller

src/main/resources/db/
├── hsqldb/
│   └── schema.sql                                 # MODIFIED: add weight_records table
├── mysql/
│   └── schema.sql                                 # MODIFIED: add weight_records table
├── postgres/
│   └── schema.sql                                 # MODIFIED: add weight_records table
└── h2/
    └── schema.sql                                 # MODIFIED: add weight_records table

src/main/webapp/WEB-INF/jsp/
└── pets/
    ├── weightRecordList.jsp                       # NEW: weight history list + chart view
    └── createWeightRecordForm.jsp                 # NEW: add weight record form

src/test/java/org/springframework/samples/petclinic/
├── service/
│   └── ClinicServiceTests.java                    # MODIFIED: add weight record tests
└── web/
    └── WeightRecordControllerTests.java           # NEW: controller tests
```

**Structure Decision**: Single-project Spring MVC web application. Follows the existing package layout exactly — new classes mirror the patterns of `Visit`/`VisitRepository`/`VisitController` as the closest analogue.

## Complexity Tracking

| Principle | Issue | Resolution Required |
|-----------|-------|---------------------|
| **II. Spring Framework XML/Java Config** | Branch not confirmed. Constitution requires XML config on `main`; Java Config is permitted only on `javaconfig` branch. plan.md currently marked ⚠️ VERIFY. | Before implementation: confirm the active branch. If `main`, ensure all Spring wiring uses XML config (not `PetclinicInitializer`). If `javaconfig`, update Principle II status to PASS and document the branch explicitly. |
