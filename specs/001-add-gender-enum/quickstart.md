# Quick Start: add-gender-enum

## Overview
This feature adds a gender attribute to the Pet entity in the Spring Framework PetClinic application. The gender is implemented as a Java Enum with values MALE, FEMALE, and UNKNOWN.

## Implementation Steps

### 1. Create Gender Enum
Create a new `Gender.java` enum in `src/main/java/org/springframework/samples/petclinic/model/`:

```java
package org.springframework.samples.petclinic.model;

public enum Gender {
    MALE,
    FEMALE,
    UNKNOWN
}
```

### 2. Update Pet Entity
Modify `Pet.java` to include the gender attribute:

```java
// Add import if needed
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

// In Pet class, add the attribute:
@Enumerated(EnumType.STRING)
@Column(name = "gender")
private Gender gender;

// Add getter and setter:
public Gender getGender() {
    return this.gender;
}

public void setGender(Gender gender) {
    if (gender == null) {
        throw new IllegalArgumentException("Gender cannot be null");
    }
    this.gender = gender;
}
```

### 3. Update Database Schema
Add a GENDER column to the PETS table:
- Column name: GENDER
- Type: VARCHAR(10)
- Constraints: NOT NULL

### 4. Update Constructors
Ensure all Pet constructors initialize the gender attribute appropriately.

### 5. Update Tests
- Add unit tests for Pet gender attribute getter/setter
- Add validation tests for null and invalid values
- Update existing tests that create Pet instances to include gender
- Add integration tests for repository persistence

## Running the Application

The application can be run using Maven:
```bash
./mvnw spring-boot:run
```

## Testing

Run tests with:
```bash
./mvnw test
```

## Verification

To verify the implementation:
1. Create a Pet object with each gender value (MALE, FEMALE, UNKNOWN)
2. Verify the gender attribute is correctly stored and retrieved
3. Attempt to set null or invalid gender values and confirm they are rejected
4. Check that Pet objects are correctly persisted to and retrieved from the database with gender values

## Dependencies
No new dependencies are required. This feature uses existing Spring Framework and Java capabilities.