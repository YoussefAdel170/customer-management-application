# Customer Management Application – Documentation

## Table of Contents

1. [Backend (`customer-backend`)](#backend-customer-backend)
   - [Maven Configuration (`pom.xml`)](#maven-configuration-pomxml)
   - [Source Code (`src/main/java/com/yadel/customer`)](#source-code-srcmainjavacomyadelcustomer)
   - [Configuration Files (`src/main/resources`)](#configuration-files-srcmainresources)
2. [Desktop Client (`customer-client`)](#desktop-client-customer-client)
   - [Maven Configuration (`pom.xml`)](#maven-configuration-pomxml-1)
   - [Source Code (`src/main/java/com/yadel/customerclient`)](#source-code-srcmainjavacomyadelcustomerclient)
3. [Database Setup (`create_database.sql`)](#database-setup-create_databasesql)

---

## Backend (`customer-backend`)

### Maven Configuration (`pom.xml`)

- Project metadata (groupId, artifactId, version).
- Dependencies: Spring Boot Web, JDBC, Validation, MySQL Connector, Java 11.
- Build plugins: Spring Boot Maven Plugin (to run the application).

### Source Code (`src/main/java/com/yadel/customer`)

| Package / File                           | Description                                                                                                                                                                                                                                                                                                                                                                                                                         |
| ---------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`config/ApiKeyFilter.java`**           | Servlet filter that intercepts all requests and validates the `Authorization` header. Expects a Bearer token matching `api.key`. Returns `401 Unauthorized` if invalid or missing; allows `OPTIONS` requests for CORS preflight.                                                                                                                                                                                                    |
| **`controller/CustomerController.java`** | REST controller exposing endpoints under `/api/customers`. Maps HTTP verbs to service methods: <br>• `GET /` → `getAllCustomers()` <br>• `GET /{id}` → `getCustomerById()` <br>• `POST /` → `createCustomer()` (returns `201 Created`) <br>• `PUT /{id}` → `updateCustomer()` (returns `204 No Content`) <br>• `DELETE /{id}` → `deleteCustomer()` (returns `204 No Content`). Uses `@Valid` to trigger validation on request body. |
| **`model/Customer.java`**                | Entity/model class representing a customer. Fields: `id`, `name`, `email`, `phone`, `createdAt`. Getters/setters and validation annotations (`@NotBlank`, `@Email`).                                                                                                                                                                                                                                                                |
| **`repository/CustomerRepository.java`** | Data access layer using Spring `JdbcTemplate`. Methods: `findAll()`, `findById(Long)`, `save(Customer)`, `update(Customer)`, `deleteById(Long)`.                                                                                                                                                                                                                                                                                    |
| **`service/CustomerService.java`**       | Business logic layer. Calls repository methods, handles validation, throws `ResponseStatusException` with appropriate HTTP status (e.g., `404 Not Found`).                                                                                                                                                                                                                                                                          |
| **`CustomerApplication.java`**           | Main Spring Boot class. Annotated with `@SpringBootApplication`. Contains `main()` that starts the embedded Tomcat server and initializes the Spring context.                                                                                                                                                                                                                                                                       |

### Configuration Files (`src/main/resources`)

| File                         | Description                                                                                                                                                                                                                             |
| ---------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`application.properties`** | Spring Boot configuration. Sets server port (`8080`), MySQL connection URL (with `allowPublicKeyRetrieval=true`), database credentials, API key (`api.key=my-secret-token`). Enables automatic schema initialization from `schema.sql`. |
| **`schema.sql`**             | SQL script to create the `customers` table if it does not exist. Columns: `id` (auto‑increment primary key), `name`, `email`, `phone`, `created_at` (default `CURRENT_TIMESTAMP`). Executed once on application startup.                |

---

## Desktop Client (`customer-client`)

### Maven Configuration (`pom.xml`)

- Dependencies: Gson (for JSON parsing).
- Build plugins:
  - `maven-compiler-plugin` (Java 11).
  - `maven-jar-plugin` (specifies main class in manifest).
  - `maven-shade-plugin` (creates an executable JAR with all dependencies bundled).

### Source Code (`src/main/java/com/yadel/customerclient`)

| Package / File                        | Description                                                                                                                                                                                                                                                                          |
| ------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **`model/Customer.java`**             | Client-side data model mirroring the backend entity. Fields: `id`, `name`, `email`, `phone`, `createdAt`. Used for data transfer.                                                                                                                                                    |
| **`service/CustomerApiService.java`** | Handles all HTTP communication with the backend using `java.net.http.HttpClient`. Methods: `getAllCustomers()`, `createCustomer()`, `updateCustomer()`, `deleteCustomer()`. Adds `Authorization: Bearer my-secret-token` header to every request. Uses Gson to parse JSON responses. |
| **`CustomerClientApp.java`**          | Entry point of the Swing application. Uses `SwingUtilities.invokeLater` to create GUI on the Event Dispatch Thread. Sets system look and feel and displays the main window.                                                                                                          |
| **`ui/MainFrame.java`**               | Main JFrame window. Contains a `JTable` with customer data, `DefaultTableModel`, `TableRowSorter` for search filtering, a search text field with live filtering, and buttons (Add, Edit, Delete, Refresh). Uses `SwingWorker` for background API calls (displays loading cursor).    |
| **`ui/CustomerDialog.java`**          | `JDialog` for adding or editing a customer. Contains form fields (name, email, phone). Validates input (name not empty, email format). Returns the updated `Customer` object if the user confirms.                                                                                   |

---

## Database Setup (`create_database.sql`)

MySQL script that:

- Creates the database `customer_db` if it does not exist.
- Creates the `customers` table (same definition as `schema.sql`).
- Inserts two sample rows for testing.

Run this script once **before** starting the backend (if the database does not already exist).

---

## Notes

- The backend and client communicate exclusively via REST API – the client has **no direct database access**.
- The API is secured with a Bearer token; the same token (`my-secret-token`) is configured in both projects.
- The client uses Java 11’s built-in `HttpClient` and Gson for JSON parsing.
- All network operations in the client are performed in background threads with loading indicators.

---

**Author:** Youssef Adel
