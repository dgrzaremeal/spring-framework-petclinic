---
description: "Task list for add-gender-enum feature"
---

# Tasks: add-gender-enum

**Input**: Design documents from `/specs/001-add-gender-enum/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md

**Tests**: The examples below include test tasks. Tests are OPTIONAL - only include them if explicitly requested in the feature specification.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Single project**: `src/`, `tests/` at repository root
- **Web app**: `backend/src/`, `frontend/src/`
- **Mobile**: `api/src/`, `ios/src/` or `android/src/`
- Paths shown below assume single project - adjust based on plan.md structure

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Verify project structure matches implementation plan
- [ ] T002 [P] Confirm Java 17 and Spring Framework 7.0.6 dependencies are configured
- [ ] T003 [P] Verify testing dependencies (JUnit 5, AssertJ, Mockito) are present

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [ ] T004 Verify database connection and H2 configuration for testing
- [ ] T005 [P] Confirm Spring Data JPA repositories are functional
- [ ] T006 [P] Validate existing Pet entity tests pass

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Add Gender Enum to Pet Entity (Priority: P1) 🎯 MVP

**Goal**: Add gender attribute of type Gender enum (MALE, FEMALE, UNKNOWN) to Pet entity with proper getters and setters

**Independent Test**: Can be fully tested by creating a Pet object with each gender value and verifying the attribute is correctly stored and retrieved.

### Tests for User Story 1 (OPTIONAL - only if tests requested) ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T007 [P] [US1] Create unit test for Pet gender attribute getter/setter in tests/unit/model/PetTest.java
- [ ] T008 [P] [US1] Create unit test for Gender enum values in tests/unit/model/GenderTest.java

### Implementation for User Story 1

- [ ] T009 [P] [US1] Create Gender.java enum in src/main/java/org/springframework/samples/petclinic/model/
- [ ] T010 [US1] Add gender attribute to Pet.java with JPA @Enumerated annotation
- [ ] T11 [US1] Add getter and setter for gender attribute in Pet.java
- [ ] T12 [US1] Update Pet constructors to initialize gender attribute
- [ ] T13 [US1] Add validation in Pet setter to prevent null gender values

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Gender Validation (Priority: P2)

**Goal**: Implement validation to ensure only valid gender values (MALE, FEMALE, UNKNOWN) can be assigned to Pet

**Independent Test**: Can be tested by attempting to assign invalid gender values and verifying they are rejected.

### Tests for User Story 2 (OPTIONAL - only if tests requested) ⚠️

- [ ] T14 [P] [US2] Create unit test for gender validation (valid values) in tests/unit/model/PetTest.java
- [ ] T15 [P] [US2] Create unit test for gender validation (invalid values) in tests/unit/model/PetTest.java

### Implementation for User Story 2

- [ ] T16 [US2] Enhance Pet.setGender() method to validate against Gender enum values
- [ ] T17 [US2] Ensure IllegalArgumentException is thrown for null or invalid gender values
- [ ] T18 [US2] Update existing Pet creation code to include gender initialization

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T19 [P] Update database schema to add GENDER column to PETS table
- [ ] T20 [P] Verify existing functionality still works with gender attribute
- [ ] T21 [P] Run quickstart.md validation steps to confirm implementation
- [ ] T22 [P] Documentation updates in code comments
- [ ] T23 [P] Code cleanup and refactoring
- [ ] T24 [P] Run full test suite to ensure no regressions

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - Builds on US1 but should be independently testable

### Within Each User Story

- Tests (if included) MUST be written and FAIL before implementation
- Models before services (not applicable here - model-only changes)
- Core implementation before integration
- Story complete before moving to next priority

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- All Foundational tasks marked [P] can run in parallel (within Phase 2)
- Once Foundational phase completes, all user stories can start in parallel (if team capacity allows)
- All tests for a user story marked [P] can run in parallel
- Model tasks within a story marked [P] can run in parallel
- Different user stories can be worked on in parallel by different team members

---

## Parallel Example: User Story 1

```bash
# Launch all tests for User Story 1 together (if tests requested):
Task: "Create unit test for Pet gender attribute getter/setter in tests/unit/model/PetTest.java"
Task: "Create unit test for Gender enum values in tests/unit/model/GenderTest.java"

# Launch all model implementation tasks for User Story 1 together:
Task: "Create Gender.java enum in src/main/java/org/springframework/samples/petclinic/model/"
Task: "Add gender attribute to Pet.java with JPA @Enumerated annotation"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1
   - Developer B: User Story 2
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