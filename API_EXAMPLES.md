# API Usage Examples

This file contains practical examples of how to use the Product Management REST API.

## Prerequisites

Make sure the application is running:
```bash
docker-compose up -d
```

The API will be available at: `http://localhost:8080`

## Testing the API

### 1. Create a Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Pro 15",
    "description": "High-performance laptop with 16GB RAM and 512GB SSD",
    "price": 1299.99,
    "quantity": 25,
    "category": "Electronics"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Laptop Pro 15",
  "description": "High-performance laptop with 16GB RAM and 512GB SSD",
  "price": 1299.99,
  "quantity": 25,
  "category": "Electronics",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "inStock": true
}
```

### 2. Get All Products

```bash
curl http://localhost:8080/api/products
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Laptop Pro 15",
    "description": "High-performance laptop with 16GB RAM and 512GB SSD",
    "price": 1299.99,
    "quantity": 25,
    "category": "Electronics",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00",
    "inStock": true
  }
]
```

### 3. Get Product by ID

```bash
curl http://localhost:8080/api/products/1
```

**Response:**
```json
{
  "id": 1,
  "name": "Laptop Pro 15",
  "description": "High-performance laptop with 16GB RAM and 512GB SSD",
  "price": 1299.99,
  "quantity": 25,
  "category": "Electronics",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "inStock": true
}
```

### 4. Update a Product

```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Pro 15 - Updated",
    "description": "High-performance laptop with 32GB RAM and 1TB SSD",
    "price": 1499.99,
    "quantity": 20,
    "category": "Electronics"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Laptop Pro 15 - Updated",
  "description": "High-performance laptop with 32GB RAM and 1TB SSD",
  "price": 1499.99,
  "quantity": 20,
  "category": "Electronics",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:45:00",
  "inStock": true
}
```

### 5. Get Products by Category

```bash
curl http://localhost:8080/api/products/category/Electronics
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Laptop Pro 15 - Updated",
    "description": "High-performance laptop with 32GB RAM and 1TB SSD",
    "price": 1499.99,
    "quantity": 20,
    "category": "Electronics",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:45:00",
    "inStock": true
  }
]
```

### 6. Delete a Product

```bash
curl -X DELETE http://localhost:8080/api/products/1
```

**Response:**
```
204 No Content
```

## Error Handling Examples

### Product Not Found

**Request:**
```bash
curl http://localhost:8080/api/products/999
```

**Response (404):**
```json
{
  "status": 404,
  "message": "Product not found with id: 999",
  "timestamp": "2024-01-15T10:50:00"
}
```

### Validation Error

**Request:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "price": -10,
    "quantity": -5
  }'
```

**Response (400):**
```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2024-01-15T10:55:00",
  "errors": {
    "name": "Product name is required",
    "price": "Price must be greater than 0",
    "quantity": "Quantity must be non-negative"
  }
}
```

## Testing with Postman

You can import these examples into Postman:

1. Create a new collection named "Product Management API"
2. Add requests for each endpoint
3. Set the base URL to `http://localhost:8080`
4. Add the Content-Type header for POST/PUT requests

## Kafka Events

After creating, updating, or deleting a product, you can view the published events in Kafka UI:

1. Open Kafka UI: http://localhost:8090
2. Navigate to Topics → product-events
3. View the messages

**Event Structure:**
```json
{
  "id": 1,
  "name": "Laptop Pro 15",
  "description": "High-performance laptop",
  "price": 1299.99,
  "quantity": 25,
  "category": "Electronics",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "eventType": "CREATED",
  "eventTimestamp": "2024-01-15T10:30:00"
}
```

## Health Check

Check if the application is running:

```bash
curl http://localhost:8080/actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

## Sample Data Script

You can use this script to populate the database with sample products:

```bash
#!/bin/bash

# Create Electronics products
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Smartphone X", "description": "Latest smartphone", "price": 799.99, "quantity": 50, "category": "Electronics"}'

curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Wireless Headphones", "description": "Noise-canceling headphones", "price": 249.99, "quantity": 75, "category": "Electronics"}'

# Create Books products
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Clean Code", "description": "A Handbook of Agile Software Craftsmanship", "price": 39.99, "quantity": 100, "category": "Books"}'

curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Design Patterns", "description": "Elements of Reusable Object-Oriented Software", "price": 44.99, "quantity": 80, "category": "Books"}'

# Create Clothing products
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "T-Shirt", "description": "Cotton T-Shirt", "price": 19.99, "quantity": 200, "category": "Clothing"}'

echo "Sample data created successfully!"
```

Save this as `populate-data.sh`, make it executable (`chmod +x populate-data.sh`), and run it.
