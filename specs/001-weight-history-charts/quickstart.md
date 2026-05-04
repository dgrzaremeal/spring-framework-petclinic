# Quickstart: Weight History & Progress Charts

## Prerequisites

- JDK 17+
- Maven 3.8+ (or use the included `mvnw` wrapper — no system Maven needed)
- Git

## Run the Application

```bash
./mvnw jetty:run-war
```

Open http://localhost:9966/petclinic/ in your browser.

## Verify the Feature

1. Navigate to **Find Owners** and open any owner's profile.
2. Click on a pet to open its profile.
3. Click **Add Weight Record** — enter a weight (e.g., `4.5`) and a date (today or earlier).
4. Submit the form. You are redirected to the weight history page.
5. Add a second record. The line chart appears above the history table.

## Run Tests

```bash
# All tests (default H2 profile)
./mvnw test

# JPA profile
./mvnw test -P jpa

# JDBC profile
./mvnw test -P jdbc

# Spring Data JPA profile
./mvnw test -P spring-data-jpa
```

All tests must pass against H2 before any PR is merged (Constitution Principle IV).

## Switch Persistence Implementation

The active persistence layer is controlled by a Spring profile. Set it in your IDE run configuration or via Maven:

```bash
# JPA (default)
./mvnw jetty:run-war -Dspring.profiles.active=jpa

# JDBC
./mvnw jetty:run-war -Dspring.profiles.active=jdbc

# Spring Data JPA
./mvnw jetty:run-war -Dspring.profiles.active=spring-data-jpa
```

## Compile CSS (if modifying SCSS)

```bash
./mvnw package -P css
```

Pre-compiled CSS must not be manually edited (Constitution — Technology Standards).

## Key Files for This Feature

| File | Purpose |
|------|---------|
| `src/main/java/.../model/WeightRecord.java` | Entity |
| `src/main/java/.../repository/WeightRecordRepository.java` | Repository interface |
| `src/main/java/.../repository/jpa/JpaWeightRecordRepositoryImpl.java` | JPA implementation |
| `src/main/java/.../repository/jdbc/JdbcWeightRecordRepositoryImpl.java` | JDBC implementation |
| `src/main/java/.../repository/springdatajpa/SpringDataWeightRecordRepository.java` | Spring Data JPA implementation |
| `src/main/java/.../service/ClinicService.java` | Service interface (modified) |
| `src/main/java/.../service/ClinicServiceImpl.java` | Service implementation (modified) |
| `src/main/java/.../web/WeightRecordController.java` | MVC controller |
| `src/main/webapp/WEB-INF/jsp/pets/weightRecordList.jsp` | List + chart view |
| `src/main/webapp/WEB-INF/jsp/pets/createWeightRecordForm.jsp` | Add weight form |
| `src/main/resources/db/h2/schema.sql` | H2 schema (modified) |
| `pom.xml` | Chart.js WebJar added |

## Chart.js WebJar

The chart is rendered using Chart.js, declared as a WebJar in `pom.xml`:

```xml
<dependency>
    <groupId>org.webjars.npm</groupId>
    <artifactId>chart.js</artifactId>
    <version>4.5.0</version>
</dependency>
```

Referenced in JSP via the WebJars servlet mapping:

```jsp
<script src="${pageContext.request.contextPath}/webjars/chart.js/4.5.0/dist/chart.umd.js"></script>
```
