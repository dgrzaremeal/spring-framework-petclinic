# TinySpec: Gender Enum in Pet

**Branch**: gh-spec-kit--constitution
**Date**: 2026-04-24
**Status**: draft
**Complexity**: small

## What

Add a Gender enum field (MALE, FEMALE, UNKNOWN) to the Pet entity to enable sex-based filtering and reporting in the pet clinic.

## Context

| File | Role |
|------|------|
| `src/main/java/org/springframework/samples/petclinic/model/Pet.java` | Will be modified — add gender field |
| `src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java` | Context — base class |
| `src/main/java/org/springframework/samples/petclinic/model/PetType.java` | Reference — similar enum pattern |

## Requirements

1. Create Gender enum with MALE, FEMALE, UNKNOWN values
2. Add gender field to Pet entity with getter/setter
3. Persist gender as a database column (type_id style)

## Plan

1. Create new `Gender.java` enum in model package
2. Add gender field to Pet.java with JPA annotations
3. Add getter/setter methods for gender

## Tasks

- [ ] Create Gender enum (MALE, FEMALE, UNKNOWN)
- [ ] Add gender field to Pet.java
- [ ] Add getGender/setGender methods

## Done When

- [ ] All tasks checked off
- [ ] Code compiles
- [ ] No lint errors