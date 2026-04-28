# Research Findings: add-gender-enum

## Decisions Made During Clarification

### 1. Gender Attribute Implementation
- **Decision**: Java Enum
- **Rationale**: Provides type safety, prevents invalid values at compile time, and follows Java best practices for fixed sets of values. The enum will be defined as `Gender` with values MALE, FEMALE, UNKNOWN.
- **Alternatives Considered**: 
  - String with validation: Less type-safe, requires runtime validation
  - Integer codes: Less readable, requires mapping constants

### 2. Gender Attribute Nullability
- **Decision**: Required (non-nullable)
- **Rationale**: Ensures data integrity by guaranteeing every Pet has a valid gender value. The UNKNOWN value covers cases where gender information is not available.
- **Alternatives Considered**:
  - Optional (nullable): Would require handling null values throughout the application and could lead to incomplete data

### 3. Invalid Value Handling
- **Decision**: Throw Exception
- **Rationale**: Prevents invalid data from entering the system and provides immediate feedback when incorrect values are used. Aligns with fail-fast principles.
- **Alternatives Considered**:
  - Default to UNKNOWN: Could mask data quality issues
  - Ignore/Allow: Would compromise data integrity

## Technical Implementation Details

### Entity Changes
- Add `Gender` enum to `org.springframework.samples.petclinic.model` package
- Add `gender` attribute of type `Gender` to `Pet` entity
- Add getter and setter for gender attribute
- Update JPA mapping to persist enum value

### Validation
- Add validation in Pet setter to throw IllegalArgumentException for null values
- Consider adding validation in service layer or web layer for user input

### Database
- Add GENDER column to PETS table
- Column type: VARCHAR(10) to store enum string values
- Column constraint: NOT NULL

### Testing Approach
- Unit tests for Pet model gender attribute getter/setter
- Unit tests for validation (valid and invalid values)
- Integration tests for repository persistence
- Web layer tests for form handling and display

## References
- Java Enum best practices: https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
- Spring Data JPA enum mapping: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.enum