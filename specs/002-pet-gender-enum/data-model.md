# Data Model: Add Gender Enum to Pet

## Entities

### Gender (Enum)

| Field | Type | Constraints |
|-------|------|-------------|
| MALE | Enum value | |
| FEMALE | Enum value | |
| UNKNOWN | Enum value | Default |

**Storage**: VARCHAR(20) in database (String representation)

### Pet (Entity) - Modified

| Field | Type | Constraints | Notes |
|-------|------|--------------|-------|
| id | Long | Primary key | From BaseEntity |
| name | String | max 30 | From NamedEntity |
| birthDate | LocalDate | Optional | |
| type | PetType | Not null | FK to types |
| owner | Owner | Not null | FK to owners |
| visits | Set<Visit> | Eager fetch | |
| **gender** | Gender | Default UNKNOWN | **NEW FIELD** |

### Relationships

- Pet (1) → Gender (composition - embedded enum)
- Pet (many) → PetType (many-to-one)
- Pet (many) → Owner (many-to-one)
- Pet (1) → Visit (one-to-many)

## Validation Rules

- Gender must be one of: MALE, FEMALE, UNKNOWN
- If gender is null during create/update, default to UNKNOWN
- No null constraint at DB level; application defaults to UNKNOWN

## State Transitions

Not applicable - Gender is a simple field, not a state machine.

## Database Schema Changes

### pets table

```sql
-- Add to H2, MySQL, PostgreSQL schemas
ALTER TABLE pets ADD COLUMN gender VARCHAR(20) DEFAULT 'UNKNOWN';
```

Or for new table creation:

```sql
CREATE TABLE pets (
  ...
  gender VARCHAR(20) DEFAULT 'UNKNOWN'
);
```

## Default Values

| Field | Default | Applied When |
|-------|---------|--------------|
| gender | UNKNOWN | Pet creation (when not specified) |
| gender | UNKNOWN | Legacy data migration |