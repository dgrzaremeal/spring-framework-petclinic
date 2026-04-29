# Quickstart: Add Gender Enum to Pet

## Prerequisites

- Java 17+
- Maven 3.8+
- Docker (for production build)

## Development Setup

```bash
# Clone and navigate to project
cd spring-framework-petclinic

# Build and run tests
./mvnw clean test
```

## Feature Implementation Steps

### 1. Create Gender Enum

Create `src/main/java/org/springframework/samples/petclinic/model/Gender.java`:

```java
public enum Gender {
    MALE,
    FEMALE,
    UNKNOWN
}
```

### 2. Update Pet Entity

Add gender field to `Pet.java`:

```java
@Enumerated(EnumType.STRING)
@Column(name = "gender")
private Gender gender = Gender.UNKNOWN;
```

### 3. Update Database Schemas

Add gender column to each schema:
- `src/main/resources/db/h2/schema.sql`
- `src/main/resources/db/mysql/schema.sql`
- `src/main/resources/db/postgresql/schema.sql`

```sql
ALTER TABLE pets ADD COLUMN gender VARCHAR(20) DEFAULT 'UNKNOWN';
```

### 4. Update JSP Views

Add gender dropdown to pet form. See existing pet type dropdown for pattern.

### 5. Run Tests

```bash
./mvnw test
```

## Testing

```bash
# Unit tests
./mvnw test -Dtest=PetTests

# Integration tests  
./mvnw verify
```

## Profiles

```bash
# H2 (default)
./mvnw test

# MySQL
./mvnw test -P MySQL

# PostgreSQL
./mvnw test -P PostgreSQL
```

## Verification

1. Create a new pet with gender Male → Verify stored as MALE
2. Create pet without gender → Verify defaults to UNKNOWN
3. View pet details → Verify gender displays correctly
4. Edit pet gender → Verify change persisted