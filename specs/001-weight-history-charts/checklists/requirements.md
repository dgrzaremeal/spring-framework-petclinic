# Requirements Quality Checklist: Weight History & Progress Charts

**Purpose**: Formal PR gate — validates that requirements in spec.md, plan.md, data-model.md, and contracts are complete, clear, consistent, measurable, and ready for implementation across all layers (entity, validation, persistence, service, UI, chart).
**Created**: 2026-05-04
**Feature**: [spec.md](../spec.md) | [plan.md](../plan.md) | [data-model.md](../data-model.md) | [contracts/ui-contracts.md](../contracts/ui-contracts.md)

---

## Requirement Completeness

- [ ] CHK001 — Are requirements defined for all three persistence implementations (JPA, JDBC, Spring Data JPA) for `WeightRecord`, or is it documented which implementations are out of scope? [Completeness, Spec §FR-003, Constitution III]
- [ ] CHK002 — Are requirements specified for what happens when a user navigates to the weight history page for a pet that belongs to a different owner (authorization boundary)? [Completeness, Gap]
- [ ] CHK003 — Are requirements defined for the weight unit label displayed in the UI (e.g., "kg" suffix on form fields, list columns, and chart axis)? [Completeness, Gap]
- [ ] CHK004 — Are loading state requirements defined for the chart while weight data is being fetched/rendered? [Completeness, Gap]
- [ ] CHK005 — Are requirements specified for how the weight history list is sorted when multiple records share the same `measurementDate` (tie-breaking order)? [Completeness, Spec §FR-004]
- [ ] CHK006 — Are requirements defined for the navigation path back to the pet's profile from the weight history page? [Completeness, Gap]
- [ ] CHK007 — Are requirements specified for the `WeightRecord` fields that must be displayed in the history list (minimum: date + weight; are any additional fields required)? [Completeness, Spec §FR-004, User Story 2 §AC-3]
- [ ] CHK008 — Are caching requirements defined for weight record queries, consistent with the existing `findVets()` caching pattern? [Completeness, Gap]

---

## Requirement Clarity

- [ ] CHK009 — Is "chronological order" in FR-004 and User Story 2 defined unambiguously as ascending (oldest first) or descending (most recent first)? The spec says "most recent first or oldest first, consistently" — is the actual direction decided? [Clarity, Spec §FR-004, User Story 2 §AC-1]
- [ ] CHK010 — Is "clear validation error" in FR-002 and User Story 1 §AC-3 defined with specific error message text or format requirements, or is the wording left to implementation? [Clarity, Spec §FR-002]
- [ ] CHK011 — Is "readable" in User Story 3 §AC-3 (50+ records chart) quantified with specific criteria (e.g., minimum point size, axis label density, scroll/zoom behavior)? [Clarity, Ambiguity, Spec §User Story 3 §AC-3]
- [ ] CHK012 — Is "friendly message" in FR-007 and User Story 2 §AC-2 defined with specific copy or content requirements, or is the wording left to implementation? [Clarity, Spec §FR-007]
- [ ] CHK013 — Is the `weightKg` field name and its display label ("Weight (kg)", "Weight", etc.) consistently defined across spec, data-model, and UI contracts? [Clarity, Consistency]
- [ ] CHK014 — Is the date format for display in the history list and chart axis explicitly specified (e.g., `yyyy/MM/dd`, `dd MMM yyyy`, ISO 8601)? [Clarity, Gap]
- [ ] CHK015 — Is "simple line or scatter chart" in Assumptions quantified — is it a line chart, scatter chart, or either? The contracts specify line chart; does the spec need to be updated to match? [Clarity, Conflict, Spec §Assumptions, contracts/ui-contracts.md]

---

## Requirement Consistency

- [ ] CHK016 — Does the validation rule "positive numbers greater than 0" in FR-002 align with the `@DecimalMin("0.001")` constraint in data-model.md, or does the spec need to explicitly state the minimum precision? [Consistency, Spec §FR-002, data-model.md]
- [ ] CHK017 — Is the maximum weight value (250 kg) stated consistently across FR-002, Edge Cases, Clarifications, and data-model.md validation rules? [Consistency, Spec §FR-002]
- [ ] CHK018 — Does the "append-only" assumption in Assumptions align with FR-008 ("without modifying or deleting existing records")? Are both sections consistent in scope and wording? [Consistency, Spec §Assumptions, §FR-008]
- [ ] CHK019 — Are the URL patterns in contracts/ui-contracts.md consistent with the controller path conventions described in plan.md? [Consistency, contracts/ui-contracts.md, plan.md]
- [ ] CHK020 — Is the "measurement date must be today or in the past" rule stated consistently across FR-002, Edge Cases, and Clarifications? [Consistency, Spec §FR-002, §Edge Cases, §Clarifications]

---

## Acceptance Criteria Quality

- [ ] CHK021 — Is SC-001 ("under 30 seconds") measurable with a defined starting point (e.g., from page load, from first click)? [Measurability, Spec §SC-001]
- [ ] CHK022 — Is SC-003 ("within 2 seconds") defined relative to a specific network/hardware baseline, or is it an absolute requirement regardless of environment? [Measurability, Spec §SC-003]
- [ ] CHK023 — Is SC-005 ("task completion rate ≥ 90%") measurable within the scope of this implementation, or does it require a usability study? If the latter, is it a release gate or a post-release metric? [Measurability, Spec §SC-005]
- [ ] CHK024 — Is SC-002 ("up to 100 recorded entries without pagination issues") defined with a specific rendering/response time threshold, or is "no pagination issues" the only criterion? [Measurability, Spec §SC-002]
- [ ] CHK025 — Are the acceptance scenarios in User Stories 1–3 sufficient to gate PR merge, or are additional scenarios needed for the three persistence implementations? [Acceptance Criteria Quality, Spec §User Stories]

---

## Scenario Coverage

- [ ] CHK026 — Are requirements defined for the alternate flow where a user submits the weight form and the persistence layer throws an unexpected exception (e.g., DB unavailable)? [Coverage, Exception Flow, Gap]
- [ ] CHK027 — Are requirements specified for the scenario where `petId` in the URL does not belong to `ownerId` (mismatched path variables)? [Coverage, Exception Flow, Gap]
- [ ] CHK028 — Are requirements defined for the scenario where a user navigates directly to `/owners/{ownerId}/pets/{petId}/weights/new` without going through the pet profile (deep link)? [Coverage, Alternate Flow, Gap]
- [ ] CHK029 — Are requirements specified for the chart behavior when all weight records for a pet are on the same date (no time spread on X axis)? [Coverage, Edge Case, Gap]
- [ ] CHK030 — Are requirements defined for the primary flow where a clinic staff member (not the owner) records a weight entry — is the UI path the same as for owners? [Coverage, Spec §FR-001, §Assumptions]

---

## Edge Case Coverage

- [ ] CHK031 — Is the behavior defined when a user enters a weight value with more than 3 decimal places (e.g., 4.1234 kg) — is it rounded, truncated, or rejected? [Edge Case, Spec §FR-002, data-model.md `DECIMAL(6,3)`]
- [ ] CHK032 — Is the behavior defined when `measurementDate` is submitted as today's date in a timezone different from the server's timezone (future date in server time)? [Edge Case, Gap]
- [ ] CHK033 — Are requirements defined for the chart when weight records span several years (X-axis label density, readability)? The spec mentions this as an open question in Edge Cases. [Edge Case, Spec §Edge Cases]
- [ ] CHK034 — Is the behavior defined when a weight record is deleted (spec says "out of scope for this version" in Assumptions) — is this explicitly documented as a deferred requirement? [Edge Case, Spec §Assumptions]
- [ ] CHK035 — Is the behavior defined for the chart when a pet has exactly one weight record — does it show a single point, a message, or both? The spec says "single data point is shown OR a message" — is the OR resolved? [Edge Case, Clarity, Spec §User Story 3 §AC-2]

---

## Non-Functional Requirements

- [ ] CHK036 — Are accessibility requirements defined for the weight entry form (keyboard navigation, screen reader labels for weight and date fields)? [Coverage, Gap, NFR]
- [ ] CHK037 — Are accessibility requirements defined for the Chart.js chart (alt text, ARIA labels, or a data table fallback for screen readers)? [Coverage, Gap, NFR]
- [ ] CHK038 — Are mobile responsiveness requirements for the chart explicitly specified beyond "follows existing application's responsive design standards"? Is the existing standard documented and referenceable? [Clarity, Spec §Assumptions, NFR]
- [ ] CHK039 — Are security/authorization requirements explicitly stated for the weight record endpoints — specifically, is it documented that owners can only access their own pets' weight data? [Coverage, Spec §FR-001, §Assumptions, NFR]
- [ ] CHK040 — Are performance requirements defined for the JDBC and JPA persistence implementations specifically (not just the UI rendering time in SC-003)? [Coverage, Gap, NFR]

---

## Dependencies & Assumptions

- [ ] CHK041 — Is the assumption "weight values are recorded in kilograms only" documented as a constraint that affects the data model (`DECIMAL(6,3)`) and UI labels? [Assumption, Spec §Assumptions, data-model.md]
- [ ] CHK042 — Is the Chart.js WebJar version (`org.webjars.npm:chart.js:4.5.0`) pinned in requirements/plan, and is there a documented process for version upgrades? [Dependency, plan.md §Technical Context]
- [ ] CHK043 — Is the assumption that "authentication and authorization reuse existing PetClinic access control" validated — is the existing access control mechanism documented well enough to implement the owner-vs-staff distinction for weight records? [Assumption, Spec §Assumptions]
- [ ] CHK044 — Is the dependency on all four database schema files (H2, HSQLDB, MySQL, PostgreSQL) being updated explicitly listed as a requirement, or could it be missed during implementation? [Dependency, data-model.md, Constitution III]

---

## Ambiguities & Conflicts

- [ ] CHK045 — The spec says "most recent first or oldest first, consistently" for list ordering (User Story 2 §AC-1) but does not decide which. The data-model.md specifies `ORDER BY measurement_date ASC`. Is this conflict resolved and reflected back in the spec? [Conflict, Spec §User Story 2 §AC-1, data-model.md]
- [ ] CHK046 — The spec Assumptions say "simple line or scatter chart" but contracts/ui-contracts.md specifies "line chart" only. Is the scatter chart option intentionally dropped, and should the spec be updated? [Conflict, Spec §Assumptions, contracts/ui-contracts.md]
- [ ] CHK047 — Is the term "pet profile page" used consistently across FR-005, FR-006, and User Stories to refer to the same URL/view, or could it refer to different pages? [Ambiguity, Spec §FR-005, §FR-006]
- [ ] CHK048 — FR-006 says "from the pet's profile page" but the contracts define a separate `/weights` URL. Is the weight history section embedded in the pet profile, or is it a separate page linked from the profile? This affects the navigation requirements. [Ambiguity, Conflict, Spec §FR-006, contracts/ui-contracts.md]

---

## Notes

- Check items off as completed: `[x]`
- Add findings or decisions inline after the item
- Items marked `[Gap]` indicate requirements not yet present in the spec — they need to be added or explicitly deferred
- Items marked `[Conflict]` indicate inconsistencies between documents that must be resolved before implementation
- Items marked `[Ambiguity]` indicate vague requirements that need clarification
- High-priority items for PR gate: CHK009, CHK015, CHK035, CHK045, CHK046, CHK048 (conflicts/ambiguities that directly affect implementation decisions)
