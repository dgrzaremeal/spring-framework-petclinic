# Research: Add Gender Enum to Pet

**Feature**: 001-add-pet-gender-enum  
**Date**: 2026-04-24

## Decisions

### 1. Gender Enum Implementation

**Decision**: Create `Gender` enum in model package with Map-based localization

**Rationale**: 
- Matches PetType approach (extends NamedEntity is a JPA entity, but this is pure enum for values)
- Enum-based Map allows localization keys: `Gender.MALE.getDisplayKey()` → `pet.gender.male`
- Used by existing pattern in PetType for type names

**Alternatives considered**:
- Hardcoded English labels: Rejected per clarification requiring localized keys
- Properties file only: Rejected - no type safety
- Database table: Overkill for 3 static values

### 2. JDBC Repository Implementation

**Decision**: Add `gender` field to JdbcPet class (extends Pet), map in JdbcPetRowMapper

**Rationale**:
- Follows existing pattern for JDBC: JdbcPet extends Pet, adds transient typeId/ownerId
- JdbcPetRowMapper shows column mapping format
- Simple VARCHAR column stores enum name

**Alternatives considered**:
- Integer gender_id with foreign key: Rejected - adds complexity without benefit
- Separate JdbcPetGender class: Overkill - simpler to store enum name directly

### 3. Database Migration

**Decision**: Add nullable `gender` column with JPA default to UNKNOWN

**Rationale**:
- Nullable column allows backward compatibility (existing pets get gender = UNKNOWN by JPA default)
- Hibernate handles @ColumnDefault or application-level default
- All schemas (H2, MySQL, PostgreSQL) need the column

**Alternatives considered**:
- Separate gender table: Too complex for 3 values
- Liquibase migration: Project doesn't use it - uses schema.sql files

### 4. UI Implementation

**Decision**: Dropdown select in createOrUpdatePetForm.jsp

**Rationale**:
- Per clarification: dropdown/radio selection
- Dropdown is cleaner for 3 options
- Following existing pet type dropdown pattern

**Alternatives considered**:
- Radio buttons: Works but takes more space

## Technical Details

### Files to Modify

| File | Change |
|------|--------|
| `src/main/java/.../model/Gender.java` | NEW - enum |
| `src/main/java/.../model/Pet.java` | Add gender field + getter/setter |
| `src/main/java/.../repository/jdbc/JdbcPet.java` | Add gender field |
| `src/main/java/.../repository/jdbc/JdbcPetRowMapper.java` | Map gender column |
| `src/main/java/.../repository/jdbc/JdbcPetRepositoryImpl.java` | Handle gender in insert/update |
| `src/main/java/.../repository/jpa/JpaPetRepositoryImpl.java` | Handle gender |
| `src/main/java/.../repository/springdatajpa/SpringDataPetRepository.java` | Handle gender |
| `src/main/webapp/WEB-INF/views/pets/createOrUpdatePetForm.jsp` | Add gender dropdown |
| `src/main/resources/db/h2/schema.sql` | Add gender column |
| `src/main/resources/db/h2/data.sql` | Add Gender seed data |
| `src/main/resources/messages.properties` | Add Gender labels |
| Test files | Add Gender tests |

### Database Schema Change

```sql
ALTER TABLE pets ADD COLUMN gender VARCHAR(20) DEFAULT 'UNKNOWN';
```

### Localization Keys

```properties
pet.gender.male=Male
pet.gender.female=Female
pet.gender.unknown=Unknown
```

### Enum Structure

```java
public enum Gender {
    MALE("pet.gender.male"),
    FEMALE("pet.gender.female"),
    UNKNOWN("pet.gender.unknown");

    private final String displayKey;

    Gender(String displayKey) { ... }
    public String getDisplayKey() { ... }
}
```

## Unresolved Items

None. All NEEDS CLARIFICATION resolved via research.

## Constitution Compliance

| Principle | Compliance |
|-----------|-------------|
| TDD | Tests will be added for Gender enum and Pet gender field |
| Layered Architecture | Follows existing Controller→Service→Repository pattern |
| Persistence Agnosticism | Implemented in all 3 persistence layers |
| Quality Gates | Maven test will run |