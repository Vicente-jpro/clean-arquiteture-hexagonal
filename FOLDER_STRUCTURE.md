# Folder Structure

This document describes the folder structure of the Product Management application following Clean Architecture and Hexagonal Architecture principles.

## Root Level
```
product-management/
├── domain/              # Core domain module
├── application/         # Application use cases
├── infrastructure/      # Infrastructure adapters
├── pom.xml              # Parent POM file
├── docker-compose.yml   # Docker Compose configuration
├── Dockerfile           # Multi-stage Docker build
└── README.md            # Project documentation
```

## Domain Module (`domain/`)

The Domain module is the core of the application and contains business logic with no external dependencies.

```
domain/
├── pom.xml
└── src/main/java/com/hexagonal/domain/
    ├── model/
    │   └── Product.java                    # Core Product entity
    ├── port/
    │   ├── in/
    │   │   └── ProductUseCase.java         # Input port (interface)
    │   └── out/
    │       ├── ProductRepository.java       # Output port (interface)
    │       └── ProductEventPublisher.java   # Output port (interface)
    └── exception/
        ├── ProductNotFoundException.java
        └── InvalidProductException.java
```

**Key Components:**
- `model/Product.java`: Domain entity with business rules
- `port/in/ProductUseCase.java`: Defines use case operations
- `port/out/ProductRepository.java`: Repository interface (to be implemented by infrastructure)
- `port/out/ProductEventPublisher.java`: Event publisher interface (to be implemented by infrastructure)

## Application Module (`application/`)

The Application module implements use cases and orchestrates business logic.

```
application/
├── pom.xml
└── src/main/java/com/hexagonal/application/
    └── service/
        └── ProductService.java             # Implements ProductUseCase
```

**Key Components:**
- `service/ProductService.java`: Implements business use cases, coordinates domain objects and ports

## Infrastructure Module (`infrastructure/`)

The Infrastructure module contains all adapters and framework-specific code.

```
infrastructure/
├── pom.xml
└── src/
    ├── main/java/com/hexagonal/infrastructure/
    │   ├── adapter/
    │   │   ├── rest/
    │   │   │   ├── ProductController.java           # REST API controller
    │   │   │   ├── ProductRequestDto.java           # Request DTO
    │   │   │   ├── ProductResponseDto.java          # Response DTO
    │   │   │   ├── ProductRestMapper.java           # DTO to Domain mapper
    │   │   │   └── GlobalExceptionHandler.java      # Error handling
    │   │   ├── persistence/
    │   │   │   ├── ProductEntity.java               # JPA entity
    │   │   │   ├── SpringDataProductRepository.java # Spring Data JPA repository
    │   │   │   ├── JpaProductRepositoryAdapter.java # Implements ProductRepository port
    │   │   │   └── ProductMapper.java               # Entity to Domain mapper
    │   │   └── messaging/
    │   │       ├── ProductEventDto.java             # Kafka event DTO
    │   │       ├── KafkaProductEventPublisher.java  # Implements ProductEventPublisher port
    │   │       └── ProductEventConsumer.java        # Kafka event consumer
    │   ├── config/
    │   │   └── KafkaConfig.java                     # Kafka configuration
    │   └── ProductManagementApplication.java        # Spring Boot main class
    └── main/resources/
        └── application.yml                          # Application configuration
```

**Key Components:**

### REST Adapter (`adapter/rest/`)
- Exposes HTTP endpoints for CRUD operations
- Handles HTTP requests/responses
- Validates input
- Handles exceptions

### Persistence Adapter (`adapter/persistence/`)
- Implements database access using JPA
- Maps between domain model and JPA entities
- Implements ProductRepository port

### Messaging Adapter (`adapter/messaging/`)
- Publishes events to Kafka
- Consumes events from Kafka
- Implements ProductEventPublisher port

### Configuration (`config/`)
- Spring Boot configuration classes
- Kafka topic configuration

## Dependencies Flow

```
infrastructure -> application -> domain
```

- **infrastructure** depends on **application** and **domain**
- **application** depends on **domain**
- **domain** has NO external dependencies

This ensures the domain layer remains pure and framework-agnostic.

## Hexagonal Architecture Visualization

```
                    ┌─────────────────────┐
                    │   REST Controller   │
                    │   (REST Adapter)    │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │   ProductUseCase    │
                    │  (Input Port)       │
                    └──────────┬──────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
        │           ┌──────────▼──────────┐          │
        │           │   ProductService    │          │
        │           │  (Use Case Impl)    │          │
        │           └──────────┬──────────┘          │
        │                      │                      │
┌───────▼────────┐    ┌────────▼──────┐    ┌────────▼────────┐
│ ProductRepo    │    │    Product    │    │  EventPublisher │
│ (Output Port)  │    │  (Domain)     │    │  (Output Port)  │
└───────┬────────┘    └───────────────┘    └────────┬────────┘
        │                                            │
┌───────▼────────┐                          ┌───────▼────────┐
│  JPA Adapter   │                          │  Kafka Adapter │
│  (Persistence) │                          │  (Messaging)   │
└────────┬───────┘                          └────────┬───────┘
         │                                           │
┌────────▼───────┐                          ┌───────▼────────┐
│   PostgreSQL   │                          │     Kafka      │
└────────────────┘                          └────────────────┘
```

## Maven Module Structure

```xml
<!-- Parent POM -->
<modules>
    <module>domain</module>
    <module>application</module>
    <module>infrastructure</module>
</modules>
```

Each module has its own `pom.xml` with appropriate dependencies, ensuring clean separation.
