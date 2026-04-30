# HTTP Form Contracts: Pet Gender Field

**Feature**: `001-pet-gender-enum_Sonnet_2`  
**Date**: 2026-04-30

This document describes the HTTP interface changes introduced by the Pet Gender feature. The application exposes HTML form endpoints (not a REST API); contracts are defined as form parameter schemas.

---

## Endpoint 1: Add New Pet

**Route**: `POST /owners/{ownerId}/pets/new`  
**Controller**: `PetController.processCreationForm`

### Form Parameters

| Parameter | Type | Required | Valid Values | Default | Notes |
|-----------|------|----------|--------------|---------|-------|
| `name` | String | Yes | Non-empty string | — | Pet name |
| `birthDate` | String | Yes | `yyyy/MM/dd` format | — | Pet birth date |
| `type` | String | Yes | Any valid PetType name | — | Pet type (existing) |
| `gender` | String | No | `MALE`, `FEMALE`, `UNKNOWN` | `UNKNOWN` | **NEW** — Gender enum name |

### Success Response
- HTTP 302 redirect to `/owners/{ownerId}`

### Validation Errors
- HTTP 200 with form view `pets/createOrUpdatePetForm`
- `gender` field: type mismatch if value is not a valid `Gender` enum name

---

## Endpoint 2: Update Existing Pet

**Route**: `POST /owners/{ownerId}/pets/{petId}/edit`  
**Controller**: `PetController.processUpdateForm`

### Form Parameters

| Parameter | Type | Required | Valid Values | Default | Notes |
|-----------|------|----------|--------------|---------|-------|
| `name` | String | Yes | Non-empty string | — | Pet name |
| `birthDate` | String | Yes | `yyyy/MM/dd` format | — | Pet birth date |
| `type` | String | No (on edit) | Any valid PetType name | — | Pet type |
| `gender` | String | No | `MALE`, `FEMALE`, `UNKNOWN` | `UNKNOWN` | **NEW** — Gender enum name |

### Success Response
- HTTP 302 redirect to `/owners/{ownerId}`

### Validation Errors
- HTTP 200 with form view `pets/createOrUpdatePetForm`

---

## Model Attribute: `genders`

**Added to**: `PetController` via `@ModelAttribute("genders")`  
**Type**: `List<Gender>`  
**Values**: `[MALE, FEMALE, UNKNOWN]` (in enum declaration order)  
**Used by**: `createOrUpdatePetForm.jsp` to populate the gender `<select>` dropdown

---

## View Contract: Gender Display

**View**: `ownerDetails.jsp` (pet profile section)  
**Expression**: `${pet.gender}` → calls `Gender.toString()`

| Stored Value | Displayed Label |
|--------------|-----------------|
| `MALE`       | `Male`          |
| `FEMALE`     | `Female`        |
| `UNKNOWN`    | `Unknown`       |
