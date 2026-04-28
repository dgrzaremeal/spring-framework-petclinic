# Tasks: Add Gender Enum to Pet

**Input**: Design documents from `/specs/001-add-pet-gender-enum/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Tests are MANDATORY per feature specification (TDD approach required)

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Foundational (Database & Localization)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented. This feature adds a single field to an existing project, so setup is minimal.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T001 Add gender column to H2 schema.sql in src/main/resources/db/h2/schema.sql
- [X] T001b Add gender column to HSQLDB schema.sql in src/main/resources/db/hsqldb/schema.sql
- [X] T002 Add gender column to MySQL schema.sql in src/main/resources/db/mysql/schema.sql
- [X] T003 Add gender column to PostgreSQL schema.sql in src/main/resources/db/postgresql/schema.sql
- [X] T004 [P] Add Gender localization keys in src/main/resources/messages.properties and ensure gender column is nullable with DEFAULT UNKNOWN in all schema.sql files (H2, MySQL, PostgreSQL)
- [X] T005 [P] Update seed data with gender values in src/main/resources/db/h2/data.sql

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 2: User Story 1 - Record Pet Gender (Priority: P1) 🎯 MVP

**Goal**: Allow pet owners to specify gender when creating a new pet

**Independent Test**: Can be tested by creating a new pet with a gender value and verifying it persists and displays correctly

### Tests for User Story 1 (MANDATORY - TDD) ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T006 [P] [US1] Create GenderTest.java for enum basic tests in src/test/java/org/springframework/samples/petclinic/model/GenderTest.java (CREATE FILE IF NOT EXISTS)
- [X] T007 [P] [US1] Create PetGenderTest.java for Pet gender field tests in src/test/java/org/springframework/samples/petclinic/model/PetGenderTest.java (CREATE FILE IF NOT EXISTS)

### Implementation for User Story 1

- [X] T008 [P] [US1] Create Gender enum in src/main/java/org/springframework/samples/petclinic/model/Gender.java
- [X] T009 [US1] Add gender field to Pet.java in src/main/java/org/springframework/samples/petclinic/model/Pet.java
- [X] T010 [P] [US1] Add gender field to JdbcPet.java in src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPet.java
- [X] T011 [US1] Map gender column in JdbcPetRowMapper.java in src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRowMapper.java
- [X] T012 [US1] Handle gender in JdbcPetRepositoryImpl.java insert in src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java
- [X] T013 [US1] Handle gender in JpaPetRepositoryImpl.java in src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetRepositoryImpl.java
- [X] T014 [US1] Handle gender in SpringDataPetRepository.java in src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetRepository.java
- [X] T015 [US1] Add gender dropdown to createOrUpdatePetForm.jsp in src/main/webapp/WEB-INF/views/pets/createOrUpdatePetForm.jsp
- [X] T016 [US1] Update PetValidator.java to validate gender in src/main/java/org/springframework/samples/petclinic/web/PetValidator.java

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 3: User Story 2 - Update Pet Gender (Priority: P1)

**Goal**: Allow pet owners to update gender information for existing pets

**Independent Test**: Can be tested by updating an existing pet's gender and verifying the change persists

**Dependencies**: User Story 1 must be complete first (uses same infrastructure)

### Implementation for User Story 2

- [X] T017 [P] [US2] Verify gender update works: Run existing repository tests with update scenarios (T012-T014 cover implementation)
- [X] T018 [US2] Verify Pet gender setter allows updates in Pet.java

**Checkpoint**: User Story 2 works (reuses User Story 1 infrastructure)

---

## Phase 4: User Story 3 - View Pet Gender (Priority: P2)

**Goal**: Display gender on pet profile page

**Independent Test**: Can be tested by viewing a pet's details and verifying gender is displayed

### Implementation for User Story 3

- [X] T019 [US3] Add gender field display in ownerDetails.jsp (src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp - inside the pet details section, line ~55 after Type)

**Checkpoint**: All user stories independently functional

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [X] T020 [P] Run full test suite: `./mvnw test`
- [X] T021 [P] Verify backward compatibility (existing pets default to UNKNOWN)
- [X] T022 [P] Test backward compatibility: create pet without gender and verify defaults to UNKNOWN
- [X] T023 Verify quickstart.md scenarios work

---

## Out of Scope

- **REST API for gender**: The spec clarification (Q6) mentioned REST API changes, but this is OUT OF SCOPE for MVP. Pet gender via REST can be added in a future enhancement.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Foundational (Phase 1)**: No dependencies - can start immediately - BLOCKS all user stories
- **User Story 1 (Phase 2)**: Depends on Foundational - MVP delivery
- **User Story 2 (Phase 3)**: Depends on Foundational and US1 - Should be independently testable
- **User Story 3 (Phase 4)**: Depends on Foundational - Uses US1 infrastructure
- **Polish (Phase 5)**: Depends on all user stories

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - No dependencies on other stories - **MVP**
- **User Story 2 (P1)**: Starts after Foundational - Uses US1 infrastructure - Can be tested independently after US1
- **User Story 3 (P2)**: Starts after Foundational - Uses US1/2 infrastructure - Can be tested independently

### Within Each User Story

- Tests MUST be written and FAIL before implementation
- Models (Gender) before entities (Pet)
- Entities before repositories
- Repositories before UI
- Story complete before moving to next priority

### Parallel Opportunities

- T001, T002, T003 can run in parallel (different DB schema files)
- T004, T005 can run in parallel (messages and seed data)
- T006, T007 can run in parallel (test files - no dependencies between tests)
- T008, T009 can run in parallel (Gender enum and Pet field - no dependency)
- T010, T011 can run in parallel (JdbcPet and RowMapper)
- T013, T014 can run in parallel (JPA repos - no dependency between them)

---

## Parallel Example: User Story 1

```bash
# Launch all tests for User Story 1 together:
Task: "Create GenderTest.java for enum basic tests in src/test/java/org/springframework/samples/petclinic/model/GenderTest.java"
Task: "Create PetGenderTest.java for Pet gender field tests in src/test/java/org/springframework/samples/petclinic/model/PetGenderTest.java"

# Launch all model work together:
Task: "Create Gender enum in src/main/java/org/springframework/samples/petclinic/model/Gender.java"
Task: "Add gender field to Pet.java in src/main/java/org/springframework/samples/petclinic/model/Pet.java"

# Launch all repository work together:
Task: "Add gender to JdbcPet.java in src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPet.java"
Task: "Map gender column in JdbcPetRowMapper.java in src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRowMapper.java"
Task: "Handle gender in JpaPetRepositoryImpl.java"
Task: "Handle gender in SpringDataPetRepository.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Foundational
2. Complete Phase 2: User Story 1
3. **STOP and VALIDATE**: Test User Story 1 independently
4. Deploy/demo if ready

### Incremental Delivery

1. Complete Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1 (Model + JDBC repository)
   - Developer B: User Story 1 (JPA/Spring Data repository + UI)
3. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests fail before implementing
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence