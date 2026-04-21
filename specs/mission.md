# Mission

## Overview

The Spring PetClinic sample application is a classic demonstration of Spring Framework capabilities, showcasing a 3-layer architecture (presentation → service → repository). Approved by the Spring team, this project serves as a reference implementation for developers learning Spring or evaluating different architectural approaches.

## Purpose

- Demonstrate **plain Spring Framework** configuration (without Spring Boot)
- Showcase a **3-layer architecture** with clear separation of concerns
- Provide multiple **persistence layer implementations** (JPA, JDBC, Spring Data JPA) for comparison
- Serve as a testing ground for Spring framework improvements

## Relationship to Other Forks

This is one of several Spring PetClinic forks, each demonstrating different technology stacks:
- **spring-projects/spring-petclinic** (canonical): Spring Boot + Thymeleaf + aggregate-oriented domain
- **spring-framework-petclinic** (this): Plain Spring Framework + XML config + JSP

## Goals

- Maintain as a reference implementation of traditional Spring Framework patterns
- Modernize and fix bugs while preserving educational value
- Support enterprise teams using this as a learning/reference codebase
- Provide working examples of Spring MVC, Spring Data, Hibernate, and caching

## Key Features

- JSP-based views with custom tags
- XML-based Spring configuration
- Switchable persistence implementations via Spring profiles
- In-memory caching with Caffeine
- Docker support with Google Jib
- CI/CD with GitHub Actions
- Code quality tracking via SonarCloud