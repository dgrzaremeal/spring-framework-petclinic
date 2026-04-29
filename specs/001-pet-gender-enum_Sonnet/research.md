# Research: Pet Gender/Sex Enum

**Feature**: `001-pet-gender-enum_Sonnet`  
**Date**: 2026-04-29  
**Status**: Complete — all NEEDS CLARIFICATION resolved

---

## 1. Database Migration Strategy

**Decision**: Update the four existing raw SQL `schema.sql` files (H2, HSQLDB, MySQL, PostgreSQL) directly — no Flyway.

**Rationale**: This project does **not** use Flyway. Database initialization is handled by Spring's `<jdbc:initialize-database>` element in `datasource-config.xml`, which loads `schema.sql` and `data.sql` from the classpath path matching the active Maven profile. The schema scripts are drop-and-recreate (they `DROP TABLE … IF EXISTS` at the top), so adding a column is simply adding it to the `CREATE TABLE pets` statement. The `data.sql` files insert seed data and must also be updated to include a `gender` value for each existing pet row.

**Alternatives considered**:
- Flyway versioned migration: Not applicable — Flyway is not a project dependency and adding it would violate Constitution Principle V (minimal dependencies).
- Liquibase: Same objection.
- ALTER TABLE migration script: Not applicable for the drop-and-recreate schema approach used here.

---

## 2. Enum Storage Strategy

**Decision**: Store `gender` as `VARCHAR(20)` in the database. In the JPA/Spring Data JPA implementations, annotate the field with `@Enumerated(EnumType.STRING)`. In the JDBC implementation, read/write the string value explicitly.

**Rationale**: The spec (FR-008, clarification session) explicitly requires VARCHAR with application-level enum validation. `EnumType.STRING` stores the enum name (e.g., `"MALE"`) rather than the ordinal, making the data human-readable and resilient to enum reordering. The JDBC layer already maps columns manually via `MapSqlParameterSource` and `BeanPropertyRowMapper`/`JdbcPetRowMapper`, so explicit string handling is straightforward.

**Alternatives considered**:
- `EnumType.ORDINAL`: Rejected — fragile if enum values are reordered; not human-readable in the DB.
- Separate `gender` lookup table (like `PetType`): Rejected — gender values are fixed and not user-configurable (spec assumption); a lookup table adds unnecessary complexity.
- CHECK constraint in DB: Optional enhancement; not required by spec. Can be added to MySQL/PostgreSQL schemas for defense-in-depth but is not the primary validation mechanism.

---

## 3. Default Value Handling

**Decision**: Set `UNKNOWN` as the Java field default in `Pet.java` (`private Gender gender = Gender.UNKNOWN`). Set `DEFAULT 'UNKNOWN'` on the column in all schema scripts. The `data.sql` seed rows will explicitly set `gender = 'UNKNOWN'` for all existing pets.

**Rationale**: FR-006 requires defaulting to UNKNOWN when no gender is provided. FR-007 requires legacy records to be treated as UNKNOWN. Since the schema is drop-and-recreate, all seed data rows are re-inserted on startup — setting them explicitly to `'UNKNOWN'` satisfies FR-007 without a separate migration step. The Java-side default ensures that programmatically created `Pet` instances without an explicit gender also default to UNKNOWN.

**Alternatives considered**:
- Nullable column with null-to-UNKNOWN mapping in service layer: Rejected — the spec explicitly states no null/invalid states should exist (SC-002). A non-null column with a default is cleaner.

---

## 4. UI Component for Gender Selection

**Decision**: Use the existing `<petclinic:selectField>` JSP tag in `createOrUpdatePetForm.jsp`, passing a `List<Gender>` (or `List<String>` of display labels) as the `names` attribute.

**Rationale**: The spec (FR-009) requires an HTML `<select>` dropdown consistent with the existing `type` field. The project already has a `selectField.tag` custom tag that wraps `<form:select>` with Bootstrap styling and error display. The `type` field uses `<petclinic:selectField name="type" label="Type " names="${types}" size="5"/>`. Gender will follow the same pattern.

**Implementation detail**: The `selectField.tag` uses `<form:select … items="${names}"/>`. For `PetType`, `items` is a `Collection<PetType>` and Spring MVC uses `PetTypeFormatter` to convert. For `Gender`, since it is a Java enum, Spring MVC has built-in enum conversion — `<form:select path="gender" items="${genders}"/>` will work directly with `Gender[]` or `List<Gender>`. Display labels will use the enum's `toString()` override (e.g., `"Male"`, `"Female"`, `"Unknown"`).

**Alternatives considered**:
- Hardcoded `<option>` tags in JSP: Rejected — inconsistent with the project's use of the `selectField` tag and harder to maintain.
- New custom tag: Rejected — unnecessary; existing tag handles this case.

---

## 5. REST API Exposure

**Decision**: The `gender` field will be serialized automatically by Jackson when `Pet` objects are returned in JSON responses. No dedicated pet REST endpoint exists today; the `/vets.json` pattern uses `@ResponseBody` on `VetController`. If a pet JSON endpoint is added in future, `gender` will appear automatically. For this feature, the `gender` field on `Pet` is sufficient — Jackson will include it in any `@ResponseBody` response involving `Pet`.

**Rationale**: FR-010 requires gender to appear in REST API responses for pet resources as a non-breaking additive field. The project currently exposes `/vets.json` and `/vets.xml` but no `/pets.json` endpoint. Adding `gender` to the `Pet` model is sufficient to satisfy the requirement for any future or existing JSON serialization of `Pet`.

**Alternatives considered**:
- Adding a `/pets/{id}.json` endpoint: Out of scope for this feature per the spec.
- Custom Jackson serializer for Gender: Not needed — Jackson serializes enums as their name string by default, which matches the VARCHAR storage strategy.

---

## 6. Validation Strategy

**Decision**: No changes to `PetValidator` are required. Gender defaults to UNKNOWN and is always valid (the enum type itself prevents invalid values at the Java level). Spring MVC's data binding will reject any unrecognized string value before it reaches the validator.

**Rationale**: FR-002 requires rejection of invalid gender values. Since `gender` is typed as `Gender` (a Java enum), Spring MVC's `ConversionService` will throw a `BindException` if an unrecognized value is submitted — this is handled automatically before `PetValidator.validate()` is called. No explicit validator logic is needed for the gender field.

**Alternatives considered**:
- Adding `if (pet.getGender() == null) errors.rejectValue(...)` in `PetValidator`: Not needed because the field defaults to UNKNOWN and the enum type prevents null from a form submission.

---

## 7. JDBC Layer Mapping

**Decision**: Update `JdbcPetRepositoryImpl` to include `gender` in the `INSERT`/`UPDATE` SQL and in `createPetParameterSource`. Update `JdbcPetRowMapper` (or `JdbcPetVisitExtractor`) to map the `gender` column from `ResultSet` to `Gender.valueOf(rs.getString("gender"))`.

**Rationale**: The JDBC implementation manually constructs SQL and maps result sets. The `createPetParameterSource` method currently maps `name`, `birth_date`, `type_id`, `owner_id`. It must also map `gender` as `pet.getGender().name()`. The row mapper must read the `gender` column and call `Gender.valueOf()` to reconstruct the enum.

**Alternatives considered**:
- `BeanPropertyRowMapper` for gender: Would require the column name to match the field name exactly and the field to be a `String`. Since the field is a `Gender` enum, a custom mapping is needed.

---

## 8. All NEEDS CLARIFICATION Items — Resolved

| Item | Resolution |
|------|-----------|
| Database migration mechanism | Raw SQL schema scripts (no Flyway) — update all 4 dialect files |
| Enum storage format | VARCHAR + `@Enumerated(EnumType.STRING)` |
| Default value | `Gender.UNKNOWN` in Java; `DEFAULT 'UNKNOWN'` in SQL |
| UI component | Existing `<petclinic:selectField>` tag |
| REST API | Automatic via Jackson on `Pet` model field |
| Validation | No new validator logic needed; enum type + Spring MVC binding handles it |
| JDBC mapping | Manual in `createPetParameterSource` + row mapper |
| Legacy data | Seed data in `data.sql` explicitly sets `'UNKNOWN'` for all existing pets |
