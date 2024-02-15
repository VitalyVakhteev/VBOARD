# VBOARD
A Java Springboot web app based on an assignment that I got too carried away with.

Use Maven for dependencies.

Run API by running `src/main/java/VBOARD/VboardApplication.java`

Run Frontend Server by running `vboard-frontend/...`

You can add your own users or data by modifying/adding `data.json` and `users.txt` under `src/main/resources/`.
- If you are getting issues with the hash in `users.txt` and an invalid salt revision, make sure the third letter for each hash is an "a".