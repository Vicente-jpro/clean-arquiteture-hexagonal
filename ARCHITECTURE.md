# Architecture Documentation

## Clean Architecture & Hexagonal Architecture

This project implements a combination of **Clean Architecture** (by Robert C. Martin) and **Hexagonal Architecture** (Ports and Adapters pattern by Alistair Cockburn).

---

## Architecture Principles

### 1. Dependency Rule

Dependencies point **inward** - from infrastructure to application to domain.

```
Infrastructure → Application → Domain
```

- **Domain** has NO dependencies on any other layer
- **Application** depends only on Domain
- **Infrastructure** depends on Application and Domain
- **Bootstrap** depends on Infrastructure (entry point)

### 2. Separation of Concerns

Each layer has a clear responsibility:

- **Domain**: Business entities and rules
- **Application**: Business use cases
- **Infrastructure**: Technical implementation details
- **Bootstrap**: Application configuration and startup

---

## Layers Explained

### 🔵 Domain Layer (Core/Hexagon)

**Purpose:** Contains pure business logic and entities.

**Characteristics:**
- No framework dependencies (Plain Java)
- No external library dependencies (except Lombok for convenience)
- Contains entity models
- Defines port interfaces (contracts)
- Business validation logic

**Components:**

1. **Entities** (`domain/model/`)
   - `Product.java` - Core business entity
   - Contains business validation methods (`isValid()`, `isAvailable()`)

2. **Output Ports** (`domain/port/output/`)
   - `ProductRepository.java` - Repository interface
   - `ProductEventPublisher.java` - Messaging interface
   - These are implemented by infrastructure adapters

3. **Exceptions** (`domain/exception/`)
   - `ProductNotFoundException.java`
   - `InvalidProductException.java`

**Why This Layer is Framework-Agnostic:**
- Can be tested without Spring, database, or any infrastructure
- Business logic remains stable even if technology changes
- Easy to understand and maintain

---

### 🟢 Application Layer (Use Cases)

**Purpose:** Orchestrates business logic and use cases.

**Characteristics:**
- Depends only on Domain layer
- Contains business workflows
- No knowledge of HTTP, databases, or messaging systems
- Framework-agnostic (no Spring annotations except in configuration)

**Components:**

1. **Input Ports** (`application/port/input/`)
   - `ProductUseCase.java` - Use case interface
   - Defines operations the application supports

2. **Services** (`application/service/`)
   - `ProductService.java` - Implements use cases
   - Orchestrates domain objects and output ports
   - Contains application-level business logic

**Example Use Case Flow:**
```java
createProduct(product) {
    1. Validate product (domain logic)
    2. Save product (via repository port)
    3. Publish event (via messaging port)
    4. Return saved product
}
```

---

### 🟡 Infrastructure Layer (Adapters)

**Purpose:** Implements technical details and external system integrations.

**Characteristics:**
- Depends on Application and Domain layers
- Contains all framework-specific code
- Implements ports defined in Domain layer
- Can be easily replaced with different implementations

**Components:**

#### Input Adapters (Primary/Driving Adapters)

These **drive** the application:

1. **REST Controller** (`infrastructure/adapter/input/rest/`)
   - `ProductController.java` - HTTP endpoints
   - `ProductRequest.java` / `ProductResponse.java` - DTOs
   - `ProductRestMapper.java` - Maps DTOs to domain models
   - `GlobalExceptionHandler.java` - Error handling

**Flow:** HTTP Request → Controller → Use Case → Domain

#### Output Adapters (Secondary/Driven Adapters)

These are **driven by** the application:

1. **Persistence Adapter** (`infrastructure/adapter/output/persistence/`)
   - `ProductEntity.java` - JPA entity
   - `JpaProductRepository.java` - Spring Data repository
   - `ProductRepositoryAdapter.java` - Implements ProductRepository port
   - `ProductMapper.java` - Maps between entity and domain model

**Flow:** Use Case → Repository Port → Adapter → Database

2. **Messaging Adapter** (`infrastructure/adapter/output/messaging/`)
   - `KafkaProductEventPublisher.java` - Implements ProductEventPublisher port
   - Publishes events to Kafka

**Flow:** Use Case → Event Publisher Port → Adapter → Kafka

#### Configuration (`infrastructure/config/`)

1. **ApplicationConfig.java**
   - Wires up use cases with their dependencies
   - Creates beans for application layer

2. **KafkaConfig.java**
   - Configures Kafka topics and serialization

---

### 🟠 Bootstrap Layer (Main)

**Purpose:** Application entry point and configuration.

**Characteristics:**
- Contains `main()` method
- Spring Boot application configuration
- Brings all modules together
- Contains `application.yml` configuration

**Components:**

1. **ProductManagementApplication.java**
   - `@SpringBootApplication` annotation
   - Application entry point

2. **application.yml**
   - Database configuration
   - Kafka configuration
   - Server configuration
   - Logging configuration

---

## Hexagonal Architecture: Ports and Adapters

### Ports

**Ports** are interfaces that define how the application interacts with the outside world.

#### Input Ports (Use Cases)
- Defined in: `application/port/input/`
- Example: `ProductUseCase`
- Called by: REST controllers, CLI, GraphQL, etc.

#### Output Ports (Driven Interfaces)
- Defined in: `domain/port/output/`
- Example: `ProductRepository`, `ProductEventPublisher`
- Implemented by: JPA adapters, Kafka adapters, etc.

### Adapters

**Adapters** are implementations that connect ports to external systems.

#### Primary Adapters (Drive the application)
- REST controllers
- GraphQL resolvers
- CLI commands
- Message consumers

#### Secondary Adapters (Driven by the application)
- Database repositories
- Message producers
- External API clients
- File systems

---

## Dependency Inversion

The key principle: **High-level modules should not depend on low-level modules. Both should depend on abstractions.**

### Example: Repository

**Traditional Approach (BAD):**
```
Service → JpaRepository (concrete class)
```

**Our Approach (GOOD):**
```
Service → ProductRepository (interface) ← ProductRepositoryAdapter (implementation)
```

**Benefits:**
- Service doesn't know about JPA
- Can replace JPA with MongoDB without changing service
- Easier to test with mock implementations

---

## Data Flow

### Create Product Example

```
1. HTTP POST /api/products
   ↓
2. ProductController (Infrastructure - Input Adapter)
   - Validates request
   - Maps ProductRequest → Product (domain model)
   ↓
3. ProductUseCase.createProduct() (Application - Input Port)
   ↓
4. ProductService (Application - Use Case Implementation)
   - Validates business rules
   - Calls ProductRepository port
   ↓
5. ProductRepository (Domain - Output Port Interface)
   ↓
6. ProductRepositoryAdapter (Infrastructure - Output Adapter)
   - Maps Product → ProductEntity
   - Calls JpaProductRepository
   ↓
7. JpaProductRepository (Spring Data JPA)
   - Saves to PostgreSQL
   ↓
8. ProductEventPublisher (Domain - Output Port Interface)
   ↓
9. KafkaProductEventPublisher (Infrastructure - Output Adapter)
   - Publishes event to Kafka
   ↓
10. Return ProductResponse to client
```

---

## Testing Strategy

### Unit Tests

1. **Domain Layer**
   ```java
   // Test business logic without any framework
   @Test
   void shouldValidateProduct() {
       Product product = Product.builder()
           .name("Test")
           .price(BigDecimal.TEN)
           .quantity(5)
           .build();
       
       assertTrue(product.isValid());
   }
   ```

2. **Application Layer**
   ```java
   // Test use cases with mock adapters
   @Test
   void shouldCreateProduct() {
       ProductRepository mockRepo = mock(ProductRepository.class);
       ProductEventPublisher mockPublisher = mock(ProductEventPublisher.class);
       
       ProductService service = new ProductService(mockRepo, mockPublisher);
       // Test use case logic
   }
   ```

### Integration Tests

3. **Infrastructure Layer**
   ```java
   // Test with real database (TestContainers)
   @SpringBootTest
   @Testcontainers
   class ProductRepositoryAdapterTest {
       // Test JPA integration
   }
   ```

4. **End-to-End Tests**
   ```java
   // Test full flow with REST endpoints
   @SpringBootTest(webEnvironment = RANDOM_PORT)
   class ProductApiTest {
       // Test REST → Service → Database
   }
   ```

---

## Benefits of This Architecture

### 1. Testability
- Domain logic can be tested without any framework
- Application logic can be tested with mock adapters
- Infrastructure can be tested in isolation

### 2. Flexibility
- Easy to swap database (PostgreSQL → MongoDB)
- Easy to add new input adapters (REST → GraphQL)
- Easy to change messaging system (Kafka → RabbitMQ)

### 3. Maintainability
- Clear separation of concerns
- Each layer has a single responsibility
- Easy to understand and navigate

### 4. Independence
- Business logic independent of frameworks
- Business logic independent of UI
- Business logic independent of database
- Business logic independent of external services

### 5. Evolution
- Can change infrastructure without touching business logic
- Can add new features without breaking existing ones
- Can refactor one layer without affecting others

---

## Common Patterns Used

### 1. Dependency Injection
- Spring manages all dependencies
- Constructor injection preferred
- No field injection

### 2. Data Transfer Objects (DTOs)
- Separate models for API (ProductRequest/Response)
- Separate models for persistence (ProductEntity)
- Domain model (Product) stays pure

### 3. Mapper Pattern
- `ProductRestMapper` - REST DTOs ↔ Domain
- `ProductMapper` - JPA Entity ↔ Domain
- Keeps layers decoupled

### 4. Repository Pattern
- Abstract data access behind interface
- Domain defines interface
- Infrastructure implements it

### 5. Publisher-Subscriber Pattern
- Domain defines publisher interface
- Infrastructure implements Kafka publisher
- Loose coupling between layers

---

## Design Decisions

### Why Multi-Module Maven Project?
- Enforces layer boundaries at compile time
- Cannot accidentally use infrastructure in domain
- Clear module dependencies

### Why Separate Entities (Domain vs JPA)?
- Domain entity is framework-agnostic
- JPA entity contains annotations
- Can evolve independently
- Easier to test domain logic

### Why Ports and Adapters?
- Flexibility to swap implementations
- Testability with mock adapters
- Clear boundaries between layers

---

## Future Enhancements

### Potential Improvements

1. **Add CQRS Pattern**
   - Separate read and write models
   - Optimize queries independently

2. **Event Sourcing**
   - Store events instead of state
   - Complete audit trail

3. **API Versioning**
   - Support multiple API versions
   - Backward compatibility

4. **Authentication & Authorization**
   - Spring Security
   - JWT tokens
   - Role-based access control

5. **Caching**
   - Redis for frequently accessed data
   - Reduce database load

6. **API Documentation**
   - OpenAPI/Swagger
   - Automatic API docs generation

7. **Monitoring**
   - Prometheus metrics
   - Grafana dashboards
   - Distributed tracing

---

## Conclusion

This architecture provides a solid foundation for building scalable, maintainable, and testable applications. The separation of concerns ensures that business logic remains the core focus, while technical details are isolated in infrastructure adapters.
