# Tech Spec

## Current Stack

| Component | Version |
|-----------|---------|
| Java | 17 |
| Spring Framework | 7.0.6 |
| Build Tool | Maven |
| Packaging | WAR |
| Views | JSP + JSTL |
| Persistence | JPA / JDBC / Spring Data JPA (selectable) |
| Hibernate | 7.3.0 |
| Database | H2 (default), MySQL, PostgreSQL |
| Server | Jetty 11 / Tomcat 11 |
| Cache | Caffeine |
| Testing | JUnit 6, AssertJ, Mockito |

## Architecture

- 3-layer: Presentation (JSP) → Service → Repository
- XML-based Spring configuration
- In-memory caching with Caffeine

## Areas for Modernization (TBD)

> **Note**: These gaps were identified by comparing against the canonical Spring Boot + Thymeleaf implementation and modern Spring best practices. Priorities TBD.

- Configuration (XML → Java)
- View layer (JSP → Thymeleaf)
- Security
- Observability
- Build optimization