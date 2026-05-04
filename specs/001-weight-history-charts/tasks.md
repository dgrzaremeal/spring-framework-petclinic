# Tasks: Weight History & Progress Charts

**Input**: Design documents from `/specs/001-weight-history-charts/`
**Prerequisites**: plan.md ✓, spec.md ✓, research.md ✓, data-model.md ✓, contracts/ui-contracts.md ✓, quickstart.md ✓

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3)
- Exact file paths are included in all task descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Add the Chart.js WebJar dependency and database schema changes that are prerequisites for all user stories.

- [x] T001 Add Chart.js WebJar dependency (`org.webjars.npm:chart.js:4.5.0`) to `pom.xml`
- [x] T002 [P] Add `weight_records` table to `src/main/resources/db/h2/schema.sql`
- [x] T003 [P] Add `weight_records` table to `src/main/resources/db/hsqldb/schema.sql`
- [x] T004 [P] Add `weight_records` table to `src/main/resources/db/mysql/schema.sql`
- [x] T005 [P] Add `weight_records` table to `src/main/resources/db/postgres/schema.sql`

**Checkpoint**: WebJar declared and all four database schemas updated — foundational layer ready.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core domain model and repository interface that ALL user stories depend on. Must be complete before any story implementation begins.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

- [x] T006 Create `WeightRecord` entity extending `BaseEntity` with fields: `pet` (`@ManyToOne`, `@NotNull`), `weightKg` (`BigDecimal`, `@NotNull`, `@DecimalMin("0.001")`, `@DecimalMax("250.0")`), `measurementDate` (`LocalDate`, `@NotNull`, `@DateTimeFormat(pattern="yyyy/MM/dd")`); annotate class with `@Entity`, `@Table(name="weight_records")` in `src/main/java/org/springframework/samples/petclinic/model/WeightRecord.java`
- [x] T007 Create `WeightRecordRepository` interface with `findByPetId(Integer petId)` and `save(WeightRecord)` methods in `src/main/java/org/springframework/samples/petclinic/repository/WeightRecordRepository.java`
- [x] T008 [P] Implement `JpaWeightRecordRepositoryImpl` using `EntityManager` (mirrors `JpaVisitRepositoryImpl`); query MUST order results by `measurementDate ASC` to satisfy FR-004 in `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaWeightRecordRepositoryImpl.java`
- [x] T009 [P] Implement `JdbcWeightRecordRepositoryImpl` using `NamedParameterJdbcTemplate` with `RowMapper` for `WeightRecord`; SQL query MUST include `ORDER BY measurement_date ASC` to satisfy FR-004 (mirrors `JdbcVisitRepositoryImpl`) in `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcWeightRecordRepositoryImpl.java`
- [x] T010 [P] Implement `SpringDataWeightRecordRepository` as a Spring Data JPA interface extending `Repository<WeightRecord, Integer>` with `findByPetIdOrderByMeasurementDateAsc` in `src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataWeightRecordRepository.java`
- [x] T011 Add `findWeightRecordsByPetId(int petId)` and `saveWeightRecord(WeightRecord)` method signatures to `src/main/java/org/springframework/samples/petclinic/service/ClinicService.java`
- [x] T012 Implement `findWeightRecordsByPetId` and `saveWeightRecord` in `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java` (inject `WeightRecordRepository`, delegate calls)

**Checkpoint**: Foundation ready — `WeightRecord` entity, all three repository implementations, and service layer wired. User story implementation can now begin.

---

## Phase 3: User Story 1 — Record Pet Weight (Priority: P1) 🎯 MVP

**Goal**: Allow clinic staff and pet owners to log a new weight measurement for a pet via a form, with full validation (positive value ≤ 250 kg, date not in the future).

**Independent Test**: Navigate to a pet's profile → click "Add Weight Record" → enter weight `4.5` and today's date → submit → verify the record appears in the weight history list. Also verify that submitting a negative value or a future date shows a validation error and does not save.

### Implementation for User Story 1

- [x] T013 [US1] Create `WeightRecordValidator` (Spring `Validator`) that rejects `measurementDate` values after `LocalDate.now()` in `src/main/java/org/springframework/samples/petclinic/web/WeightRecordValidator.java`
- [x] T014 [US1] Create `WeightRecordController` with `@InitBinder` (registers `WeightRecordValidator`), `GET /owners/{ownerId}/pets/{petId}/weights/new` (show form), and `POST /owners/{ownerId}/pets/{petId}/weights/new` (save record, redirect to list on success, re-display form on validation failure); implement **only** these two endpoints — no edit or delete routes (append-only per spec Assumption) in `src/main/java/org/springframework/samples/petclinic/web/WeightRecordController.java`
- [x] T015 [US1] Create `createWeightRecordForm.jsp` with a form bound to `weightRecord`, fields for `weightKg` and `measurementDate` (format `yyyy/MM/dd`), and `<form:errors>` display for both fields in `src/main/webapp/WEB-INF/jsp/pets/createWeightRecordForm.jsp`

**Checkpoint**: User Story 1 fully functional — weight records can be created with validation enforced.

---

## Phase 4: User Story 2 — View Weight History List (Priority: P2)

**Goal**: Display all weight records for a pet in chronological order on the pet's profile, with a friendly message when no records exist.

**Independent Test**: Navigate to a pet's profile that has existing weight records → verify all entries appear in date order with measurement date and weight value shown. Navigate to a pet with no records → verify the "No weight history recorded yet." message is displayed.

### Implementation for User Story 2

- [x] T016 [US2] Add `GET /owners/{ownerId}/pets/{petId}/weights` handler to `WeightRecordController` that loads the pet and its weight records (via `ClinicService.findWeightRecordsByPetId`) and puts `pet` and `weightRecords` into the model in `src/main/java/org/springframework/samples/petclinic/web/WeightRecordController.java`
- [x] T017 [US2] Create `weightRecordList.jsp` displaying a table of all weight records (columns: measurement date, weight in kg) ordered by date, with a "No weight history recorded yet." message when the list is empty, and an "Add Weight Record" link to the new-record form in `src/main/webapp/WEB-INF/jsp/pets/weightRecordList.jsp`
- [x] T018 [US2] Add a "Weight History" link on the pet detail view to navigate to `GET /owners/{ownerId}/pets/{petId}/weights` in `src/main/webapp/WEB-INF/jsp/pets/petDetails.jsp` (or equivalent pet profile JSP)

**Checkpoint**: User Story 2 fully functional — weight history list is visible from the pet's profile.

---

## Phase 5: User Story 3 — View Weight Progress Chart (Priority: P3)

**Goal**: Render a Chart.js line chart on the weight history page when two or more weight records exist, showing weight (kg) on the Y axis and measurement date on the X axis.

**Independent Test**: Navigate to a pet's profile with two or more weight records → verify a line chart is rendered above the history table with all data points plotted. Navigate to a pet with one record → verify a single data point is shown with a note "Add more records to see a trend". Navigate to a pet with 50+ records → verify the chart remains readable.

### Implementation for User Story 3

- [x] T019 [US3] Add Chart.js `<script>` tag (referencing the WebJar at `/webjars/chart.js/4.5.0/dist/chart.umd.js`) and inline JavaScript to `weightRecordList.jsp` that renders a `type: 'line'` Chart.js chart using JSTL/EL to embed `measurementDate` labels and `weightKg` data arrays; show chart when `weightRecords.size() >= 2`, show single-point message when `weightRecords.size() == 1` in `src/main/webapp/WEB-INF/jsp/pets/weightRecordList.jsp`

**Checkpoint**: All three user stories fully functional — weight recording, history list, and progress chart all work end-to-end.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Integration verification, edge-case hardening, and quickstart validation.

- [x] T020 [P] Verify `WeightRecordController` validates pet ownership (owner ID in path matches pet's owner) and returns 404 for mismatches in `src/main/java/org/springframework/samples/petclinic/web/WeightRecordController.java`
- [x] T021 [P] Add `WeightRecord` to JPA `persistence.xml` (or equivalent ORM config) so the entity is recognized by the JPA persistence unit
- [x] T027 Write unit tests for `saveWeightRecord` and `findWeightRecordsByPetId` in `ClinicServiceImpl` (mock `WeightRecordRepository`; assert delegation and ordering) in `src/test/java/org/springframework/samples/petclinic/service/ClinicServiceTests.java`
- [x] T028 Write controller tests for `WeightRecordController` (GET list, GET new form, POST valid record → redirect, POST invalid record → form re-displayed with errors) in `src/test/java/org/springframework/samples/petclinic/web/WeightRecordControllerTests.java`
- [x] T029 [P] Write integration test for `JpaWeightRecordRepositoryImpl` — save and retrieve records by pet ID, assert chronological order, against H2 in `src/test/java/org/springframework/samples/petclinic/repository/JpaWeightRecordRepositoryTests.java`
- [x] T030 [P] Write integration test for `JdbcWeightRecordRepositoryImpl` — save and retrieve records by pet ID, assert chronological order, against H2 in `src/test/java/org/springframework/samples/petclinic/repository/JdbcWeightRecordRepositoryTests.java`
- [x] T031 [P] Write integration test for `SpringDataWeightRecordRepository` — save and retrieve records by pet ID, assert chronological order, against H2 in `src/test/java/org/springframework/samples/petclinic/repository/SpringDataWeightRecordRepositoryTests.java`
- [x] T022 Run `./mvnw test` (default H2 profile); ensure all tests pass — do not skip or suppress failing tests
- [x] T023 Run `./mvnw test -P jpa`; ensure all tests pass — do not skip or suppress failing tests
- [x] T024 Run `./mvnw test -P jdbc`; ensure all tests pass — do not skip or suppress failing tests
- [x] T025 Run `./mvnw test -P spring-data-jpa`; ensure all tests pass — do not skip or suppress failing tests
- [x] T026 Run `./mvnw jetty:run-war` and execute the quickstart.md verification steps end-to-end
- [x] T032 Manually verify SC-003: with 50+ weight records for a single pet, load the weight history page and confirm the Chart.js chart is visible within 2 seconds; record the result in a comment or PR description

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — start immediately; T002–T005 are parallel
- **Foundational (Phase 2)**: Depends on Phase 1 completion — **BLOCKS all user stories**
  - T006 (entity) must complete before T007 (repository interface)
  - T007 must complete before T008, T009, T010 (implementations — parallel)
  - T011 (service interface) must complete before T012 (service impl)
  - T008/T009/T010 must complete before T012
- **User Story 1 (Phase 3)**: Depends on Phase 2 — T013 before T014; T014 before T015
- **User Story 2 (Phase 4)**: Depends on Phase 2 — T016 before T017; T017 before T018
- **User Story 3 (Phase 5)**: Depends on Phase 4 (list view must exist before chart is added to it)
- **Polish (Phase 6)**: Depends on all user story phases complete

### User Story Dependencies

- **US1 (P1)**: Depends only on Foundational phase — independently testable
- **US2 (P2)**: Depends only on Foundational phase — independently testable (list view does not require the form to exist)
- **US3 (P3)**: Depends on US2 (chart is added to the list JSP) — not independently testable without the list view

### Parallel Opportunities

- T002–T005 (schema files): all parallel
- T008, T009, T010 (repository implementations): all parallel
- T013, T016 (validator and list handler): parallel (different concerns, same controller file — coordinate if pair-programming)
- T020, T021 (polish tasks): parallel
- T029, T030, T031 (repository integration tests): all parallel
- T022–T025 (Maven test runs): **sequential** — concurrent Maven executions against the same `target/` directory can corrupt the build

---

## Parallel Example: Foundational Phase

```
# After T006 (entity) and T007 (interface) complete, launch in parallel:
Task T008: JpaWeightRecordRepositoryImpl
Task T009: JdbcWeightRecordRepositoryImpl
Task T010: SpringDataWeightRecordRepository
```

## Parallel Example: User Stories 1 & 2

```
# After Phase 2 (Foundational) completes, US1 and US2 can proceed in parallel:
Developer A → Phase 3 (US1): T013 → T014 → T015
Developer B → Phase 4 (US2): T016 → T017 → T018
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup (schema + WebJar)
2. Complete Phase 2: Foundational (entity, repositories, service) — **CRITICAL**
3. Complete Phase 3: User Story 1 (form + validation + controller)
4. **STOP and VALIDATE**: Add a weight record via the form; confirm it saves and appears in the list redirect
5. Demo / merge if ready

### Incremental Delivery

1. Phase 1 + Phase 2 → Foundation ready
2. Phase 3 (US1) → Weight recording works → **MVP**
3. Phase 4 (US2) → History list visible from pet profile
4. Phase 5 (US3) → Chart renders for 2+ records
5. Phase 6 → Polish and full test-suite green

### Parallel Team Strategy

With two developers after Phase 2 completes:
- Developer A: Phase 3 (US1 — form and validation)
- Developer B: Phase 4 (US2 — list view)
- Both merge → Developer A or B: Phase 5 (US3 — chart, builds on US2 list JSP)

---

## Notes

- [P] tasks operate on different files and have no inter-task dependencies within the same phase
- [Story] labels map each task to its user story for traceability
- US3 depends on US2's `weightRecordList.jsp` — do not start T019 until T017 is complete
- All four schema files (T002–T005) must be updated; the app switches between them via Maven profiles
- The Chart.js WebJar (T001) must be in `pom.xml` before T019 can reference it in the JSP
- Run `./mvnw test` after each phase to catch regressions early
- Refer to `quickstart.md` for manual end-to-end verification steps

