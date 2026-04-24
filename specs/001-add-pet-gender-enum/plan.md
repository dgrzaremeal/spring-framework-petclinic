# Implementation Plan: Add Gender Enum to Pet

**Branch**: `001-add-pet-gender-enum` | **Date**: 2026-04-24 | **Spec**: [link](./spec.md)

**Input**: Feature specification from `/specs/001-add-pet-gender-enum/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Add a Gender enumeration (MALE, FEMALE, UNKNOWN) to the Pet entity with localized display labels, dropdown/radio selection UI, and JPA default handling for backward compatibility. Uses enum-based Map for localization keys and defaults to UNKNOWN for null/invalid values.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Framework 7.0.6, Hibernate 7.3.0, Jakarta JPA 3.2  
**Storage**: H2 (default in-memory), MySQL, PostgreSQL  
**Testing**: JUnit 6, Spring Test framework  
**Target Platform**: Linux server, Docker (Jib)  
**Project Type**: Web application (JSP/JSTL)  
**Performance Goals**: N/A - typical pet clinic load  
**Constraints**: Must support three persistence implementations (JPA, JDBC, Spring Data JPA)  
**Scale/Scope**: Single feature, 10-20 files expected  

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Gate | Status | Notes |
|------|--------|-------|
| TDD - tests required | ✅ PASS | Feature requires tests for Gender enum, Pet updates, UI |
| Layered Architecture | ✅ PASS | Uses existing 3-layer pattern (Controller→Service→Repository) |
| Persistence Agnosticism | ✅ PASS | JDBC: store as VARCHAR(20), Map MALE/FEMALE/UNKNOWN strings |
| Quality Gates | ✅ PASS | Maven compile/test must pass |
| Observability | ✅ PASS | Logging exists for key operations |

## Project Structure

### Documentation (this feature)

```text
specs/001-add-pet-gender-enum/
├── plan.md              # This file
├── research.md          # Phase 0 output (to be generated)
├── data-model.md        # Phase 1 output (to be generated)
├── quickstart.md       # Phase 1 output (to be generated)
└── contracts/          # Phase 1 output (to be generated)
```

### Source Code (repository root)

```text
src/main/java/org/springframework/samples/petclinic/
├── model/
│   ├── Gender.java        # NEW - enum (MALE, FEMALE, UNKNOWN)
│   └── Pet.java           # MODIFY - add gender field
├── repository/
│   ├── jdbc/
│   │   ├── JdbcPetRepositoryImpl.java   # MODIFY
│   │   └── JdbcPetRowMapper.java       # MODIFY
│   ├── jpa/
│   │   └── JpaPetRepositoryImpl.java   # MODIFY
│   └── springdatajpa/
│       └── SpringDataPetRepository.java # MODIFY
├── service/
│   └── ClinicServiceImpl.java          # MODIFY (if getPet needs Gender)
└── web/
    ├── PetController.java              # MODIFY - pass Gender to view
    └── PetValidator.java             # MODIFY - validate Gender

src/main/webapp/WEB-INF/
└── views/
    └── pets/
        ├── createOrUpdatePetForm.jsp  # MODIFY - add gender dropdown/radio

src/main/resources/
├── messages.properties               # MODIFY - add Gender labels
└── db/
    └── database.sql                   # MODIFY - seed data with Gender values

src/test/java/...
├── model/GenderTest.java              # NEW
├── repository/jdbc/JdbcPetRepositoryImplTest.java  # MODIFY
└── ...existing tests                # MODIFY for Gender
```

**Structure Decision**: Single monolithic structure (standard Spring MVC). No additional modules required.

**JDBC Gender Mapping Strategy**: Store gender as VARCHAR(20) in database ('MALE', 'FEMALE', 'UNKNOWN'), map to enum in JdbcPetRowMapper.java using Gender.valueOf(row.getString("gender")). Default to Gender.UNKNOWN for null values.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|------------|------------|-------------------------------------|
| Three persistence impls required | Constitution mandates JPA, JDBC, Spring Data JPA support | Need to implement Gender field in all three repository implementations |
