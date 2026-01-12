# API Documentation - Product Management System

## Base URL
```
http://localhost:8080/api
```

## Endpoints

### 1. Create Product

Creates a new product in the system.

**Endpoint:** `POST /products`

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Laptop",
  "description": "High-performance laptop for developers",
  "price": 1299.99,
  "quantity": 10,
  "category": "Electronics"
}
```

**Success Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop for developers",
  "price": 1299.99,
  "quantity": 10,
  "category": "Electronics",
  "createdAt": "2025-12-16T23:15:00",
  "updatedAt": "2025-12-16T23:15:00"
}
```

**Error Response:** `400 Bad Request`
```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2025-12-16T23:15:00",
  "errors": {
    "name": "Product name is required",
    "price": "Price must be positive"
  }
}
```

**cURL Example:**
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

---

### 2. Get All Products

Retrieves all products in the system.

**Endpoint:** `GET /products`

**Query Parameters:**
- `category` (optional): Filter products by category

**Success Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "quantity": 10,
    "category": "Electronics",
    "createdAt": "2025-12-16T23:15:00",
    "updatedAt": "2025-12-16T23:15:00"
  },
  {
    "id": 2,
    "name": "Smartphone",
    "description": "Latest model smartphone",
    "price": 799.99,
    "quantity": 20,
    "category": "Electronics",
    "createdAt": "2025-12-16T23:16:00",
    "updatedAt": "2025-12-16T23:16:00"
  }
]
```

**cURL Example:**
```bash
# Get all products
curl http://localhost:8080/api/products

# Get products by category
curl http://localhost:8080/api/products?category=Electronics
```

---

### 3. Get Product by ID

Retrieves a specific product by its ID.

**Endpoint:** `GET /products/{id}`

**Path Parameters:**
- `id` (required): Product ID

**Success Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1299.99,
  "quantity": 10,
  "category": "Electronics",
  "createdAt": "2025-12-16T23:15:00",
  "updatedAt": "2025-12-16T23:15:00"
}
```

**Error Response:** `404 Not Found`
```json
{
  "status": 404,
  "message": "Product not found with id: 999",
  "timestamp": "2025-12-16T23:15:00"
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/products/1
```

---

### 4. Update Product

Updates an existing product.

**Endpoint:** `PUT /products/{id}`

**Path Parameters:**
- `id` (required): Product ID

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Gaming Laptop",
  "description": "High-performance gaming laptop with RTX 4090",
  "price": 1499.99,
  "quantity": 5,
  "category": "Electronics"
}
```

**Success Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Gaming Laptop",
  "description": "High-performance gaming laptop with RTX 4090",
  "price": 1499.99,
  "quantity": 5,
  "category": "Electronics",
  "createdAt": "2025-12-16T23:15:00",
  "updatedAt": "2025-12-16T23:20:00"
}
```

**Error Response:** `404 Not Found`
```json
{
  "status": 404,
  "message": "Product not found with id: 999",
  "timestamp": "2025-12-16T23:15:00"
}
```

**cURL Example:**
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Laptop",
    "description": "High-performance gaming laptop",
    "price": 1499.99,
    "quantity": 5,
    "category": "Electronics"
  }'
```

---

### 5. Delete Product

Deletes a product from the system.

**Endpoint:** `DELETE /products/{id}`

**Path Parameters:**
- `id` (required): Product ID

**Success Response:** `204 No Content`

**Error Response:** `404 Not Found`
```json
{
  "status": 404,
  "message": "Product not found with id: 999",
  "timestamp": "2025-12-16T23:15:00"
}
```

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

---

## Validation Rules

### Product Request Validation

- **name**: 
  - Required
  - Must not be blank
  - Maximum 255 characters

- **description**: 
  - Optional
  - No length limit

- **price**: 
  - Required
  - Must be greater than or equal to 0
  - Decimal with 2 decimal places

- **quantity**: 
  - Required
  - Must be greater than or equal to 0
  - Integer value

- **category**: 
  - Optional
  - String value

---

## Kafka Events

The application publishes events to the `product-events` Kafka topic when:

### Event: PRODUCT_CREATED
```json
{
  "eventType": "PRODUCT_CREATED",
  "payload": {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "quantity": 10,
    "category": "Electronics",
    "createdAt": "2025-12-16T23:15:00",
    "updatedAt": "2025-12-16T23:15:00"
  }
}
```

### Event: PRODUCT_UPDATED
```json
{
  "eventType": "PRODUCT_UPDATED",
  "payload": {
    "id": 1,
    "name": "Gaming Laptop",
    "description": "High-performance gaming laptop",
    "price": 1499.99,
    "quantity": 5,
    "category": "Electronics",
    "createdAt": "2025-12-16T23:15:00",
    "updatedAt": "2025-12-16T23:20:00"
  }
}
```

### Event: PRODUCT_DELETED
```json
{
  "eventType": "PRODUCT_DELETED",
  "payload": 1
}
```

---

## Health Check

**Endpoint:** `GET /actuator/health`

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
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

---

## Testing with Postman

You can import this collection into Postman:

1. Create a new collection named "Product Management API"
2. Add the base URL: `http://localhost:8080/api`
3. Create requests for each endpoint above
4. Set appropriate headers and request bodies

---

## Error Codes

| Status Code | Description |
|------------|-------------|
| 200 | OK - Request succeeded |
| 201 | Created - Resource created successfully |
| 204 | No Content - Resource deleted successfully |
| 400 | Bad Request - Invalid request data |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error |

---

## Rate Limiting

Currently, there is no rate limiting implemented. This can be added using Spring Cloud Gateway or custom interceptors.

---

## Authentication

Currently, there is no authentication required. For production use, consider adding:
- Spring Security with JWT
- OAuth2
- API Keys
