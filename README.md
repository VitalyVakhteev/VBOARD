# VBOARD
An open-source Java Spring Boot imageboard.

## Version: 1.0.3
Features:

- Topic and Reply system with messages including:
  - IDs
  - Authors
  - Timestamps
  - Images
- Registering users, logging in
- Requests performed through Postgres
- Ability to add links with <a> html tag
- Reference other messages with #{id} in body

Plans:

- Moderation (also includes role management)
- Pagination

# Building the VBOARD locally
Requires:
- Java 21
- NPM
- Maven
- All dependencies under pom.xml

Use Maven for dependencies.
Make sure to run the Postgres DB through `docker-compose.yml`, with `docker-compose up -d`.
If you don't have data immediately initialized, use the .sql file in the `psql` folder under `devdb`
Then run the appropriate command referencing that .sql file using `psql` in your terminal of choice.

Run the API by running `src/main/java/VBOARD/VboardApplication.java`

Finally, run the Frontend Server by running `npm start` with your current working directory as `vboard-frontend/...`
