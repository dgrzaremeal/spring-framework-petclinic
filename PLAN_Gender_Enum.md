# Plan: Add Gender Enum to Pet

## Status: COMPLETED

## Overview
Add a Gender/Sex enum field (MALE, FEMALE, UNKNOWN) to the Pet entity in the PetClinic application.

## Files Created

### 1. `src/main/java/org/springframework/samples/petclinic/model/Gender.java`
New enum with values: MALE, FEMALE, UNKNOWN

### 2. `src/main/java/org/springframework/samples/petclinic/web/GenderFormatter.java`
Formatter for Spring MVC binding

## Files Modified

### 3. `src/main/java/org/springframework/samples/petclinic/model/Pet.java`
- Added `gender` field with `@Enumerated` annotation
- Added getter `getGender()` and setter `setGender(Gender gender)`

### 4. Database Schemas (4 files)
Added `gender` column to `pets` table:
- `src/main/resources/db/h2/schema.sql`
- `src/main/resources/db/mysql/schema.sql`
- `src/main/resources/db/postgresql/schema.sql`
- `src/main/resources/db/hsqldb/schema.sql`

### 5. Database Data Files (4 files)
Updated INSERT statements to include gender column:
- `src/main/resources/db/h2/data.sql`
- `src/main/resources/db/mysql/data.sql`
- `src/main/resources/db/postgresql/data.sql`
- `src/main/resources/db/hsqldb/data.sql`

### 6. `src/main/java/org/springframework/samples/petclinic/web/PetController.java`
- Added `@ModelAttribute("genders")` method to populate enum values for form dropdown

### 7. `src/main/resources/spring/mvc-core-config.xml`
- Registered `GenderFormatter`

### 8. `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`
- Added select dropdown for gender field

### 9. `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`
- Added gender display in pet details

### 10. JDBC Repository Files
- `JdbcPet.java` - Added gender field and getter/setter
- `JdbcPetRowMapper.java` - Added gender column mapping
- `JdbcOwnerRepositoryImpl.java` - Updated SQL to include gender
- `JdbcVisitRepositoryImpl.java` - Updated SQL to include gender

### 11. `src/test/java/org/springframework/samples/petclinic/model/PetTests.java`
- Added tests for gender field

## Test Results
All 77 tests pass:
- 4 model tests
- 33 service tests (JDBC, JPA, Spring Data JPA)
- 28 web tests