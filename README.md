# Product Management System - Clean Architecture & Hexagonal Architecture

A multi-module Spring Boot application demonstrating **Clean Architecture** and **Hexagonal Architecture** principles with CRUD operations, PostgreSQL, JPA, and Kafka integration.

## 🏗️ Architecture Overview

This project implements **Clean Architecture** (layered architecture) combined with **Hexagonal Architecture** (ports and adapters pattern):

- **Domain Layer** (Innermost): Pure business logic, entities, and port interfaces
- **Application Layer**: Use cases and business orchestration
- **Infrastructure Layer**: Adapters for external systems (REST, JPA, Kafka)
- **Bootstrap Layer**: Application entry point and configuration

### Hexagonal Architecture Principles

- **Ports**: Interfaces defined in the domain layer
  - **Input Ports**: Use case interfaces (e.g., ProductUseCase)
  - **Output Ports**: Repository and messaging interfaces (e.g., ProductRepository, ProductEventPublisher)
- **Adapters**: Implementations in the infrastructure layer
  - **Input Adapters**: REST controllers
  - **Output Adapters**: JPA repositories, Kafka producers

## 📁 Project Structure

```
product-management/
├── pom.xml                          # Parent POM with module definitions
├── docker-compose.yml                # Docker Compose for PostgreSQL & Kafka
├── Dockerfile                        # Multi-stage Dockerfile for the application
├── .gitignore
│
├── domain/                           # Core Business Logic (Hexagon Core)
│   ├── pom.xml
│   └── src/main/java/com/cleanarch/domain/
│       ├── model/
│       │   └── Product.java         # Domain entity
│       ├── port/
│       │   └── output/
│       │       ├── ProductRepository.java          # Repository port
│       │       └── ProductEventPublisher.java      # Messaging port
│       └── exception/
│           ├── ProductNotFoundException.java
│           └── InvalidProductException.java
│
├── application/                      # Use Cases Layer
│   ├── pom.xml
│   └── src/main/java/com/cleanarch/application/
│       ├── port/
│       │   └── input/
│       │       └── ProductUseCase.java             # Use case interface
│       └── service/
│           └── ProductService.java                 # Use case implementation
│
├── infrastructure/                   # Adapters Layer
│   ├── pom.xml
│   └── src/main/java/com/cleanarch/infrastructure/
│       ├── adapter/
│       │   ├── input/
│       │   │   └── rest/
│       │   │       ├── ProductController.java      # REST adapter (input)
│       │   │       ├── dto/
│       │   │       │   ├── ProductRequest.java
│       │   │       │   └── ProductResponse.java
│       │   │       ├── mapper/
│       │   │       │   └── ProductRestMapper.java
│       │   │       └── exception/
│       │   │           └── GlobalExceptionHandler.java
│       │   └── output/
│       │       ├── persistence/
│       │       │   ├── entity/
│       │       │   │   └── ProductEntity.java      # JPA entity
│       │       │   ├── mapper/
│       │       │   │   └── ProductMapper.java
│       │       │   └── repository/
│       │       │       ├── JpaProductRepository.java
│       │       │       └── ProductRepositoryAdapter.java  # JPA adapter
│       │       └── messaging/
│       │           └── KafkaProductEventPublisher.java    # Kafka adapter
│       └── config/
│           ├── ApplicationConfig.java              # Bean configuration
│           └── KafkaConfig.java                    # Kafka configuration
│
└── bootstrap/                        # Application Entry Point
    ├── pom.xml
    └── src/main/
        ├── java/com/cleanarch/
        │   └── ProductManagementApplication.java   # Main class
        └── resources/
            └── application.yml                     # Configuration file
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose (for running PostgreSQL and Kafka)

### 1. Clone the Repository

```bash
git clone https://github.com/Vicente-jpro/clean-arquiteture-hexagonal.git
cd clean-arquiteture-hexagonal
```

### 2. Start Infrastructure Services

Start PostgreSQL and Kafka using Docker Compose:

```bash
docker-compose up -d
```

This will start:
- PostgreSQL on port 5432
- Kafka on port 9092
- Zookeeper on port 2181
- Kafka UI on port 8090 (http://localhost:8090)

### 3. Build the Application

```bash
mvn clean install
```

### 4. Run the Application

```bash
cd bootstrap
mvn spring-boot:run
```

Or run the JAR:

```bash
java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar
```

The application will be available at: http://localhost:8080

### 5. Health Check

```bash
curl http://localhost:8080/actuator/health
```

## 📡 API Endpoints

### Create a Product
```bash
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1299.99,
  "quantity": 10,
  "category": "Electronics"
}
```

### Get All Products
```bash
GET http://localhost:8080/api/products
```

### Get Product by ID
```bash
GET http://localhost:8080/api/products/{id}
```

### Get Products by Category
```bash
GET http://localhost:8080/api/products?category=Electronics
```

### Update a Product
```bash
PUT http://localhost:8080/api/products/{id}
Content-Type: application/json

{
  "name": "Gaming Laptop",
  "description": "High-performance gaming laptop",
  "price": 1499.99,
  "quantity": 5,
  "category": "Electronics"
}
```

### Delete a Product
```bash
DELETE http://localhost:8080/api/products/{id}
```

## 🔄 Kafka Events

The application publishes events to Kafka topic `product-events` for:
- Product Created
- Product Updated
- Product Deleted

You can monitor these events using Kafka UI at http://localhost:8090

## 🧪 Testing the Application

### Using cURL

Create a product:
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone",
    "description": "Latest model smartphone",
    "price": 799.99,
    "quantity": 20,
    "category": "Electronics"
  }'
```

Get all products:
```bash
curl http://localhost:8080/api/products
```

## 🐳 Running with Docker

### Build the Docker Image

```bash
docker build -t product-management:latest .
```

### Run with Docker Compose

Uncomment the `app` service in `docker-compose.yml` and run:

```bash
docker-compose up -d
```

## 📦 Module Dependencies

```
bootstrap
  └── infrastructure
       └── application
            └── domain
```

- **Domain**: No external dependencies (pure business logic)
- **Application**: Depends on Domain
- **Infrastructure**: Depends on Application (and transitively Domain)
- **Bootstrap**: Depends on Infrastructure (ties everything together)

## 🎯 Clean Architecture Benefits

1. **Independence of Frameworks**: Business logic is not coupled to Spring or any framework
2. **Testability**: Business logic can be tested without UI, database, or external systems
3. **Independence of UI**: REST API can be replaced with GraphQL, gRPC, etc.
4. **Independence of Database**: Can switch from PostgreSQL to MongoDB without changing business logic
5. **Independence of External Systems**: Kafka can be replaced with RabbitMQ, AWS SNS, etc.

## 🔌 Hexagonal Architecture Benefits

1. **Port-Adapter Pattern**: Clear separation between business logic and technical details
2. **Dependency Inversion**: Domain defines interfaces, infrastructure implements them
3. **Interchangeable Adapters**: Easy to swap implementations (e.g., REST → gRPC)
4. **Testing**: Can test with mock adapters without real database or message broker

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Apache Kafka**
- **Maven**
- **Docker & Docker Compose**
- **Lombok**

## 📝 Configuration

Database and Kafka configurations are in `bootstrap/src/main/resources/application.yml`:

- Database URL: `jdbc:postgresql://localhost:5432/productdb`
- Database credentials: `postgres/postgres`
- Kafka broker: `localhost:9092`

## 🔍 Monitoring

- Health check: http://localhost:8080/actuator/health
- Kafka UI: http://localhost:8090

## 📄 License

This project is open source and available under the MIT License.