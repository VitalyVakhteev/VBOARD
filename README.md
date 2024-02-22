# VBOARD
A Java Spring Boot image based on an assignment that I got too carried away with.

Use Maven for dependencies.

Make sure to run the Postgres server through `docker-compose.yml`.
If you don't have data immediately initialized, use the .sql file in the `psql` folder under `src/main/java/VBOARD/` 

Run API by running `src/main/java/VBOARD/VboardApplication.java`

Run Frontend Server by running npm start with your current working directory as `vboard-frontend/...`

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
