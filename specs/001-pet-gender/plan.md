# Implementation Plan: Add Gender Enum to Pet

**Branch**: `001-pet-gender` | **Date**: 2026-04-24 | **Spec**: [spec.md](./spec.md)

## Summary

Add a Gender enum (MALE, FEMALE, UNKNOWN) to the Pet entity in Spring PetClinic. Users can select gender when creating/editing pets, with UNKNOWN as default. Pet list can be filtered by gender.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Framework 6.x (plain XML config), jakarta.persistence, JUnit 5  
**Storage**: H2 (default in-memory), MySQL, PostgreSQL via Maven profiles  
**Testing**: JUnit, Spring Test framework  
**Target Platform**: Linux server with Jetty container via Jib Docker  
**Project Type**: Web application (JSP/JSTL views)  
**Performance Goals**: Standard web app performance (domain-specific)  
**Constraints**: Must work with all three persistence implementations (JPA, JDBC, Spring Data JPA)  
**Scale/Scope**: Small application (~50K LOC, single team)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **I. Test-Driven Development**: All new features require tests. Feature adds new Pet field → unit tests for service logic, integration tests for persistence. **MUST PASS**: Tests must fail before implementation (Red-Green-Refactor).

- **II. Layered Architecture**: Feature adds Gender field to Pet model. Presentation (PetController) → Service (ClinicService) → Repository (PetRepository). Must NOT import lower-layer implementations directly. Use existing service interface patterns.

- **III. Persistence Agnosticism**: Must work with all three persistence implementations: JPA (default), JDBC, Spring Data JPA. **CRITICAL**: Gender field must be handled in JdbcPetRowMapper, SpringDataPetRepository, and JPA entity. Use enum with ordinal/string storage.

- **IV. Quality Gates**: Maven compile must pass, Maven test must pass, code coverage must not regress.

- **V. Observability**: Log Gender field updates at appropriate level.

**Gate Status**: All gates applicable. Will verify post-design.

## Project Structure

### Documentation (this feature)

```text
specs/001-pet-gender/
├── plan.md              # This file
├── research.md          # Phase 0 output ✓
├── data-model.md        # Phase 1 output ✓
├── quickstart.md        # Phase 1 output ✓
├── contracts/           # Phase 1 output ✓
└── tasks.md             # Phase 2 output (/speckit.tasks - NOT created here)
```

### Source Code (repository root)

```text
src/main/java/org/springframework/samples/petclinic/
├── model/
│   ├── Pet.java              # Add gender field to Pet entity
│   ├── Gender.java           # NEW: Gender enum (MALE, FEMALE, UNKNOWN)
│   ├── NamedEntity.java      # Parent of Pet
│   └── BaseEntity.java       # Base ID entity
├── service/
│   ├── ClinicService.java    # Interface (add findByGender if filtering needed)
│   └── ClinicServiceImpl.java
├── repository/
│   ├── PetRepository.java
│   ├── jpa/JpaPetRepositoryImpl.java
│   ├── jdbc/JdbcPetRepositoryImpl.java
│   ├── jdbc/JdbcPetRowMapper.java   # Add gender mapping
│   └── springdatajpa/SpringDataPetRepository.java
├── web/
│   ├── PetController.java    # Add gender to form
│   └── PetValidator.java    # Add gender validation
src/main/webapp/WEB-INF/
├── petclinic-servlet.xml     # Spring config
└── jsp/pets/
    └── createOrUpdatePetForm.jsp  # Add gender dropdown
src/test/java/
├── repository/              # Tests for all persistence impls
├── service/                 # Service layer tests
└── web/                    # Controller tests
```

**Structure Decision**: Add Gender enum to model layer, implement field across all three persistence implementations (JPA/JDBC/Spring Data JPA), add UI in existing JSP form and controller.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |
