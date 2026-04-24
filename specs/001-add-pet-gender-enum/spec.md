# Feature Specification: Add Gender Enum to Pet

**Feature Branch**: `001-add-pet-gender-enum`  
**Created**: 2026-04-24  
**Status**: Draft  
**Input**: User description: "Gender/Sex Gender enum (MALE, FEMALE, UNKNOWN) in Pet."

## Clarifications

### Session 2026-04-24

- Q: Gender display labels (Male/Female/Unknown) - should these be hardcoded English, properties file, or enum-based Map with localized keys? → A: Enum-based Map with localized keys (e.g., `Gender.MALE.getDisplayKey()` → `pet.gender.male`)
- Q: Gender input UI - should this be free-text, dropdown/radio, or combobox? → A: Dropdown/Radio selection
- Q: Database migration strategy for adding gender column? → A: Add nullable column with JPA default
- Q: Handling invalid/corrupt gender data in database? → A: Log warning and default to UNKNOWN
- Q: REST API changes for gender? → A: New endpoint or request parameter

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Record Pet Gender (Priority: P1)

As a pet owner, I want to specify the gender of my pet when adding or updating its profile, so that the veterinary staff have accurate biological information for medical care.

**Why this priority**: Gender is a fundamental attribute needed for proper veterinary care, including breeding management, medication dosing, and health records.

**Independent Test**: Can be tested by creating a new pet with a gender value and verifying it persists and displays correctly.

**Acceptance Scenarios**:

1. **Given** a new pet registration form, **When** the owner selects "MALE", **Then** the pet's gender is stored as MALE
2. **Given** a new pet registration form, **When** the owner selects "FEMALE", **Then** the pet's gender is stored as FEMALE
3. **Given** a new pet registration form, **When** the owner selects "UNKNOWN", **Then** the pet's gender is stored as UNKNOWN (default)

---

### User Story 2 - Update Pet Gender (Priority: P1)

As a pet owner, I want to update my pet's gender information, so that the records remain accurate if the original gender was unknown or incorrectly recorded.

**Why this priority**: Initial gender information may be unknown, especially for shelter animals, and should be correctable.

**Independent Test**: Can be tested by updating an existing pet's gender and verifying the change persists.

**Acceptance Scenarios**:

1. **Given** an existing pet with gender UNKNOWN, **When** the owner updates to MALE, **Then** the pet's gender is updated to MALE
2. **Given** an existing pet with gender FEMALE, **When** the owner updates to UNKNOWN, **Then** the pet's gender is updated to UNKNOWN

---

### User Story 3 - View Pet Gender (Priority: P2)

As a veterinary staff member, I want to view a pet's gender on its profile, so that I can provide appropriate care.

**Why this priority**: Staff need to quickly see gender information when treating animals.

**Independent Test**: Can be tested by viewing a pet's details and verifying gender is displayed.

**Acceptance Scenarios**:

1. **Given** a pet with gender MALE, **When** viewing the pet profile, **Then** "Male" is displayed
2. **Given** a pet with gender FEMALE, **When** viewing the pet profile, **Then** "Female" is displayed
3. **Given** a pet with gender UNKNOWN, **When** viewing the pet profile, **Then** "Unknown" is displayed

---

### User Story 4 - Edge Case Handling (Priority: P2)

As a system administrator, I want the system to handle missing or invalid gender data gracefully, so that the application remains stable and provides sensible defaults.

**Acceptance Scenarios**:

1. **Given** a new pet registration form, **When** no gender is selected, **Then** gender defaults to UNKNOWN
2. **Given** a pet created before this feature, **When** viewing the pet profile, **Then** gender displays as "Unknown"
3. **Given** corrupt gender data in database, **When** loading the pet, **Then** log warning and display "Unknown"

---

### Edge Cases

- What happens when gender is not specified during pet creation? (Default to UNKNOWN)
- How does the system handle pets created before this feature was added? (Gender should default to UNKNOWN for backward compatibility)
- Invalid/corrupt gender data in database: log warning and default to UNKNOWN

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST allow users to set a gender value (via dropdown selection) when creating a new pet
- **FR-002**: System MUST persist the selected gender value (MALE, FEMALE, or UNKNOWN) with each pet record
- **FR-003**: Users MUST be able to update the gender of an existing pet
- **FR-004**: System MUST display the gender value on the pet details page
- **FR-005**: System MUST default new pets to UNKNOWN gender if no gender is selected

### Key Entities *(include if feature involves data)*

- **Pet**: Updated entity that now includes gender attribute with values MALE, FEMALE, or UNKNOWN; column is nullable with JPA default of UNKNOWN
- **Gender**: Enumeration type with three possible values: MALE, FEMALE, UNKNOWN; each value maps to a localized display key via `Map<Gender, String>` with method `getDisplayKey()` returning key like `pet.gender.male`

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can select and save a gender value for any new pet
- **SC-002**: Existing pet records can be updated with a gender value
- **SC-003**: Gender information is visible on the pet profile page
- **SC-004**: All pets created before this feature have gender set to UNKNOWN (backward compatibility)

## Assumptions

- The gender enum is sufficient for this feature scope; no additional attributes like "Neutered" status are included
- Pet type (cat, dog, etc.) is separate from gender and will not be affected by this change
- The three-value enum (MALE, FEMALE, UNKNOWN) is consistent with common veterinary practice for recording animal gender