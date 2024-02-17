# VBOARD
A Java Springboot web app based on an assignment that I got too carried away with.

Use Maven for dependencies.

Run API by running `src/main/java/VBOARD/VboardApplication.java`

Run Frontend Server by running `vboard-frontend/...`

You can add your own users or data by modifying/adding `data.json` and `users.txt` under `src/main/resources/`.
- **NOTE:** If you are getting issues with the hash in `users.txt` and an invalid salt revision, make sure the third letter for each hash is an "a".
## Version: 0.0.3
Features:

- Registering users, logging in
- Adding new topics
- Replying to topics
- Messages contain IDs, Author, Timestamp

Plans:

- Add body scanning (to detect links, message mentions, etc.)
- Images in messages
- Moderation
- Pagination
- Rework data storage to MySQL