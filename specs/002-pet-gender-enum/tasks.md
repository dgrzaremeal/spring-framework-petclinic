---

description: "Task list for Add Gender Enum to Pet feature implementation"
---

# Tasks: Add Gender Enum to Pet

**Input**: Design documents from `/specs/002-pet-gender-enum/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), data-model.md, contracts/

**Tests**: Required - TDD approach per constitution check

**Organization**: Tasks are grouped by user story to enable independent implementation and testing

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization - No additional setup needed for this small feature

- [ ] T001 Verify build environment with `./mvnw clean test` passes

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core changes required before any user story can function

**Critical**: Database schema must be updated before any pet operations work

- [ ] T002 [P] Add gender column to H2 schema in src/main/resources/db/h2/schema.sql
- [ ] T003 [P] Add gender column to MySQL schema in src/main/resources/db/mysql/schema.sql
- [ ] T004 [P] Add gender column to PostgreSQL schema in src/main/resources/db/postgresql/schema.sql
- [ ] T002a [P] Add gender column to Spring Data JPA schema in src/main/resources/db/spring-data-jpa/schema.sql
- [ ] T002b [P] Add gender column to JDBC schema in src/main/resources/db/jdbc/schema.sql
- [ ] T005 Update H2 data.sql with default gender values for existing pets
- [ ] T006 Update MySQL data.sql with default gender values for existing pets
- [ ] T007 Update PostgreSQL data.sql with default gender values for existing pets

**Checkpoint**: Database schemas ready - user story implementation can begin

---

## Phase 3: User Story 1 - Record Pet Gender (Priority: P1) 🎯 MVP

**Goal**: Staff can record gender when creating a new pet and view it in pet details

**Independent Test**: Create a new pet with gender Male/Female/Unknown and verify it displays correctly

### Tests for User Story 1 ⚠️

> **Write tests FIRST, ensure they FAIL before implementation**

- [ ] T008 [P] [US1] Unit test for Gender enum in tests/unit/PetTests.java (add gender tests)
- [ ] T009 [P] [US1] Integration test for pet creation with gender in tests/integration/PetCreationTests.java
- [ ] T010 [P] [US1] Contract test for Pet repository with gender field in tests/contract/PetRepositoryTests.java

### Implementation for User Story 1

- [ ] T011 [P] [US1] Create Gender enum in src/main/java/org/springframework/samples/petclinic/model/Gender.java
- [ ] T012 [P] [US1] Add gender field to Pet model in src/main/java/org/springframework/samples/petclinic/model/Pet.java
- [ ] T013 [US1] Update JdbcPetRepository to handle gender in src/main/java/org/springframework/samples/petclinic/repository/JdbcPetRepositoryImpl.java
- [ ] T014 [US1] Update JdbcOwnerRepository to handle pet gender (if needed)
- [ ] T015 [US1] Add gender dropdown to pet form JSP in src/main/webapp/WEB-INF/views/pets/createOrUpdatePetForm.jsp
- [ ] T016 [US1] Display gender in pet details JSP in src/main/webapp/WEB-INF/views/pets/petDetails.jsp
- [ ] T012a [P] [US1] Initialize Pet.gender to Gender.UNKNOWN as default in Pet.java constructor
- [ ] T015a [US1] Update PetController to bind gender form field in src/main/java/org/springframework/samples/petclinic/web/PetController.java
- [ ] T013a [P] [US1] Add @Enumerated(Gender) annotation to Pet.gender field for JPA
- [ ] T013b [P] [US1] Update JPA Pet repository tests
- [ ] T013c [P] [US1] Create Spring Data JPA PetRepository interface in src/main/java/org/springframework/samples/petclinic/repository/PetRepository.java

**Checkpoint**: User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Edit Pet Gender (Priority: P1)

**Goal**: Staff can update gender of existing pets

**Independent Test**: Edit an existing pet's gender and verify the change persists

### Tests for User Story 2 ⚠️

- [ ] T017 [P] [US2] Integration test for pet gender update in tests/integration/PetUpdateTests.java

### Implementation for User Story 2

- [ ] T018 [US2] Update pet form JSP to support gender editing in src/main/webapp/WEB-INF/views/pets/createOrUpdatePetForm.jsp
- [ ] T019 [US2] Ensure JdbcPetRepository.updatePet handles gender update
- [ ] T020 [US2] Verify gender displays correctly after update

**Checkpoint**: Both User Stories 1 and 2 should work independently

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Quality improvements across stories

- [ ] T021 [P] Run full test suite `./mvnw test` passes
- [ ] T021a Verify JPA persistence profile works `./mvnw test -Dspring.profiles.active=jpa`
- [ ] T021b Verify JDBC persistence profile works `./mvnw test -Dspring.profiles.active=jdbc`
- [ ] T021c Verify Spring Data JPA profile works `./mvnw test -Dspring.profiles.active=spring-data-jpa`
- [ ] T022 Verify Maven MySQL profile works `./mvnw test -P MySQL`
- [ ] T023 Verify Maven PostgreSQL profile works `./mvnw test -P PostgreSQL`
- [ ] T024 Quickstart.md validation per specs/002-pet-gender-enum/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - No dependencies on other stories
- **User Story 2 (P1)**: Can start after Foundational - Uses same model, independent implementation

### Within Each User Story

- Tests MUST be written and FAIL before implementation
- Gender enum created before Pet model update
- Database schemas before repository updates
- Story complete before moving to Polish phase

### Parallel Opportunities

- T002, T003, T004, T002a, T002b can run in parallel (different DB schemas)
- T008, T009, T010 can run in parallel (different test types/layers)
- T011, T012 can run in parallel (Gender enum first, then Pet model uses it)
- T013, T014 can run in parallel (different repository files)
- T012a, T013a, T013b, T013c can run in parallel (JPA/Spring Data JPA variations)

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Foundational → Database schemas ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Polish phase → Full validation

### Parallel Team Strategy

With multiple developers:

1. Developer A: Foundational (DB schemas) - T002-T007
2. Developer B: User Story 1 implementation - T011-T016
3. Once Foundational done, can run these in parallel

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests fail before implementing
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Must support all 3 persistence implementations (H2, MySQL, PostgreSQL) per plan.md