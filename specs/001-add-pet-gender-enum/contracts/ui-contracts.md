# Contracts: Add Gender Enum to Pet

**Feature**: 001-add-pet-gender-enum  
**Date**: 2026-04-24

## UI Contracts

### Pet Form (createOrUpdatePetForm.jsp)

**Form Binding**: `org.springframework.samples.petclinic.model.Pet`

| Field | Input Type | Options | Required | Default |
|-------|------------|---------|----------|---------|
| name | text | 30 chars | Yes | - |
| birthDate | date | yyyy/MM/dd | No | null |
| type | select | PetType list | Yes | - |
| **gender** | **select** | **Gender list** | **No** | **UNKNOWN** |

**Gender Select Options**:
```jstl
<form:option value="MALE" label="${msg['pet.gender.male']}"/>
<form:option value="FEMALE" label="${msg['pet.gender.female']}"/>
<form:option value="UNKNOWN" label="${msg['pet.gender.unknown']}"/>
```

### Pet Details Display

**View**: `petDetails.jsp` (or similar)

| Field | Display | Format |
|-------|---------|--------|
| name | Text | Plain |
| birthDate | Text | yyyy/MM/dd |
| type | Text | type.name |
| **gender** | **Text** | **${msg[pet.gender]}/td** |

Display lookup: `pet.gender` returns full message key (e.g., "pet.gender.male")

## Internal Contracts

### Gender Enum

```java
public enum Gender {
    MALE("pet.gender.male"),
    FEMALE("pet.gender.female"),
    UNKNOWN("pet.gender.unknown");

    private final String displayKey;
    
    public String getDisplayKey();
}
```

### Pet Entity (additions)

```java
public class Pet extends NamedEntity {
    private Gender gender;
    
    public Gender getGender();
    public void setGender(Gender gender);
}
```

### JdbcPet (additions)

```java
class JdbcPet extends Pet {
    private Gender gender;
    
    @Override
    public Gender getGender();
    @Override
    public void setGender(Gender gender);
}
```

## REST API (if applicable)

This feature does not introduce new REST endpoints. Gender is handled through existing MVC controllers.

## Database Contracts

### pets table

| Column | Type | Nullable | Default |
|--------|------|----------|---------|
| gender | VARCHAR(20) | YES | 'UNKNOWN' |

Valid values: 'MALE', 'FEMALE', 'UNKNOWN'