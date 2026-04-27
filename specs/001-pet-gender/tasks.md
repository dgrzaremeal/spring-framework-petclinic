---

description: "Task list for Add Gender Enum to Pet feature"
---

# Tasks: Add Gender Enum to Pet

**Input**: Design documents from `/specs/001-pet-gender/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2)
- Include exact file paths in descriptions

---

## Phase 1: Foundational (Core Model Changes)

**Purpose**: Core model and enum changes required before any user story

- [X] T001 Create Gender enum in src/main/java/org/springframework/samples/petclinic/model/Gender.java
- [X] T002 [P] Add gender field to Pet entity in src/main/java/org/springframework/samples/petclinic/model/Pet.java
- [X] T003 [P] Add gender field to JdbcPet in src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPet.java
- [X] T004 Add gender mapping in JdbcPetRowMapper in src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRowMapper.java

**Checkpoint**: Core model ready - user story implementation can now begin

---

## Phase 2: User Story 1 - Record Pet Gender Information (Priority: P1) 🎯 MVP

**Goal**: Users can specify gender when creating/editing pets, with UNKNOWN as default

**Independent Test**: Create/edit a pet profile and select a gender value from the available options

### Tests for User Story 1 ⚠️

> **NOTE: Tests MUST be written FIRST per Constitution (TDD), ensure they FAIL before implementation**

- [ ] T005 [P] [US1] Write Pet gender persistence tests in src/test/java/org/springframework/samples/petclinic/repository/jpa/JpaPetRepositoryTest.java
- [ ] T006 [P] [US1] Write JDBC pet gender tests in src/test/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryTest.java

### Implementation for User Story 1

- [X] T007 [P] [US1] Add findByGender to PetRepository interface in src/main/java/org/springframework/samples/petclinic/repository/PetRepository.java
- [X] T008 [US1] Implement findByGender in JpaPetRepositoryImpl in src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetRepositoryImpl.java
- [X] T009 [US1] Implement findByGender in JdbcPetRepositoryImpl in src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java
- [X] T010 [P] [US1] Add findByGender to SpringDataPetRepository in src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetRepository.java
- [X] T011 Add gender column to H2 schema in src/main/resources/db/h2/schema.sql
- [X] T012 Add gender column to MySQL schema in src/main/resources/db/mysql/schema.sql
- [X] T013 Add gender column to PostgreSQL schema in src/main/resources/db/postgresql/schema.sql
- [X] T014 [US1] Update PetController with genders model attribute in src/main/java/org/springframework/samples/petclinic/web/PetController.java
- [X] T016 [US1] Add gender dropdown to createOrUpdatePetForm.jsp in src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp

**Checkpoint**: User Story 1 should be fully functional and testable independently

---

## Phase 3: User Story 2 - View and Filter Pets by Gender (Priority: P2)

**Goal**: Users can filter the pet list by gender

**Independent Test**: Apply a gender filter to the pet list and verify only matching pets are displayed

### Tests for User Story 2 ⚠️

- [ ] T017 [P] [US2] Write gender filter tests in src/test/java/org/springframework/samples/petclinic/repository/jpa/JpaPetRepositoryTest.java

### Implementation for User Story 2

- [ ] T018 [US2] Add gender filter parameter to PetController in src/main/java/org/springframework/samples/petclinic/web/PetController.java
- [ ] T019 [US2] Add findByOwnerAndGender to ClinicService interface in src/main/java/org/springframework/samples/petclinic/service/ClinicService.java
- [ ] T020 [US2] Implement findByOwnerAndGender in ClinicServiceImpl in src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java

**Checkpoint**: Both User Stories 1 and 2 should work independently

---

## Phase 4: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T021 [P] Update PetValidator to validate gender in src/main/java/org/springframework/samples/petclinic/web/PetValidator.java
- [ ] T022 Run Maven tests with `./mvnw test` to validate implementation
- [ ] T023 Run Maven verify with `./mvnw verify` to ensure full build passes
- [ ] T024 [P] Update data-model.md with final implementation details

---

## Dependencies & Execution Order

### Phase Dependencies

- **Foundational (Phase 1)**: No dependencies - can start immediately
- **User Story 1 (Phase 2)**: Depends on Foundational - introduces gender to models
- **User Story 2 (Phase 3)**: Depends on Foundational - adds filtering on top of US1 model
- **Polish (Phase 4)**: Depends on all user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - MVP deliverable
- **User Story 2 (P2)**: Can run in parallel with User Story 1 after Foundational - adds filtering but can reuse gender model

### Within Each User Story

- Tests MUST be written and FAIL before implementation (Red-Green-Refactor)
- Models before repositories
- Repository implementations before service layer
- Service layer before controller endpoints
- Core implementation before integration

### Parallel Opportunities

- T002 (Pet entity) and T003 (JdbcPet) can run in parallel (different files)
- T005 (JPA tests) and T006 (JDBC tests) can run in parallel (different files)
- T011, T012, T013 (schema updates) can run in parallel (different files)
- T007, T008, T009, T010 (findByGender implementations) can run in parallel (different repositories)
- T018 (filter param) and T019 (service method) can run in parallel

---

## Parallel Example: Foundational Phase

```bash
# These can run in parallel:
Task: "Add gender field to Pet entity in src/main/java/org/springframework/samples/petclinic/model/Pet.java"
Task: "Add gender field to JdbcPet in src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPet.java"
```

---

## Parallel Example: User Story 1

```bash
# These can run in parallel:
Task: "Add findByGender to PetRepository interface"
Task: "Implement findByGender in JpaPetRepositoryImpl"
Task: "Implement findByGender in JdbcPetRepositoryImpl"
Task: "Add findByGender to SpringDataPetRepository"

# These can run in parallel:
Task: "Add gender column to H2 schema"
Task: "Add gender column to MySQL schema"
Task: "Add gender column to PostgreSQL schema"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Foundational (core model changes)
2. Complete Phase 2: User Story 1
3. **STOP and VALIDATE**: Test User Story 1 independently
4. Deploy/demo if ready

### Incremental Delivery

1. Complete Foundational → Core model ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Developer A: Complete Foundational (T001-T004)
2. Once Foundational is done:
   - Developer A: User Story 1 database + repository
   - Developer B: User Story 1 UI (Controller + JSP)
3. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Per Constitution (TDD), tests MUST fail before implementation
- Must work with all three persistence implementations (JPA, JDBC, Spring Data JPA)
- UNKNOWN is the default gender value per FR-004
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently