# Payment Gateway Simulation

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-005C84)

Demo project built with Java 17, Spring Boot, and MySQL to simulate a simple payment gateway workflow.

## Features

- Create simulated payment transactions
- Store payments in MySQL using Spring Data JPA
- Return `SUCCESS`, `FAILED`, or `PENDING` based on simple demo rules
- Fetch all payments, or look up a payment by ID or merchant order ID
- Validate request payloads and return friendly API errors

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- MySQL
- Maven

## Project Structure

```text
src/main/java/com/demo/paymentgateway
|- controller
|- dto
|- entity
|- exception
|- repository
|- service
```

## MySQL Setup

Create a MySQL instance and make sure the credentials in `src/main/resources/application.yml` match your local setup.

Default configuration:

- Database: `payment_gateway_demo`
- Username: `root`
- Password: `root`
- Port: `3306`

You can also start MySQL with Docker:

```bash
docker compose up -d
```

## Run the Project

```bash
mvn spring-boot:run
```

If Maven is not installed on your machine yet, install Maven first and then run the command above.

GitHub Actions will also run the test suite automatically on pushes and pull requests.

Application base URL:

```text
http://localhost:8080
```

## API Endpoints

### Health

`GET /`

### Create Payment

`POST /api/payments`

Sample request:

```json
{
  "merchantOrderId": "ORD-10001",
  "amount": 1499.00,
  "currency": "INR",
  "paymentMethod": "CARD",
  "paymentSource": "4111111111111111"
}
```

Simulation rules:

- `paymentSource` containing `FAIL` or ending with `0000` => `FAILED`
- amount greater than or equal to `50000` or source containing `PENDING` => `PENDING`
- everything else => `SUCCESS`

### Get All Payments

`GET /api/payments`

### Get Payment By ID

`GET /api/payments/{id}`

### Get Payment By Merchant Order ID

`GET /api/payments/merchant/{merchantOrderId}`

## Test

```bash
mvn test
```

## Suggested GitHub Description

Spring Boot + MySQL demo project that simulates payment gateway transactions with success, failure, and pending flows.
