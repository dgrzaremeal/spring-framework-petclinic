# Quickstart: Implementing Pet Gender/Sex Enum

**Feature**: `001-pet-gender-enum_Sonnet`  
**Branch**: `001-pet-gender-enum_Sonnet`  
**Date**: 2026-04-29

---

## Prerequisites

- JDK 17+
- Maven Wrapper available (`./mvnw` or `mvnw.cmd`)
- Working directory: repository root

---

## Build & Run

```bash
# Run with default H2 in-memory database (JPA profile)
./mvnw jetty:run-war

# Run with JDBC profile
./mvnw jetty:run-war -P jdbc

# Run with Spring Data JPA profile
./mvnw jetty:run-war -P spring-data-jpa
```

Application starts at `http://localhost:8080/petclinic`

---

## Run Tests

```bash
# All tests (H2 in-memory, default JPA profile)
./mvnw test

# Tests with JDBC profile
./mvnw test -P jdbc

# Tests with Spring Data JPA profile
./mvnw test -P spring-data-jpa
```

All three profiles must pass before the feature is considered complete.

---

## Implementation Order

Follow this order to avoid compilation errors at each step:

### Step 1 — Create the Gender enum

Create `src/main/java/org/springframework/samples/petclinic/model/Gender.java`:

```java
package org.springframework.samples.petclinic.model;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    UNKNOWN("Unknown");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
```

### Step 2 — Add gender field to Pet entity

In `src/main/java/org/springframework/samples/petclinic/model/Pet.java`, add:

```java
@Enumerated(EnumType.STRING)
@Column(name = "gender")
private Gender gender = Gender.UNKNOWN;

public Gender getGender() { return gender; }
public void setGender(Gender gender) { this.gender = gender; }
```

### Step 3 — Update all four schema.sql files

Add `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` to the `CREATE TABLE pets` statement in:
- `src/main/resources/db/h2/schema.sql`
- `src/main/resources/db/hsqldb/schema.sql`
- `src/main/resources/db/mysql/schema.sql`
- `src/main/resources/db/postgresql/schema.sql`

### Step 4 — Update all four data.sql files

Update the `INSERT INTO pets` statements to include `gender = 'UNKNOWN'` for all existing seed rows. Use explicit column names for clarity. Update:
- `src/main/resources/db/h2/data.sql`
- `src/main/resources/db/hsqldb/data.sql`
- `src/main/resources/db/mysql/data.sql`
- `src/main/resources/db/postgresql/data.sql`

### Step 5 — Update JDBC implementation

In `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPet.java`:
- Add `gender` field with getter/setter (same as Pet.java addition)

In `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java`:
- Add `gender` to `createPetParameterSource`: `.addValue("gender", pet.getGender().name())`
- Add `gender` to the UPDATE SQL statement

In the row mapper (find `JdbcPetRowMapper` or `JdbcPetVisitExtractor`):
- Map `gender` column: `pet.setGender(Gender.valueOf(rs.getString("gender")))`

### Step 6 — Update PetController

In `src/main/java/org/springframework/samples/petclinic/web/PetController.java`, add:

```java
@ModelAttribute("genders")
public Gender[] populateGenders() {
    return Gender.values();
}
```

### Step 7 — Update the pet form JSP

In `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`, add after the type select:

```jsp
<div class="control-group">
    <petclinic:selectField name="gender" label="Gender " names="${genders}" size="3"/>
</div>
```

### Step 8 — Update the owner details JSP

In `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`, add after the Type display:

```jsp
<dt>Gender</dt>
<dd><c:out value="${pet.gender.displayName}"/></dd>
```

### Step 9 — Add/update tests

- `ClinicServiceTests`: Assert that saved pets have the correct gender value; test UNKNOWN default.
- `PetControllerTests`: Verify `genders` model attribute is populated; test form submission with each gender value.
- Integration tests: Run against all three persistence profiles.

---

## Verify the Feature

1. Start the app: `./mvnw jetty:run-war`
2. Navigate to `http://localhost:8080/petclinic/owners/1`
3. Click "Add New Pet" — verify the Gender dropdown appears with Male, Female, Unknown options and Unknown is pre-selected.
4. Create a pet with gender "Male" — verify it saves and displays "Male" on the owner detail page.
5. Edit the pet — verify the gender dropdown shows the current value and can be changed.
6. Run all tests: `./mvnw test` — all must pass.

---

## Key Files Reference

| File | Change |
|------|--------|
| `src/main/java/.../model/Gender.java` | NEW |
| `src/main/java/.../model/Pet.java` | Add `gender` field |
| `src/main/resources/db/*/schema.sql` | Add `gender` column (×4) |
| `src/main/resources/db/*/data.sql` | Add `gender` values to pet inserts (×4) |
| `src/main/java/.../repository/jdbc/JdbcPet.java` | Add `gender` field |
| `src/main/java/.../repository/jdbc/JdbcPetRepositoryImpl.java` | Include `gender` in SQL |
| `src/main/java/.../web/PetController.java` | Add `genders` model attribute |
| `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp` | Add gender dropdown |
| `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp` | Display gender |
