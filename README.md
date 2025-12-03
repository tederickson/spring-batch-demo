# Demo using Spring Batch to read a file and store the contents in the database.

# Run the Application

Run the following command in a terminal window:

```bash
mvn clean spring-boot:run
```

The H2 database console address is [h2-console](http://localhost:8080/h2-console/)

# Postman initiates the import
Postman sends a POST to perform the Person import

```
http://localhost:8080/jobs/import/person
```
