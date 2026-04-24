# Quickstart: Add Gender Enum to Pet

**Feature**: 001-add-pet-gender-enum  
**Date**: 2026-04-24

## Prerequisites

- Java 17+
- Maven 3.8+
- Working Spring PetClinic build

## Build & Run

```bash
./mvnw clean package
./mvnw jetty:run
```

Access: http://localhost:8080/petclinic/

## Quick Changes (Priority Order)

1. **Create Gender enum** (`src/main/java/.../model/Gender.java`)
2. **Add gender to Pet entity**
3. **Add DB columns** (H2, MySQL, PostgreSQL schema)
4. **Add gender dropdown to JSP**
5. **Run tests**: `./mvnw test`

## Key Files

| File | Action |
|------|--------|
| `src/main/java/.../model/Gender.java` | NEW |
| `src/main/java/.../model/Pet.java` | MODIFY - add gender field |
| `src/main/java/.../repository/jdbc/JdbcPet.java` | MODIFY |
| `src/main/java/.../repository/jdbc/JdbcPetRowMapper.java` | MODIFY |
| `src/main/resources/db/*/schema.sql` | MODIFY |
| `src/main/resources/messages.properties` | MODIFY |
| `src/main/webapp/WEB-INF/views/pets/createOrUpdatePetForm.jsp` | MODIFY |

## Verification

1. Create new pet → select gender from dropdown
2. View pet details → gender displays correctly
3. Update pet → gender changes persist
4. `./mvnw test` → all tests pass

## Localization

Add to `messages.properties`:
```properties
pet.gender.male=Male
pet.gender.female=Female
pet.gender.unknown=Unknown
```