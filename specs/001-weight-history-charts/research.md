# Research: Weight History & Progress Charts

## Chart Library

**Decision**: Use Chart.js via WebJar `org.webjars.npm:chart.js:4.5.0`

**Rationale**: Chart.js is the most widely used open-source charting library for the web. The `org.webjars.npm` mirror is actively maintained (mirrors npm releases). Version 4.x is the current stable major. Adding it as a WebJar satisfies Constitution Principle V (JavaScript dependencies declared as WebJars, not checked-in static files). The existing project already uses `org.webjars.npm` for Bootstrap and Flatpickr, so this follows established precedent.

**Alternatives considered**:
- `org.webjars:chartjs` — outdated (last published 2014, version `26962ce-1`). Rejected.
- D3.js — far more complex API; overkill for a simple line chart. Rejected.
- Highcharts — commercial license. Rejected.
- Server-side chart rendering (e.g., JFreeChart) — produces static images, not interactive; adds a heavier server-side dependency. Rejected.

---

## WeightRecord Entity Design

**Decision**: Model `WeightRecord` as a standalone `@Entity` extending `BaseEntity`, with a `@ManyToOne` to `Pet` — mirroring the `Visit` entity pattern exactly.

**Rationale**: `Visit` is the closest existing analogue: it is a time-stamped, per-pet record with a `@ManyToOne Pet` relationship. Reusing this pattern keeps the codebase consistent and educational. The `WeightRecord` is intentionally NOT embedded in `Pet` (no `@OneToMany` on `Pet`) to keep `Pet` unchanged and avoid cascading complexity.

**Alternatives considered**:
- Embedding weight history as a `@OneToMany` collection on `Pet` (like `visits`) — would require modifying the `Pet` entity and all three persistence implementations of `PetRepository`. Rejected in favor of a standalone repository that mirrors `VisitRepository`.
- Storing weight as a JSON column — not portable across H2/MySQL/PostgreSQL without extra dependencies. Rejected.

---

## Persistence Layer Pattern

**Decision**: Add `WeightRecordRepository` interface + three implementations: `JpaWeightRecordRepositoryImpl`, `JdbcWeightRecordRepositoryImpl`, `SpringDataWeightRecordRepository`.

**Rationale**: Constitution Principle III requires all three implementations. The `VisitRepository` pattern is the direct template: `save(WeightRecord)` and `findByPetId(Integer)` are the only two operations needed.

**Alternatives considered**:
- Implementing only JPA and Spring Data JPA — violates Constitution Principle III. Rejected.

---

## Service Layer Pattern

**Decision**: Add `saveWeightRecord(WeightRecord)` and `findWeightRecordsByPetId(int)` to `ClinicService` and implement in `ClinicServiceImpl`.

**Rationale**: All data access goes through `ClinicService` (Constitution Principle I). The existing `saveVisit` / `findVisitsByPetId` methods are the direct template.

---

## URL / Controller Pattern

**Decision**: Mount weight record endpoints under `/owners/{ownerId}/pets/{petId}/weights/` — mirroring the visit URL pattern `/owners/*/pets/{petId}/visits/`.

**Rationale**: Consistent with existing URL conventions. The pet context (owner + pet) is always present for weight records.

**Endpoints**:
- `GET  /owners/{ownerId}/pets/{petId}/weights`       — list + chart view
- `GET  /owners/{ownerId}/pets/{petId}/weights/new`   — add weight form
- `POST /owners/{ownerId}/pets/{petId}/weights/new`   — save weight record

---

## Validation

**Decision**: Use Bean Validation (`@NotNull`, `@DecimalMin`, `@DecimalMax`) on `WeightRecord` fields, plus a custom `WeightRecordValidator` for the date constraint (must not be in the future).

**Rationale**: Existing `PetValidator` demonstrates the custom validator pattern for constraints not expressible with standard Bean Validation annotations. The future-date check requires comparing against `LocalDate.now()`, which is a runtime check.

**Rules**:
- `weight`: `@NotNull`, `@DecimalMin("0.001")`, `@DecimalMax("250.0")` (kg)
- `measurementDate`: `@NotNull`; custom validator rejects future dates

---

## Chart Rendering

**Decision**: Render the chart client-side in JSP using Chart.js. Pass weight data as a JSON array embedded in the page (inline `<script>` block with server-rendered data).

**Rationale**: No AJAX endpoint needed for the initial load — the data is already fetched by the controller and available in the model. This keeps the implementation simple and avoids adding a new JSON API endpoint. Chart.js 4.x supports this pattern natively.

**Data format passed to chart**:
```javascript
const labels = ["2025-01-10", "2025-02-14", ...];  // ISO dates
const data   = [4.2, 4.5, ...];                     // kg values
```

---

## Database Schema

**Decision**: Add a `weight_records` table to all four schema files (H2, HSQLDB, MySQL, PostgreSQL).

```sql
CREATE TABLE weight_records (
    id               INTEGER IDENTITY PRIMARY KEY,  -- H2/HSQLDB syntax
    pet_id           INTEGER NOT NULL,
    weight_kg        DECIMAL(6,3) NOT NULL,
    measurement_date DATE NOT NULL,
    FOREIGN KEY (pet_id) REFERENCES pets(id)
);
```

MySQL/PostgreSQL use `INT AUTO_INCREMENT` / `SERIAL` respectively, following existing schema conventions.

---

## NEEDS CLARIFICATION — All Resolved

| Item | Resolution |
|------|-----------|
| Chart.js WebJar coordinates | `org.webjars.npm:chart.js:4.5.0` |
| Entity relationship approach | Standalone entity + `@ManyToOne Pet`, mirrors `Visit` |
| URL structure | `/owners/{ownerId}/pets/{petId}/weights/` |
| Validation approach | Bean Validation + custom `WeightRecordValidator` |
| Chart data delivery | Inline JSON in JSP, client-side Chart.js rendering |
| Schema column type for weight | `DECIMAL(6,3)` — supports values 0.001–999.999 kg (capped at 250 by validation) |
