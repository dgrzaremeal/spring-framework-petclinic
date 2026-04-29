# Implementation Plan: Add Gender Enum to Pet

**Branch**: `002-pet-gender-enum` | **Date**: 2026-04-29 | **Spec**: [link to spec.md]
**Input**: Feature specification from `/specs/002-pet-gender-enum/spec.md`

## Summary

Add a Gender enum (MALE, FEMALE, UNKNOWN) to the Pet entity in Spring PetClinic. This involves creating the Gender enum class, adding a gender field to the Pet model, updating the database schema for all three persistence implementations (H2, MySQL, PostgreSQL), updating the service and repository layers, and adding UI controls in the JSP views. Default value is UNKNOWN.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Framework (XML configuration, no Spring Boot), Jakarta Persistence API, H2/MySQL/PostgreSQL  
**Storage**: H2 (default in-memory), MySQL, PostgreSQL (via Maven profiles)  
**Testing**: JUnit, Spring Test framework  
**Target Platform**: Linux server (Docker/Jib)  
**Project Type**: Web application (JSP/JSTL with custom tags)  
**Performance Goals**: Standard web application latency  
**Constraints**: Must support all three persistence implementations (JPA, JDBC, Spring Data JPA)  
**Scale/Scope**: Small feature - single field addition to Pet entity

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Gate | Status | Notes |
|------|--------|-------|
| TDD - Tests required | PASS | Feature must include unit/integration tests |
| Layered Architecture | PASS | Follow Presentation→Service→Repository pattern |
| Persistence Agnosticism | PASS | Must update all three persistence implementations (H2, MySQL, PostgreSQL) schema and data |
| Quality Gates | PASS | Maven test phase must pass |
| Observability | N/A | No new logging required for this feature |

**No constitution violations detected for this feature.**

## Project Structure

### Documentation (this feature)

```text
specs/002-pet-gender-enum/
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
│   ├── Pet.java              # Add gender field
│   ├── Gender.java           # NEW - Gender enum
│   ├── NamedEntity.java     # Base class
│   └── BaseEntity.java      # Base class
├── service/
│   └── ClinicService.java   # Interface
├── repository/
│   └── Jdbc*Repository.java # Repository implementations
└── web/
    └── PetController.java   # Controller

src/main/resources/db/
├── h2/
│   ├── schema.sql            # UPDATE - Add gender column
│   └── data.sql
├── mysql/
│   ├── schema.sql
│   └── data.sql
├── postgresql/
│   ├── schema.sql
│   └── data.sql
├── jdbc/
│   └── schema.sql
└── spring-data-jpa/
    └── schema.sql

src/main/webapp/WEB-INF/
├── views/
│   └── pets/                 # Update JSP views
└── petclinic.tiles-defs.xml  # Tiles definition

tests/
├── unit/
│   └── PetTests.java        # UPDATE
├── integration/
│   └── PetIntegrationTests.java  # UPDATE
└── contract/
    └── PetRepositoryTests.java  # UPDATE
```

**Structure Decision**: Single web application project following existing Spring PetClinic structure. Feature adds Gender enum, modifies Pet entity, updates all database schemas (H2, MySQL, PostgreSQL), updates service/repository layers for all three persistence implementations, and adds gender selection UI to JSP forms.
