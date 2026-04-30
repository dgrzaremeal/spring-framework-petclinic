# Research: Pet Gender/Sex Enum

**Feature**: `001-pet-gender-enum_Sonnet_2`  
**Date**: 2026-04-30  
**Status**: Complete — all NEEDS CLARIFICATION resolved

---

## R-001: Enum Storage Strategy in JPA

**Decision**: `@Enumerated(EnumType.STRING)` on the `Pet.gender` field, stored as `VARCHAR(20)` in the `pets` table.

**Rationale**: The spec explicitly mandates this approach (FR-001, Clarifications). `EnumType.STRING` stores the enum name (`'MALE'`, `'FEMALE'`, `'UNKNOWN'`) rather than an ordinal integer, making the column human-readable and resilient to enum reordering. `VARCHAR(20)` is sufficient for the three values and leaves room for future additions without a schema change.

**Alternatives considered**:
- `EnumType.ORDINAL`: Rejected — ordinal values break if enum order changes; not human-readable in the DB.
- Separate lookup table (like `types`): Rejected — the spec defines a fixed, exhaustive set of three values; a lookup table adds unnecessary complexity (Constitution Principle V).

---

## R-002: Default Value Strategy

**Decision**: Default `gender` to `Gender.UNKNOWN` in the `Pet` entity field initializer (`private Gender gender = Gender.UNKNOWN;`). No runtime null-coercion in application code beyond this initializer.

**Rationale**: The spec requires defaulting to UNKNOWN when no selection is made (FR-003, SC-002). Initializing at the field level ensures the default is applied regardless of how a `Pet` instance is created (form binding, programmatic construction, JDBC row mapping). The spec explicitly states no runtime null-coercion is required beyond the DB migration.

**Alternatives considered**:
- Default in `PetValidator`: Rejected — the validator should reject invalid values, not silently coerce them; defaulting in the entity is cleaner.
- `@ColumnDefault("'UNKNOWN'")` only: Rejected — this only applies at the DB level; the Java object would still be null before persistence.

---

## R-003: JSP View Binding for Enum Dropdown

**Decision**: Use `<form:select path="gender" items="${genders}"/>` inside the existing `<petclinic:selectField>` tag pattern, with `@ModelAttribute("genders")` in `PetController` returning `List<Gender>` (via `Arrays.asList(Gender.values())`).

**Rationale**: The constitution (§View Layer Conventions) explicitly mandates `List<EnumType>` return type for `@ModelAttribute` methods exposing enum values to JSP views. The existing `selectField.tag` accepts a `java.util.List` via its `names` attribute. The `<form:select>` tag will call `toString()` on each enum value for display; a custom `toString()` override on `Gender` returning the human-readable label ("Male", "Female", "Unknown") satisfies FR-005 without additional JSP logic.

**Alternatives considered**:
- `Gender[]` return from `@ModelAttribute`: Rejected — explicitly prohibited by constitution; causes `ELException` at runtime.
- Separate display label map in the model: Rejected — overriding `toString()` on the enum is simpler and keeps display logic co-located with the enum definition.
- Custom `Converter<String, Gender>`: Not needed — Spring MVC's default enum binding by name handles form submission correctly.

---

## R-004: JDBC Persistence — Gender Column Handling

**Decision**: Add `gender` to the `MapSqlParameterSource` in `JdbcPetRepositoryImpl.createPetParameterSource()` as `pet.getGender().name()` (the string name). Update the `UPDATE` SQL to include `gender=:gender`. Update `JdbcPetRowMapper` to read the `gender` column and call `Gender.valueOf(rs.getString("gender"))`.

**Rationale**: The JDBC implementation manually maps columns; it must be updated explicitly unlike JPA which handles `@Enumerated` automatically. Using `.name()` for write and `Gender.valueOf()` for read is consistent with `EnumType.STRING` semantics.

**Alternatives considered**:
- Store as ordinal in JDBC: Rejected — inconsistent with JPA implementation; breaks cross-profile data compatibility.

---

## R-005: Database Migration Strategy

**Decision**: Add `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` to the `pets` table DDL in all four schema files (H2, HSQLDB, MySQL, PostgreSQL). Update `data.sql` files to include `gender='UNKNOWN'` in all existing `INSERT` statements for seed data.

**Rationale**: The spec mandates a DB migration that sets NULL → UNKNOWN for existing rows (FR-007, Clarifications). Since the project uses schema.sql + data.sql for all databases (no Flyway/Liquibase), the migration is achieved by:
1. Adding the column with `DEFAULT 'UNKNOWN'` so existing rows get the default on schema recreation.
2. Updating `data.sql` seed inserts to include the gender column explicitly.

The `NOT NULL DEFAULT 'UNKNOWN'` constraint enforces FR-006 at the DB level as a secondary guard.

**Alternatives considered**:
- Flyway/Liquibase migration scripts: Rejected — the project does not use a migration tool; adding one would violate Constitution Principle V (minimal dependencies).
- Nullable column with application-level null-coercion: Rejected — spec explicitly states no runtime null-coercion; `NOT NULL DEFAULT 'UNKNOWN'` is cleaner.

---

## R-006: Validation of Gender Field

**Decision**: Add gender validation to `PetValidator`: reject if `pet.getGender() == null` (should not occur given field initializer, but defensive). No additional validation needed since the dropdown only presents valid enum values; invalid string submissions are rejected by Spring MVC's enum binding before reaching the validator.

**Rationale**: Spring MVC's `@InitBinder` with `PetValidator` already handles field-level validation. The enum binding itself rejects unrecognized string values with a type mismatch error before `PetValidator.validate()` is called, satisfying FR-006. The validator null-check is a defensive backstop.

**Alternatives considered**:
- Bean Validation `@NotNull` on `Pet.gender`: Could work, but the project's existing pattern (per `PetValidator` comments) is to use the programmatic `Validator` rather than Bean Validation annotations for Pet forms.

---

## R-007: Display in Owner Detail View

**Decision**: Add a `<dt>Gender</dt><dd><c:out value="${pet.gender}"/></dd>` row to the pet section in `ownerDetails.jsp`. The `Gender.toString()` override returns the human-readable label.

**Rationale**: The owner detail view (`ownerDetails.jsp`) is the pet profile view in this application (there is no separate pet detail page). Adding a `<dt>/<dd>` pair consistent with the existing Name/Birth Date/Type pattern satisfies FR-005 and US-3.

**Alternatives considered**:
- Separate pet detail page: Not applicable — no such page exists in the current application.
- JSTL `<c:choose>` for label mapping: Rejected — `toString()` override on the enum is cleaner and keeps label logic in Java.

---

## Summary of Resolved Unknowns

| # | Unknown | Resolution |
|---|---------|-----------|
| R-001 | Enum storage strategy | `@Enumerated(EnumType.STRING)`, `VARCHAR(20)` |
| R-002 | Default value strategy | Field initializer `= Gender.UNKNOWN` in `Pet.java` |
| R-003 | JSP dropdown binding | `List<Gender>` from `@ModelAttribute`, `toString()` for labels |
| R-004 | JDBC gender handling | Manual column mapping in `JdbcPetRepositoryImpl` + `JdbcPetRowMapper` |
| R-005 | DB migration | `NOT NULL DEFAULT 'UNKNOWN'` in schema DDL + updated data.sql inserts |
| R-006 | Gender validation | Defensive null-check in `PetValidator`; enum binding rejects invalid strings |
| R-007 | Display in profile view | `<dt>Gender</dt><dd>` in `ownerDetails.jsp` using `toString()` |
