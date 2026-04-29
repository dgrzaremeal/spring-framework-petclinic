# Tasks: Pet Gender/Sex Enum

**Input**: Design documents from `/specs/001-pet-gender-enum_Sonnet/`
**Prerequisites**: plan.md ✅, spec.md ✅, research.md ✅, data-model.md ✅, contracts/pet-api.md ✅, quickstart.md ✅

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3)
- Exact file paths are included in all task descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Create the foundational `Gender` enum that all user stories depend on.

- [X] T001 Create `Gender` enum with MALE, FEMALE, UNKNOWN values and `getDisplayName()`/`toString()` in `src/main/java/org/springframework/samples/petclinic/model/Gender.java`

**Checkpoint**: `Gender.java` compiles — all subsequent tasks can reference this type.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Add the `gender` field to the `Pet` entity and update all four database schema/seed files. These changes are required before any persistence or UI work can proceed.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

- [X] T002 Add `gender` field (`@Enumerated(EnumType.STRING)`, default `Gender.UNKNOWN`) with getter/setter to `src/main/java/org/springframework/samples/petclinic/model/Pet.java`
- [X] T003 [P] Add `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` column to `CREATE TABLE pets` in `src/main/resources/db/h2/schema.sql`
- [X] T004 [P] Add `gender VARCHAR(20) DEFAULT 'UNKNOWN' NOT NULL` column to `CREATE TABLE pets` in `src/main/resources/db/hsqldb/schema.sql`
- [X] T005 [P] Add `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` column to `CREATE TABLE pets` in `src/main/resources/db/mysql/schema.sql`
- [X] T006 [P] Add `gender VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'` column to `CREATE TABLE pets` in `src/main/resources/db/postgresql/schema.sql`
- [X] T007 [P] Update all `INSERT INTO pets` statements to use explicit column list including `gender` with value `'UNKNOWN'` for all 13 seed rows in `src/main/resources/db/h2/data.sql`
- [X] T008 [P] Update all `INSERT INTO pets` statements to use explicit column list including `gender` with value `'UNKNOWN'` for all 13 seed rows in `src/main/resources/db/hsqldb/data.sql`
- [X] T009 [P] Update all `INSERT INTO pets` statements to use explicit column list including `gender` with value `'UNKNOWN'` for all 13 seed rows in `src/main/resources/db/mysql/data.sql`
- [X] T010 [P] Update all `INSERT INTO pets` statements to use explicit column list including `gender` with value `'UNKNOWN'` for all 13 seed rows in `src/main/resources/db/postgresql/data.sql`

**Checkpoint**: Foundation ready — `Pet` entity has `gender` field, all four DB dialects have the column and seed data. Run `./mvnw test` to confirm compilation and H2 tests pass before proceeding.

---

## Phase 3: User Story 1 — Record Pet Gender at Registration (Priority: P1) 🎯 MVP

**Goal**: Staff can select a gender (MALE, FEMALE, UNKNOWN) when creating a new pet. The value is persisted correctly across all three persistence implementations (JPA, JDBC, Spring Data JPA). UNKNOWN is the default when no selection is made.

**Independent Test**: Create a new pet via the form, select each gender value in turn, save, and verify the value is stored and displayed. Run `./mvnw test` (all three profiles) to confirm persistence.

### Implementation for User Story 1

- [X] T011 [P] [US1] Add `gender` field with getter/setter to `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPet.java` (default `Gender.UNKNOWN`)
- [X] T012 [US1] Add `gender` to `createPetParameterSource` (`.addValue("gender", pet.getGender().name())`) and to the UPDATE SQL statement in `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java`
- [X] T013 [US1] Add `gender` column mapping in the row mapper inside `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java` (`pet.setGender(genderStr != null ? Gender.valueOf(genderStr) : Gender.UNKNOWN)`)
- [X] T014 [P] [US1] Add `@ModelAttribute("genders")` method returning `Gender.values()` to `src/main/java/org/springframework/samples/petclinic/web/PetController.java`
- [X] T015 [P] [US1] Add gender `<select>` dropdown using `<petclinic:selectField name="gender" label="Gender " names="${genders}" size="3"/>` after the type field in `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`
- [X] T016 [US1] Update `src/test/java/org/springframework/samples/petclinic/service/ClinicServiceTests.java` — add assertions that a newly saved pet has the correct gender value and that omitting gender defaults to UNKNOWN
- [X] T017 [US1] Update `src/test/java/org/springframework/samples/petclinic/web/PetControllerTests.java` — verify `genders` model attribute is populated and that form submission with each gender value (MALE, FEMALE, UNKNOWN) binds correctly
- [X] T017b [US1] Add an integration or MockMvc test in `src/test/java/org/springframework/samples/petclinic/web/PetControllerTests.java` that performs a GET request to the JSON endpoint for a pet resource and asserts that the response body contains a `gender` field with value `"MALE"`, `"FEMALE"`, or `"UNKNOWN"` — confirming FR-010 (gender included in REST API responses as a non-breaking additive field)

**Checkpoint**: User Story 1 is fully functional. Run `./mvnw test`, `./mvnw test -P jdbc`, and `./mvnw test -P spring-data-jpa` — all must pass. Manually verify the gender dropdown appears on the new-pet form with Unknown pre-selected.

---

## Phase 4: User Story 2 — Update Pet Gender on Existing Record (Priority: P2)

**Goal**: Staff can open an existing pet's edit form, change the gender to any valid value, save, and see the updated value. Invalid values are rejected by Spring MVC binding.

**Independent Test**: Open an existing pet record (e.g., owner 1, pet "Leo"), change gender from UNKNOWN to FEMALE, save, and verify the profile reflects FEMALE. Attempt to submit an invalid gender string and confirm a binding error is returned.

### Implementation for User Story 2

- [X] T018 [US2] Verify that `PetController.java` edit flow (`initUpdateForm` / `processUpdateForm`) correctly pre-populates the `gender` field from the existing `Pet` object and that the `genders` model attribute (added in T014) is available on the edit form — no code change expected, but confirm by inspection of `src/main/java/org/springframework/samples/petclinic/web/PetController.java`
- [X] T019 [US2] Update `src/test/java/org/springframework/samples/petclinic/web/PetControllerTests.java` — add test that editing an existing pet's gender from UNKNOWN to FEMALE persists the new value; add a second test that submits an unrecognized gender string and asserts: (a) a binding/validation error is returned, and (b) the response re-renders the edit form (HTTP 200, not a redirect) with an error message present in the model or view — satisfying US2 Acceptance Scenario 2 ("system rejects the input and displays an appropriate error message").

**Checkpoint**: User Story 2 is fully functional. The edit form shows the current gender pre-selected and allows changing it. Run `./mvnw test` to confirm.

---

## Phase 5: User Story 3 — View Pet Gender on Profile (Priority: P3)

**Goal**: The owner detail page displays the pet's gender in human-readable format ("Male", "Female", "Unknown") using `pet.gender.displayName`.

**Independent Test**: Navigate to an owner's detail page for a pet with a known gender value and confirm the label is displayed correctly next to the pet's other attributes.

### Implementation for User Story 3

- [X] T020 [US3] Add gender display (`<dt>Gender</dt><dd><c:out value="${pet.gender.displayName}"/></dd>`) after the Type display in the pet detail section of `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

**Checkpoint**: User Story 3 is fully functional. Start the app with `./mvnw jetty:run-war`, navigate to an owner detail page, and confirm gender is displayed as "Male", "Female", or "Unknown".

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Verify the complete feature end-to-end across all profiles and confirm the JSON contract.

- [X] T021 [P] Confirm JSON serialization format for `gender`: per `contracts/pet-api.md`, **Option B is selected** — default Jackson behavior serializes as `"MALE"`, `"FEMALE"`, `"UNKNOWN"` (no `@JsonValue` needed). Verify no `@JsonValue` annotation is present on `getDisplayName()` in `src/main/java/org/springframework/samples/petclinic/model/Gender.java` and add a comment documenting the decision.
- [X] T022 Run full test suite across all three persistence profiles: `./mvnw test`, `./mvnw test -P jdbc`, `./mvnw test -P spring-data-jpa` — all must pass
- [X] T023 Run quickstart.md end-to-end validation: start app with `./mvnw jetty:run-war`, navigate to `http://localhost:8080/petclinic/owners/1`, add a new pet with gender "Male", verify it saves and displays correctly, edit the pet and change gender, verify the update persists

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: No dependencies — start immediately
- **Phase 2 (Foundational)**: Depends on Phase 1 — BLOCKS all user stories
- **Phase 3 (US1)**: Depends on Phase 2 — can start as soon as foundation is complete
- **Phase 4 (US2)**: Depends on Phase 2 — largely depends on Phase 3 (T014 adds the `genders` model attribute reused by edit form)
- **Phase 5 (US3)**: Depends on Phase 2 — independent of US1/US2 (display only)
- **Phase 6 (Polish)**: Depends on all user story phases

### User Story Dependencies

- **US1 (P1)**: Requires Phase 2 complete. No dependency on US2 or US3.
- **US2 (P2)**: Requires Phase 2 complete. Reuses `genders` model attribute from T014 (US1) — implement US1 first.
- **US3 (P3)**: Requires Phase 2 complete. Independent of US1/US2 (JSP display only).

### Within Each User Story

- JDBC layer (T011–T013) before running JDBC profile tests
- `@ModelAttribute` (T014) before JSP form (T015)
- Implementation tasks before test update tasks

### Parallel Opportunities

- T003–T010 (schema + seed data for all four dialects) can all run in parallel
- T011 and T014–T015 can run in parallel (different files)
- T016 and T017 can run in parallel (different test files)
- US3 (T020) can be worked on in parallel with US1/US2 after Phase 2

---

## Parallel Example: Phase 2 (Schema + Seed Data)

```
# All eight schema/data tasks can run simultaneously (different files):
Task T003: h2/schema.sql
Task T004: hsqldb/schema.sql
Task T005: mysql/schema.sql
Task T006: postgresql/schema.sql
Task T007: h2/data.sql
Task T008: hsqldb/data.sql
Task T009: mysql/data.sql
Task T010: postgresql/data.sql
```

## Parallel Example: User Story 1

```
# After T011-T013 (JDBC layer) are done, these can run in parallel:
Task T014: PetController.java — add genders model attribute
Task T015: createOrUpdatePetForm.jsp — add gender dropdown

# After T014-T015 are done, these can run in parallel:
Task T016: ClinicServiceTests.java — gender persistence assertions
Task T017: PetControllerTests.java — form binding tests
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Create `Gender.java`
2. Complete Phase 2: Add field to `Pet.java`, update all 4 schema + data files
3. Complete Phase 3: JDBC layer, controller, JSP form, tests
4. **STOP and VALIDATE**: Run `./mvnw test` (all 3 profiles), manually test the new-pet form
5. Deploy/demo if ready

### Incremental Delivery

1. Phase 1 + Phase 2 → Foundation ready (compiles, H2 tests pass)
2. Phase 3 (US1) → New pet creation with gender works → **MVP**
3. Phase 4 (US2) → Edit pet gender works
4. Phase 5 (US3) → Gender displayed on owner detail page
5. Phase 6 → Full validation across all profiles

### Parallel Team Strategy

With multiple developers (after Phase 2 is complete):
- Developer A: Phase 3 (US1 — JDBC layer + controller + form)
- Developer B: Phase 5 (US3 — ownerDetails.jsp display)
- Developer A continues: Phase 4 (US2 — edit flow verification + tests)

---

## Notes

- [P] tasks = different files, no blocking dependencies between them
- [Story] label maps each task to its user story for traceability
- JPA and Spring Data JPA implementations require **no code changes** — `@Enumerated(EnumType.STRING)` on `Pet.gender` handles persistence automatically
- Only the JDBC implementation requires explicit SQL and row-mapper changes (T011–T013)
- `PetValidator` requires **no changes** — Spring MVC's `ConversionService` rejects unrecognized enum strings before validation runs
- `ClinicServiceImpl` requires **no changes** — `savePet` already delegates to the repository
- All four DB dialect schema/data files must be updated even though only H2 is used in tests
- Run all three Maven profiles (`default`, `-P jdbc`, `-P spring-data-jpa`) before marking the feature complete
