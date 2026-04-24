# Data Model: Add Gender Enum to Pet

## Entities

### Pet (existing, modified)

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | Integer | PK, auto-generated | Existing |
| name | String(30) | Not null | Existing |
| birthDate | LocalDate | Nullable | Existing |
| type | PetType | FK → types.id | Existing |
| owner | Owner | FK → owners.id | Existing |
| visits | Set\<Visit\> | Cascade ALL | Existing |
| **gender** | **Gender** | **Nullable** | **NEW** |

### Gender (NEW)

| Value | Description |
|-------|-------------|
| MALE | Male pet |
| FEMALE | Female pet |
| UNKNOWN | Gender not specified (default) |

### Relationships

- Pet 1:1 PetType (existing)
- Pet 1:∞ Visit (existing)
- Pet → Gender: 1:1 (new)

## Validation Rules

- Gender field defaults to UNKNOWN when null
- Gender values restricted to enum constants (validated by type system)
- FR-004: UNKNOWN is default for new pets - handled in entity constructor or service layer

## State Transitions

- Pet without gender → Gender set to UNKNOWN (default on read)
- Gender can be updated via form (MALE, FEMALE, UNKNOWN)