# Quickstart: Add Gender Enum to Pet

## Developer Setup

```bash
# Prerequisites
- Java 17+
- Maven 3.8+
- Git

# Clone and build
git clone <repo-url>
cd spring-framework-petclinic
./mvnw compile
```

## Implementation Steps

### Step 1: Create Gender Enum

Create `src/main/java/org/springframework/samples/petclinic/model/Gender.java`:
```java
public enum Gender {
    MALE,
    FEMALE,
    UNKNOWN
}
```

### Step 2: Add Gender Field to Pet Entity

Modify `src/main/java/org/springframework/samples/petclinic/model/Pet.java`:

```java
@Column(name = "gender")
private Gender gender;

public Gender getGender() {
    return gender;
}

public void setGender(Gender gender) {
    this.gender = gender;
}
```

Set default in constructor: `this.gender = Gender.UNKNOWN;`

### Step 3: Update All Persistence Layers

**JPA**: Add column mapping (handled by javax.persistence.Column annotation)

**JDBC - JdbcPetRowMapper**: Add:
```java
String genderStr = rs.getString("gender");
pet.setGender(genderStr != null ? Gender.valueOf(genderStr) : Gender.UNKNOWN);
```

**JDBC - JdbcPet**: Add gender field, getters/setters

**Spring Data JPA**: Add derived query method in repository:
```java
List<Pet> findByGender(Gender gender);
```

### Step 4: Update Database Schemas

Add to all schema files (H2, MySQL, PostgreSQL):
```sql
ALTER TABLE pets ADD gender VARCHAR(20);
```

### Step 5: Update UI

**PetController.java**: 
- Add `@ModelAttribute("genders")` method returning Gender.values()
- Add genders to model in create/update methods

**createOrUpdatePetForm.jsp**:
- Add `<form:select path="gender">` with options
- Default to UNKNOWN

### Step 6: Filtering by Gender (if implementing User Story 2)

**PetRepository** (interface):
```java
List<Pet> findByGender(Gender gender);
```

Add implementation in all three repository implementations.

### Step 7: Run Tests

```bash
./mvnw test
```

All tests must pass. Feature tests should fail before implementation (Red phase of Red-Green-Refactor).

## Key Files Modified

| File | Change |
|------|--------|
| `model/Gender.java` | NEW - Gender enum |
| `model/Pet.java` | Add gender field |
| `repository/jdbc/JdbcPet.java` | Add gender field |
| `repository/jdbc/JdbcPetRowMapper.java` | Map gender column |
| `repository/springdatajpa/SpringDataPetRepository.java` | Add findByGender |
| `web/PetController.java` | Add genders to model |
| `web/petclinic-servlet.xml` | Add genders property editor |
| `WEB-INF/jsp/pets/createOrUpdatePetForm.jsp` | Add gender dropdown |
| `db/h2/schema.sql` | Add gender column |
| `db/mysql/schema.sql` | Add gender column |
| `db/postgresql/schema.sql` | Add gender column |