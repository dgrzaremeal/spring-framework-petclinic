# API Contracts: Pet Gender/Sex Enum

**Feature**: `001-pet-gender-enum_Sonnet`  
**Date**: 2026-04-29  
**Contract Type**: HTTP response shape (JSON) — additive, non-breaking

---

## Overview

This project exposes JSON/XML responses via `@ResponseBody` on Spring MVC controllers (e.g., `/vets.json`). There is no dedicated `/pets/{id}.json` endpoint today. The `gender` field is added to the `Pet` model and will appear automatically in any future or existing JSON serialization of `Pet` objects.

This contract documents the expected JSON shape for `Pet` objects after the feature is implemented.

---

## Pet JSON Shape (after feature)

When a `Pet` object is serialized to JSON (e.g., embedded in an owner response or a future `/pets/{id}.json` endpoint), the `gender` field MUST appear as a string with one of three values.

### Example: Pet with gender MALE

```json
{
  "id": 1,
  "name": "Leo",
  "birthDate": "2010-09-07",
  "type": {
    "id": 1,
    "name": "cat"
  },
  "gender": "MALE"
}
```

### Example: Pet with gender UNKNOWN (default)

```json
{
  "id": 2,
  "name": "Basil",
  "birthDate": "2012-08-06",
  "type": {
    "id": 6,
    "name": "hamster"
  },
  "gender": "UNKNOWN"
}
```

---

## Gender Field Contract

| Property | Type | Required | Values | Default |
|----------|------|----------|--------|---------|
| `gender` | string | yes | `"MALE"`, `"FEMALE"`, `"UNKNOWN"` | `"UNKNOWN"` |

**Notes**:
- **Decision (Option B selected)**: The `gender` field serializes using the default Jackson behavior — i.e., the enum constant name (`"MALE"`, `"FEMALE"`, `"UNKNOWN"`). This is consistent with the DB storage format and requires no `@JsonValue` annotation. No external API consumers exist today, making this the lower-risk, simpler choice.
- The JSON examples above show display-name format (`"Male"`, `"Unknown"`); those examples should be read as illustrative of the field's presence. The actual serialized values are `"MALE"`, `"FEMALE"`, `"UNKNOWN"`.
- `PetValidator` and `PetController` are unaffected by this decision.

---

## Form Submission Contract

### POST /owners/{ownerId}/pets/new
### POST /owners/{ownerId}/pets/{petId}/edit

**New form field**:

| Field | Type | Required | Accepted Values | Default |
|-------|------|----------|----------------|---------|
| `gender` | string (form param) | no | `MALE`, `FEMALE`, `UNKNOWN` | `UNKNOWN` |

The form submits the enum constant name (e.g., `"MALE"`) as the string value. Spring MVC's `ConversionService` converts this to `Gender.MALE` automatically. An unrecognized value results in a binding error.

---

## Backward Compatibility

- This is a **non-breaking additive change**. Existing API consumers that do not read the `gender` field are unaffected.
- No API versioning is required (per spec FR-010).
- Existing pet records will have `gender = "Unknown"` after the schema update.
