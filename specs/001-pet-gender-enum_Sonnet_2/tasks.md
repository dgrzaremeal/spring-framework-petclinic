# Tasks: Pet Gender/Sex Enum

**Input**: Design documents from `/specs/001-pet-gender-enum_Sonnet_2/`
**Prerequisites**: plan.md ✅, spec.md ✅, research.md ✅, data-model.md ✅, contracts/ ✅, quickstart.md ✅

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3)
- Exact file paths are included in all task descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Create the `Gender` enum — the single foundational type that all three user stories depend on.

- [ ] T001 Create `Gender` enum with MALE, FEMALE, UNKNOWN constants and `toString()` override in `src/main/java/org/springframework/samples/petclinic/model/Gender.java`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Add the `gender` field to the `Pet` entity and update all four database schemas. These changes are required before any user story can be implemented or tested.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

- [ ] T002 Add `gender` field (`@Enumerated(EnumType.STRING)`, default `Gender.UNKNOWN`), getter, and setter to `src/main/java/org/springframework/samples/petclinic/model/Pet.java`
- [ ] T003 [P] Add `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` column to `pets` table in `src/main/resources/db/h2/schema.sql` and update all `INSERT INTO pets` rows in `src/main/resources/db/h2/data.sql` to include `gender = 'UNKNOWN'`
- [ ] T004 [P] Add `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` column to `pets` table in `src/main/resources/db/hsqldb/schema.sql` and update all `INSERT INTO pets` rows in `src/main/resources/db/hsqldb/data.sql` to include `gender = 'UNKNOWN'`
- [ ] T005 [P] Add `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` column to `pets` table in `src/main/resources/db/mysql/schema.sql` and update all `INSERT INTO pets` rows in `src/main/resources/db/mysql/data.sql` to include `gender = 'UNKNOWN'`
- [ ] T006 [P] Add `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` column to `pets` table in `src/main/resources/db/postgresql/schema.sql` and update all `INSERT INTO pets` rows in `src/main/resources/db/postgresql/data.sql` to include `gender = 'UNKNOWN'`

**Checkpoint**: Foundation ready — `Gender` enum exists, `Pet.gender` field is mapped, all four DB schemas include the column. User story implementation can now begin.

---

## Phase 3: User Story 1 — Record Pet Gender When Adding a Pet (Priority: P1) 🎯 MVP

**Goal**: Staff can select a gender (MALE, FEMALE, UNKNOWN) when registering a new pet; the value is persisted and defaults to UNKNOWN when omitted.

**Independent Test**: Add a new pet via `POST /owners/{ownerId}/pets/new` with each gender value (MALE, FEMALE, UNKNOWN) and with no gender value; verify the saved record reflects the chosen value (or UNKNOWN by default). Run `./mvnw test` and confirm `PetControllerTests` and `AbstractClinicServiceTests` pass.

### Implementation for User Story 1

- [ ] T007 [US1] Add `@ModelAttribute("genders")` method returning `List<Gender>` (via `Arrays.asList(Gender.values())`) to `src/main/java/org/springframework/samples/petclinic/web/PetController.java`
- [ ] T008 [US1] Add defensive null-check for `pet.getGender()` in `validate()` method of `src/main/java/org/springframework/samples/petclinic/web/PetValidator.java`
- [ ] T009 [US1] Add `gender=:gender` to the `UPDATE` SQL and add `.addValue("gender", pet.getGender().name())` to `createPetParameterSource()` in `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java`
- [ ] T010 [US1] Add `pet.setGender(Gender.valueOf(rs.getString("gender")))` mapping in `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRowMapper.java`
- [ ] T011 [US1] Add gender `<select>` dropdown using `<petclinic:selectField name="gender" label="Gender " names="${genders}" size="3"/>` after the type field in `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`

**Checkpoint**: User Story 1 is fully functional. Staff can add a new pet with a gender value; the value is persisted across all three persistence profiles (JPA, JDBC, Spring Data JPA).

---

## Phase 4: User Story 2 — Update Pet Gender on Existing Pet Record (Priority: P2)

**Goal**: Staff can edit an existing pet's gender; the current value is pre-selected in the form and the updated value is persisted after saving.

**Independent Test**: Edit an existing pet via `POST /owners/{ownerId}/pets/{petId}/edit`, change the gender, save, and verify the updated value is displayed and persisted. Verify the edit form pre-selects the current gender value. Run `./mvnw test`.

### Implementation for User Story 2

- [ ] T012 [US2] Verify that `PetController.initUpdateForm` populates the model with the existing `Pet` (including `gender`) and that the `genders` model attribute (added in T007) is available on the edit form — no new code required if T007 is complete; confirm by reviewing `src/main/java/org/springframework/samples/petclinic/web/PetController.java`
- [ ] T013 [US2] Verify that `createOrUpdatePetForm.jsp` gender dropdown (added in T011) correctly pre-selects the current `pet.gender` value via Spring MVC form binding — no new code required if T011 is complete; confirm by reviewing `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`

**Checkpoint**: User Story 2 is fully functional. The edit form pre-selects the current gender and persists updates correctly.

---

## Phase 5: User Story 3 — View Pet Gender in Pet Profile (Priority: P3)

**Goal**: Staff and owners can see the pet's gender displayed as a human-readable label ("Male", "Female", "Unknown") on the owner detail/pet profile view.

**Independent Test**: View the owner detail page for a pet with each gender value (MALE, FEMALE, UNKNOWN) and confirm the labels "Male", "Female", "Unknown" are displayed correctly. Run `./mvnw test`.

### Implementation for User Story 3

- [ ] T014 [US3] Add `<dt>Gender</dt><dd><c:out value="${pet.gender}"/></dd>` after the Type row in the pet `<dl>` section of `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

**Checkpoint**: All three user stories are independently functional. Gender is recorded, editable, and displayed with human-readable labels.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Test coverage, build validation, and smoke testing across all persistence profiles.

- [ ] T015 [P] Add gender default/assignment unit tests to `src/test/java/org/springframework/samples/petclinic/model/PetTests.java` (verify `new Pet().getGender() == Gender.UNKNOWN` and explicit assignment)
- [ ] T016 [P] Add gender persistence integration tests to `src/test/java/org/springframework/samples/petclinic/service/AbstractClinicServiceTests.java` (verify gender is saved and retrieved correctly for each enum value)
- [ ] T017 [P] Add gender form binding tests to `src/test/java/org/springframework/samples/petclinic/web/PetControllerTests.java` (verify `genders` model attribute is present, form submission with each gender value, default UNKNOWN when omitted)
- [ ] T018 Run `./mvnw test` and confirm all tests pass with the H2 in-memory profile
- [ ] T019 Run quickstart.md smoke test: start app with `./mvnw jetty:run-war`, navigate to Add New Pet, verify gender dropdown shows Male/Female/Unknown, submit with each value, verify display on owner detail page, edit an existing pet and change gender

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — start immediately
- **Foundational (Phase 2)**: Depends on Phase 1 (T001) — **BLOCKS all user stories**
- **User Story 1 (Phase 3)**: Depends on Phase 2 completion
- **User Story 2 (Phase 4)**: Depends on Phase 3 (T007, T011 must be complete — US2 reuses them)
- **User Story 3 (Phase 5)**: Depends on Phase 2 completion — independent of US1/US2
- **Polish (Phase 6)**: Depends on all user story phases being complete

### User Story Dependencies

- **User Story 1 (P1)**: Requires Phase 2 complete. No dependency on US2 or US3.
- **User Story 2 (P2)**: Reuses `@ModelAttribute("genders")` (T007) and the form dropdown (T011) from US1. Must follow US1.
- **User Story 3 (P3)**: Requires only Phase 2 (Pet.gender field). Can be implemented in parallel with US1.

### Within Each User Story

- Models/entities before services/repositories
- Repository layer before controller layer
- Controller before view (JSP)
- All DB schema files (T003–T006) are independent and can run in parallel

### Parallel Opportunities

- T003, T004, T005, T006 (DB schema + data.sql updates) — all parallel, different files
- T007, T008 (PetController, PetValidator) — parallel, different files
- T009, T010 (JdbcPetRepositoryImpl, JdbcPetRowMapper) — parallel, different files
- T015, T016, T017 (test files) — all parallel, different files
- US3 (T014) can be implemented in parallel with US1 (Phase 3)

---

## Parallel Example: Foundational Phase

```
# All four DB schema+data updates can run simultaneously:
Task: T003 — H2 schema.sql + data.sql
Task: T004 — HSQLDB schema.sql + data.sql
Task: T005 — MySQL schema.sql + data.sql
Task: T006 — PostgreSQL schema.sql + data.sql
```

## Parallel Example: User Story 1

```
# Controller and validator are independent files:
Task: T007 — PetController.java (@ModelAttribute genders)
Task: T008 — PetValidator.java (null check)

# JDBC repository and row mapper are independent files:
Task: T009 — JdbcPetRepositoryImpl.java (SQL + parameter source)
Task: T010 — JdbcPetRowMapper.java (column mapping)
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Create `Gender` enum (T001)
2. Complete Phase 2: Add `Pet.gender` field + all DB schemas (T002–T006)
3. Complete Phase 3: Controller, validator, JDBC, JSP form (T007–T011)
4. **STOP and VALIDATE**: Add a new pet, select each gender, verify persistence
5. Deploy/demo if ready

### Incremental Delivery

1. Phase 1 + Phase 2 → Foundation ready (Gender enum + Pet field + DB schemas)
2. Phase 3 (US1) → Staff can record gender on new pets → **MVP**
3. Phase 4 (US2) → Staff can update gender on existing pets
4. Phase 5 (US3) → Gender displayed on pet profile
5. Phase 6 → Tests pass, smoke test complete

### Parallel Team Strategy

With multiple developers (after Phase 1 + Phase 2 complete):
- Developer A: User Story 1 (T007–T011)
- Developer B: User Story 3 (T014) — independent of US1

---

## Notes

- [P] tasks operate on different files with no shared dependencies
- [Story] label maps each task to its user story for traceability
- US2 has minimal new code — it reuses the `@ModelAttribute` and JSP dropdown from US1
- JPA and Spring Data JPA profiles require no JDBC changes — `@Enumerated(EnumType.STRING)` handles persistence automatically
- The `Gender.toString()` override drives all human-readable display; no JSP label mapping needed
- Verify `./mvnw test` passes after each phase before proceeding
