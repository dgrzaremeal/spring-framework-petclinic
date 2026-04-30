# Quickstart: Implementing Pet Gender/Sex Enum

**Feature**: `001-pet-gender-enum_Sonnet_2`  
**Branch**: `001-pet-gender-enum_Sonnet_2`

---

## Prerequisites

- JDK 17+
- Maven Wrapper available (`./mvnw` on Linux/macOS, `mvnw.cmd` on Windows)
- No external database required for development (H2 in-memory is the default)

---

## Build & Run

```bash
# Run with default H2 in-memory database
./mvnw jetty:run-war

# Run tests (all three persistence implementations)
./mvnw test

# Run with MySQL (requires running MySQL instance)
./mvnw jetty:run-war -P MySQL

# Run with PostgreSQL
./mvnw jetty:run-war -P PostgreSQL
```

Application is available at: `http://localhost:8080/`

---

## Implementation Order

Follow this order to avoid compilation errors at each step:

### Step 1 — Create `Gender` enum

**File**: `src/main/java/org/springframework/samples/petclinic/model/Gender.java`

```java
package org.springframework.samples.petclinic.model;

public enum Gender {
    MALE, FEMALE, UNKNOWN;

    @Override
    public String toString() {
        String name = this.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
```

### Step 2 — Add `gender` field to `Pet`

**File**: `src/main/java/org/springframework/samples/petclinic/model/Pet.java`

Add imports:
```java
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
```

Add field and accessors:
```java
@Enumerated(EnumType.STRING)
@Column(name = "gender")
private Gender gender = Gender.UNKNOWN;

public Gender getGender() { return this.gender; }
public void setGender(Gender gender) { this.gender = gender; }
```

### Step 3 — Update database schemas (all four)

Add `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` to the `pets` table in:
- `src/main/resources/db/h2/schema.sql`
- `src/main/resources/db/hsqldb/schema.sql`
- `src/main/resources/db/mysql/schema.sql`
- `src/main/resources/db/postgresql/schema.sql`

Update all `INSERT INTO pets` rows in the corresponding `data.sql` files to include `gender = 'UNKNOWN'`.

### Step 4 — Update `PetController`

**File**: `src/main/java/org/springframework/samples/petclinic/web/PetController.java`

Add imports:
```java
import org.springframework.samples.petclinic.model.Gender;
import java.util.Arrays;
import java.util.List;
```

Add model attribute method:
```java
@ModelAttribute("genders")
public List<Gender> populateGenders() {
    return Arrays.asList(Gender.values());
}
```

### Step 5 — Update `PetValidator`

**File**: `src/main/java/org/springframework/samples/petclinic/web/PetValidator.java`

Add defensive null check in `validate()`:
```java
if (pet.getGender() == null) {
    errors.rejectValue("gender", REQUIRED, REQUIRED);
}
```

### Step 6 — Update JDBC persistence

**File**: `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java`

In `createPetParameterSource()`, add:
```java
.addValue("gender", pet.getGender().name())
```

Update the `UPDATE` SQL to include `gender=:gender`.

**File**: `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRowMapper.java` (or equivalent)

Add:
```java
pet.setGender(Gender.valueOf(rs.getString("gender")));
```

### Step 7 — Update JSP views

**`createOrUpdatePetForm.jsp`** — add after the type select:
```jsp
<div class="control-group">
    <petclinic:selectField name="gender" label="Gender " names="${genders}" size="3"/>
</div>
```

**`ownerDetails.jsp`** — add after the Type row in the pet `<dl>`:
```jsp
<dt>Gender</dt>
<dd><c:out value="${pet.gender}"/></dd>
```

### Step 8 — Update tests

- `PetControllerTests`: add `gender` param to form submission tests; add test for `genders` model attribute
- `AbstractClinicServiceTests`: add test asserting gender is persisted and retrieved correctly
- `PetTests` (model): add test for default gender value

---

## Verify

```bash
# Build and run all tests
./mvnw test

# Manual smoke test
# 1. Navigate to http://localhost:8080/owners/find
# 2. Find any owner → Add New Pet
# 3. Verify Gender dropdown shows Male / Female / Unknown
# 4. Submit with each value and verify it appears on the owner detail page
# 5. Edit an existing pet and change the gender — verify it updates
```

---

## Key Files Reference

| File | Change |
|------|--------|
| `model/Gender.java` | NEW |
| `model/Pet.java` | Add `gender` field |
| `web/PetController.java` | Add `@ModelAttribute("genders")` |
| `web/PetValidator.java` | Add null check |
| `repository/jdbc/JdbcPetRepositoryImpl.java` | Add gender to SQL |
| `repository/jdbc/JdbcPetRowMapper.java` | Map gender column |
| `db/*/schema.sql` (×4) | Add gender column |
| `db/*/data.sql` (×4) | Add gender to seed inserts |
| `jsp/pets/createOrUpdatePetForm.jsp` | Add gender dropdown |
| `jsp/owners/ownerDetails.jsp` | Display gender label |
