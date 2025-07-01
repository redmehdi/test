# Pilotes Ordering API

This project provides a simple REST API to manage orders of Miquel Montoro's *pilotes*.

## Requirements
- Java 21
- Maven
- Lombok plugin for your IDE

## Running
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`. H2 console is available at `/h2-console`.

## Authentication
Only the search endpoint is secured using HTTP Basic authentication.
Default credentials:
- user: `user`
- password: `password`

## API
- `POST /orders` – create order
- `PUT /orders/{id}` – update order within 5 minutes of creation
- `GET /orders/search?name=...` – search orders by client name (requires auth)

## Tests
Run tests using:
```bash
mvn test
```
