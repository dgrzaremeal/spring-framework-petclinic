# Data Model: Add Gender Enum to Pet

**Feature**: 001-add-pet-gender-enum  
**Date**: 2026-04-24

## Entities

### Gender (NEW)

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| name | Gender enum | Not null, 3 values | MALE, FEMALE, UNKNOWN |
| displayKey | String | Not null | Localization key (e.g., "pet.gender.male") |

**Values**:
- `MALE` → display key: `pet.gender.male`
- `FEMALE` → display key: `pet.gender.female`  
- `UNKNOWN` → display key: `pet.gender.unknown`

**Default**: `UNKNOWN` (used when gender is null or invalid)

### Pet (MODIFIED)

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Integer | Primary key | Auto-generated |
| name | String | Not null, 30 chars max | Pet name |
| birthDate | LocalDate | Nullable | Birth date |
| type | PetType | Not null | Reference to PetType |
| owner | Owner | Not null | Reference to Owner |
| visits | Set<Visit> | Eager fetch | Medical visits |
| **gender** | **Gender** | **Nullable, default UNKNOWN** | **NEW FIELD** |

**Relationships**:
- Pet → PetType: ManyToOne
- Pet → Owner: ManyToOne  
- Pet → Visit: OneToMany (cascade ALL, eager)

**State Transitions**:
- New pet: gender defaults to UNKNOWN
- Update pet: gender can be set to any value

## Validation Rules

| Rule | Error Message | Trigger |
|------|---------------|----------|
| Gender not null (after default applied) | N/A | JPA default handles |
| Valid enum value | N/A | Enum type safety |

## Database Schema

### pets table (H2)

```sql
ALTER TABLE pets ADD COLUMN gender VARCHAR(20) DEFAULT 'UNKNOWN';
```

### Seed Data

```sql
-- No separate Gender seed - values are in enum code
```

## Integration Points

| Layer | Interface | Method Changes |
|-------|-----------|-----------------|
| Model | Pet | getGender(), setGender() |
| Service | ClinicService | N/A (Pet entity carries Gender) |
| Repository | PetRepository | N/A (handled by JPA) |
| Web | PetController | Pass genders to JSP |
| View | createOrUpdatePetForm.jsp | Add gender dropdown |

## Backward Compatibility

- Existing pets: gender column is nullable, JPA default = UNKNOWN
- Pet type created before feature: gender will be null, defaults to UNKNOWN
- Invalid/corrupt data: Log warning, default to UNKNOWN