# Micronaut Oracle REST API

This is a RESTful API built with Micronaut framework and Oracle database. It demonstrates how to create a REST service with GraalVM support for native compilation and Oracle database persistence.

## Prerequisites

- Java 21 or higher
- Maven
- Oracle Database (XE, Standard, or Enterprise Edition)
- GraalVM (optional, for native image compilation)

## Database Setup

Before running the application, you need to set up the Oracle database:

1. Install Oracle Database (XE is a good option for development)
2. Create a new user and grant necessary privileges:

```sql
-- Connect as SYSDBA
ALTER SESSION SET CONTAINER=XEPDB1;
CREATE USER restuser IDENTIFIED BY password;
GRANT CONNECT, RESOURCE, CREATE SESSION, CREATE TABLE, CREATE VIEW TO restuser;
ALTER USER restuser QUOTA UNLIMITED ON USERS;
```

3. Update the database connection details in `src/main/resources/application.properties` if necessary:

```properties
datasources.default.url=jdbc:oracle:thin:@//localhost:1521/XEPDB1
datasources.default.username=restuser
datasources.default.password=password
```

## Running the Application

### JVM Mode

```bash
./mvnw mn:run
```

### Native Image (GraalVM)

To build a native executable:

```bash
./mvnw package -Dpackaging=native-image
```

Then run the native executable:

```bash
./target/restapi
```

## Running Tests

The project includes a comprehensive test suite that validates the API endpoints, repository operations, and data validation. The tests use an H2 in-memory database to avoid the need for a real Oracle instance during testing.

To run the tests:

```bash
./mvnw test
```

The test suite includes:

1. **ProductControllerTest**: Tests all REST endpoints (GET, POST, PUT, DELETE)
2. **ProductRepositoryTest**: Tests database operations through the repository
3. **ProductValidationTest**: Tests validation constraints on the Product model
4. **ProductControllerValidationTest**: Tests error handling for invalid inputs

## API Endpoints

The application exposes the following REST endpoints:

- `GET /products` - Get all products
- `GET /products/{id}` - Get a product by ID
- `POST /products` - Create a new product
- `PUT /products/{id}` - Update a product
- `DELETE /products/{id}` - Delete a product

### Example Requests

#### Create a Product
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Product 1","description":"Description for Product 1","price":19.99}'
```

#### Get all Products
```bash
curl -X GET http://localhost:8080/products
```

#### Get a Product by ID
```bash
curl -X GET http://localhost:8080/products/1
```

#### Update a Product
```bash
curl -X PUT http://localhost:8080/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Product","description":"Updated description","price":29.99}'
```

#### Delete a Product
```bash
curl -X DELETE http://localhost:8080/products/1
```

## Docker Support

This project includes Docker support. To build and run the application in a Docker container:

```bash
./mvnw package
docker build -t restapi .
docker run -p 8080:8080 restapi
```

For native Docker image:

```bash
./mvnw package -Dpackaging=native-image
docker build -t restapi -f Dockerfile.native .
docker run -p 8080:8080 restapi
```

## Micronaut 4.7.6 Documentation

- [User Guide](https://docs.micronaut.io/4.7.6/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.7.6/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.7.6/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)


## Feature data-jdbc documentation

- [Micronaut Data JDBC documentation](https://micronaut-projects.github.io/micronaut-data/latest/guide/index.html#jdbc)


## Feature test-resources documentation

- [Micronaut Test Resources documentation](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/)


## Feature jdbc-hikari documentation

- [Micronaut Hikari JDBC Connection Pool documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc)


## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


