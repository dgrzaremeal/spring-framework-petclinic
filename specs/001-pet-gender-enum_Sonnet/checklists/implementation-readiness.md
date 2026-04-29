# Implementation Readiness Checklist: Pet Gender/Sex Enum

**Purpose**: Validate that all requirements, contracts, and design decisions are complete, clear, and unambiguous before implementation is merged in PR review
**Created**: 2026-04-29
**Feature**: [spec.md](../spec.md) | [plan.md](../plan.md) | [data-model.md](../data-model.md) | [contracts/pet-api.md](../contracts/pet-api.md)
**Audience**: PR Reviewer
**Depth**: Full-spectrum

---

## Requirement Completeness

- [ ] CHK001 - Are requirements defined for all three persistence implementations (JPA, JDBC, Spring Data JPA) individually, or does the spec assume they are interchangeable? [Completeness, Spec §FR-001, Plan §Pluggable Persistence]
- [ ] CHK002 - Are requirements specified for all four database dialects (H2, HSQLDB, MySQL, PostgreSQL) schema changes, or is only one dialect documented as representative? [Completeness, data-model.md]
- [ ] CHK003 - Are seed data (`data.sql`) update requirements explicitly documented for all four dialects, or is this left as an implementation assumption? [Completeness, Gap, data-model.md §Seed Data]
- [ ] CHK004 - Are requirements defined for how the JDBC row mapper handles a `NULL` gender value read from the database (e.g., legacy rows before migration)? [Completeness, Gap, data-model.md §JDBC Layer]
- [ ] CHK005 - Are test coverage requirements specified for each of the three persistence profiles (JPA, JDBC, Spring Data JPA), or only for the service layer? [Completeness, Plan §Test Coverage]
- [ ] CHK006 - Are requirements defined for `PetControllerTests` covering the gender field in both form rendering and form submission scenarios? [Completeness, Plan §Project Structure]
- [ ] CHK007 - Is there a requirement specifying what the `<select>` dropdown renders when the bound `Pet` object has a `null` gender (e.g., during a new-pet form before defaults are applied)? [Completeness, Gap, Spec §FR-009]

---

## Requirement Clarity

- [ ] CHK008 - Is "human-readable format" in FR-005 and User Story 3 defined with the exact display strings ("Male", "Female", "Unknown"), or is the mapping left implicit? [Clarity, Spec §FR-005, data-model.md §Values]
- [ ] CHK009 - Is the term "pre-selected by default" in FR-009 clarified to mean the HTML `selected` attribute on the Unknown `<option>`, or model-level defaulting, or both? [Clarity, Spec §FR-009]
- [ ] CHK010 - Is "any other value MUST be rejected" in FR-002 clarified with the specific rejection mechanism (binding error, validation error, HTTP 400) and the exact error message or message key? [Clarity, Spec §FR-002]
- [ ] CHK011 - Is "appropriate error message" in User Story 2 Acceptance Scenario 2 defined with the exact message text or a reference to a message key? [Clarity, Spec §US-2]
- [ ] CHK012 - Is the scope of FR-010 ("any REST API responses for pet resources") bounded to currently existing endpoints, or does it include future endpoints? The contract doc notes no `/pets/{id}.json` endpoint exists today. [Clarity, Spec §FR-010, contracts/pet-api.md §Overview]
- [ ] CHK013 - Is "same authorization rules as existing pet create/edit operations" in FR-011 traceable to a specific documented authorization policy, or is it a forward reference with no backing spec? [Clarity, Spec §FR-011]

---

## Requirement Consistency

- [ ] CHK014 - Does FR-008 reference Flyway versioned migration scripts, while the plan.md and data-model.md describe raw `schema.sql` changes loaded by `<jdbc:initialize-database>` with no Flyway? Is this conflict resolved? [Conflict, Spec §FR-008, Plan §Storage]
- [ ] CHK015 - Are the JSON serialization values consistent between the contract doc ("Male", "Female", "Unknown" via `toString()`) and the spec's FR-010 ("gender field included in REST API responses")? The contract doc explicitly flags this as an unresolved decision. [Conflict, **Mandatory Gate**, contracts/pet-api.md §Gender Field Contract]
- [ ] CHK016 - Is the form submission contract (enum constant name `"MALE"`) consistent with the JSON response contract (display name `"Male"` or `"MALE"` — unresolved)? Are these two serialization contexts documented as intentionally different? [Consistency, contracts/pet-api.md §Form Submission]
- [ ] CHK017 - Are the `VARCHAR(20)` column size requirements consistent across all four dialect schema definitions in data-model.md? [Consistency, data-model.md §Database Schema]
- [ ] CHK018 - Is the `NOT NULL DEFAULT 'UNKNOWN'` constraint requirement consistent across all four dialect schema definitions, given that HSQLDB uses `DEFAULT 'UNKNOWN' NOT NULL` (reversed order)? [Consistency, data-model.md §Database Schema]

---

## Acceptance Criteria Quality

- [ ] CHK019 - Can SC-001 ("under 10 seconds") be objectively measured in the context of this feature, and is the measurement method defined (e.g., manual stopwatch, automated timing)? [Measurability, Spec §SC-001]
- [ ] CHK020 - Is SC-002 ("100% of pet records have a valid gender value") measurable with a defined verification query or mechanism post-deployment? [Measurability, Spec §SC-002]
- [ ] CHK021 - Are the acceptance scenarios in User Story 1 sufficient to cover the FEMALE case independently (Scenario 2), or does it duplicate Scenario 1 with only the value changed? Is this intentional? [Acceptance Criteria Quality, Spec §US-1]
- [ ] CHK022 - Is there an acceptance scenario covering the case where a staff member submits the form with an explicitly invalid gender value (not just omitting it)? [Coverage, Spec §US-2 Scenario 2]

---

## Scenario Coverage

- [ ] CHK023 - Are requirements defined for the concurrent edit scenario: two staff members editing the same pet's gender simultaneously? [Coverage, Gap]
- [ ] CHK024 - Are requirements defined for the behavior when the JDBC row mapper encounters an unrecognized string value in the `gender` column (e.g., data corruption or future enum expansion)? [Coverage, Edge Case, data-model.md §Row Mapper]
- [ ] CHK025 - Are requirements defined for the display of gender on the owner details page (`ownerDetails.jsp`) when a pet has `null` gender (pre-migration legacy row)? [Coverage, Edge Case, Spec §FR-007]
- [ ] CHK026 - Are requirements defined for the behavior of the gender dropdown when the form is re-displayed after a validation error on another field (e.g., is the selected gender value preserved)? [Coverage, Exception Flow, Spec §FR-009]
- [ ] CHK027 - Are requirements defined for XML serialization of the `gender` field, given the project supports both JSON and XML `@ResponseBody` responses? [Coverage, Gap, contracts/pet-api.md §Overview]

---

## Edge Case Coverage

- [ ] CHK028 - Is the legacy data migration requirement (FR-007, SC-005) specified with a concrete mechanism — e.g., does the schema `DEFAULT 'UNKNOWN'` handle existing rows, or is an explicit `UPDATE` statement required? [Edge Case, Clarity, Spec §FR-007, data-model.md §Seed Data]
- [ ] CHK029 - Is the behavior defined when `Gender.valueOf()` throws `IllegalArgumentException` during JDBC row mapping (e.g., corrupted DB value)? Should it fall back to UNKNOWN or propagate the error? [Edge Case, Gap, data-model.md §Row Mapper]
- [ ] CHK030 - Is the behavior defined when the `genders` model attribute is unavailable in the view (e.g., controller method not invoked due to exception)? [Edge Case, Gap, data-model.md §PetController]
- [ ] CHK031 - Are requirements defined for the gender field behavior during a pet import or bulk data load scenario (if applicable)? [Edge Case, Assumption, Spec §Assumptions]

---

## Non-Functional Requirements

- [ ] CHK032 - Are accessibility requirements defined for the gender `<select>` dropdown (e.g., `<label>` association, ARIA attributes, keyboard navigation)? [Coverage, Gap, Spec §FR-009]
- [ ] CHK033 - Are internationalisation (i18n) requirements defined for the gender display labels ("Male", "Female", "Unknown"), or is English-only explicitly scoped? [Coverage, Assumption, Spec §Assumptions]
- [ ] CHK034 - Are there performance requirements defined for the schema migration execution time against large `pets` tables (e.g., if MySQL/PostgreSQL tables have millions of rows)? [Non-Functional, Gap]
- [ ] CHK035 - Are security requirements defined to ensure the gender field is not a vector for injection (e.g., is enum-only binding sufficient, or is explicit sanitization required)? [Non-Functional, Spec §FR-002]

---

## Dependencies & Assumptions

- [ ] CHK036 - Is the assumption "three allowed values are fixed and not user-configurable" documented as a formal constraint in the spec, or only in the data-model.md? [Assumption, Spec §Assumptions, data-model.md §Validation Rules]
- [ ] CHK037 - Is the assumption that `Spring MVC ConversionService` automatically converts the form string `"MALE"` to `Gender.MALE` validated against the actual Spring Framework 7.0.6 configuration in this project? [Assumption, contracts/pet-api.md §Form Submission]
- [ ] CHK038 - Is the dependency on `@Enumerated(EnumType.STRING)` for JPA persistence documented as a requirement, and is the behavior for the JDBC and Spring Data JPA profiles explicitly specified separately? [Dependency, data-model.md §Modified Entity]
- [ ] CHK039 - Is the assumption that `ClinicServiceImpl.savePet` requires no changes validated against all three persistence profiles, or only the JPA profile? [Assumption, Plan §Project Structure]

---

## Ambiguities & Conflicts

- [ ] CHK040 - **[BLOCKING]** Is the JSON serialization format for the `gender` field resolved and documented? The contract doc presents two options (display name `"Male"` vs. enum constant `"MALE"`) and explicitly defers the decision. This must be resolved before implementation. [Conflict, **Mandatory Gate**, contracts/pet-api.md §Gender Field Contract]
- [ ] CHK041 - **[BLOCKING]** Is the Flyway vs. raw SQL migration conflict between FR-008 and the plan/data-model resolved? FR-008 mandates a "Flyway versioned SQL migration script" but the plan explicitly states "no Flyway in this project." [Conflict, **Mandatory Gate**, Spec §FR-008, Plan §Storage]
- [ ] CHK042 - Is it specified whether `PetValidator` should be modified to validate the gender field, or is enum-type binding alone sufficient? The plan states "NO CHANGE" for `PetValidator` but does not document the rationale. [Ambiguity, Plan §Project Structure, Spec §FR-002]
- [ ] CHK043 - Is the scope of "existing pet records" in FR-007 and SC-005 clarified to include only the seed `data.sql` rows, or also any rows in a live database at deployment time? [Ambiguity, Spec §FR-007, Spec §SC-005]

---

## Notes

- Items marked **[BLOCKING]** or **[Mandatory Gate]** must be resolved before implementation proceeds.
- CHK014 and CHK041 flag the same Flyway/raw-SQL conflict from two angles — both must be addressed together.
- CHK015 and CHK040 flag the same JSON serialization ambiguity — resolving CHK040 resolves CHK015.
- Mark items complete with `[x]` and add inline findings or resolution notes.
