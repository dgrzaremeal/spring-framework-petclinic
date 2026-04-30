# Feature Specification: Pet Gender/Sex Enum

**Feature Branch**: `001-pet-gender-enum_Sonnet_2`  
**Created**: 2026-04-30  
**Status**: Draft  
**Input**: User description: "Gender/Sex Gender enum (MALE, FEMALE, UNKNOWN) in Pet."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Record Pet Gender When Adding a Pet (Priority: P1)

A clinic staff member or pet owner registers a new pet in the system and selects the pet's gender/sex from a predefined list of options: Male, Female, or Unknown. This ensures accurate pet records and supports clinical workflows that may depend on gender (e.g., spay/neuter status, reproductive health).

**Why this priority**: Recording gender at the time of pet registration is the primary use case. It delivers immediate value by enriching pet profiles with a clinically relevant attribute.

**Independent Test**: Can be fully tested by adding a new pet and selecting each gender option (MALE, FEMALE, UNKNOWN), then verifying the saved record reflects the chosen value.

**Acceptance Scenarios**:

1. **Given** a staff member is adding a new pet, **When** they select "Male" as the gender, **Then** the pet record is saved with gender set to MALE.
2. **Given** a staff member is adding a new pet, **When** they select "Female" as the gender, **Then** the pet record is saved with gender set to FEMALE.
3. **Given** a staff member is adding a new pet, **When** the gender is not known, **Then** they can select "Unknown" and the pet record is saved with gender set to UNKNOWN.
4. **Given** a staff member is adding a new pet, **When** no gender is explicitly selected, **Then** the system defaults to UNKNOWN.

---

### User Story 2 - Update Pet Gender on Existing Pet Record (Priority: P2)

A clinic staff member edits an existing pet's profile to correct or update the gender/sex field. For example, a pet initially registered as UNKNOWN may later have its gender confirmed.

**Why this priority**: Existing records may lack gender data or have incorrect values. Allowing updates ensures data accuracy over time.

**Independent Test**: Can be fully tested by editing an existing pet record, changing the gender value, saving, and verifying the updated value is displayed and persisted.

**Acceptance Scenarios**:

1. **Given** an existing pet with gender UNKNOWN, **When** a staff member updates the gender to FEMALE, **Then** the pet record reflects FEMALE after saving.
2. **Given** an existing pet with a gender value, **When** a staff member views the edit form, **Then** the current gender value is pre-selected in the gender field.

---

### User Story 3 - View Pet Gender in Pet Profile (Priority: P3)

A clinic staff member or pet owner views a pet's profile and can see the pet's gender/sex displayed in a human-readable format (e.g., "Male", "Female", "Unknown") rather than a raw code.

**Why this priority**: Displaying gender in the pet profile completes the data visibility loop and supports clinical staff in quickly assessing pet information.

**Independent Test**: Can be fully tested by viewing a pet's detail page and confirming the gender label is displayed correctly for each enum value.

**Acceptance Scenarios**:

1. **Given** a pet with gender MALE, **When** a user views the pet's profile, **Then** the gender is displayed as "Male".
2. **Given** a pet with gender FEMALE, **When** a user views the pet's profile, **Then** the gender is displayed as "Female".
3. **Given** a pet with gender UNKNOWN, **When** a user views the pet's profile, **Then** the gender is displayed as "Unknown".

---

### Edge Cases

- What happens when an existing pet record has no gender value (null/empty) after the feature is introduced? A DB migration script sets all NULL gender values to UNKNOWN at deploy time.
- How does the system handle an invalid or unrecognized gender value submitted via the form? The system should reject it with a validation error and prompt the user to select a valid option.
- What happens if a user submits the pet form without selecting a gender? The system defaults to UNKNOWN.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The Pet entity MUST include a gender/sex attribute with exactly three allowed values: MALE, FEMALE, and UNKNOWN.
- **FR-002**: The system MUST display a gender dropdown (HTML `<select>`) on the pet add and edit forms, listing all three options (Male, Female, Unknown), consistent with existing form controls.
- **FR-003**: The system MUST default the gender field to UNKNOWN when no selection is made by the user.
- **FR-004**: The system MUST persist the selected gender value with the pet record.
- **FR-005**: The system MUST display the pet's gender in a human-readable label on the pet detail/profile view.
- **FR-006**: The system MUST validate that only MALE, FEMALE, or UNKNOWN are accepted as gender values; any other value MUST be rejected.
- **FR-007**: Existing pet records without a gender value MUST be migrated to UNKNOWN via a database migration script executed at deploy time; no runtime null-coercion in application code is required.

### Key Entities

- **Pet**: Represents an animal registered at the clinic. Gains a new `gender` attribute with values MALE, FEMALE, or UNKNOWN, stored as a VARCHAR string column using `@Enumerated(EnumType.STRING)`. Existing attributes (name, birth date, type, owner) are unchanged.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Staff can select and save a pet's gender in under 10 seconds during pet registration or editing.
- **SC-002**: 100% of new pet records include a gender value (defaulting to UNKNOWN if not specified).
- **SC-003**: All three gender options (Male, Female, Unknown) are available and selectable on pet add and edit forms.
- **SC-004**: Pet gender is correctly displayed on the pet profile for all three enum values with no display errors.
- **SC-005**: Invalid gender submissions are rejected 100% of the time with a clear user-facing message.

## Assumptions

- The gender field is optional at input; the system defaults to UNKNOWN when not provided.
- The three enum values (MALE, FEMALE, UNKNOWN) are fixed and exhaustive — no additional values are required for this feature.
- Existing pet records without a gender value will be migrated to UNKNOWN via a DB migration script at deploy time; no data loss is expected.
- The feature applies to all pet types in the system (dogs, cats, etc.) without distinction.
- No access control changes are required — any user who can add or edit a pet can also set its gender.
- No audit logging or audit trail is required for gender field changes.
- The human-readable labels for display are: MALE → "Male", FEMALE → "Female", UNKNOWN → "Unknown". Labels are English-only; no i18n externalization is required for this feature.

## Clarifications

### Session 2026-04-30

- Q: How should existing pet rows with a NULL gender column be handled at the database level when this feature is deployed? → A: DB migration sets NULL → UNKNOWN for all existing rows at deploy time.
- Q: What UI control should be used for the gender field on the pet add/edit forms? → A: Dropdown (HTML `<select>`), consistent with existing form controls.
- Q: How should the `gender` column be stored in the database? → A: VARCHAR string via `@Enumerated(EnumType.STRING)` (e.g., `'MALE'`, `'FEMALE'`, `'UNKNOWN'`).
- Q: Should changes to a pet's gender be logged or audited? → A: No audit logging required.
- Q: Should gender display labels be i18n-ready or English-only? → A: English-only; no i18n infrastructure needed.
