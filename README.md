## Reward Service

The **Reward Service** application is a Spring Boot–based rewards calculation system designed to demonstrate reward point logic for customer transactions.

---

## Overview

A retailer offers a rewards program to its customers, awarding points based on each recorded purchase:

- A customer receives **2 points** for every dollar spent **over $100** in each transaction.
- A customer receives **1 point** for every dollar spent **between $50 and $100** in each transaction.

**Example:**
> A $120 purchase = (2 × $20) + (1 × $50) = **90 points**

Given a record of transactions over a **three-month period**, the application calculates the **reward points earned per customer per month** and the **total points** across all months.

---

## Design

### **Endpoint**

```

GET {URL}/api/rewards/{customerId}

````

Retrieves the rewards summary for a specific customer by their unique ID.

---

### **Schemas**

#### `RewardSummary`
| Field | Type | Description | Example |
|-------|------|-------------|----------|
| `customerId` | String | Unique identifier of the customer | `"customerId"` |
| `customerName` | String | Customer’s full name | `"John Smith"` |
| `monthlyPoints` | Map<Integer, Integer> | 3-month breakdown of earned points (key = month number) | `{ "8": 120, "9": 300, "10": 150 }` |
| `totalPoints` | Integer | Total number of points earned | `750` |

---

#### `Customer`
| Field | Type | Description | Example |
|-------|------|-------------|----------|
| `id` | String | Unique identifier of the customer | `"customerId"` |
| `name` | String | Customer’s full name | `"John Smith"` |

---

#### `Transaction`
| Field | Type | Description | Example |
|-------|------|-------------|----------|
| `id` | String | Unique identifier of the transaction | `"transactionId"` |
| `customerId` | String | Identifier linking to the customer | `"customerId"` |
| `date` | String | Transaction date (`YYYY-MM-DD`) | `"2025-10-01"` |
| `amount` | Double | Total amount of the transaction | `120.25` |

---

## Running the Project Locally

The project is configured to run locally using Gradle.

```bash
./gradlew clean build bootRun
````

Once running, the API will be available at:

```
http://localhost:8080/api/rewards/{customerId}
```

---

## Assumptions

The project’s requirements were intentionally open-ended.
Without detailed specifications, the implementation focuses on:

* Returning a specific customer’s **points breakdown** when provided with their unique ID.
* Providing a **Proof of Concept (POC)** that can be extended later.

---

## Technology

* **Spring Boot** – Application framework
* **Spring Data JPA** – ORM for data access
* **Lombok** – Reduces boilerplate code
* **Swagger / OpenAPI** – API documentation (available at `/swagger-ui/index.html`)
* **H2 Database** – In-memory database for testing and demo purposes

  * Prepopulated with sample data via `data.sql`

---

## Future Enhancements

This initial version is a simplified **proof of concept**. Potential future improvements include:

* `PUT /api/transactions` – Add new transactions dynamically
* `GET /api/rewards` – Retrieve all customer summaries
* Authentication & authorization
* Persistent database support (PostgreSQL, MySQL, etc.)
* Frontend dashboard integration for reward visualization
