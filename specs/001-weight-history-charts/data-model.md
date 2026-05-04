# Data Model: Weight History & Progress Charts

## Entities

### WeightRecord (NEW)

Represents a single weight measurement for a pet at a specific point in time. Records are append-only (no edit or delete in this version).

| Field | Java Type | Column | Constraints | Notes |
|-------|-----------|--------|-------------|-------|
| `id` | `Integer` | `id` (PK, IDENTITY) | Inherited from `BaseEntity` | Auto-generated |
| `pet` | `Pet` | `pet_id` (FK → `pets.id`) | `@NotNull`, `@ManyToOne` | Owning side of relationship |
| `weightKg` | `BigDecimal` | `weight_kg` | `@NotNull`, `@DecimalMin("0.001")`, `@DecimalMax("250.0")` | Stored as `DECIMAL(6,3)` |
| `measurementDate` | `LocalDate` | `measurement_date` | `@NotNull`; must not be in the future (custom validator) | `@DateTimeFormat(pattern="yyyy/MM/dd")` |

**Class hierarchy**: `WeightRecord extends BaseEntity`

**Table name**: `weight_records`

---

### Pet (EXISTING — unchanged)

No fields added to `Pet`. The relationship is navigated via `WeightRecordRepository.findByPetId(Integer petId)`, not via a collection on `Pet`. This avoids modifying the existing entity and all three `PetRepository` implementations.

---

## Relationships

```
Pet (1) ──────────────── (0..*) WeightRecord
         pet_id FK
```

- A `Pet` may have zero or more `WeightRecord` entries.
- Each `WeightRecord` belongs to exactly one `Pet`.
- The relationship is unidirectional: `WeightRecord` holds the `@ManyToOne Pet` reference; `Pet` has no `@OneToMany` collection for weight records.

---

## Validation Rules

| Rule | Enforcement |
|------|-------------|
| `weightKg` must be > 0 and ≤ 250 kg | Bean Validation: `@DecimalMin("0.001")` + `@DecimalMax("250.0")` on `WeightRecord.weightKg` |
| `measurementDate` must not be null | Bean Validation: `@NotNull` |
| `measurementDate` must not be in the future | Custom `WeightRecordValidator` (Spring `Validator`): rejects if `date.isAfter(LocalDate.now())` |
| `pet` must not be null | Bean Validation: `@NotNull` (set by controller before validation) |

---

## State Transitions

`WeightRecord` is immutable after creation (append-only). No state machine applies.

```
[form submitted] → [validated] → [saved] → [immutable record]
                        ↓ (validation failure)
                   [form re-displayed with errors]
```

---

## Database Schema

### H2 / HSQLDB

```sql
CREATE TABLE weight_records (
    id               INTEGER IDENTITY PRIMARY KEY,
    pet_id           INTEGER NOT NULL,
    weight_kg        DECIMAL(6,3) NOT NULL,
    measurement_date DATE NOT NULL,
    CONSTRAINT fk_weight_records_pet FOREIGN KEY (pet_id) REFERENCES pets(id)
);
```

### MySQL

```sql
CREATE TABLE weight_records (
    id               INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    pet_id           INT(4) UNSIGNED NOT NULL,
    weight_kg        DECIMAL(6,3) NOT NULL,
    measurement_date DATE NOT NULL,
    CONSTRAINT fk_weight_records_pet FOREIGN KEY (pet_id) REFERENCES pets(id)
) ENGINE=InnoDB;
```

### PostgreSQL

```sql
CREATE TABLE weight_records (
    id               SERIAL PRIMARY KEY,
    pet_id           INTEGER NOT NULL,
    weight_kg        DECIMAL(6,3) NOT NULL,
    measurement_date DATE NOT NULL,
    CONSTRAINT fk_weight_records_pet FOREIGN KEY (pet_id) REFERENCES pets(id)
);
```

---

## Repository Interface

```java
package org.springframework.samples.petclinic.repository;

import org.springframework.samples.petclinic.model.WeightRecord;
import java.util.List;

public interface WeightRecordRepository {
    /** Returns all weight records for the given pet, ordered by measurement_date ASC. */
    List<WeightRecord> findByPetId(Integer petId);

    /** Persists a new weight record. */
    void save(WeightRecord weightRecord);
}
```

---

## Service Interface Additions

New methods added to `ClinicService`:

```java
/** Returns all weight records for the given pet, ordered by date ascending. */
List<WeightRecord> findWeightRecordsByPetId(int petId);

/** Saves a new weight record. */
void saveWeightRecord(WeightRecord weightRecord);
```

---

## JDBC Column Mapping

| Java Field | SQL Column | JDBC `get*` method |
|------------|------------|--------------------|
| `id` | `id` | `getInt("id")` |
| `pet.id` | `pet_id` | `getInt("pet_id")` |
| `weightKg` | `weight_kg` | `getBigDecimal("weight_kg")` |
| `measurementDate` | `measurement_date` | `getObject("measurement_date", LocalDate.class)` |
