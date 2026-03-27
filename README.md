# clean-arquiteture-hexagonal

Here’s a clean and practical folder structure for a **DDD + Clean Architecture** project with **Domain, Application, and Infrastructure** modules. I’ll keep it language-agnostic but realistic (works well for .NET, Java, Node, etc.).

---

# 🧱 Overall Solution Structure

```
/src
  /Domain
  /Application
  /Infrastructure
  /Presentation   (optional but common: API/UI layer)
  /Shared         (optional: cross-cutting concerns)
```

---

# 🧠 1. Domain Layer (Core Business Logic)

> Pure business rules — **no dependencies on other layers**

```
/Domain
  /Entities
    Order.cs
    Customer.cs

  /ValueObjects
    Money.cs
    Address.cs

  /Aggregates
    OrderAggregate.cs

  /Enums
    OrderStatus.cs

  /Events
    OrderCreatedEvent.cs

  /Exceptions
    DomainException.cs

  /Repositories   (interfaces only)
    IOrderRepository.cs

  /Services       (domain services)
    PricingService.cs

  /Specifications (optional)
    OrderSpecification.cs
```

✅ Rules:

* No frameworks
* No database logic
* Only interfaces for persistence

---

# ⚙️ 2. Application Layer (Use Cases)

> Orchestrates domain logic — **depends on Domain only**

```
/Application
  /UseCases
    /CreateOrder
      CreateOrderCommand.cs
      CreateOrderHandler.cs
      CreateOrderValidator.cs

    /GetOrder
      GetOrderQuery.cs
      GetOrderHandler.cs

  /DTOs
    OrderDto.cs

  /Interfaces
    IUnitOfWork.cs
    IEmailService.cs

  /Mappings
    OrderMappingProfile.cs

  /Behaviors        (pipeline/middleware, e.g. MediatR)
    LoggingBehavior.cs
    ValidationBehavior.cs

  /Exceptions
    ApplicationException.cs
```

✅ Responsibilities:

* Handles **commands & queries (CQRS)**
* Coordinates repositories & services
* No direct DB or framework code

---

# 🏗️ 3. Infrastructure Layer (External Concerns)

> Implements interfaces — **depends on Application + Domain**

```
/Infrastructure
  /Persistence
    /Context
      AppDbContext.cs

    /Repositories
      OrderRepository.cs

    /Configurations
      OrderConfiguration.cs

    /Migrations

  /Services
    EmailService.cs
    PaymentGatewayService.cs

  /External
    /Stripe
      StripeClient.cs

    /Logging
      LoggerService.cs

  /Identity
    AuthService.cs

  /Files
    FileStorageService.cs
```

✅ Responsibilities:

* Database (ORM, queries)
* External APIs
* File systems, email, etc.

---

# 🌐 4. Presentation Layer (API / UI)

> Entry point (controllers, routes)

```
/Presentation
  /Controllers
    OrderController.cs

  /Middleware
    ExceptionMiddleware.cs

  /Filters
    ValidationFilter.cs

  /Contracts
    /Requests
      CreateOrderRequest.cs

    /Responses
      OrderResponse.cs
```

---

# 🔁 Dependency Flow (IMPORTANT)

```
Presentation → Application → Domain
                   ↓
             Infrastructure (implements interfaces)
```

✔ Domain ← no dependencies
✔ Application ← depends on Domain
✔ Infrastructure ← depends on both
✔ Presentation ← depends on Application

---

# 🧩 Optional: Shared Layer

```
/Shared
  /Kernel
    BaseEntity.cs
    Result.cs

  /Utilities
    DateTimeProvider.cs
```

---

# 💡 Tips (from real-world usage)

* Keep **Domain small and pure** — that’s your core value
* Use **CQRS** inside Application for clarity
* Avoid putting logic in Infrastructure
* Use **dependency injection** to wire everything

---

If you want, I can generate:

* A **real example (e.g., Order module end-to-end)**
* Or a **.NET / Node.js / Java version** with actual code files


Good question—this is exactly where **DDD + Clean Architecture + event streaming (Kafka)** come together in a nice, structured way.

Let’s walk through the **data flow step by step**, from Kafka → Spring Boot → database, aligned with **Clean Architecture layers**.

---

# 🧭 High-level idea

Kafka is an **external system**, so in Clean Architecture it sits **outside your core**.

Your app flow will look like:

```
Kafka → Infrastructure (Consumer) → Application Layer → Domain Layer → Infrastructure (Repository) → DB
```

---

# 🔄 Step-by-step data flow

## 1. Kafka produces an event

Some external service publishes an event, e.g.:

```json
{
  "orderId": "123",
  "customerId": "456",
  "amount": 99.99
}
```

This is **NOT your domain model**—just a transport message.

---

## 2. Infrastructure Layer: Kafka Consumer

In Spring Boot, you typically use `@KafkaListener`.

This belongs to the **Infrastructure layer**, because:

* Kafka is an external dependency
* Messaging is a technical concern

```java
@KafkaListener(topics = "orders")
public void consume(OrderEventDto event) {
    orderMessageHandler.handle(event);
}
```

👉 Important:

* This class should be **thin**
* It should delegate immediately

---

## 3. Application Layer: Message Handler / Use Case

Now you enter the **Application layer**.

This is where you:

* Orchestrate logic
* Convert DTO → Domain
* Call use cases

```java
public class OrderMessageHandler {

    private final CreateOrderUseCase createOrderUseCase;

    public void handle(OrderEventDto dto) {
        CreateOrderCommand command = mapToCommand(dto);
        createOrderUseCase.execute(command);
    }
}
```

---

## 4. Application Layer: Use Case

Use cases define **business actions**.

```java
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;

    public void execute(CreateOrderCommand command) {
        Order order = Order.create(
            command.getOrderId(),
            command.getCustomerId(),
            command.getAmount()
        );

        orderRepository.save(order);
    }
}
```

---

## 5. Domain Layer: Core Business Logic

This is the **heart of DDD**.

```java
public class Order {

    private OrderId id;
    private CustomerId customerId;
    private Money amount;

    public static Order create(OrderId id, CustomerId customerId, Money amount) {
        // business rules here
        return new Order(id, customerId, amount);
    }
}
```

✅ No Spring
✅ No Kafka
✅ No database annotations

---

## 6. Domain → Repository (Port)

In Clean Architecture, repositories are **interfaces in the domain or application layer**.

```java
public interface OrderRepository {
    void save(Order order);
}
```

---

## 7. Infrastructure Layer: Repository Implementation

Now we go back outward.

```java
@Repository
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository repository;

    @Override
    public void save(Order order) {
        repository.save(mapToEntity(order));
    }
}
```

---

## 8. Database Persistence

Finally, Spring Data JPA handles persistence.

```java
@Entity
public class OrderEntity {
    @Id
    private String id;
    private String customerId;
    private BigDecimal amount;
}
```

---

# 📊 Full Flow Summary

```
[Kafka Topic]
      ↓
[Kafka Consumer - Infrastructure]
      ↓
[Message Handler - Application]
      ↓
[Use Case - Application]
      ↓
[Domain Model - Domain Layer]
      ↓
[Repository Interface - Domain/Application]
      ↓
[Repository Implementation - Infrastructure]
      ↓
[Database]
```

---

# 🧠 Key DDD + Clean Architecture Principles Here

### 1. Separation of concerns

* Kafka ≠ business logic
* DTO ≠ domain model

---

### 2. Dependency rule (IMPORTANT)

Dependencies always point **inward**:

```
Infrastructure → Application → Domain
```

Never the opposite.

---

### 3. Mapping at boundaries

You map:

* Kafka DTO → Command
* Command → Domain
* Domain → Entity

---

# ⚠️ Common mistakes to avoid

❌ Putting `@KafkaListener` in your domain
❌ Using Kafka DTOs inside domain models
❌ Skipping use cases and calling repository directly
❌ Making domain depend on Spring

---

# ✅ Bonus: Optional improvements

### 🔹 Add validation in Application layer

Before creating domain objects

### 🔹 Use domain events

Your domain can emit events like:

```java
OrderCreatedEvent
```

### 🔹 Add transactional boundary

Use `@Transactional` in the **use case layer**, not the consumer

---

# 🧩 Mental model

Think of it like this:

* Kafka = **input adapter**
* Use case = **brain**
* Domain = **rules**
* Repository = **gateway**
* DB = **storage**

---

If you want, I can draw a **folder/package structure** or give you a **real Spring Boot project layout** for this setup.
