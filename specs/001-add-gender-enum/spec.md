# Feature Specification: add-gender-enum

**Feature Branch**: `001-add-pet-gender-enum`  
**Created**: 2026-04-28  
**Status**: Draft  
**Input**: User description: "Gender/Sex Gender enum (MALE, FEMALE, UNKNOWN) in Pet."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Add Gender Enum to Pet Entity (Priority: P1)

As a user of the PetClinic system, I want the Pet entity to have a gender/sex attribute with predefined values (MALE, FEMALE, UNKNOWN) so that I can accurately record and track pet gender information.

**Why this priority**: This is a core attribute for pet records that enables proper pet management and reporting capabilities.

**Independent Test**: Can be fully tested by creating a Pet object with each gender value and verifying the attribute is correctly stored and retrieved.

**Acceptance Scenarios**:
1. **Given** a new Pet object, **When** setting gender to MALE, **Then** the pet's gender attribute equals MALE
2. **Given** a new Pet object, **When** setting gender to FEMALE, **Then** the pet's gender attribute equals FEMALE
3. **Given** a new Pet object, **When** setting gender to UNKNOWN, **Then** the pet's gender attribute equals UNKNOWN
4. **Given** a Pet with gender set, **When** retrieving the gender attribute, **Then** the correct gender value is returned

### User Story 2 - Gender Validation (Priority: P2)

As a user, I want the system to validate that only valid gender values (MALE, FEMALE, UNKNOWN) can be assigned to a Pet so that invalid gender values are prevented.

**Why this priority**: Data integrity is important to ensure only valid gender values are stored in the system.

**Independent Test**: Can be tested by attempting to assign invalid gender values and verifying they are rejected.

**Acceptance Scenarios**:
1. **Given** a Pet object, **When** attempting to set gender to an invalid value (e.g., "OTHER"), **Then** the system rejects the invalid value
2. **Given** a Pet object, **When** attempting to set gender to null/empty, **Then** the system handles this appropriately (may allow null or require a value based on business rules)

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST include a gender/sex attribute of type Gender enum in the Pet entity
- **FR-002**: System MUST restrict the gender attribute to predefined enum values: MALE, FEMALE, UNKNOWN
- **FR-003**: System MUST allow setting and retrieving the gender attribute for Pet objects
- **FR-004**: System MUST validate that only valid gender enum values can be assigned to the Pet gender attribute, throwing an exception for invalid values
- **FR-005**: System MUST require the gender attribute to have a value (non-nullable) for all Pet objects

### Key Entities

- **Pet**: Represents a pet in the clinic system, now includes a gender attribute of type Gender enum with values MALE, FEMALE, UNKNOWN
- **Gender**: Enum type representing pet gender with values MALE, FEMALE, UNKNOWN

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of Pet objects created in the system have a valid gender value (MALE, FEMALE, or UNKNOWN)
- **SC-002**: 0% of Pet objects have null gender values (gender attribute is required)
- **SC-003**: 0% of Pet objects have invalid gender values assigned (invalid values throw exceptions)
- **SC-004**: Users can successfully set and retrieve gender values for all Pet objects without errors

## Assumptions

- The gender attribute will be stored as a Java Enum value in the Pet entity
- Existing Pet creation and retrieval functionality will continue to work with the addition of the gender attribute
- The UNKNOWN value will be used when gender information is not available or not applicable
- No changes are required to the database schema beyond adding a gender column to the PETS table (if applicable)

## Clarifications

### Session 2026-04-28

- Q: How should the gender attribute be implemented in the Pet entity? → A: Java Enum (Recommended) - Provides type safety and prevents invalid values at compile time
- Q: Should the gender attribute be required (non-nullable) or optional (nullable)? → A: Required (Recommended) - Ensures data integrity by always having a valid gender value
- Q: What should happen when an invalid gender value is assigned? → A: Throw Exception (Recommended) - Prevents invalid data from entering the system