# 🛠️ Issues & Solutions You Could Face

## Customer Management Application

> **Project:** Spring Boot Backend + Swing Client  
> **Prepared by:** Youssef Adel
> <br>
> **Date:** 2026-05-23

---

## Overview

During the setup and execution of the Customer Management Application, several technical issues were encountered. This document details each problem, its root cause, and the solution applied.

---

## Issue #1 — MySQL Command Not Recognized in PowerShell

**Error**

```
mysql : The term 'mysql' is not recognized as the name of a cmdlet...
```

**Root Cause**  
MySQL was not installed, or its `bin` directory was missing from the `PATH` environment variable.

**Solution**

1. Downloaded and installed **MySQL Community Server**.
2. Added the MySQL `bin` folder to the system `PATH`:
   ```
   C:\Program Files\MySQL\MySQL Server 8.0\bin
   ```
3. Verified the installation:
   ```bash
   mysql --version
   ```

---

## Issue #2 — PowerShell Does Not Support `<` Redirection

**Error**

```
The '<' operator is reserved for future use.
```

**Root Cause**  
PowerShell does not support input redirection using `<` — that syntax only works in Command Prompt (`cmd.exe`).

**Solution**

Use `Get-Content` with a pipe in PowerShell:

```powershell
Get-Content create_database.sql | mysql -u root -p
```

Or switch to Command Prompt:

```cmd
mysql -u root -p < create_database.sql
```

---

## Issue #3 — Maven Compilation Error: "No compiler is provided"

**Error**

```
No compiler is provided in this environment.
Perhaps you are running on a JRE rather than a JDK?
```

**Root Cause**  
Only a Java Runtime Environment (JRE) was installed — the Java compiler (`javac`) is only included in a full JDK.

**Solution**

1. Installed **Eclipse Temurin JDK 11**.
2. Set the `JAVA_HOME` environment variable to the JDK installation folder.
3. Added `%JAVA_HOME%\bin` to the `PATH`.
4. Verified:
   ```bash
   javac -version
   ```

---

## Issue #4 — Filename Mismatch: `customerApplication.java`

**Error**

```
class CustomerApplication is public, should be declared in a file named CustomerApplication.java
```

**Root Cause**  
The file was saved as `customerApplication.java` (lowercase `c`), but Java requires the filename to **exactly match** the public class name — including case.

**Solution**

Renamed the file using File Explorer or PowerShell:

```powershell
ren customerApplication.java CustomerApplication.java
```

---

## Issue #5 — MySQL Connection Error: "Public Key Retrieval is not allowed"

**Error**

```
java.sql.SQLNonTransientConnectionException: Public Key Retrieval is not allowed
```

**Root Cause**  
MySQL 8 uses the `caching_sha2_password` authentication plugin. By default, the JDBC driver blocks public key retrieval for security reasons.

**Solution**

Added `allowPublicKeyRetrieval=true` to the JDBC URL in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/customer_db\
  ?useSSL=false\
  &serverTimezone=UTC\
  &allowPublicKeyRetrieval=true
```

---

## Issue #6 — Client Compilation Error: Method Not Found

**Error**

```
cannot find symbol method getSystemLookAndFeelFeel()
```

**Root Cause**  
Typo in the method name. The correct `UIManager` method is `getSystemLookAndFeelClassName()`.

**Solution**

In `CustomerClientApp.java`, changed:

```java
// ❌ Before (typo)
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelFeel());
```

```java
// ✅ After (correct)
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
```

---

## Issue #7 — Client JAR Fails to Find Main Class

**Error**

```
Error: Could not find or load main class com.yadel.customerclient.CustomerClientApp
Caused by: java.lang.ClassNotFoundException: ...
```

**Root Cause**  
The JAR built by `mvn package` lacked a `Main-Class` entry in its manifest, and dependencies (e.g., Gson) were not bundled.

**Solution**

**Option A — Quick Test (run directly with Maven):**

```powershell
mvn compile exec:java "-Dexec.mainClass=com.yadel.customerclient.CustomerClientApp"
```

**Option B — Runnable Uber-JAR:**

1. Added the `maven-shade-plugin` to `pom.xml` to bundle all dependencies and set the manifest entry.
2. Rebuilt:
   ```bash
   mvn clean package
   ```
3. Ran the JAR:
   ```bash
   java -jar target/customer-client-1.0.0.jar
   ```

---

## ✅ Outcome

After applying all fixes:

| Component              | Status                                                   |
| ---------------------- | -------------------------------------------------------- |
| Spring Boot backend    | Starts successfully on port `8080` and connects to MySQL |
| Swing client           | Launches and loads customer data via the REST API        |
| CRUD operations        | Fully functional with search support                     |
| Test task requirements | All requirements met ✓                                   |
