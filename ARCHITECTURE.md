# Architecture Guide

This document explains the architectural decisions and patterns used in this Product Management application.

## Table of Contents
- [Clean Architecture](#clean-architecture)
- [Hexagonal Architecture](#hexagonal-architecture)
- [Module Dependencies](#module-dependencies)
- [Design Patterns](#design-patterns)
- [Technology Stack](#technology-stack)
- [Best Practices](#best-practices)

## Clean Architecture

Clean Architecture, introduced by Robert C. Martin (Uncle Bob), organizes code to separate concerns and make the system:
- **Independent of frameworks**: Business logic doesn't depend on Spring Boot
- **Testable**: Business rules can be tested without UI, database, or external services
- **Independent of UI**: Can easily add new interfaces (REST, GraphQL, gRPC)
- **Independent of database**: Can switch from PostgreSQL to any other database
- **Independent of external agencies**: Business rules don't know about external systems

### Layers

```
┌─────────────────────────────────────────┐
│         Infrastructure Layer            │  ← Frameworks, Drivers
│  (REST API, JPA, Kafka, Spring Boot)    │
├─────────────────────────────────────────┤
│         Application Layer               │  ← Use Cases
│       (ProductService)                  │
├─────────────────────────────────────────┤
│           Domain Layer                  │  ← Business Rules
│  (Product, Ports, Exceptions)           │
└─────────────────────────────────────────┘
```

**Dependency Rule**: Dependencies point inward. Inner layers don't know about outer layers.

## Hexagonal Architecture

Hexagonal Architecture (also known as Ports and Adapters) complements Clean Architecture by explicitly defining:

### Ports

**Ports** are interfaces that define how the application communicates with the outside world.

#### Input Ports (Driving Ports)
- Define **what** the application can do
- Located in `domain/port/in/`
- Example: `ProductUseCase` interface

```java
public interface ProductUseCase {
    Product createProduct(Product product);
    Product getProductById(Long id);
    // ... other operations
}
```

#### Output Ports (Driven Ports)
- Define **what** the application needs from external systems
- Located in `domain/port/out/`
- Examples: `ProductRepository`, `ProductEventPublisher`

```java
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    // ... other operations
}
```

### Adapters

**Adapters** are implementations that connect the application to the real world.

#### Input Adapters (Driving Adapters)
- Implementations that call the application
- Located in `infrastructure/adapter/rest/`
- Example: `ProductController` (REST API)

#### Output Adapters (Driven Adapters)
- Implementations of output ports
- Located in `infrastructure/adapter/persistence/` and `infrastructure/adapter/messaging/`
- Examples: `JpaProductRepositoryAdapter`, `KafkaProductEventPublisher`

### Hexagonal Flow

```
REST Request → ProductController → ProductUseCase (port)
                                         ↓
                                   ProductService (use case implementation)
                                         ↓
                       ┌─────────────────┴─────────────────┐
                       ↓                                   ↓
              ProductRepository (port)          ProductEventPublisher (port)
                       ↓                                   ↓
          JpaProductRepositoryAdapter           KafkaProductEventPublisher
                       ↓                                   ↓
                   PostgreSQL                            Kafka
```

## Module Dependencies

### Dependency Graph

```
infrastructure
    ├── depends on → application
    └── depends on → domain

application
    └── depends on → domain

domain
    └── depends on → nothing (pure Java)
```

### Module Responsibilities

#### Domain Module
- **No external dependencies** (except validation API)
- Contains business entities (`Product`)
- Defines ports (interfaces)
- Contains domain exceptions
- Business rules and logic

#### Application Module
- Depends on **domain** only
- Implements use cases (`ProductService`)
- Orchestrates domain objects
- Coordinates between ports
- Contains application-specific logic

#### Infrastructure Module
- Depends on **application** and **domain**
- Implements adapters (REST, JPA, Kafka)
- Spring Boot configuration
- Framework-specific code
- Entry point (`ProductManagementApplication`)

## Design Patterns

### 1. Dependency Inversion Principle (DIP)

High-level modules (domain, application) don't depend on low-level modules (infrastructure). Both depend on abstractions (ports).

```java
// High-level module defines the interface
public interface ProductRepository { ... }

// Low-level module implements it
public class JpaProductRepositoryAdapter implements ProductRepository { ... }
```

### 2. Repository Pattern

Abstracts data access logic behind a repository interface.

```java
// Domain defines what it needs
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
}

// Infrastructure provides implementation
public class JpaProductRepositoryAdapter implements ProductRepository {
    // JPA-specific implementation
}
```

### 3. Adapter Pattern

Converts one interface to another that clients expect.

```java
// REST DTO → Domain Entity
ProductRestMapper.toDomain(ProductRequestDto)

// Domain Entity → JPA Entity
ProductMapper.toEntity(Product)
```

### 4. Factory Pattern

Used by Spring to create and wire dependencies.

```java
@Service
public class ProductService implements ProductUseCase {
    private final ProductRepository repository;
    // Spring creates and injects dependencies
}
```

### 5. Strategy Pattern

Different implementations of the same port.

```java
// Could have multiple implementations
public interface ProductEventPublisher { ... }

// Kafka implementation
public class KafkaProductEventPublisher implements ProductEventPublisher { ... }

// Could also have RabbitMQ, SQS, etc.
```

## Technology Stack

### Core Technologies
- **Java 17**: Modern Java features (records, sealed classes, pattern matching)
- **Spring Boot 3.2**: Latest Spring framework with Jakarta EE
- **Maven**: Multi-module project management

### Persistence
- **PostgreSQL**: Relational database
- **Spring Data JPA**: Data access abstraction
- **Hibernate**: ORM implementation

### Messaging
- **Apache Kafka**: Event streaming platform
- **Spring Kafka**: Kafka integration for Spring

### Web
- **Spring Web**: REST API
- **Spring Validation**: Input validation
- **Spring Actuator**: Health checks and metrics

### Development
- **Lombok**: Reduce boilerplate code
- **MapStruct**: Object mapping
- **Docker**: Containerization
- **Docker Compose**: Multi-container orchestration

## Best Practices

### 1. Separation of Concerns

Each module has a single, well-defined responsibility:
- **Domain**: Business logic
- **Application**: Use cases
- **Infrastructure**: Technical details

### 2. Domain Model Purity

The domain model is framework-agnostic:
```java
// No JPA annotations
public class Product {
    private Long id;
    private String name;
    // Pure business logic
}
```

### 3. Mapping Between Layers

Use mappers to convert between representations:
- REST DTOs ↔ Domain entities
- Domain entities ↔ JPA entities

### 4. Interface Segregation

Define focused interfaces:
```java
// Separate input and output ports
ProductUseCase (input port)
ProductRepository (output port)
ProductEventPublisher (output port)
```

### 5. Dependency Injection

Use constructor injection for required dependencies:
```java
@Service
@RequiredArgsConstructor  // Lombok generates constructor
public class ProductService implements ProductUseCase {
    private final ProductRepository repository;
    private final ProductEventPublisher publisher;
}
```

### 6. Validation

Validate at boundaries:
- **REST layer**: Input validation with Bean Validation
- **Domain layer**: Business rules validation

### 7. Error Handling

- Domain exceptions for business errors
- Global exception handler for consistent error responses

### 8. Event-Driven Architecture

Publish events for important domain changes:
```java
Product saved = repository.save(product);
publisher.publishProductCreated(saved);  // Decouple concerns
```

### 9. Immutability

Use immutable DTOs where possible (Java records are great for this):
```java
public record ProductCreatedEvent(
    Long id,
    String name,
    LocalDateTime timestamp
) {}
```

### 10. Testing Strategy

- **Domain**: Pure unit tests (no mocks needed)
- **Application**: Unit tests with mocked ports
- **Infrastructure**: Integration tests with real infrastructure

## Benefits of This Architecture

1. **Flexibility**: Easy to change technologies (switch database, messaging system)
2. **Testability**: Business logic is easily testable
3. **Maintainability**: Clear structure makes code easy to understand
4. **Scalability**: Can split into microservices by module
5. **Team Productivity**: Teams can work on different modules independently
6. **Framework Independence**: Not locked into Spring Boot
7. **Business Focus**: Domain logic is separated from technical details

## Trade-offs

1. **Complexity**: More layers and abstractions
2. **Boilerplate**: More interfaces and implementations
3. **Learning Curve**: Team needs to understand the architecture
4. **Initial Development Time**: Takes longer to set up

However, these trade-offs pay off in medium to large projects with long lifespans.

## Further Reading

- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture by Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design by Eric Evans](https://www.domainlanguage.com/ddd/)
- [Building Microservices by Sam Newman](https://samnewman.io/books/building_microservices/)
