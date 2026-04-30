# Implementation Plan Checklist: Pet Gender/Sex Enum

**Purpose**: Validate the quality, completeness, and consistency of the implementation plan and technical requirements before merging the feature branch
**Created**: 2026-04-30
**Feature**: [spec.md](../spec.md) | [plan.md](../plan.md) | [data-model.md](../data-model.md) | [contracts/http-form-contracts.md](../contracts/http-form-contracts.md)

---

## Requirement Completeness

- [ ] CHK001 - Are requirements defined for all three persistence profiles (JPA, JDBC, Spring Data JPA) regarding how the `gender` field is read and written? [Completeness, Plan §Project Structure]
- [ ] CHK002 - Are requirements specified for what happens when `Gender.valueOf()` receives an unrecognized string from the database (e.g., legacy data corruption)? [Completeness, Gap]
- [ ] CHK003 - Are the `@ModelAttribute("genders")` population requirements documented for both the add and edit form flows, not just one? [Completeness, data-model.md §Controller Changes]
- [ ] CHK004 - Are requirements defined for the `PetValidator` null-check behavior when the field initializer is bypassed (e.g., via direct JDBC row mapping returning null)? [Completeness, data-model.md §Validator Changes]
- [ ] CHK005 - Are seed data (`data.sql`) migration requirements specified for all four databases (H2, HSQLDB, MySQL, PostgreSQL)? [Completeness, data-model.md §Database Schema Changes]
- [ ] CHK006 - Are test coverage requirements defined for all three persistence layer implementations (JPA, JDBC, Spring Data JPA), not just the service layer? [Completeness, Plan §Project Structure]

## Requirement Clarity

- [ ] CHK007 - Is "type mismatch error" (for invalid gender form submission) defined with a specific user-facing message or error code? [Clarity, contracts/http-form-contracts.md §Validation Errors]
- [ ] CHK008 - Is the display format for `${pet.gender}` in `ownerDetails.jsp` specified beyond "calls `Gender.toString()`"? Is the exact rendered string ("Male", "Female", "Unknown") contractually defined? [Clarity, data-model.md §View Changes]
- [ ] CHK009 - Is "consistent with existing form controls" (FR-002) quantified — e.g., same CSS class, same label alignment, same dropdown size attribute? [Clarity, Spec §FR-002]
- [ ] CHK010 - Is the `size="3"` attribute on the gender `<select>` tag intentional and specified as a requirement, or is it an implementation detail that could vary? [Clarity, data-model.md §View Changes]
- [ ] CHK011 - Is the ordering of enum values in the dropdown (`MALE`, `FEMALE`, `UNKNOWN`) explicitly required, or is declaration order assumed? [Clarity, contracts/http-form-contracts.md §Model Attribute]
- [ ] CHK012 - Is "under 10 seconds" (SC-001) defined with a measurement method — e.g., wall-clock time from form submission to redirect, or time to first byte? [Clarity, Spec §SC-001]

## Requirement Consistency

- [ ] CHK013 - Does the `gender` column definition (`NOT NULL DEFAULT 'UNKNOWN'`) align consistently across all four database schema files (H2, HSQLDB, MySQL, PostgreSQL)? [Consistency, data-model.md §Database Schema Changes]
- [ ] CHK014 - Is the `gender` parameter marked as `Required: No` consistently in both the add-pet and edit-pet form contracts? [Consistency, contracts/http-form-contracts.md §Endpoint 1 & 2]
- [ ] CHK015 - Does the validator null-check requirement align with the field initializer default (`Gender.UNKNOWN`)? Is there a documented rationale for why both are needed? [Consistency, data-model.md §Validator Changes]
- [ ] CHK016 - Are the display labels ("Male", "Female", "Unknown") consistent between the `Gender.toString()` specification, the view contract table, and the spec's human-readable label list? [Consistency, Spec §Assumptions, data-model.md §New Entity, contracts/http-form-contracts.md §View Contract]
- [ ] CHK017 - Does the JDBC `INSERT`/`UPDATE` SQL requirement use `.name()` (raw enum name) consistently with how `JdbcPetRowMapper` reads it back via `Gender.valueOf()`? [Consistency, data-model.md §JDBC Layer Changes]

## Acceptance Criteria Quality

- [ ] CHK018 - Are acceptance scenarios defined for the case where a user submits an invalid gender string via a crafted HTTP request (bypassing the dropdown)? [Acceptance Criteria, Spec §Edge Cases]
- [ ] CHK019 - Is SC-002 ("100% of new pet records include a gender value") measurable without runtime instrumentation — i.e., is the enforcement mechanism (field initializer + NOT NULL constraint) specified as the acceptance mechanism? [Acceptance Criteria, Spec §SC-002]
- [ ] CHK020 - Are acceptance scenarios defined for the edit form pre-population case (User Story 2, Scenario 2) across all three persistence implementations? [Acceptance Criteria, Spec §User Story 2]
- [ ] CHK021 - Is SC-005 ("rejected 100% of the time") defined with a specific rejection mechanism (HTTP 200 + form re-render vs. HTTP 400) and a user-facing error message template? [Acceptance Criteria, Spec §SC-005]

## Scenario Coverage

- [ ] CHK022 - Are requirements defined for the scenario where a pet is loaded via JDBC and the `gender` column contains `NULL` after migration (defensive read path)? [Coverage, Gap]
- [ ] CHK023 - Are requirements specified for the concurrent edit scenario — two staff members editing the same pet's gender simultaneously? [Coverage, Gap]
- [ ] CHK024 - Are requirements defined for the GET form pre-population flow (loading the edit form with the current gender pre-selected) separately from the POST submission flow? [Coverage, Spec §User Story 2]
- [ ] CHK025 - Are requirements specified for what the dropdown renders when `pet.gender` is `null` at view time (e.g., no option selected, or UNKNOWN selected)? [Coverage, Gap]

## Edge Case Coverage

- [ ] CHK026 - Is the behavior defined when `Gender.valueOf()` throws `IllegalArgumentException` due to an unrecognized DB value — is this a silent default, a logged warning, or a hard failure? [Edge Case, Gap]
- [ ] CHK027 - Is the migration rollback scenario defined — what happens to the `gender` column if the deployment is rolled back after the schema migration runs? [Edge Case, Gap]
- [ ] CHK028 - Are requirements defined for the case where `data.sql` seed data is re-run on a database that already has `gender` values set (idempotency of seed scripts)? [Edge Case, Gap]
- [ ] CHK029 - Is the behavior specified when the `genders` model attribute is empty or unavailable at view render time (e.g., controller method throws before populating it)? [Edge Case, Gap]

## Non-Functional Requirements

- [ ] CHK030 - Are accessibility requirements defined for the gender `<select>` dropdown — e.g., `<label>` association, ARIA attributes, keyboard navigation? [Non-Functional, Gap]
- [ ] CHK031 - Are there any security requirements specified for the gender field — e.g., input sanitization beyond enum binding, or protection against mass-assignment? [Non-Functional, Gap]
- [ ] CHK032 - Is the VARCHAR(20) column width requirement justified and documented — is 20 characters sufficient for all current and plausible future enum values? [Non-Functional, data-model.md §New field]
- [ ] CHK033 - Are performance requirements defined for the DB migration script execution time on large `pets` tables (e.g., acceptable downtime window)? [Non-Functional, Gap]

## Dependencies & Assumptions

- [ ] CHK034 - Is the assumption that "no access control changes are required" validated against the existing role/permission model in the application? [Assumption, Spec §Assumptions]
- [ ] CHK035 - Is the assumption that "three enum values are fixed and exhaustive" documented as a formal constraint with a change-control implication if new values are needed later? [Assumption, Spec §Assumptions]
- [ ] CHK036 - Is the dependency on `petclinic:selectField` JSP tag for rendering the dropdown documented, including its expected model attribute type (`List<Gender>` vs `Gender[]`)? [Dependency, data-model.md §View Changes, Plan §Constitution Check]
- [ ] CHK037 - Is the assumption that `Gender.toString()` is the sole display mechanism documented — i.e., no JSP tag or message bundle override is expected? [Assumption, data-model.md §View Changes]
- [ ] CHK038 - Are the test framework version dependencies (JUnit Jupiter 6.0.2, Mockito 5.23.0) documented as constraints that affect how gender-related tests must be written? [Dependency, Plan §Technical Context]

## Ambiguities & Conflicts

- [ ] CHK039 - Is there a potential conflict between the `NOT NULL DEFAULT 'UNKNOWN'` DB constraint and the application-level field initializer — which takes precedence for new records, and is this documented? [Ambiguity, data-model.md §New field]
- [ ] CHK040 - Is it clear whether `JpaPetRepositoryImpl` and `SpringDataPetRepository` require no changes because JPA handles `@Enumerated` automatically, or because gender is excluded from their queries? The rationale should be explicit. [Ambiguity, Plan §Project Structure]
- [ ] CHK041 - Is the phrase "no runtime null-coercion in application code is required" (FR-007) unambiguous — does it mean null coercion is forbidden, or merely not required (i.e., optional)? [Ambiguity, Spec §FR-007]
- [ ] CHK042 - Is it specified whether the `PetValidator` gender null-check should produce the same error key (`REQUIRED`) as other required field validations, or a gender-specific error key? [Ambiguity, data-model.md §Validator Changes]

## Notes

- Check items off as completed: `[x]`
- Add findings or comments inline after the item
- Items marked `[Gap]` indicate requirements not currently present in the spec/plan — confirm whether intentionally excluded or missing
- Items marked `[Ambiguity]` require clarification from the spec author before implementation proceeds
