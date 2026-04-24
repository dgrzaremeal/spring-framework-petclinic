🟢 LOW DIFFICULTY

# Feature Description

1 Pet photo Add a photoUrl field or photo (BLOB) to Pet. Basic CRUD.

2 Microchip ID microchipId field in Pet + unique validation. Official identification.

3 Color/Breed Add color and breed as simple fields in Pet.

4 Active/inactive status active field (boolean) to mark deceased or transferred pets.

5 Pet weight weight field (decimal) with optional history.

6 Owner notes notes field (text) in Pet for free-form observations.

7 Gender/Sex Gender enum (MALE, FEMALE, UNKNOWN) in Pet.

---

🟡 MEDIUM DIFFICULTY

# Feature Description

8 Weight history Separate WeightRecord entity for tracking progress. Charts.

9 Vet-Visit relationship Assign a veterinarian to each Visit. Visit.vet (ManyToOne).

10 Pet transfer Flow to change the owner of an existing pet (with history).

11 Attached medical file MedicalDocument entity for PDFs/photos of studies.uploads.

12 Advanced pet search Filters by type, age, breed, active/inactive. Dedicated endpoint.

13 Labels/Tags Free tag system (PetTag) to categorize pets.

---

🔴 HIGH DIFFICULTY (1)

# Functional Definition: Vaccination Management System

## 1. Overview

The vaccination management system allows the veterinary clinic to administer the complete vaccination lifecycle of pets: from catalog and schedule configuration, to application recordkeeping and follow-up of upcoming doses through a reminders dashboard.

---

## 2. Vaccine Catalog (Vaccine)

### 2.1 Entity

| Attribute | Type | Description | Constraints |
| - | - | - | - |
| id | Integer | Unique identifier | PK, auto-generated |
| name | String(80) | Commercial or generic name | Required, unique |
| manufacturer | String(100) | Manufacturer laboratory | Optional |
| description | String(255) | Vaccine description | Optional |
| immunityDays | Integer | Number of immunity days provided | Required, > 0 |

### 2.2 Functionalities

| Functionality | Description |
| - | - |
| **List vaccines** | Show all vaccines in the catalog in a table with columns: name, manufacturer, immunity days |
| **Create vaccine** | Form with validation: unique name, required immunity days |
| **Edit vaccine** | Modify any field (except id) |
| **Delete vaccine** | Only if it has no application records or associated schedules. Show warning if it has dependencies |
| **View detail** | View with complete information + associated schedules + usage statistics |

### 2.3 Business Validations

- name must be unique in the system
- immunityDays must be greater than 0
- A vaccine cannot be deleted if:
    - It has application records (VaccinationRecord)
    - It has configured schedules (VaccinationSchedule)

---

## 3. Vaccination Schedules (VaccinationSchedule)

### 3.1 Entity

| Attribute | Type | Description | Constraints |
| - | - | - | - |
| id | Integer | Unique identifier | PK, auto-generated |
| vaccine | Vaccine | Reference to the vaccine | FK, required |
| petType | PetType | Pet type (cat, dog, etc.) | FK, required |
| minAgeDays | Integer | Minimum age in days for the first dose | Required, >= 0 |
| boosterDays | Integer | Interval in days for boosters | Required, > 0 (0 = single dose) |
| mandatory | Boolean | Whether it is mandatory for that pet type | Required, default false |
| notes | String(255) | Additional notes | Optional |

### 3.2 Functionalities

| Functionality | Description |
| - | - |
| **List schedules** | View grouped by pet type. Example: |
| | **Dogs:** Rabies (4 months, yearly), Parvovirus (6 weeks, yearly)... |
| | **Cats:** Rabies (4 months, yearly), FVRCP (8 weeks, yearly)... |
| **Create schedule** | Select vaccine + pet type + configure parameters |
| **Edit schedule** | Modify minimum age, booster interval, mandatory flag |
| **Delete schedule** | Delete without restrictions (does not affect historical records) |
| **Duplicate schedule** | Option to quickly create a schedule similar to an existing one |

### 3.3 Business Validations

- The combination (vaccine, petType) must be unique (a vaccine cannot have 2 schedules for the same type)
- minAgeDays >= 0 (0 = can be applied at birth)
- boosterDays > 0 (use 0 or null to indicate "single dose without booster")

### 3.4 Schedule View by Pet Type

┌─────────────────────────────────────────────────────────────────────────────┐
│  VACCINATION SCHEDULE - DOGS (dog)                                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Vaccine          │ Minimum age │ Booster     │ Req.   │ Actions          │
│  ─────────────────┼─────────────┼─────────────┼────────┼───────────────────│
│  Rabies           │ 4 months    │ Yearly      │   ✓    │ \[Edit] \[Delete] │
│  Parvovirus       │ 6 weeks     │ Yearly      │   ✓    │ \[Edit] \[Delete] │
│  Distemper        │ 6 weeks     │ Yearly      │   ✓    │ \[Edit] \[Delete] │
│  Leptospirosis    │ 8 weeks     │ Yearly      │   ✗    │ \[Edit] \[Delete] │
│  Bordetella       │ 8 weeks     │ Semiannual  │   ✗    │ \[Edit] \[Delete] │
│                                                                             │
│  \[+ Add vaccine to dog schedule]                                           │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

---

## 4. Vaccination Record (VaccinationRecord)

### 4.1 Entity

| Attribute | Type | Description | Constraints |
| - | - | - | - |
| id | Integer | Unique identifier | PK, auto-generated |
| pet | Pet | Vaccinated pet | FK, required |
| vaccine | Vaccine | Applied vaccine | FK, required |
| vet | Vet | Veterinarian who administered the vaccine | FK, required |
| dateAdministered | LocalDate | Administration date | Required, default today |
| lotNumber | String(50) | Vial lot number | Optional |
| nextDueDate | LocalDate | Next dose date | Automatically calculated |
| notes | String(255) | Clinical observations | Optional |

### 4.2 Functionalities

| Functionality | Description |
| - | - |
| **Register vaccination** | From the pet record, form with: vaccine, vet, date, lot, notes |
| **View history** | Reverse chronological list of all vaccines applied to a pet |
| **Edit record** | Only fields: lotNumber, notes, nextDueDate (do not change vaccine or date) |
| **Delete record** | With confirmation. Allowed to correct errors |
| **Calculate next dose** | Automatic: dateAdministered + vaccine.immunityDays |

### 4.3 Automatic Calculation of nextDueDate

nextDueDate = dateAdministered + vaccine.immunityDays days

Example:

- Rabies vaccine applied on 2024-01-15

- immunityDays = 365

- nextDueDate = 2025-01-15

### 4.4 Business Validations

- dateAdministered cannot be in the future
- dateAdministered cannot be earlier than the pet's birth date
- If a record already exists for the same vaccine and the same pet with a date difference of < immunityDays, show warning:  
  "This pet already received this vaccine X days ago. Do you still want to register it?"

### 4.5 Registration Flow


┌─────────────────────────────────────────────────────────────────────────────┐
│  REGISTER VACCINATION - \[Pet name]                                         │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Vaccine:           \[▼ Select vaccine                   ]                  │
│                    ┌─ Rabies                            ─┐                  │
│                    │  Parvovirus                        │                  │
│                    │  Distemper                         │                  │
│                    │  FVRCP                             │                  │
│                    └────────────────────────────────────┘                  │
│                                                                             │
│  Veterinarian:     \[▼ Select vet                     ]                    │
│                                                                             │
│  Application date: \[📅 2024-01-15                    ]                    │
│                                                                             │
│  Lot number:       \[\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_] (optional)                 │
│                                                                             │
│  Notes:            \[\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_]                    │
│                    \[\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_]                    │
│                                                                             │
│  ────────────────────────────────────────────────────────────────────────   │
│  ⓘ Calculated next dose: 2025-01-15 (in 365 days)                          │
│                                                                             │
│           \[Cancel]                                \[Register]              │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

---

## 5. Vaccination Dashboard

### 5.1 Purpose

Centralized view so clinic staff can quickly identify pets requiring vaccination attention.

### 5.2 Available Filters

| Filter | Values | Behavior |
| - | - | - |
| **Status** | Overdue, Upcoming (7 days), Upcoming (30 days), Up to date | Multiple selection |
| **Pet type** | cat, dog, bird, etc. | Multiple selection |
| **Veterinarian** | List of vets | Single selection (to view their patients) |
| **Owner** | Search by last name | Autocomplete |

### 5.3 Classification Criteria

| Status | Criteria | Color/Icon |
| - | - | - |
| **Overdue** | nextDueDate < today | Red |
| **Upcoming** | today <= nextDueDate <= today + N days | Yellow |
| **Up to date** | nextDueDate > today + 30 days | Green |
| **No vaccines** | No vaccination records | Gray |

### 5.4 Dashboard View


┌─────────────────────────────────────────────────────────────────────────────┐
│  VACCINATION DASHBOARD                                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Filters: \[Status ▼] \[Pet type ▼] \[Veterinarian ▼] \[Owner...           ]│
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ SUMMARY                                                              │   │
│  │ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐                │   │
│  │ │ 🔴 12    │ │ 🟡 8     │ │ 🟢 45    │ │ ⚪ 3     │                │   │
│  │ │ Overdue  │ │ Upcoming │ │ Up to date│ │No vacc. │                │   │
│  │ └──────────┘ └──────────┘ └──────────┘ └──────────┘                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
│  DETAIL OF OVERDUE AND UPCOMING VACCINES                                    │
│  ─────────────────────────────────────────────────────────────────────────  │
│                                                                             │
│  Pet       │ Owner         │ Vaccine  │ Due date  │ Days │ Actions       │
│  ──────────┼───────────────┼──────────┼───────────┼──────┼────────────────│
│  🔴 Max    │ George Frank  │ Rabies   │ 2024-01-  │ -15  │ \[Register]    │
│  🔴 Leo    │ Betty Davis   │ Parvo    │ 2024-01-  │ -10  │ \[Register]    │
│  🟡 Rosy   │ Eduar Rodriguez│ Rabies  │ 2024-02-  │  5   │ \[View]        │
│  🟡 Jewel  │ Eduar Rodriguez│ Rabies  │ 2024-02-  │  7   │ \[View]        │
│                                                                             │
│  \[View all pets up to date →]                                              │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

### 5.5 Dashboard Functionalities

| Functionality | Description |
| - | - |
| **Visual summary** | Cards with counters by status (clickable for filtering) |
| **Prioritized list** | Ordered by urgency (overdue first, then closest upcoming) |
| **Quick action** | "Register" button takes the user directly to the vaccination form |
| **Export** | Option to download CSV with filtered pets |
| **Refresh** | Recalculated on each page load (no cache) |

---

## 6. Integration in Pet Record

### 6.1 Vaccines Section in Pet Detail


┌─────────────────────────────────────────────────────────────────────────────┐
│  PET RECORD - Max                                                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Owner: George Franklin    Type: dog    Birth: 2012-09-04                  │
│                                                                             │
│  ═══════════════════════════════════════════════════════════════════════   │
│                                                                             │
│  VACCINES                                           Status: Up to date     │
│  ────────────────────────────────────────────────────────────────────────   │
│                                                                             │
│  Vaccine   │ Last dose    │ Next dose     │ Veterinarian │ Lot            │
│  ──────────┼──────────────┼───────────────┼──────────────┼────────────────│
│  Rabies    │ 2024-01-15   │ 2025-01-15    │ H. Leary     │ RAB-2024-012   │
│  Parvo     │ 2024-02-20   │ 2025-02-20    │ R. Ortega    │ PAR-2024-045   │
│  Distemper │ 2024-02-20   │ 2025-02-20    │ R. Ortega    │ PAR-2024-045   │
│                                                                             │
│  \[+ Register vaccine]  \[View full history]                                │
│                                                                             │
│  ═══════════════════════════════════════════════════════════════════════   │
│                                                                             │
│  VISITS                                                                     │
│  ────────────────────────────────────────────────────────────────────────   │
│  ...                                                                        │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

### 6.2 Suggested Schedule

When a pet does not yet have all the vaccines from its schedule:


┌─────────────────────────────────────────────────────────────────────────────┐
│  PENDING VACCINES ACCORDING TO SCHEDULE                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ⚠ Leptospirosis - Recommended for dogs. Apply from 8 weeks onward.       │
│  ⚠ Bordetella - Optional. Recommended if the dog lives with others.       │
│                                                                             │
│  \[Register Leptospirosis]  \[Mark as not applicable]                       │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

### 6.3 Vaccination Status

Conspicuous visual indicator with different statuses:

| Status | Icon | Color | Tooltip |
| - | - | - | - |
| Up to date | ✓ | Green | "All vaccines are up to date" |
| Due soon | ⚠ | Yellow | "X vaccines are due soon" |
| Overdue | ✗ | Red | "X vaccines are overdue" |
| No vaccines | ○ | Gray | "No vaccination records" |

---

## 7. Complete Data Model

### 7.1 Entity-Relationship Diagram


&#x20;                                   ┌─────────────────┐
&#x20;                                   │     Vaccine     │
&#x20;                                   ├─────────────────┤
&#x20;                                   │ PK id           │
&#x20;                                   │    name         │
&#x20;                                   │    manufacturer │
&#x20;                                   │    description  │
&#x20;                                   │    immunityDays │
&#x20;                                   └────────┬────────┘
&#x20;                                            │
&#x20;                       ┌────────────────────┼────────────────────┐
&#x20;                       │                    │                    │
&#x20;                       │ 1:N                │                    │ 1:N
&#x20;                       ▼                    │                    ▼
&#x20;           ┌─────────────────────┐          │          ┌────────────────────┐
&#x20;           │ VaccinationSchedule │          │          │ VaccinationRecord  │
&#x20;           ├─────────────────────┤          │          ├────────────────────┤
&#x20;           │ PK id               │          │          │ PK id              │
&#x20;           │ FK vaccine\_id       │──────────┘          │ FK vaccine\_id      │
&#x20;           │ FK pet\_type\_id      │                     │ FK pet\_id          │
&#x20;           │    minAgeDays       │                     │ FK vet\_id          │
&#x20;           │    boosterDays      │                     │    dateAdministered│
&#x20;           │    mandatory        │                     │    lotNumber       │
&#x20;           │    notes            │                     │    nextDueDate     │
&#x20;           └──────────┬──────────┘                     │    notes           │
&#x20;                      │                                └────────┬───────────┘
&#x20;                      │ N:1                                     │
&#x20;                      ▼                                         │
&#x20;           ┌─────────────────┐                                  │
&#x20;           │     PetType     │                                  │
&#x20;           ├─────────────────┤                                  │
&#x20;           │ PK id           │                                  │
&#x20;           │    name         │◀─────────────────┐               │
&#x20;           └─────────────────┘                  │               │
&#x20;                                                  │               │
&#x20;           ┌─────────────────┐                  │               │
&#x20;           │       Pet       │──────────────────┘               │
&#x20;           ├─────────────────┤                  N:1             │
&#x20;           │ PK id           │                                  │
&#x20;           │ FK type\_id      │                                  │
&#x20;           │ FK owner\_id     │                                  │
&#x20;           │    name         │◀─────────────────────────────────┘
&#x20;           │    birthDate    │                                  N:1
&#x20;           └────────┬────────┘
&#x20;                    │
&#x20;                    │ N:1
&#x20;                    ▼
&#x20;           ┌─────────────────┐
&#x20;           │      Owner      │
&#x20;           ├─────────────────┤
&#x20;           │ PK id           │
&#x20;           │    firstName    │
&#x20;           │    lastName     │
&#x20;           │    ...          │
&#x20;           └─────────────────┘


&#x20;           ┌─────────────────┐
&#x20;           │       Vet       │
&#x20;           ├─────────────────┤
&#x20;           │ PK id           │
&#x20;           │    firstName    │
&#x20;           │    lastName     │
&#x20;           └─────────────────┘

### 7.2 SQL Schema


\-- Table: vaccines

CREATE TABLE vaccines (
&#x20;   id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
&#x20;   name         VARCHAR(80) NOT NULL UNIQUE,
&#x20;   manufacturer VARCHAR(100),
&#x20;   description  VARCHAR(255),
&#x20;   immunity\_days INTEGER NOT NULL CHECK (immunity\_days > 0)
);



\-- Table: vaccination\_schedules  

CREATE TABLE vaccination\_schedules (
&#x20;   id            INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
&#x20;   vaccine\_id    INTEGER NOT NULL,
&#x20;   pet\_type\_id   INTEGER NOT NULL,
&#x20;   min\_age\_days  INTEGER NOT NULL DEFAULT 0,
&#x20;   booster\_days  INTEGER NOT NULL DEFAULT 365,
&#x20;   mandatory     BOOLEAN NOT NULL DEFAULT FALSE,
&#x20;   notes         VARCHAR(255),
&#x20;   CONSTRAINT fk\_schedule\_vaccine FOREIGN KEY (vaccine\_id) REFERENCES vaccines(id),
&#x20;   CONSTRAINT fk\_schedule\_pet\_type FOREIGN KEY (pet\_type\_id) REFERENCES types(id),
&#x20;   CONSTRAINT uq\_schedule\_vaccine\_pet\_type UNIQUE (vaccine\_id, pet\_type\_id)
);



\-- Table: vaccination\_records

CREATE TABLE vaccination\_records (
&#x20;   id               INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
&#x20;   pet\_id           INTEGER NOT NULL,
&#x20;   vaccine\_id       INTEGER NOT NULL,
&#x20;   vet\_id           INTEGER NOT NULL,
&#x20;   date\_administered DATE NOT NULL,
&#x20;   lot\_number       VARCHAR(50),
&#x20;   next\_due\_date    DATE,
&#x20;   notes            VARCHAR(255),
&#x20;   CONSTRAINT fk\_record\_pet FOREIGN KEY (pet\_id) REFERENCES pets(id),
&#x20;   CONSTRAINT fk\_record\_vaccine FOREIGN KEY (vaccine\_id) REFERENCES vaccines(id),
&#x20;   CONSTRAINT fk\_record\_vet FOREIGN KEY (vet\_id) REFERENCES vets(id),
&#x20;   CONSTRAINT ck\_date\_not\_future CHECK (date\_administered <= CURRENT\_DATE)
);



CREATE INDEX idx\_vaccination\_records\_pet ON vaccination\_records(pet\_id);

CREATE INDEX idx\_vaccination\_records\_next\_due ON vaccination\_records(next\_due\_date);

CREATE INDEX idx\_vaccination\_records\_vet ON vaccination\_records(vet\_id);

---

## 8. Service API (ClinicService)

### 8.1 Methods to Add


// === Vaccine ===

Vaccine findVaccineById(int id);

Collection<Vaccine> findAllVaccines();

Vaccine saveVaccine(Vaccine vaccine);

void deleteVaccine(Vaccine vaccine);



// === VaccinationSchedule ===

VaccinationSchedule findScheduleById(int id);

Collection<VaccinationSchedule> findAllSchedules();

Collection<VaccinationSchedule> findSchedulesByPetType(PetType petType);

VaccinationSchedule saveSchedule(VaccinationSchedule schedule);

void deleteSchedule(VaccinationSchedule schedule);



// === VaccinationRecord ===

VaccinationRecord findRecordById(int id);

Collection<VaccinationRecord> findRecordsByPetId(int petId);

Collection<VaccinationRecord> findRecordsByVetId(int vetId);

VaccinationRecord saveRecord(VaccinationRecord record);

void deleteRecord(VaccinationRecord record);



// === Dashboard / Queries ===

Collection<VaccinationRecord> findOverdueVaccinations();

Collection<VaccinationRecord> findUpcomingVaccinations(int daysAhead);

Collection<Pet> findPetsWithoutVaccinations();

Collection<Pet> findPetsWithOverdueVaccinations();



// === Utility ===

void calculateNextDueDate(VaccinationRecord record);

boolean isVaccinationDue(Pet pet, Vaccine vaccine);

Collection<VaccinationSchedule> getApplicableSchedules(Pet pet);

Collection<Vaccine> getMissingVaccines(Pet pet);

---

## 9. Controllers and Routes

### 9.1 URL Mapping

| URL | Method | Controller | View |
| - | - | - | - |
| /vaccines | GET | VaccineController | vaccines/list |
| /vaccines/new | GET | VaccineController | vaccines/form |
| /vaccines/new | POST | VaccineController | redirect:/vaccines |
| /vaccines/{id}/edit | GET | VaccineController | vaccines/form |
| /vaccines/{id}/edit | POST | VaccineController | redirect:/vaccines |
| /vaccines/{id}/delete | POST | VaccineController | redirect:/vaccines |
| | | | |
| /schedules | GET | ScheduleController | schedules/list |
| /schedules/new | GET | ScheduleController | schedules/form |
| /schedules/new | POST | ScheduleController | redirect:/schedules |
| /schedules/{id}/edit | GET | ScheduleController | schedules/form |
| /schedules/{id}/edit | POST | ScheduleController | redirect:/schedules |
| /schedules/{id}/delete | POST | ScheduleController | redirect:/schedules |
| | | | |
| /owners/{ownerId}/pets/{petId}/vaccinations | GET | VaccinationController | vaccinations/list |
| /owners/{ownerId}/pets/{petId}/vaccinations/new | GET | VaccinationController | vaccinations/form |
| /owners/{ownerId}/pets/{petId}/vaccinations/new | POST | VaccinationController | redirect:/pets/{petId} |
| /vaccinations/{id}/edit | GET | VaccinationController | vaccinations/form |
| /vaccinations/{id}/delete | POST | VaccinationController | redirect:pets/{petId} |
| | | | |
| /vaccinations/dashboard | GET | DashboardController | vaccinations/dashboard |

---

## 10. Sample Data (Seed Data)

### 10.1 Vaccines

| id | name | manufacturer | description | immunityDays |
| - | - | - | - | - |
| 1 | Rabies | Merial | Inactivated rabies vaccine | 365 |
| 2 | Canine Parvovirus | Pfizer | Protection against canine parvovirus | 365 |
| 3 | Canine Distemper | Pfizer | Protection against distemper | 365 |
| 4 | Canine Hepatitis | Merial | Canine infectious hepatitis (adenovirus) | 365 |
| 5 | Leptospirosis | Zoetis | Protection against leptospira | 365 |
| 6 | Canine Parainfluenza | Merial | Protection against parainfluenza | 365 |
| 7 | Bordetella | Zoetis | Infectious tracheobronchitis | 180 |
| 8 | FVRCP | Merial | Panleukopenia, Calicivirus, Herpesvirus | 365 |
| 9 | Feline Leukemia | Pfizer | FeLV - Feline leukemia virus | 365 |

### 10.2 Schedules by Type

**Dogs (dog, type_id=2):**

| vaccine_id | minAgeDays | boosterDays | mandatory |
| - | - | - | - |
| 1 (Rabies) | 112 (4 months) | 365 | true |
| 2 (Parvo) | 42 (6 weeks) | 365 | true |
| 3 (Distemper) | 42 (6 weeks) | 365 | true |
| 4 (Hepatitis) | 42 (6 weeks) | 365 | true |
| 5 (Lepto) | 56 (8 weeks) | 365 | false |
| 7 (Bordetella) | 56 (8 weeks) | 180 | false |

**Cats (cat, type_id=1):**

| vaccine_id | minAgeDays | boosterDays | mandatory |
| - | - | - | - |
| 1 (Rabies) | 112 (4 months) | 365 | true |
| 8 (FVRCP) | 56 (8 weeks) | 365 | true |
| 9 (Leukemia) | 56 (8 weeks) | 365 | false |

**Hamster (hamster, type_id=6):**

- No schedules (vaccination not applicable)

---

## 11. Acceptance Criteria by User Story

### US1: As a veterinarian, I want to see the vaccine catalog

- [ ] I can access /vaccines and see all vaccines in a table

- [ ] The table shows: name, manufacturer, immunity days

- [ ] I can sort by each column

- [ ] There is a button to create a new vaccine

### US2: As a veterinarian, I want to create a new vaccine

- [ ] I can access the form from the listing

- [ ] The name field is required and unique

- [ ] The immunity days field is required and must be > 0

- [ ] After saving, I see a success message and return to the listing

- [ ] If the name already exists, I see an error message

### US3: As a veterinarian, I want to configure the vaccination schedule for dogs

- [ ] I can see schedules grouped by pet type

- [ ] For "dogs" I see vaccines with minimum age and frequency

- [ ] I can add a vaccine to the dog schedule

- [ ] I can mark a vaccine as mandatory or optional

- [ ] I cannot add the same vaccine twice to the same type

### US4: As a veterinarian, I want to register a vaccine administered to a pet

- [ ] From the pet record I can access "Register vaccine"

- [ ] I can select the vaccine from a dropdown

- [ ] I can select the veterinarian who administered it

- [ ] The default date is today but I can change it

- [ ] I can enter the lot number (optional)

- [ ] After saving, the calculated next dose is shown

- [ ] The record appears in the pet's history

### US5: As a veterinarian, I want to see which vaccines are overdue or due soon

- [ ] I can access the vaccination dashboard

- [ ] I see a summary with counters: overdue, upcoming, up to date

- [ ] The list shows pets with overdue/upcoming vaccines

- [ ] I can filter by status, pet type, veterinarian

- [ ] I can click "Register" from the dashboard

### US6: As a veterinarian, I want to see a pet's vaccination history

- [ ] In the pet record there is a "Vaccines" section

- [ ] I see all vaccines administered with date and next expiration

- [ ] Vaccination status is shown with color (green/yellow/red)

- [ ] I can see which scheduled vaccines are still missing

- [ ] I can see the complete history in a separate view

---

## 12. Not Included in This Scope (Out of Scope)

| Functionality | Justification |
| - | - |
| Email/SMS notifications | Only dashboard was agreed |
| Authentication and authorization | Does not exist in the current system |
| Vaccination certificates in PDF | Possible future extension |
| Integration with laboratories | Additional complexity |
| Vaccine inventory management | Outside the clinical scope |
| Combined/polyvalent vaccines | Simplification of the initial model |
| Multiple doses per application | Each record = one vaccine |

🔴 HIGH DIFFICULTY (2)

📄 FUNCTIONAL DOCUMENT: Veterinary Shop, Stock, and Billing Module

1. Introduction and Objective

The objective of this functional evolution is to equip the Spring Petclinic application with a comprehensive inventory management module ("Veterinary Shop"). This module will allow management of the catalog of clinical and commercial products, synchronization of stock with external providers (Fake Store API) individually or in bulk, low-stock alerts, and linking consumption of these products to pet medical visits for integrated management.

2. Use Cases and Functional Flows

2.1. Local Inventory Management (CRUD)

Product Create/Edit: Employees can register the clinic's own products by defining name, description, price, initial stock, and optionally an image URL.

Smart Listing: A main view that shows all products. It includes visual validations: if an item's stock is lower than 5 units, the system will automatically mark the product as being in alert status.

Logical Deletion: Products that are no longer sold may be removed from the catalog.

2.2. Integration with External Provider (Fake Store API)

Bulk Import (Initial Catalog): To simplify startup, the system will include an "Import Catalog" button. This will automatically download the complete list of products from the external provider, saving them into the local database with image, price, and simulated stock.

Individual Synchronization: For products imported from the external provider, there will be a "Refresh Stock" action that queries the API in real time and updates the units available in the clinic.

2.3. Consumption in Medical Visits (Cross Billing)

When a veterinarian registers a new visit for a pet (e.g., vaccination, surgery), the visit form will include a new section: "Consumed/Sold Products".

The veterinarian will be able to select multiple products from the inventory.

When the visit is saved, the system will automatically subtract the consumed units from the stock_local of those products.

3. Data Model and Relational Impact

New structures will be introduced and related to the current Petclinic model.

New Table: products

---------------------------------------------------------------------

id (INT, PK)

name (VARCHAR 255): Item name.

description (VARCHAR 500): Detailed description.

price (DECIMAL 10,2): Unit price.

stock_local (INT): Quantity available in the clinic.

external_provider_id (INT, Nullable): Product ID in the external provider (used for synchronization).

image_url (VARCHAR 500, Nullable): URL of the product thumbnail.

New Join Table: visits_products (N:M Relationship)

Allows associating products with a specific medical visit.

visit_id (INT, FK referencing visits.id)

product_id (INT, FK referencing products.id)

quantity (INT): Quantity of that product consumed in the visit.

---------------------------------------------------------------------

4. Integration Contracts (Fake Store API)

To connect the system with the external provider, two operations are defined based on the Fake Store API:

A. Bulk Import Operation

Endpoint: GET https://fakestoreapi.com/products

Response: Array of JSON objects.

Functional Mapping:

id -> Saved locally as external_provider_id.

title -> name

price -> price

image -> image_url

rating.count -> Will be used to simulate and initialize stock_local.

B. Individual Stock Synchronization Operation

Endpoint: GET https://fakestoreapi.com/products/{external_provider_id}

Flow: The system extracts the value of rating.count from the JSON response and overwrites the current stock_local value in the Petclinic database for that specific item.

5. Interface Design and Mockups (UI Mockups)

The design will respect the native Thymeleaf + Bootstrap visual ecosystem of Spring Petclinic.

Mockup 1: Navigation Menu and Inventory Screen

The "Shop/Inventory" tab is added to the main menu.

================================================================================

🐾 Spring Petclinic HOME FIND OWNERS VETERINARIANS [ SHOP/INVENTORY ]

================================================================================

[ + Add New Product ] [ 🔄 Import Catalog from Provider ]
Search Product: [_______________________] [🔍 Find]

| Image | Product Name | Price | Stock Status | Actions |
| - | - | - | - | - |
| [🖼️] | Flea Collar | $15.50 | [🟢 24 units] | [Edit] [Delete] |
| [🖼️] | Amoxicillin Antibiotic | $22.00 | [🔴 3 units - LOW!] | [Edit] [Delete] |
| [🖼️] | Fjallraven Transport Bag | $109.95 | [🟢 120 units] | [Edit][Delete] [🔄 Sync API]|
| [🖼️] | Premium Dog Food | $45.00 | [🟡 0 units - EMPTY] | [Edit] [Delete] |

--------------------------------------------------------------------------------

UI Detail:

Images (image_url) will be displayed as rounded thumbnails (class="rounded-circle").

"Stock Status" will use Bootstrap badges (badge bg-success, badge bg-danger if < 5 units).

The [🔄 Sync API] button only appears if the product has an external_provider_id.

Mockup 2: Product Create/Edit Form

Maintains the same field format and validation style as the rest of the application.

================================================================================

Product Details

================================================================================

Name: [__________________________________________]

Description:[__________________________________________]

Price ($): [___________]

Initial Stock: [___________]

Image URL:[__________________________________________] (Optional)

[ < Back ] [ Save Product ]

================================================================================

Mockup 3: New Medical Visit Form (Updated)

The existing "Add Visit" view is modified to include the product selector.

================================================================================

New Visit for Pet: "Leo"

================================================================================

Date:[ 2026-04-16 ]

Description: [ General checkup and annual vaccination._____]

--- Consumed / Sold Products (Optional) ---

Select Product:[▼ Flea Collar ($15.50) (Stock: 24)] Quantity: [ 1 ] [ + Add ]


List of products applied to this visit:

1. Polyvalent Vaccine - Qty: 1 - $30.00 [x]

2. Flea Collar - Qty: 1 - $15.50 [x]

TOTAL TO BILL: $45.50

[ Cancel ][ Save Visit ]

================================================================================

UI Detail: When this visit is saved, the system will internally update the stock of "Polyvalent Vaccine" and "Flea Collar" by reducing 1 unit from the products table and saving the record in visits_products.
