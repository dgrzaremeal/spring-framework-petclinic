# Implementation Plan: Pet Gender/Sex Enum

**Branch**: `001-pet-gender-enum_Sonnet` | **Date**: 2026-04-29 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-pet-gender-enum_Sonnet/spec.md`

## Summary

Add a `Gender` enum (MALE, FEMALE, UNKNOWN) to the `Pet` entity, persisted as a `VARCHAR` column in the `pets` table via SQL schema migration scripts (one per DB dialect), with UNKNOWN as the default. The gender field is exposed on the pet create/edit form as a `<select>` dropdown, displayed on the owner detail view, and included in the `/vets.json`-style JSON response pattern for pet resources. All three persistence implementations (JPA, JDBC, Spring Data JPA) must be updated consistently.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Framework 7.0.6, Hibernate ORM 7.3.0.Final, Hibernate Validator 9.1.0.Final, Jackson 3.1.1, Spring Data JPA (managed by Spring Data BOM 2025.1.2)  
**Storage**: H2 (default/test), MySQL, PostgreSQL, HSQLDB — all via raw SQL schema scripts (`schema.sql` + `data.sql`) loaded by Spring's `<jdbc:initialize-database>`; **no Flyway** in this project  
**Testing**: JUnit Jupiter 6.0.2, Mockito 5.23.0, AssertJ 3.27.7; integration tests run against H2 in-memory  
**Target Platform**: Servlet container (Jetty 11+ / Tomcat 11+), WAR packaging  
**Project Type**: Web application (Spring MVC + JSP views + JSON/XML REST endpoints)  
**Performance Goals**: No specific throughput targets; standard web application response times  
**Constraints**: Must maintain pluggable persistence (JPA / JDBC / Spring Data JPA profiles); no Spring Boot; XML-based Spring configuration; WAR must remain buildable with `./mvnw jetty:run-war`  
**Scale/Scope**: Small additive change — one new enum class, one new column, updates to 3 persistence implementations, 1 JSP form, 1 JSP detail view, 1 validator

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Notes |
|-----------|--------|-------|
| **I. Three-Layer Architecture** | PASS | Gender field flows: Controller → ClinicService → PetRepository. No layer bypass. |
| **II. Spring Framework XML Configuration** | PASS | No Spring Boot dependencies introduced. Enum is a plain Java class; no new XML config needed beyond what already exists. |
| **III. Pluggable Persistence Layer** | PASS — requires action | All three implementations (JPA, JDBC, Spring Data JPA) must be updated. Schema scripts must be updated for all four DB dialects (H2, HSQLDB, MySQL, PostgreSQL). |
| **IV. Test Coverage** | PASS — requires action | Unit tests for `PetValidator` (if gender validation added), integration tests for each persistence impl must cover the new column. |
| **V. Simplicity and Minimal Dependencies** | PASS | No new dependencies. Java enum is a built-in language feature. `@Enumerated(EnumType.STRING)` is standard JPA. |

**Gate result**: PASS. No violations. Proceed to Phase 0.

## Project Structure

### Documentation (this feature)

```text
specs/001-pet-gender-enum_Sonnet/
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
│   ├── Gender.java                          # NEW — enum MALE, FEMALE, UNKNOWN
│   └── Pet.java                             # MODIFY — add gender field
├── repository/
│   ├── jdbc/
│   │   ├── JdbcPetRepositoryImpl.java       # MODIFY — include gender in INSERT/UPDATE/SELECT
│   │   └── JdbcPet.java                     # MODIFY — add gender field for row mapping
│   ├── jpa/
│   │   └── JpaPetRepositoryImpl.java        # NO CHANGE — JPA handles via @Enumerated
│   └── springdatajpa/
│       └── SpringDataPetRepository.java     # NO CHANGE — inherits from PetRepository
├── web/
│   ├── PetController.java                   # MODIFY — populate genders model attribute
│   └── PetValidator.java                    # NO CHANGE — gender is optional (defaults to UNKNOWN)
└── service/
    └── ClinicServiceImpl.java               # NO CHANGE — savePet already delegates to repository

src/main/resources/db/
├── h2/schema.sql                            # MODIFY — add gender column to pets table
├── hsqldb/schema.sql                        # MODIFY — add gender column to pets table
├── mysql/schema.sql                         # MODIFY — add gender column to pets table
└── postgresql/schema.sql                    # MODIFY — add gender column to pets table

src/main/webapp/WEB-INF/jsp/
├── pets/createOrUpdatePetForm.jsp           # MODIFY — add gender <select> dropdown
└── owners/ownerDetails.jsp                  # MODIFY — display gender on pet profile

src/test/java/org/springframework/samples/petclinic/
├── service/
│   └── ClinicServiceTests.java              # MODIFY — add gender assertions to pet tests
└── web/
    └── PetControllerTests.java              # MODIFY — verify gender field in form/submit
```

**Structure Decision**: Single project (Option 1). Standard Maven layout. Feature is purely additive within the existing package structure.

## Complexity Tracking

> No constitution violations — table not required.
