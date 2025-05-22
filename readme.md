#  Weather Sensor REST API

A Spring Boot-based REST API that simulates a weather monitoring system using sensors that track various metrics like temperature, humidity, and wind speed.

---

##  Problem Understanding

We are building a service that:

* Receives **weather metric data** from sensors.
* Allows querying **sensor metrics** based on:

  * One or more sensor IDs
  * One or more metric types
  * A statistical function (`min`, `max`, `average`, `sum`)
  * A time range (e.g., `7 days`, `1 month`)

---
##  Database Design

We are using SQLite as the database for this application. The database consists of the following tables:

###  `Sensor` Table

* `id` (String) — primary key
* `name` (String) — friendly name of the sensor

###  `MetricType` Table

* `name` (String) — e.g., `temperature`, `humidity`, `wind_speed`

###  `MetricData` Table

* `id` (Long) — primary key
* `sensor` (Sensor) — foreign key
* `metricType` (String)
* `value` (double)
* `timestamp` (LocalDateTime)

---

##  API Endpoints

###  Sensor API

####  Add Sensor

`POST /sensors`

```json
{
  "id": "S100",
  "name": "Main Sensor"
}
```

####  Get All Sensors List

`GET /sensors`

####  Get Sensor by ID

`GET /sensors/{id}`

---

###  Metric Type API

####  Add Metric Type

`POST /metrics`

```json
{
  "name": "wind_speed"
}
```

####  Get All Metric Types

`GET /metrics`

---

###  Metric Data API

####  Upload Sensor Data

`POST /metrics/upload`

```json
{
  "sensorId": "S100",
  "metrics": {
    "temperature": 24.5,
    "humidity": 78.0
  },
  "timestamp": "2025-05-20T10:30:00"
}
```

####  Query Metric Stats

`POST /metrics/query`

```json
{
  "sensorIds": ["S100","S200"],
  "metrics": ["temperature", "humidity"],
  "statistic": "average",
  "period": "7 days"
}
```
#### Sample Response of the Query
---
```json
{
    "sensorIds": [
        "S100",
        "S200"
    ],
    "metrics": {
        "temperature": 23.11904761904762,
        "humidity": 76.85
    },
    "statistic": "average",
    "period": "2 day"
}
```

##  Validation

* Every metric in the request must be **predefined** in `MetricType`
* Query/request fails if unknown metric is passed
* Sensor ID must be **predefined** in `Sensor` table while querying
* Sensor ID will created dynamically if not present in `Sensor` table while uploading data

---

##  Default Metrics (Initialized on startup)

* temperature
* humidity
* wind_speed

---

##  To Run

* Developed using **Java 24** with **Spring Initializr**.
* Database: **SQLite**.
* Build tool: **Maven**.
* IDE: **VS Code** with **Lombok** and **Spring Boot extensions**.

If using IntelliJ, you can run the tests directly from the IDE. [AnishsensorapiApplication.java](src/main/java/com/example/anishsensorapi/AnishsensorapiApplication.java)

Windows (PowerShell)
```powershell
```bash
.\mvnw.cmd spring-boot:run
```

Linux/Mac
```bash
./mvnw spring-boot:run
```

App runs at: `http://localhost:8080`

---
##  Postman Collection

* [Postman Collection](./readme_assests/Java-sensor-backend-api.postman_collection.json)

---
##  Testing

*  Tests are written using **JUnit** and **Mockito** and **Spring Boot Test**.

*  To run the tests, use the following command:

In terminal or command prompt:
```bash
./mvnw test
```
If using IntelliJ, you can run the tests directly from the IDE. [AnishsensorapiApplicationTests.java](src/test/java/com/example/anishsensorapi/AnishsensorapiApplicationTests.java)

---

##  Tech Stack

* Java 17/21/24
* Spring Boot 3.4+
* Hibernate ORM (JPA)
* SQLite DB
* Maven
* Lombok

---

##  Video Demonstration

[![Demo of REST APIs](./readme_assests/Demo%20of%20Rest%20APIs.mp4)](./readme_assests/Demo%20of%20Rest%20APIs.mp4)  
[![Demo of Running Tests](./readme_assests/Demo%20of%20Running%20Test.mp4)](./readme_assests/Demo%20of%20Running%20Test.mp4)

### References

Here are some of the resources that were helpful in building this project:

* [Using SQLite with Spring Boot](https://www.baeldung.com/spring-boot-sqlite)
* [Testing Spring Boot Applications](https://reflectoring.io/spring-boot-test/)
* [Unit Testing in Spring Boot](https://reflectoring.io/unit-testing-spring-boot/)
* [Using Predicate in Java 8](https://howtodoinjava.com/java8/how-to-use-predicate-in-java-8/)
* [AI-Powered Regex Generator](https://workik.com/ai-powered-regex-generator)
* [Spring Boot Guides](https://spring.io/guides/gs/spring-boot)


Built by Anish M Raghavendra
