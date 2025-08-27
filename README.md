# poc_automated_test_a_b

This project is a **Proof of Concept (POC)** to implement an **Automated A/B Test** using **Spring Boot** and **Java**.

---

## 📌 Project Overview

The application simulates A/B testing experiments by exposing REST APIs to evaluate and compare two different approaches (Experiment A and Experiment B).  
The goal is to measure performance differences (e.g., execution time in microseconds) and provide summary results for decision-making.

---

## 🛠️ Technologies Used

- **Java 17+**
- **Spring Boot**
- **Spring Web**
- **Maven/Gradle**
- **JUnit 5** for testing

---

## 📂 Package Structure

```
com.myprojecticaro.poc_automated_test_a_b
│
├── controller       # REST Controllers exposing endpoints
├── service          # Business logic and A/B experiment services
├── model            # DTOs and response objects
└── PocAutomatedTestABApplication.java   # Main application entrypoint
```

---

## 🚀 How to Run the Application

### 1. Clone the repository
```bash
git clone https://github.com/your-username/poc_automated_test_a_b.git
cd poc_automated_test_a_b
```

### 2. Build the project
Using Maven:
```bash
mvn clean install
```

Using Gradle:
```bash
./gradlew build
```

### 3. Run the application
```bash
mvn spring-boot:run
```
or
```bash
./gradlew bootRun
```

---

## 📡 API Endpoints

### 🔹 Run Experiment A
**POST** `/experiments/a`  
Request Body:
```json
{
  "dungeon": [[0]]
}
```
Response:
```json
{
  "result": "some-value"
}
```

### 🔹 Run Experiment B
**POST** `/experiments/b`  
Request Body:
```json
{
  "dungeon": [[0]]
}
```
Response:
```json
{
  "result": "some-value"
}
```

### 🔹 Experiment Summary
**GET** `/experiments/summary`  
Response:
```json
{
  "countA": 10,
  "countB": 12,
  "avgMicros": 1532
}
```

---

## ✅ A/B Test Workflow

1. A client sends requests to both `/experiments/a` and `/experiments/b`.
2. The service records the **execution count** and **average runtime (µs)**.
3. Access `/experiments/summary` to get aggregated results of the A/B test.

---

## 🧪 Running Tests

To run the unit and integration tests:
```bash
mvn test
```
or
```bash
./gradlew test
```

---

## 📈 Example Usage of A/B Test

1. Send 100 requests to `/experiments/a`.
2. Send 100 requests to `/experiments/b`.
3. Check `/experiments/summary` to compare performance.

This allows automated benchmarking between the two experiment versions.

---
