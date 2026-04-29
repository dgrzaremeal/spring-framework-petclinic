# Spring PetClinic Constitution

## Core Principles

### I. Test-Driven Development
All new features and bug fixes MUST be accompanied by tests. Unit tests are required for service logic, repository contracts, and controller behavior. Integration tests are required when crossing layer boundaries or involving database interactions. Tests MUST fail before implementation and pass upon completion (Red-Green-Refactor). The Maven test phase MUST pass for any PR to be merged.

### II. Layered Architecture
The application follows a strict 3-layer architecture: Presentation (Controllers, Views) → Service (Business Logic) → Repository (Data Access). Dependencies MUST only flow downward. Upper layers MUST NOT import lower-layer implementations directly. Cross-layer communication MUST occur through defined interfaces. Any deviation requires documented justification.

### III. Persistence Agnosticism
The system supports three persistence implementations: JPA (default), JDBC, and Spring Data JPA. Business logic in the Service layer MUST be written against the ClinicService interface, not specific implementations. Repository implementations MUST be independently swappable via Spring profiles. No feature implementation SHOULD assume a specific persistence mechanism unless explicitly required.

**Schema and Data Synchronization**: When database schema or initial data changes, ALL three persistence implementations MUST be updated before tests can pass. This includes: (1) DDL scripts for each implementation, (2) initialization data in each required format, (3) integration tests verifying data loads correctly under each profile. A schema change that breaks even one persistence implementation constitutes incomplete implementation.

### IV. Quality Gates
All changes MUST pass CI gates before merge. Required gates: Maven compile, Maven test, SonarCloud quality analysis. Code coverage MUST not regress below existing thresholds. Static analysis warnings MUST be addressed or explicitly justified. Security scans MUST pass with no new critical/high vulnerabilities.

### V. Observability
Application operations MUST be observable through structured logging. Key operations (database access, transaction boundaries, HTTP requests) MUST be logged at appropriate levels. Exceptions MUST be logged with sufficient context for debugging. Log configuration MUST support multiple outputs (console, file) with configurable levels.

## Technology Standards

**Language**: Java 17+ (full JDK required)
**Build Tool**: Maven 3.8+
**Framework**: Spring Framework (plain XML configuration, no Spring Boot)
**Views**: JSP/JSTL with custom tags
**Database**: H2 (default, in-memory), MySQL, PostgreSQL (via Maven profiles)
**Persistence**: JPA (default), JDBC, Spring Data JPA (via Spring profiles)
**Testing**: JUnit, Spring Test framework
**Container**: Docker via Jib (distroless Jetty base)

## Development Workflow

**Architecture Review**: Architectural changes affecting layer boundaries require review
**Testing Discipline**: All tests run via `./mvnw test`; integration tests via `./mvnw verify`
**Profile Usage**: Use `-P MySQL`, `-P PostgreSQL` for persistent databases; `-Dspring.profiles.active=jdbc|spring-data-jpa|jpa` for persistence layer selection
**CSS Workflow**: SCSS changes require `./mvnw generate-resources -P css` to compile
**Deployment**: Docker image via `mvn jib:build`

## Governance

This constitution supersedes all informal development practices. Amendments require:
1. Documented rationale explaining the need for change
2. PR review with at least one approval
3. Migration plan for existing code if principles affect current implementations
4. Version increment per semantic versioning rules

**Version Bump Policy**:
- MAJOR: Backward-incompatible architectural changes or principle removals
- MINOR: New principles added or material expansion of existing guidance
- PATCH: Clarifications, wording fixes, non-semantic refinements

All contributors MUST verify compliance with these principles before submitting changes. Complexity beyond the minimum required MUST be justified in the implementation plan.

**Version**: 1.1.0 | **Ratified**: 2026-04-24 | **Last Amended**: 2026-04-29