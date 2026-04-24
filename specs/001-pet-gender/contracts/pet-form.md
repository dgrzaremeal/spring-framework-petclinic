# Contracts: Add Gender Enum to Pet

## Interface Contracts

This project exposes web interfaces for pet management. The feature adds gender selection to existing forms.

## Web Interface: Pet Form

### Create/Edit Pet Form

**Endpoint**: `POST /owners/{ownerId}/pets/new` and `POST /owners/{ownerId}/pets/{petId}/edit`

**Request Body** (form fields):
- `name`: String (required)
- `birthDate`: String "yyyy/MM/dd" (optional)
- `type.id`: Integer (required)
- `gender`: String ("MALE", "FEMALE", "UNKNOWN") - NEW field

**Response**: Redirect to Owner details page

### Form Fields Added

| Field | Type | Values | Default |
|-------|------|--------|---------|
| gender | Select | MALE, FEMALE, UNKNOWN | UNKNOWN |

## Filter Interface: Pet List

**Endpoint**: `GET /owners/{ownerId}/pets` with query parameter

**Query Parameters** (added for filtering):
- `gender`: String (optional) - Filter by gender value

**Response**: HTML page with filtered pet list

## Service Interface Changes

### ClinicService (unchanged signatures)

The interface remains unchanged. Implementation handles default UNKNOWN gender at entity level.

### Repository Interface Changes (NEW methods)

| Method | Description |
|--------|-------------|
| `findByGender(Gender gender)` | Find all pets with given gender |

## Database Schema Changes

### Pets Table

```sql
ALTER TABLE pets ADD gender VARCHAR(20);
```

Nullable column - existing records will have NULL gender, application defaults to UNKNOWN.