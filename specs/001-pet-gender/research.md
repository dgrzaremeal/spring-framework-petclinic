# Research: Add Gender Enum to Pet

## Decision: Gender Field Storage Format

**Decision**: Store gender as VARCHAR(20) with enum name string ("MALE", "FEMALE", "UNKNOWN")

### Rationale

- String storage is database-agnostic and works with all three persistence impls (JPA, JDBC, Spring Data JPA)
- Ordinal-based storage is fragile across database migrations
- Explicit string names are more readable in database

### Alternatives Considered

1. **Ordinal (INT)**: Rejected - not portable, ordinal values shift if enum order changes
2. **Enum name (VARCHAR)**: Chosen - portable, readable, works with all persistence layers
3. **Separate lookup table**: Rejected - overkill for 3-value enum

## Decision: Filtering by Gender

**Decision**: Add filtering capability via ClinicService and PetRepository

### Rationale

- Feature spec requires filtering pets by gender (FR-005)
- Need to add findByGender method to all three repository implementations
- Service layer passes filter through to repository

### Implementation Approach

- JPA: Add query method: `findByGender(Gender gender)` 
- JDBC: Add WHERE clause with parameter
- Spring Data JPA: Derived query method

## Decision: Database Column Addition

**Decision**: Add nullable gender column to pets table in all schema files

### Rationale

- Existing pets pre-date this feature - must default to UNKNOWN
- Use nullable column with UNKNOWN as application-level default
- No data migration needed - handles existing records

### Schema Changes Required

- H2: `ALTER TABLE pets ADD gender VARCHAR(20)`
- MySQL: Same
- PostgreSQL: Same
- Remove constraint to make it nullable for backward compatibility