# Data Model: add-gender-enum

## Entities

### Pet
Represents a pet in the clinic system.

**Attributes**:
- id: Long (inherited from BaseEntity)
- name: String (inherited from NamedEntity)
- birthDate: LocalDate
- type: PetType
- owner: Owner
- visits: Set<Visit>
- gender: Gender (NEW)

**Relationships**:
- ManyToOne with PetType
- ManyToOne with Owner
- OneToMany with Visit (cascade = ALL, mappedBy = "pet")

### Gender (NEW)
Enum type representing pet gender.

**Values**:
- MALE
- FEMALE
- UNKNOWN

## Field Details

### Pet.gender
- **Type**: Gender (Java Enum)
- **Nullability**: NOT NULL (required)
- **Validation**: 
  - Must not be null
  - Must be one of MALE, FEMALE, UNKNOWN
  - Invalid values throw IllegalArgumentException
- **Persistence**: 
  - Stored as VARCHAR(10) in database
  - Maps to enum string value
- **Usage**:
  - Getter: getGender()
  - Setter: setGender(Gender gender)

## Database Schema Changes

### PETS Table
Add column:
- GENDER: VARCHAR(10) NOT NULL

## Validation Rules

1. Gender attribute must always have a value (non-nullable)
2. Gender value must be one of the predefined enum values (MALE, FEMALE, UNKNOWN)
3. Attempts to set null or invalid values will result in IllegalArgumentException

## Implementation Notes

- The Gender enum will be placed in org.springframework.samples.petclinic.model package
- Pet entity will be updated to include the gender attribute with appropriate JPA annotations
- Existing constructors and methods will need to be updated to handle the new attribute
- Validation will be implemented in the setter method to enforce business rules