# Demo using Spring Batch to read a file and store the contents in the database.
Workflow:
1. User sends a request to import a file into the database
2. Request is validated to ensure file exists
3. Job is launched with parameters
4. Each line of the file is read and turned into a Person
5. The phone number is validated.  Invalid phones are logged and not placed in the database.
6. Valid Person objects are stored in the database
7. Controller returns import job statistics
   * Batch Status
   * number of rows inserted
   * number of rows rejected
   * duration

Rerunning the job updates existing database entries.

Processing a 1000 line file takes about 343 microseconds.

Reprocessing the file takes about 182 microseconds to update the records.

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
The body looks like
```json
{
    "fileName": "people-1000.csv"
}
```

