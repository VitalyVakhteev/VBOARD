CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       hashed_pwd VARCHAR(255) NOT NULL
);

CREATE TABLE messages (
                          id SERIAL PRIMARY KEY,
                          message_type VARCHAR(10) NOT NULL,
                          author VARCHAR(255) NOT NULL,
                          subject VARCHAR(255),
                          body TEXT NOT NULL,
                          timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          parent_id INTEGER NULL,
                          CONSTRAINT fk_parent
                              FOREIGN KEY(parent_id)
                                  REFERENCES messages(id)
                                  ON DELETE CASCADE
);
