-- Setup environment
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';
SET default_table_access_method = heap;

-- Create messages table
CREATE TABLE public.messages (
                                 id SERIAL PRIMARY KEY,
                                 message_type VARCHAR(10) NOT NULL,
                                 author VARCHAR(255) NOT NULL,
                                 subject VARCHAR(255),
                                 body TEXT NOT NULL,
                                 "timestamp" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                 parent_id BIGINT,
                                 image_url VARCHAR(255),
                                 CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES public.messages(id) ON DELETE CASCADE
);

-- Create users table
CREATE TABLE public.users (
                              id SERIAL PRIMARY KEY,
                              username VARCHAR(255) NOT NULL UNIQUE,
                              hashed_pwd VARCHAR(255) NOT NULL
);