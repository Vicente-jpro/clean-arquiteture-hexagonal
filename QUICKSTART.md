# Quick Start Guide

This guide will help you get the Product Management System up and running in minutes.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose

## Step 1: Clone the Repository

```bash
git clone https://github.com/Vicente-jpro/clean-arquiteture-hexagonal.git
cd clean-arquiteture-hexagonal
```

## Step 2: Start Infrastructure Services

Start PostgreSQL and Kafka using Docker Compose:

```bash
docker-compose up -d
```

Wait about 30 seconds for services to be ready. Check status:

```bash
docker-compose ps
```

All services should show as "healthy" or "running".

## Step 3: Build the Application

```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile all modules
- Create the executable JAR file

## Step 4: Run the Application

```bash
cd bootstrap
mvn spring-boot:run
```

Or run the JAR directly:

```bash
java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar
```

The application will start on http://localhost:8080

## Step 5: Verify the Application

Check health status:

```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

## Step 6: Create Your First Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "quantity": 10,
    "category": "Electronics"
  }'
```

Expected response:
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1299.99,
  "quantity": 10,
  "category": "Electronics",
  "createdAt": "2025-12-16T23:30:00",
  "updatedAt": "2025-12-16T23:30:00"
}
```

## Step 7: Get All Products

```bash
curl http://localhost:8080/api/products
```

## Step 8: Monitor Kafka Events

Access Kafka UI at http://localhost:8090

Navigate to Topics → product-events to see the events published by the application.

## Useful Commands

### Stop the application
Press `Ctrl+C` in the terminal running the application.

### Stop infrastructure services
```bash
docker-compose down
```

### Clean restart everything
```bash
docker-compose down -v  # Remove volumes
docker-compose up -d    # Start fresh
mvn clean install       # Rebuild
mvn spring-boot:run     # Run application
```

### View logs
```bash
# Application logs (if running in background)
tail -f logs/application.log

# Docker logs
docker-compose logs -f postgres
docker-compose logs -f kafka
```

## Development Mode

Run with development profile for verbose logging:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Or with JAR:

```bash
java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev
```

## Testing the API

### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Smartphone","description":"Latest model","price":799.99,"quantity":20,"category":"Electronics"}'
```

### Get All Products
```bash
curl http://localhost:8080/api/products
```

### Get Product by ID
```bash
curl http://localhost:8080/api/products/1
```

### Get Products by Category
```bash
curl http://localhost:8080/api/products?category=Electronics
```

### Update Product
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Gaming Laptop","description":"High-performance gaming","price":1499.99,"quantity":5,"category":"Electronics"}'
```

### Delete Product
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

## Troubleshooting

### Port Already in Use

If port 8080, 5432, or 9092 is already in use:

**Option 1:** Stop the conflicting service

**Option 2:** Change ports in configuration

Edit `bootstrap/src/main/resources/application.yml`:
```yaml
server:
  port: 8081  # Change to available port
```

For Docker services, edit `docker-compose.yml`.

### Database Connection Failed

Check PostgreSQL is running:
```bash
docker-compose ps postgres
```

Check logs:
```bash
docker-compose logs postgres
```

### Kafka Connection Failed

Check Kafka is running:
```bash
docker-compose ps kafka
```

Wait for Kafka to be fully started (takes ~30 seconds).

### Build Failures

Clear Maven cache and rebuild:
```bash
mvn clean
rm -rf ~/.m2/repository/com/cleanarch
mvn install
```

## Environment Variables

Override configuration using environment variables:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=productdb
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export SERVER_PORT=8080

java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar
```

## Docker Deployment

Build Docker image:
```bash
docker build -t product-management:latest .
```

Run with Docker Compose (uncomment app service in docker-compose.yml):
```bash
docker-compose up -d
```

## Next Steps

- Read [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for detailed API reference
- Read [ARCHITECTURE.md](ARCHITECTURE.md) to understand the architecture
- Explore the code starting from `bootstrap` module
- Add your own features following the Clean Architecture principles

## Support

For issues or questions:
1. Check the [README.md](README.md)
2. Check the [ARCHITECTURE.md](ARCHITECTURE.md)
3. Open an issue on GitHub

Happy coding! 🚀
