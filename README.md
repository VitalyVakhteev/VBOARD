# VBOARD
A Java Spring Boot imageboard based on an assignment that I got too carried away with.

## Version: 0.0.4
Features:

- Registering users, logging in
- Adding new topics
- Replying to topics
- Messages contain IDs, Author, Timestamp
- Requests performed through Postgres

Plans:

- Add body scanning (to detect links, message mentions, etc.)
- Images in messages
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
If you don't have data immediately initialized, use the .sql file in the `psql` folder under `src/main/java/VBOARD/`
Then run the appropriate command referencing that .sql file using `psql` in your terminal of choice.

Run the API by running `src/main/java/VBOARD/VboardApplication.java`

Finally, run the Frontend Server by running `npm start` with your current working directory as `vboard-frontend/...`
