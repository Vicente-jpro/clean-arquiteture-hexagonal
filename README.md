# Product Management - Clean Architecture Hexagonal

A Spring Boot application implementing Clean Architecture and Hexagonal (Ports and Adapters) pattern with multiple Maven modules.

## 🏗️ Architecture

This project follows **Clean Architecture** and **Hexagonal Architecture** principles:

### Module Structure

```
product-management/
├── domain/              # Core business logic (entities, ports)
│   └── src/main/java/com/hexagonal/domain/
│       ├── model/       # Domain entities (Product)
│       ├── port/
│       │   ├── in/      # Input ports (use cases)
│       │   └── out/     # Output ports (repositories, event publishers)
│       └── exception/   # Domain exceptions
│
├── application/         # Use case implementations
│   └── src/main/java/com/hexagonal/application/
│       └── service/     # Use case implementations (ProductService)
│
└── infrastructure/      # Adapters and frameworks
    └── src/main/java/com/hexagonal/infrastructure/
        ├── adapter/
        │   ├── rest/        # REST API controllers
        │   ├── persistence/ # JPA repositories
        │   └── messaging/   # Kafka producers/consumers
        ├── config/          # Spring Boot configuration
        └── ProductManagementApplication.java
```

### Hexagonal Architecture Layers

1. **Domain Layer** (Center)
   - Pure business logic
   - No framework dependencies
   - Defines ports (interfaces)

2. **Application Layer** (Use Cases)
   - Implements business use cases
   - Orchestrates domain objects
   - Depends only on domain

3. **Infrastructure Layer** (Adapters)
   - REST API adapter
   - PostgreSQL/JPA adapter
   - Kafka messaging adapter
   - Spring Boot configuration

## 🚀 Features

- ✅ **Clean Architecture** with clear separation of concerns
- ✅ **Hexagonal Architecture** (Ports and Adapters)
- ✅ **Multi-module Maven** project structure
- ✅ **CRUD Operations** for Product entity
- ✅ **PostgreSQL** database with JPA/Hibernate
- ✅ **Apache Kafka** event publishing
- ✅ **REST API** with validation
- ✅ **Docker Compose** for easy deployment
- ✅ **Spring Boot 3.2** with Java 17
- ✅ **MapStruct** for object mapping
- ✅ **Lombok** for reducing boilerplate

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose (for containerized deployment)

## 🛠️ Build the Application

```bash
# Build all modules
mvn clean install

# Build without tests
mvn clean install -DskipTests

# Build only specific module
cd domain && mvn clean install
```

## 🐳 Running with Docker Compose

The easiest way to run the entire stack (PostgreSQL, Kafka, Application):

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f product-app

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

Services available:
- **Application**: http://localhost:8080
- **PostgreSQL**: localhost:5432
- **Kafka**: localhost:9092
- **Kafka UI**: http://localhost:8090

## 🏃 Running Locally

### 1. Start PostgreSQL and Kafka

```bash
# Start only infrastructure services
docker-compose up -d postgres kafka zookeeper kafka-ui
```

### 2. Run the Application

```bash
# From the root directory
cd infrastructure
mvn spring-boot:run
```

Or run the JAR:

```bash
java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar
```

## 📡 API Endpoints

### Product Operations

#### Create Product
```bash
POST /api/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 10,
  "category": "Electronics"
}
```

#### Get All Products
```bash
GET /api/products
```

#### Get Product by ID
```bash
GET /api/products/{id}
```

#### Update Product
```bash
PUT /api/products/{id}
Content-Type: application/json

{
  "name": "Updated Laptop",
  "description": "Updated description",
  "price": 1099.99,
  "quantity": 15,
  "category": "Electronics"
}
```

#### Delete Product
```bash
DELETE /api/products/{id}
```

#### Get Products by Category
```bash
GET /api/products/category/{category}
```

### Example cURL Commands

```bash
# Create a product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone",
    "description": "Latest smartphone model",
    "price": 699.99,
    "quantity": 50,
    "category": "Electronics"
  }'

# Get all products
curl http://localhost:8080/api/products

# Get product by ID
curl http://localhost:8080/api/products/1

# Update product
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Smartphone",
    "description": "Updated description",
    "price": 649.99,
    "quantity": 45,
    "category": "Electronics"
  }'

# Delete product
curl -X DELETE http://localhost:8080/api/products/1

# Get products by category
curl http://localhost:8080/api/products/category/Electronics
```

## 📊 Kafka Events

The application publishes events to Kafka topic `product-events` for:
- Product Created
- Product Updated
- Product Deleted

View Kafka messages in Kafka UI: http://localhost:8090

## 🔧 Configuration

Configuration is in `infrastructure/src/main/resources/application.yml`:

```yaml
# Database
DB_HOST: localhost
DB_PORT: 5432
DB_NAME: productdb
DB_USER: postgres
DB_PASSWORD: postgres

# Kafka
KAFKA_BOOTSTRAP_SERVERS: localhost:9092

# Server
SERVER_PORT: 8080
```

Override with environment variables in Docker or when running locally.

## 📁 Folder Structure

```
.
├── pom.xml                          # Parent POM
├── domain/                          # Domain module
│   ├── pom.xml
│   └── src/main/java/com/hexagonal/domain/
│       ├── model/Product.java       # Domain entity
│       ├── port/in/ProductUseCase.java
│       ├── port/out/ProductRepository.java
│       └── port/out/ProductEventPublisher.java
│
├── application/                     # Application module
│   ├── pom.xml
│   └── src/main/java/com/hexagonal/application/
│       └── service/ProductService.java
│
├── infrastructure/                  # Infrastructure module
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/hexagonal/infrastructure/
│       │   ├── adapter/
│       │   │   ├── rest/
│       │   │   │   ├── ProductController.java
│       │   │   │   ├── ProductRequestDto.java
│       │   │   │   └── ProductResponseDto.java
│       │   │   ├── persistence/
│       │   │   │   ├── ProductEntity.java
│       │   │   │   ├── JpaProductRepositoryAdapter.java
│       │   │   │   └── SpringDataProductRepository.java
│       │   │   └── messaging/
│       │   │       ├── KafkaProductEventPublisher.java
│       │   │       └── ProductEventConsumer.java
│       │   ├── config/KafkaConfig.java
│       │   └── ProductManagementApplication.java
│       └── main/resources/
│           └── application.yml
│
├── docker-compose.yml               # Docker Compose configuration
├── Dockerfile                       # Multi-stage Docker build
└── README.md                        # This file
```

## 🧪 Health Check

```bash
curl http://localhost:8080/actuator/health
```

## 📚 Technologies Used

- **Java 17**: Programming language
- **Spring Boot 3.2**: Application framework
- **Spring Data JPA**: Database access
- **Spring Kafka**: Messaging
- **PostgreSQL**: Relational database
- **Apache Kafka**: Event streaming
- **Maven**: Build tool
- **Lombok**: Reduce boilerplate
- **MapStruct**: Object mapping
- **Docker**: Containerization
- **Docker Compose**: Multi-container orchestration

## 🎯 Clean Architecture Benefits

1. **Independence of Frameworks**: Business logic doesn't depend on Spring or JPA
2. **Testability**: Business rules can be tested without external dependencies
3. **Independence of UI**: Can easily add new interfaces (GraphQL, gRPC)
4. **Independence of Database**: Can switch from PostgreSQL to MongoDB
5. **Maintainability**: Clear separation makes code easier to understand and modify

## 📝 License

This project is open source and available under the MIT License.