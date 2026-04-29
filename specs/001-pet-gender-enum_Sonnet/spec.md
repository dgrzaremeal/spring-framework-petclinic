# Feature Specification: Pet Gender/Sex Enum

**Feature Branch**: `001-pet-gender-enum_Sonnet`  
**Created**: 2026-04-29  
**Status**: Draft  
**Input**: User description: "Gender/Sex Gender enum (MALE, FEMALE, UNKNOWN) in Pet."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Record Pet Gender at Registration (Priority: P1)

A clinic staff member registers a new pet and selects the pet's gender/sex from a predefined list of options: Male, Female, or Unknown. This ensures accurate medical records from the point of entry.

**Why this priority**: Gender/sex is a medically relevant attribute for pets (e.g., reproductive health, dosage calculations). Capturing it at registration is the primary use case and delivers immediate value.

**Independent Test**: Can be fully tested by creating a new pet record and verifying that the gender field accepts only the three valid values (MALE, FEMALE, UNKNOWN) and persists correctly.

**Acceptance Scenarios**:

1. **Given** a staff member is creating a new pet record, **When** they select "Male" from the gender field, **Then** the pet is saved with gender MALE and the value is displayed on the pet's profile.
2. **Given** a staff member is creating a new pet record, **When** they select "Female" from the gender field, **Then** the pet is saved with gender FEMALE and the value is displayed on the pet's profile.
3. **Given** a staff member is creating a new pet record, **When** the pet's gender is not known, **Then** they can select "Unknown" and the pet is saved with gender UNKNOWN.
4. **Given** a staff member is creating a new pet record, **When** they do not select a gender, **Then** the system defaults to UNKNOWN and saves successfully.

---

### User Story 2 - Update Pet Gender on Existing Record (Priority: P2)

A clinic staff member edits an existing pet's profile to correct or update the gender/sex value. This supports data correction when a pet's gender was initially unknown or entered incorrectly.

**Why this priority**: Existing records may have been created before this feature existed or with incorrect data. Editing is essential for data accuracy but is secondary to initial capture.

**Independent Test**: Can be fully tested by opening an existing pet record, changing the gender field to a different valid value, saving, and verifying the updated value is displayed.

**Acceptance Scenarios**:

1. **Given** an existing pet record with gender UNKNOWN, **When** a staff member updates the gender to FEMALE and saves, **Then** the pet's profile reflects FEMALE.
2. **Given** an existing pet record with a gender value, **When** a staff member attempts to set an invalid gender value, **Then** the system rejects the input and displays an appropriate error message.

---

### User Story 3 - View Pet Gender on Profile (Priority: P3)

A clinic staff member or veterinarian views a pet's profile and can clearly see the pet's gender/sex displayed in a human-readable format (e.g., "Male", "Female", "Unknown").

**Why this priority**: Display is dependent on data capture (P1) and is a read-only concern. It delivers value only after gender data exists.

**Independent Test**: Can be fully tested by viewing a pet profile with a known gender value and confirming the label is displayed correctly.

**Acceptance Scenarios**:

1. **Given** a pet record with gender MALE, **When** a staff member views the pet's profile, **Then** the gender is displayed as "Male" (or equivalent human-readable label).
2. **Given** a pet record with gender UNKNOWN, **When** a staff member views the pet's profile, **Then** the gender is displayed as "Unknown".

---

### Edge Cases

- What happens when an existing pet record has no gender value (legacy data)? The system should treat it as UNKNOWN.
- How does the system handle an attempt to save a pet with an unrecognized gender value? The system must reject it with a validation error.
- What happens if the gender field is omitted entirely during pet creation? The system defaults to UNKNOWN.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The Pet entity MUST have a gender/sex attribute that accepts exactly three values: MALE, FEMALE, and UNKNOWN.
- **FR-002**: The system MUST validate that any gender value assigned to a pet is one of the three allowed values (MALE, FEMALE, UNKNOWN); any other value MUST be rejected.
- **FR-003**: Users MUST be able to select a gender value when creating a new pet record.
- **FR-004**: Users MUST be able to update the gender value on an existing pet record.
- **FR-005**: The system MUST display the pet's gender in a human-readable format on the pet's profile view.
- **FR-006**: The system MUST default to UNKNOWN when no gender is explicitly provided during pet creation.
- **FR-007**: Legacy pet records without a gender value MUST be treated as UNKNOWN. In this project, all data is loaded from `data.sql` seed files on startup (no persistent live database); the `DEFAULT 'UNKNOWN'` column definition in `schema.sql` and the explicit `'UNKNOWN'` values in all `INSERT INTO pets` seed rows are sufficient to satisfy this requirement. No separate backfill script is needed.
- **FR-008**: The `gender` column MUST be added to the database by modifying the raw SQL `schema.sql` file for each supported dialect (H2, HSQLDB, MySQL, PostgreSQL) to include `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` in the `CREATE TABLE pets` statement. All `INSERT INTO pets` seed rows in the corresponding `data.sql` files MUST be updated to include an explicit `gender` column value of `'UNKNOWN'`. No Flyway migration is used in this project.
- **FR-009**: The gender field on the pet create/edit form MUST be rendered as an HTML `<select>` dropdown, consistent with other constrained fields (e.g., pet type). The dropdown MUST list options: Male, Female, Unknown, with Unknown pre-selected by default.
- **FR-010**: The `gender` field MUST be included in any REST API responses for pet resources (e.g., `/pets`, `/pets/{id}`) as a new additive field. This is a non-breaking change; no API versioning is required.
- **FR-011**: Access to create or update a pet's gender MUST follow the same authorization rules as existing pet create/edit operations. No new roles or permissions are introduced for this field.

### Key Entities

- **Pet**: Represents an animal registered at the clinic. Gains a new `gender` attribute with three possible values: MALE, FEMALE, UNKNOWN.
- **Gender (Enum)**: A constrained set of values — MALE, FEMALE, UNKNOWN — representing the biological sex of a pet. Stored as `VARCHAR` in the database; validation enforced at the application layer via the Java enum type.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Staff can select or update a pet's gender in under 10 seconds during pet registration or profile editing.
- **SC-002**: 100% of pet records have a valid gender value (MALE, FEMALE, or UNKNOWN) — no null or invalid states exist after the feature is deployed.
- **SC-003**: All three gender values (MALE, FEMALE, UNKNOWN) are selectable and persistable without error.
- **SC-004**: Invalid gender values are rejected 100% of the time, with a clear error message presented to the user.
- **SC-005**: Existing pet records without a gender value are automatically assigned UNKNOWN with no data loss.

## Clarifications

### Session 2026-04-29

- Q: How should the database migration for adding the `gender` column be handled? → A: Modify the raw `schema.sql` file for each supported DB dialect (H2, HSQLDB, MySQL, PostgreSQL) directly. No Flyway is used in this project; Spring's `<jdbc:initialize-database>` loads `schema.sql` and `data.sql` on startup.
- Q: What UI component should be used to present the gender options on the pet form? → A: Dropdown (HTML `<select>`)
- Q: How should the `gender` field be stored in the database? → A: VARCHAR with application-level enum validation
- Q: Does the gender field need REST API exposure? → A: Include as new additive field — no breaking change
- Q: Should access to create/update gender be restricted by user role? → A: Same authorization as existing pet create/edit — no new role restrictions

## Assumptions

- The Pet entity already exists in the system; this feature adds a new attribute to it.
- The three allowed values (MALE, FEMALE, UNKNOWN) are fixed and not user-configurable.
- UNKNOWN is the appropriate default for pets whose gender has not been determined or recorded.
- Legacy pet records (created before this feature) will be migrated to UNKNOWN rather than left null.
- The gender field is not mandatory at the UI level — omitting it defaults to UNKNOWN.
- Display labels (e.g., "Male", "Female", "Unknown") are the human-readable representations of the enum values.
- No additional gender values (e.g., NEUTERED, SPAYED) are in scope for this feature.
