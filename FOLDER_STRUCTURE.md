# Folder Structure Visualization

## Project Root Structure

```
clean-arquiteture-hexagonal/
│
├── 📄 pom.xml                          # Parent POM - Maven multi-module configuration
├── 📄 docker-compose.yml                # Docker Compose for PostgreSQL, Kafka, Zookeeper
├── 📄 Dockerfile                        # Multi-stage build for containerizing the app
├── 📄 .gitignore                        # Git ignore rules
│
├── 📚 Documentation
│   ├── 📄 README.md                     # Main project documentation
│   ├── 📄 QUICKSTART.md                 # Quick start guide
│   ├── 📄 API_DOCUMENTATION.md          # API endpoint documentation
│   ├── 📄 ARCHITECTURE.md               # Architecture explanation
│   └── 📄 FOLDER_STRUCTURE.md           # This file
│
├── 📦 domain/                           # DOMAIN LAYER (Core Business Logic)
│   ├── 📄 pom.xml
│   └── src/main/java/com/cleanarch/domain/
│       ├── 📁 model/
│       │   └── 📄 Product.java          # Domain entity (pure business model)
│       ├── 📁 port/output/
│       │   ├── 📄 ProductRepository.java         # Repository port interface
│       │   └── 📄 ProductEventPublisher.java     # Messaging port interface
│       └── 📁 exception/
│           ├── 📄 ProductNotFoundException.java
│           └── 📄 InvalidProductException.java
│
├── 📦 application/                      # APPLICATION LAYER (Use Cases)
│   ├── 📄 pom.xml
│   └── src/main/java/com/cleanarch/application/
│       ├── 📁 port/input/
│       │   └── 📄 ProductUseCase.java            # Use case interface (input port)
│       └── 📁 service/
│           └── 📄 ProductService.java            # Use case implementation
│
├── 📦 infrastructure/                   # INFRASTRUCTURE LAYER (Adapters)
│   ├── 📄 pom.xml
│   └── src/main/java/com/cleanarch/infrastructure/
│       ├── 📁 adapter/
│       │   ├── 📁 input/rest/                    # REST Input Adapter (Primary)
│       │   │   ├── 📄 ProductController.java
│       │   │   ├── 📁 dto/
│       │   │   │   ├── 📄 ProductRequest.java
│       │   │   │   └── 📄 ProductResponse.java
│       │   │   ├── 📁 mapper/
│       │   │   │   └── 📄 ProductRestMapper.java
│       │   │   └── 📁 exception/
│       │   │       └── 📄 GlobalExceptionHandler.java
│       │   │
│       │   └── 📁 output/                        # Output Adapters (Secondary)
│       │       ├── 📁 persistence/               # JPA Adapter
│       │       │   ├── 📁 entity/
│       │       │   │   └── 📄 ProductEntity.java
│       │       │   ├── 📁 mapper/
│       │       │   │   └── 📄 ProductMapper.java
│       │       │   └── 📁 repository/
│       │       │       ├── 📄 JpaProductRepository.java
│       │       │       └── 📄 ProductRepositoryAdapter.java
│       │       │
│       │       └── 📁 messaging/                 # Kafka Adapter
│       │           └── 📄 KafkaProductEventPublisher.java
│       │
│       └── 📁 config/
│           ├── 📄 ApplicationConfig.java         # Spring Bean configuration
│           └── 📄 KafkaConfig.java               # Kafka configuration
│
└── 📦 bootstrap/                        # BOOTSTRAP LAYER (Entry Point)
    ├── 📄 pom.xml
    └── src/main/
        ├── java/com/cleanarch/
        │   └── 📄 ProductManagementApplication.java  # Main class (@SpringBootApplication)
        └── resources/
            ├── 📄 application.yml                    # Main configuration
            └── 📄 application-dev.yml                # Development profile config
```

## Module Dependencies

```
┌─────────────────────────────────────────────┐
│              bootstrap                       │
│         (Application Entry Point)            │
│     - ProductManagementApplication.java      │
│     - application.yml                        │
└──────────────────┬──────────────────────────┘
                   │
                   │ depends on
                   ↓
┌─────────────────────────────────────────────┐
│           infrastructure                     │
│          (Adapters Layer)                    │
│     - REST Controllers                       │
│     - JPA Repositories                       │
│     - Kafka Publishers                       │
└──────────────────┬──────────────────────────┘
                   │
                   │ depends on
                   ↓
┌─────────────────────────────────────────────┐
│            application                       │
│          (Use Cases Layer)                   │
│     - ProductService                         │
│     - ProductUseCase (interface)             │
└──────────────────┬──────────────────────────┘
                   │
                   │ depends on
                   ↓
┌─────────────────────────────────────────────┐
│              domain                          │
│          (Core Business Logic)               │
│     - Product (entity)                       │
│     - ProductRepository (port)               │
│     - ProductEventPublisher (port)           │
│     - NO framework dependencies              │
└─────────────────────────────────────────────┘
```

## Layer Responsibilities

### 🔵 Domain Layer (Core)
**Location:** `domain/`

**Purpose:** Contains pure business logic and rules

**No dependencies on:**
- Spring Framework
- Database (JPA)
- Web frameworks
- External libraries (except Lombok)

**Contains:**
- ✅ Business entities (Product)
- ✅ Business rules and validation
- ✅ Port interfaces (contracts)
- ✅ Domain exceptions

### 🟢 Application Layer (Use Cases)
**Location:** `application/`

**Purpose:** Orchestrates business operations

**Dependencies:**
- Domain layer only

**Contains:**
- ✅ Use case implementations
- ✅ Business workflows
- ✅ Application services
- ✅ Input port interfaces

### 🟡 Infrastructure Layer (Adapters)
**Location:** `infrastructure/`

**Purpose:** Implements technical details

**Dependencies:**
- Application layer
- Domain layer
- Spring Boot
- JPA, Kafka, etc.

**Contains:**
- ✅ REST controllers (input adapters)
- ✅ JPA repositories (output adapters)
- ✅ Kafka publishers (output adapters)
- ✅ DTOs and mappers
- ✅ Configuration classes

### 🟠 Bootstrap Layer (Main)
**Location:** `bootstrap/`

**Purpose:** Application entry point and configuration

**Dependencies:**
- Infrastructure layer (brings all others)

**Contains:**
- ✅ Main class with @SpringBootApplication
- ✅ Configuration files (application.yml)
- ✅ Profiles (dev, prod)

## File Count Summary

```
Total Java Files: 20

Domain Layer:        5 files
  - Entities:        1 (Product.java)
  - Ports:           2 (ProductRepository, ProductEventPublisher)
  - Exceptions:      2 (ProductNotFoundException, InvalidProductException)

Application Layer:   2 files
  - Use Cases:       1 (ProductUseCase interface)
  - Services:        1 (ProductService)

Infrastructure:     12 files
  - Controllers:     1 (ProductController)
  - DTOs:            2 (ProductRequest, ProductResponse)
  - Mappers:         2 (ProductRestMapper, ProductMapper)
  - Entities:        1 (ProductEntity)
  - Repositories:    2 (JpaProductRepository, ProductRepositoryAdapter)
  - Messaging:       1 (KafkaProductEventPublisher)
  - Exception:       1 (GlobalExceptionHandler)
  - Config:          2 (ApplicationConfig, KafkaConfig)

Bootstrap:           1 file
  - Main:            1 (ProductManagementApplication)
```

## Configuration Files

```
📄 pom.xml (parent)                      # Maven multi-module configuration
├── 📄 domain/pom.xml                    # Domain module dependencies (minimal)
├── 📄 application/pom.xml               # Application module dependencies
├── 📄 infrastructure/pom.xml            # Infrastructure dependencies (Spring, JPA, Kafka)
└── 📄 bootstrap/pom.xml                 # Bootstrap dependencies + Spring Boot plugin

📄 docker-compose.yml                    # PostgreSQL, Kafka, Zookeeper, Kafka UI
📄 Dockerfile                            # Multi-stage Docker build
📄 .gitignore                            # Git ignore rules for Maven/Java

📄 bootstrap/src/main/resources/
├── application.yml                      # Main configuration (production)
└── application-dev.yml                  # Development profile configuration
```

## Data Flow Example: Create Product

```
1. HTTP Request (POST /api/products)
   ↓
2. ProductController (infrastructure/adapter/input/rest)
   - Validates request
   - Maps ProductRequest → Product (domain)
   ↓
3. ProductUseCase.createProduct() (application/port/input)
   ↓
4. ProductService (application/service)
   - Validates business rules
   - Calls output ports
   ↓
5. ProductRepository.save() (domain/port/output)
   ↓
6. ProductRepositoryAdapter (infrastructure/adapter/output/persistence)
   - Maps Product → ProductEntity
   - Saves to database via JPA
   ↓
7. ProductEventPublisher.publishProductCreated() (domain/port/output)
   ↓
8. KafkaProductEventPublisher (infrastructure/adapter/output/messaging)
   - Publishes event to Kafka
   ↓
9. Return ProductResponse to client
```

## Key Design Patterns

1. **Hexagonal Architecture (Ports & Adapters)**
   - Ports: Interfaces in domain/application
   - Adapters: Implementations in infrastructure

2. **Dependency Inversion Principle**
   - Domain defines interfaces
   - Infrastructure implements them

3. **Clean Architecture**
   - Dependencies point inward
   - Business logic is framework-agnostic

4. **Repository Pattern**
   - Abstract data access
   - ProductRepository (interface) → ProductRepositoryAdapter (implementation)

5. **DTO Pattern**
   - Separate API models from domain models
   - ProductRequest/Response vs Product

6. **Mapper Pattern**
   - Convert between layers
   - ProductRestMapper, ProductMapper

## Building the Project

```bash
# Compile all modules
mvn clean compile

# Package into JAR
mvn clean package

# Install to local Maven repository
mvn clean install

# Run tests
mvn test

# Run specific module
cd domain && mvn test
```

## Running the Application

```bash
# Development mode (from bootstrap/)
mvn spring-boot:run

# With dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production mode
java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar

# With custom port
java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar --server.port=8081
```

---

This structure ensures:
- ✅ Clear separation of concerns
- ✅ Framework independence in core business logic
- ✅ Easy testability
- ✅ Flexibility to change implementations
- ✅ Maintainability and scalability
