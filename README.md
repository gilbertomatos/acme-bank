# 🏦 Acme Bank — Spring Boot + JPA + Clean Architecture Example

> A complete and elegant example of a banking system built with **Java**, **Spring Boot**, **Spring Data JPA**, and **Clean Architecture**.

---

## ✨ Overview

**Acme Bank** is a small yet production-like banking system that demonstrates how to apply **Clean Architecture principles** in a modern Java application.

It provides basic banking operations:

- 📓 Create account
- 💰 Deposit
- 💸 Withdraw
- 🧾 Transaction history (statement)

All operations are designed to be **thread-safe** and **transactionally consistent**, even under **high concurrency** (many users transferring money simultaneously).

---

## 🧱 Architecture

This project follows the **Clean Architecture** pattern to achieve separation of concerns and high testability.

---

## 🧩 Tech Stack

| Category | Technology                    |
|-----------|-------------------------------|
| 🧠 Language | Java 21                       |
| ⚙️ Framework | Spring Boot 4                 |
| 💾 Persistence | Spring Data JPA (Hibernate)   |
| 🧱 Database | H2 (in-memory)                |
| 🧰 Build Tool | Maven                         |
| 🧪 Testing | JUnit 5, Spring Test, Mockito |

---

## 💡 Key Features

✅ **Immutability** with Java **Records** in the domain layer  
✅ **Pessimistic locking** for critical operations  
✅ **Transactional consistency**  
✅ **Spring Data JPA repositories** for persistence

---

## 🧠 Key Learnings

- Applying **Clean Architecture** in a real-world scenario
- Designing **immutable domain models** using Records
- Using **pessimistic locking** with Spring Data JPA
- Ensuring **data consistency** under high concurrency
---

🚀 Running the Application Prerequisites

- Java 21+
- Maven 3.9+

Steps

#### How to run
```bash
./mvnw clean install -DskipTests
./mvnw -pl acme-bank-app-rest-api -am spring-boot:run
```

#### How to create an account:
```bash
curl --location 'http://localhost:8080/api/accounts' --form 'accountNumber="<accountNumber>"'
```

#### How to make a deposit:
```bash
curl --location 'http://localhost:8080/api/accounts/<acount number>/deposit' --form 'amount="<amount>"'
```

#### How to get the account statement:
```bash
curl --location 'http://localhost:8080/api/accounts/<acount number>/statement'
```
#### How to make a withdrawal:
```bash
curl --location 'http://localhost:8080/api/accounts/<acount number>/withdraw' --form 'amount="<amount>"'
```
---

📜 License

This project is open-source under the MIT License.
Feel free to use, modify, and share it.

“Clean architecture is about independence: from frameworks, databases, UI, and external changes.”
— Robert C. Martin (Uncle Bob)

---
[![Java CI with Maven](https://github.com/gilbertomatos/acme-bank/actions/workflows/maven.yml/badge.svg)](https://github.com/gilbertomatos/acme-bank/actions/workflows/maven.yml)
