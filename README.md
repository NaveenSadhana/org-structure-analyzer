
## **Org Structure Analyzer** 📊

✅ **A REST service has been added to help you run and test more scenarios easily. The main logic is implemented in the `EmployeeAnalyzerService.readCSV()` method.**

This is a **Spring Boot application** that analyzes the organizational structure of a company based on employee data. It ensures that managers earn within an acceptable salary range compared to their subordinates and identifies employees with excessively long reporting lines.

---
## **Assumptions**

- **The CSV file is well-formed** and follows the specified format.
- **The salary range for managers** is hardcoded in the service.
- **The reporting depth limit** is hardcoded in the service.
- **The default CSV file** is used if no file is uploaded.
- **The service is stateless** and does not store any data between requests.
- **The service is not optimized** for large datasets and may have performance issues with a large number of employees.
- **The service is not secure** and does not handle authentication or authorization.
- **The service is not production-ready** and may require additional testing and validation before deployment.
- **The service is a prototype** to demonstrate the functionality and can be extended further based on requirements.
- **The service is a standalone application** and does not integrate with other systems or databases.
- **The service is a proof of concept** and may require additional features and improvements for real-world use cases.

## **Features** 🚀

✅ **Upload a CSV file via REST API**  
✅ **Validates CSV format** (Checks headers, numeric values)  
✅ **Throws an error if the file is invalid**  
✅ **Uses `@ControllerAdvice` for centralized exception handling**  
✅ **Returns meaningful error messages in JSON format**  
✅ **Uses a default CSV file if no file is uploaded**  

---
## **Environment Setup** 🛠️

- **Java 21+** (JDK)
- **Maven 3.8+** (Dependency Management)
- **Spring Boot 3.2.0** (Spring MVC, REST)
- **JUnit 5.8.0** (Unit Testing)

## **Project Structure** 🏗️

The project follows the **Spring Boot MVC architecture**.

```
src/main/java/com/bigcompany/organizer/
│── controller/
│   ├── EmployeeController.java
│
│── service/
│   ├── EmployeeAnalyzerService.java
│   ├── CSVValidator.java
│
│── exception/
│   ├── InvalidCSVException.java
│   ├── GlobalExceptionHandler.java

│── OrgStructureAnalyzerApplication.java  (Main class)
```
## **Installation & Setup** ⚙️

## **1️⃣ Clone the Repository**
```sh
git clone https://github.com/your-repo/org-structure-analyzer.git
cd org-structure-analyzer
```

**2️⃣ Build the Project using Maven**
```sh
mvn clean install
```

### **3️⃣ Run the Application**
```sh
mvn spring-boot:run
```

---

## **API Endpoints** 🔗

### **📤 Upload CSV File**
- **URL:** `POST /api/upload`
- **Description:** Uploads a CSV file for analysis. If no file is uploaded, it uses the default CSV.
- **Request:**
  ```
  Content-Type: multipart/form-data
  file: (CSV File)
  ```
- **Example using `curl`:**
  ```sh
  curl -X POST -F "file=@/path/to/employees.csv" http://localhost:8080/api/upload
  ```
  - **Response (JSON format):**
    ```json
        {
          "underpaidManagers": [
              "Manager sdfe earns 10000.00 less than required",
              "Manager nbtft earns 130000.00 less than required",
              "Manager Martin earns 15000.00 less than required",
              "Manager Grap earns 10000.00 less than required",
              "Manager sdf earns 10000.00 less than required",
              "Manager ergdr earns 10000.00 less than required",
              "Manager swef earns 1330000.00 less than required"
          ],
          "excessiveDepthEmployees": [
              "Employee wedsf:304 has a reporting depth of 6, which is 2 too many",
              "Employee sdfe:305 has a reporting depth of 7, which is 3 too many",
              "Employee nbtft:306 has a reporting depth of 8, which is 4 too many",
              "Employee zdfsd:307 has a reporting depth of 9, which is 5 too many",
              "Employee srdgdf:308 has a reporting depth of 10, which is 6 too many",
              "Employee swef:303 has a reporting depth of 5, which is 1 too many"
          ],
          "overpaidManagers": [
              "Manager wedsf earns 1075000.00 more than allowed",
              "Manager zdfsd earns 75000.00 more than allowed"
          ]
      }
    ```

---

## **CSV File Format** 📄

The input CSV file must have the following columns:

```
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300
```

- `Id` → Unique Employee ID
- `firstName` → Employee's first name
- `lastName` → Employee's last name
- `salary` → Employee's salary
- `managerId` → ID of the employee’s direct manager (CEO has no manager)

---

## **Error Handling** ⚠️

If an invalid CSV is uploaded, the API will return an error response:

```json
{
  "error": "Invalid CSV header. Expected: Id,firstName,lastName,salary,managerId"
}
```

If an unexpected error occurs:

```json
{
  "error": "An unexpected error occurred: [error details]"
}
```

---

## **Technologies Used** 🛠️

- **Java SE** (Any version)
- **Spring Boot** (Spring MVC, REST)
- **Maven** (Dependency Management)
- **JUnit** (Unit Testing)

---

## **License** 📜

This project is **open-source** and available under the **MIT License**.

---
## **Improvements**

- **Concurrent processing** to handle multiple requests simultaneously.
- **Add more unit tests** to cover edge cases and improve code coverage.
- **Add more validation checks** for the CSV file.
- **Add more error handling** for different scenarios.
- **Add more features** to the service, such as exporting the results to a CSV file.
- **Add more logging** to track the application's behavior.
- **Add more documentation** to explain the project's architecture and design decisions.
- **Add more REST endpoints** to support additional functionalities.

