BuildingStore

Backend for an online building materials store, implemented with Java and Spring Boot.
Exposes a JWT-protected REST API for managing products, categories, roles, orders, carts, and authentication.

Server URL:

http://localhost:8080

 Features

Product catalog

Full CRUD

Quantity, price, and discount updates

Search and advanced filtering

Category management

CRUD operations

Category renaming

Role management

Role creation

Role assignment to users

Orders

Order creation and retrieval

Order status updates

Shopping cart

Per-user cart

Add, update, delete items

Clear cart

Authentication & Authorization

User registration and login

JWT-based authentication

Role-based access control

Pagination & filtering

Pageable listings

Filtered endpoints

🛠️ Tech Stack

Java 17+

Spring Boot

Spring Web

Spring Data JPA

Spring Security

Maven

Database

H2 (development)

MySQL or PostgreSQL (production)

JWT for authentication

 Quick Start (Windows / IntelliJ)

Open the project in IntelliJ IDEA

Configure:

src/main/resources/application.yml


or

src/main/resources/application.properties


Run the main Application class
or use Maven:

mvn clean package
mvn spring-boot:run

 Authentication
Register
POST /api/auth/register


Body:

{
  "name": "John Doe",
  "email": "user@example.com",
  "password": "password"
}

Login
POST /api/auth/login


Body:

{
  "email": "user@example.com",
  "password": "password"
}


Returns a JWT token.

Authorization Header
Authorization: Bearer <token>

 API Endpoints (Summary)
Product Controller

GET /api/products/{id} – Get product by ID

GET /api/products – List products (pagination & sorting)

POST /api/products – Create product (ADMIN)

PUT /api/products/{id} – Update product (ADMIN)

DELETE /api/products/{id} – Delete product (ADMIN)

PATCH /api/products/{id}/quantity – Update stock (ADMIN)

PATCH /api/products/{id}/price – Update price (ADMIN)

PATCH /api/products/{id}/discount – Apply/update discount (ADMIN)

GET /api/products/search – Search by name/term

GET /api/products/filtered – Filter by attributes

GET /api/products/discounted – List discounted products

GET /api/products/by-price-range – Price range filter

GET /api/products/by-category – Products by category

Category Controller

GET /api/categories/{id} – Get category

GET /api/categories – List categories

POST /api/categories – Create category (ADMIN)

PUT /api/categories/{id} – Update category (ADMIN)

DELETE /api/categories/{id} – Delete category (ADMIN)

PATCH /api/categories/{id}/name – Rename category (ADMIN)

Role Controller

POST /api/roles – Create role (ADMIN)

POST /api/roles/assign – Assign role to user (ADMIN)

Order Controller

GET /api/orders – List orders (user or ADMIN)

POST /api/orders – Create order (authenticated user)

GET /api/orders/{orderId} – Get order by ID (owner or ADMIN)

GET /api/orders/user/{userId} – User orders (owner or ADMIN)

PATCH /api/orders/{orderId}/status – Update order status (ADMIN)

Cart Controller

GET /api/cart – Get current cart

POST /api/cart/items – Add item to cart

PATCH /api/cart/items/{itemId} – Update item quantity

DELETE /api/cart/items/{itemId} – Remove cart item

DELETE /api/cart – Clear cart

Auth Controller

POST /api/auth/register – Register user

POST /api/auth/login – Login and receive JWT

 Sample Requests
Login
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}

Create Product (ADMIN)
POST /api/products
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "name": "Cement 50kg",
  "description": "High quality cement",
  "price": 12.5,
  "stock": 120,
  "categoryId": 1
}

Update Product Price
PATCH /api/products/123/price
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "price": 14.0
}

Create Order
POST /api/orders
Authorization: Bearer <token>
Content-Type: application/json

{
  "cartId": 5,
  "shippingAddress": "..."
}

 Data Model (High Level)

Product

id, name, description, price, stock

category, discount, createdAt, updatedAt

Category

id, name, parentCategory

User

id, name, email, passwordHash

roles, addresses

Order

id, user, items (product, qty, price)

total, status, createdAt

Cart

id, user, items

 Notes & Best Practices

Protect admin endpoints with role-based authorization

Store passwords securely using BCrypt

Use @Valid for request validation

Handle errors centrally with @ControllerAdvice

Enable pagination for all list endpoints

Store production secrets (DB credentials, jwt.secret) in environment variables

Never commit secrets to source control
