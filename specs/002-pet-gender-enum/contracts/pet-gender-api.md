# Contracts: Add Gender Enum to Pet

## External Interfaces

This project exposes a JSP-based web interface. The contracts below document the public API contracts for the Pet feature.

## Pet Entity Contract

### Java Model

```java
// New file: src/main/java/org/springframework/samples/petclinic/model/Gender.java
public enum Gender {
    MALE,
    FEMALE, 
    UNKNOWN
}

// Modified: Pet.java
public class Pet extends NamedEntity {
    // ... existing fields ...
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender = Gender.UNKNOWN;  // Default
    
    public Gender getGender() { ... }
    public void setGender(Gender gender) { ... }
}
```

### REST/Form Contract

#### Create Pet Form submission

```http
POST /owners/{ownerId}/pets
Content-Type: application/x-www-form-urlencoded

name=Max&typeId=1&birthDate=2020/01/01&gender=MALE
```

#### Update Pet Form submission

```http
POST /owners/{ownerId}/pets/{petId}
Content-Type: application/x-www-form-urlencoded

name=Max&typeId=1&birthDate=2020/01/01&gender=FEMALE
```

#### Pet Details View

The gender is displayed in the pet details page as a human-readable label:
- MALE → "Male"
- FEMALE → "Female"
- UNKNOWN → "Unknown"

### Form Field Options

For the gender dropdown/select in the JSP form:

```html
<form:select path="gender">
  <form:option label="Unknown" value="UNKNOWN"/>
  <form:option label="Male" value="MALE"/>
  <form:option label="Female" value="FEMALE"/>
</form:select>
```

### Validation

- Gender field accepts: MALE, FEMALE, UNKNOWN (case-sensitive in form submission)
- If not provided, defaults to UNKNOWN
- Required to pass valid Gender enum value

### Error Responses

- 400 Bad Request: If invalid gender value provided
- The form re-displays with validation error message