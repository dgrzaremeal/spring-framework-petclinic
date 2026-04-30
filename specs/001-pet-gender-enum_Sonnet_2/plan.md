# Implementation Plan: Pet Gender/Sex Enum

**Branch**: `001-pet-gender-enum_Sonnet_2` | **Date**: 2026-04-30 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-pet-gender-enum_Sonnet_2/spec.md`

## Summary

Add a `Gender` enum (MALE, FEMALE, UNKNOWN) to the `Pet` entity, stored as a VARCHAR column via `@Enumerated(EnumType.STRING)`. The feature adds a dropdown to the pet add/edit form, displays the gender label on the owner detail view, defaults to UNKNOWN when unset, and migrates existing NULL rows to UNKNOWN via DB migration scripts across all four supported databases (H2, HSQLDB, MySQL, PostgreSQL).

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Framework 7.0.6, Hibernate ORM 7.3.0.Final, Jakarta Persistence 3.2.0, Hibernate Validator 9.1.0.Final, Spring Data JPA 2025.1.2  
**Storage**: H2 (default, in-memory), HSQLDB, MySQL 8, PostgreSQL — all via Maven profiles  
**Testing**: JUnit Jupiter 6.0.2, Mockito 5.23.0, AssertJ 3.27.7; Spring TestContext Framework  
**Target Platform**: Servlet container (Jetty 11+ / Tomcat 11+), WAR packaging  
**Project Type**: Web application (Spring MVC + JSP views)  
**Performance Goals**: No specific performance targets for this feature (UI form field addition)  
**Constraints**: No Spring Boot; XML-based Spring configuration; three-layer architecture (Presentation → Service → Repository); pluggable persistence (JPA, JDBC, Spring Data JPA profiles)  
**Scale/Scope**: Single enum field addition; affects Pet entity, all three persistence implementations, two JSP views, one controller, one validator

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Notes |
|-----------|--------|-------|
| I. Three-Layer Architecture | PASS | Gender field flows Controller → Service → Repository; no layer bypass |
| II. Spring Framework XML Configuration | PASS | No Spring Boot introduced; existing XML config unchanged |
| III. Pluggable Persistence Layer | PASS | Must add `gender` column handling to JPA, JDBC, and Spring Data JPA implementations |
| IV. Test Coverage | PASS | Unit tests for PetValidator (gender validation), integration tests for all three persistence impls, controller tests for form binding |
| V. Simplicity and Minimal Dependencies | PASS | No new dependencies; enum is a plain Java type; `@Enumerated(EnumType.STRING)` is standard JPA |
| View Layer Convention | PASS | `@ModelAttribute("genders")` MUST return `List<Gender>` (not `Gender[]`) per constitution §View Layer Conventions |

**No violations. No Complexity Tracking entries required.**

## Project Structure

### Documentation (this feature)

```text
specs/001-pet-gender-enum_Sonnet_2/
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
│   ├── Gender.java                          # NEW: enum MALE, FEMALE, UNKNOWN
│   └── Pet.java                             # MODIFY: add gender field + getter/setter
├── web/
│   ├── PetController.java                   # MODIFY: add @ModelAttribute("genders") returning List<Gender>
│   └── PetValidator.java                    # MODIFY: validate gender is non-null (default UNKNOWN)
└── repository/
    ├── jdbc/
    │   ├── JdbcPetRepositoryImpl.java        # MODIFY: include gender in INSERT/UPDATE SQL + parameter source
    │   └── JdbcPetRowMapper.java             # MODIFY: map gender column to Pet.gender
    ├── jpa/
    │   └── JpaPetRepositoryImpl.java         # No change needed (JPA handles via @Enumerated)
    └── springdatajpa/
        └── SpringDataPetRepository.java      # No change needed (JPA handles via @Enumerated)

src/main/webapp/WEB-INF/jsp/pets/
└── createOrUpdatePetForm.jsp                 # MODIFY: add gender <select> dropdown

src/main/webapp/WEB-INF/jsp/owners/
└── ownerDetails.jsp                          # MODIFY: display pet.gender label

src/main/resources/db/
├── h2/schema.sql                             # MODIFY: add gender VARCHAR column to pets table
├── h2/data.sql                               # MODIFY: set gender='UNKNOWN' on existing seed rows
├── hsqldb/schema.sql                         # MODIFY: add gender VARCHAR column
├── hsqldb/data.sql                           # MODIFY: set gender='UNKNOWN' on existing seed rows
├── mysql/schema.sql                          # MODIFY: add gender VARCHAR column
├── mysql/data.sql                            # MODIFY: set gender='UNKNOWN' on existing seed rows
└── postgresql/schema.sql                     # MODIFY: add gender VARCHAR column
    postgresql/data.sql                       # MODIFY: set gender='UNKNOWN' on existing seed rows

src/test/java/org/springframework/samples/petclinic/
├── model/
│   └── PetTests.java                         # MODIFY: add gender default/assignment tests
├── service/
│   └── AbstractClinicServiceTests.java       # MODIFY: add gender persistence tests
└── web/
    └── PetControllerTests.java               # MODIFY: add gender form binding tests
```

**Structure Decision**: Single-project layout (Option 1). The feature is a vertical slice through the existing layered structure — no new packages or modules required.

## Complexity Tracking

> No constitution violations to justify.
