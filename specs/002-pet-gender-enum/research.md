# Research: Add Gender Enum to Pet

## Decision: Gender Enum Implementation

**Decision**: Create a Gender enum class with MALE, FEMALE, UNKNOWN values, stored as a String/VARCHAR column in the database, using Jakarta Persistence @Enumerated annotation.

**Rationale**: 
- Enum is the most type-safe approach for fixed values
- Using String storage (VARCHAR) rather than ordinal provides better data portability and readability
- Default value of UNKNOWN ensures all existing and new pets have valid gender
- Follows existing pattern used for PetType (which extends NamedEntity and stores name as String)

**Alternatives considered**:
- Store as ordinal integer - Rejected because ordinal values change if enum order changes, breaking data integrity
- Use separate Gender table with foreign key - Over-engineered for 3 fixed values

## Decision: Schema Changes for All Persistence Implementations

**Decision**: Add gender column to pets table in H2, MySQL, and PostgreSQL schemas.

**Rationale**: Constitution requires updating all three persistence implementations (JPA default, JDBC, Spring Data JPA). Each has its own schema file that must be updated.

**Alternatives considered**:
- Only update H2 schema - Rejected because Constitution requires all three implementations
- Use Liquibase migration - Not used in this project; manual schema updates required

## Decision: Service Layer Changes

**Decision**: No changes to ClinicService interface required; add gender field to Pet entity, service layer handles it automatically through entity persistence.

**Rationale**: Adding a field to existing entity doesn't require service interface changes. Gender is a simple attribute like name/birthDate.

**Alternatives considered**:
- Add gender-specific methods to service - Unnecessary complexity; field access follows existing patterns

## Decision: UI Implementation

**Decision**: Add gender selection dropdown to pet form JSP, display gender in pet details. Use human-readable labels (Male/Female/Unknown) per spec assumption.

**Rationale**: Follows existing UI patterns for pet type selection. Display uses labels, not enum codes per spec.

**Alternatives considered**:
- Use radio buttons - Less space-efficient for 3 options
- Free text input - Would require validation, less user-friendly

## Research Complete

All NEEDS CLARIFICATION items have been resolved. The implementation path is clear:
1. Create Gender enum class
2. Add gender field to Pet entity
3. Update all three database schemas (H2, MySQL, PostgreSQL)
4. Add gender to views/forms
5. Add/update tests