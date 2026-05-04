<!--
SYNC IMPACT REPORT
==================
Version change: (unversioned template) → 1.0.0
Modified principles: N/A (initial fill from template)
Added sections: Core Principles (I–V), Technology Standards, Development Workflow, Governance
Removed sections: None
Templates requiring updates:
  ✅ .specify/templates/plan-template.md — Constitution Check section references this constitution; no structural changes needed
  ✅ .specify/templates/spec-template.md — Requirements section aligns with principles; no changes needed
  ✅ .specify/templates/tasks-template.md — Task categories (testing, observability, layering) align with principles; no changes needed
Follow-up TODOs:
  - TODO(RATIFICATION_DATE): Exact original project adoption date unknown; set to first known commit date approximation. Verify and update if needed.
-->

# Spring PetClinic Constitution

## Core Principles

### I. Three-Layer Architecture (NON-NEGOTIABLE)

The application MUST maintain a strict three-layer architecture: **Presentation → Service →
Repository**. No layer MUST bypass another; controllers MUST NOT call repositories directly,
and repositories MUST NOT contain business logic. Each layer MUST be independently testable
via Spring profiles and dependency injection.

**Rationale**: The explicit layering is the defining characteristic of this fork and the
primary educational value it provides to the Spring community. Violating it undermines the
project's purpose.

### II. Spring Framework XML Configuration

The application MUST use plain Spring Framework XML configuration as its default wiring
mechanism (not Spring Boot auto-configuration). Java Config is permitted only on the
dedicated `javaconfig` branch. New features MUST NOT introduce Spring Boot dependencies
on the main branch.

**Rationale**: This fork exists specifically to demonstrate Spring Framework (non-Boot)
configuration patterns. Mixing Boot auto-configuration defeats the educational goal.

### III. Pluggable Persistence Layer

The persistence layer MUST support at least three interchangeable implementations: JPA
(default), JDBC, and Spring Data JPA. Switching between implementations MUST be achievable
solely by activating a Spring profile (`jpa`, `jdbc`, `spring-data-jpa`) with no code
changes. New data-access code MUST be added to all three implementations or clearly
documented as implementation-specific.

**Rationale**: Demonstrating multiple persistence strategies is a core learning objective.
Implementations MUST remain functionally equivalent so learners can compare approaches.

### IV. Test Coverage (NON-NEGOTIABLE)

All new service-layer logic MUST be covered by unit tests. Integration tests MUST exist for
each persistence implementation (JPA, JDBC, Spring Data JPA). Tests MUST pass against the
default H2 in-memory database before any PR is merged. Tests MUST NOT depend on external
services or require a running database server unless executed under a named Maven profile.

**Rationale**: The project serves as a reference implementation; untested code misleads
learners and degrades confidence in the sample.

### V. Simplicity and Minimal Dependencies

New dependencies MUST be justified against an existing alternative in the project. The
dependency list MUST remain minimal; convenience libraries that duplicate Spring's built-in
capabilities MUST NOT be added. JavaScript dependencies MUST be declared as WebJars in
`pom.xml` rather than checked-in static files.

**Rationale**: Keeping the dependency surface small ensures the project remains approachable
and easy to understand for developers learning Spring.

## Technology Standards

- **Java**: 17 or newer (full JDK required).
- **Build**: Maven 3.8+; the Maven Wrapper (`mvnw` / `mvnw.cmd`) MUST be used in all
  documented commands so contributors do not need a system-level Maven installation.
- **Servlet Container**: Jetty 11.0+ or Tomcat 11+ for local development; WAR packaging
  MUST be maintained.
- **Database**: H2 (default, in-memory), MySQL, and PostgreSQL MUST all be supported via
  Maven profiles. Docker Compose examples MUST be kept up to date when database versions
  change.
- **CSS**: Compiled from SCSS source using the `css` Maven profile; pre-compiled CSS MUST
  NOT be manually edited.
- **Containerization**: Google Jib MUST be used for Docker image builds; Dockerfile-based
  builds are not the primary path.

## Development Workflow

- All bug reports and feature requests MUST go through the GitHub issue tracker before
  work begins.
- Pull requests MUST reference the relevant issue and include a description of the change.
- Editor preferences are defined in `.editorconfig`; contributors MUST configure their
  editor accordingly before submitting PRs.
- The `main` branch MUST always be in a buildable, runnable state (`./mvnw jetty:run-war`
  succeeds with no external dependencies).
- Breaking changes to the public WAR API or Spring XML configuration structure MUST be
  discussed in an issue before implementation.

## Governance

This constitution supersedes all other informal practices. Amendments require:

1. An open GitHub issue describing the proposed change and rationale.
2. Consensus from at least one maintainer (approval via PR review).
3. A version bump to this document following semantic versioning:
   - **MAJOR**: Removal or redefinition of a core principle.
   - **MINOR**: Addition of a new principle or technology standard.
   - **PATCH**: Clarifications, wording fixes, non-semantic refinements.
4. Update of all dependent templates in `.specify/templates/` if the amendment affects
   task categories, plan gates, or spec requirements.

All PRs and code reviews MUST verify compliance with the principles above. Complexity
violations MUST be documented in the plan's Complexity Tracking table with explicit
justification. For runtime development guidance, refer to `README.md`.

**Version**: 1.0.0 | **Ratified**: TODO(RATIFICATION_DATE): verify original adoption date | **Last Amended**: 2026-04-29
