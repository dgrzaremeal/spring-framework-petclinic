# UI Contracts: Weight History & Progress Charts

This project is a server-rendered Spring MVC web application. The "contracts" are the HTTP endpoints exposed to the browser and the view model (model attributes) each endpoint provides to its JSP template.

---

## Endpoint: List Weight History + Chart

**URL**: `GET /owners/{ownerId}/pets/{petId}/weights`

**Access**: Pet owner (own pets only) or clinic staff (all pets)

### Path Variables

| Variable | Type | Description |
|----------|------|-------------|
| `ownerId` | `int` | ID of the pet's owner |
| `petId` | `int` | ID of the pet |

### Model Attributes

| Attribute | Type | Description |
|-----------|------|-------------|
| `pet` | `Pet` | The pet whose weight history is shown |
| `weightRecords` | `List<WeightRecord>` | All weight records ordered by `measurementDate` ASC |

### View

`WEB-INF/jsp/pets/weightRecordList.jsp`

### Behaviour

- If `weightRecords` is empty → display "No weight history recorded yet." message
- If `weightRecords` has 1 entry → display single data point; chart shows point with note "Add more records to see a trend"
- If `weightRecords` has 2+ entries → display line chart (Chart.js) + tabular list below

### Error Cases

| Condition | Response |
|-----------|----------|
| `petId` not found | 404 (Spring MVC default) |
| `ownerId` does not own `petId` | 404 (controller validates ownership) |

---

## Endpoint: Show Add Weight Record Form

**URL**: `GET /owners/{ownerId}/pets/{petId}/weights/new`

**Access**: Pet owner (own pets only) or clinic staff (all pets)

### Path Variables

| Variable | Type | Description |
|----------|------|-------------|
| `ownerId` | `int` | ID of the pet's owner |
| `petId` | `int` | ID of the pet |

### Model Attributes

| Attribute | Type | Description |
|-----------|------|-------------|
| `pet` | `Pet` | The pet for which a weight record is being added |
| `weightRecord` | `WeightRecord` | Empty/pre-populated form-backing object (date defaults to today) |

### View

`WEB-INF/jsp/pets/createWeightRecordForm.jsp`

---

## Endpoint: Save Weight Record

**URL**: `POST /owners/{ownerId}/pets/{petId}/weights/new`

**Access**: Pet owner (own pets only) or clinic staff (all pets)

### Path Variables

| Variable | Type | Description |
|----------|------|-------------|
| `ownerId` | `int` | ID of the pet's owner |
| `petId` | `int` | ID of the pet |

### Form Parameters (bound to `WeightRecord`)

| Parameter | Type | Validation |
|-----------|------|------------|
| `weightKg` | `BigDecimal` | Required; 0.001–250.0 |
| `measurementDate` | `LocalDate` (format `yyyy/MM/dd`) | Required; not in the future |

### Success Response

Redirect to `GET /owners/{ownerId}/pets/{petId}/weights`

### Validation Failure Response

Re-display `createWeightRecordForm.jsp` with `BindingResult` errors

### Error Cases

| Condition | Response |
|-----------|----------|
| `weightKg` missing or ≤ 0 | Form re-displayed with field error |
| `weightKg` > 250 | Form re-displayed with field error |
| `measurementDate` in the future | Form re-displayed with field error |
| `measurementDate` missing | Form re-displayed with field error |

---

## Chart Data Contract (inline, not a separate API)

The chart data is embedded in `weightRecordList.jsp` as inline JavaScript arrays, rendered server-side by JSTL/EL. No separate JSON endpoint is exposed.

```javascript
// Rendered inline in weightRecordList.jsp
const weightLabels = [<c:forEach items="${weightRecords}" var="r" varStatus="s">
    "${r.measurementDate}"<c:if test="${!s.last}">,</c:if>
</c:forEach>];

const weightData = [<c:forEach items="${weightRecords}" var="r" varStatus="s">
    ${r.weightKg}<c:if test="${!s.last}">,</c:if>
</c:forEach>];
```

**Chart type**: Line chart (Chart.js `type: 'line'`)  
**X axis**: `measurementDate` (ISO date string, displayed as-is)  
**Y axis**: `weightKg` (decimal, labelled "Weight (kg)")  
**Minimum data points for chart**: 2 (single-point case shows message instead of trend line)
