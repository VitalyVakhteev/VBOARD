--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2 (Debian 16.2-1.pgdg120+2)
-- Dumped by pg_dump version 16.1

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

--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    id integer NOT NULL,
    message_type character varying(10) NOT NULL,
    author character varying(255) NOT NULL,
    subject character varying(255),
    body text NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    parent_id integer
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.messages_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.messages_id_seq OWNER TO postgres;

--
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.messages_id_seq OWNED BY public.messages.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    username character varying(255) NOT NULL,
    hashed_pwd character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: messages id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages ALTER COLUMN id SET DEFAULT nextval('public.messages_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messages (id, message_type, author, subject, body, "timestamp", parent_id) FROM stdin;
1	Topic	ricq7	Your favorite TV show	So guys, what is your favorite tv show?	2023-10-12 05:05:17.128692	\N
2	Topic	messi	Towel Day	Bring your towels on May 25.\\nSee here: http://www.towelday.org/	2023-10-17 17:55:17.128692	\N
3	Topic	vitalyv	Favorite Movie	Another member mentioned they had a favorite show. But what about a favorite movie?	2024-02-16 19:59:27.331051	\N
4	Topic	testregister	Just registered	Just registered on the forum. What should I do?	2024-02-16 21:12:41.651931	\N
5	Reply	mike	Re: Your favorite TV show	Game of Thrones is an awesome show but there are too many characters to remember.	2023-10-14 03:45:17.128692	1
6	Reply	ali87	Re: Your favorite TV show	Whose line is it anyway!!!	2023-10-13 02:10:17.128692	1
7	Reply	ricq7	Re: Your favorite TV show	Well the book is one hell of a resource.	2023-10-14 20:05:17.128692	1
8	Reply	mike	Re: Your favorite TV show	I'll check that. Thanks.	2023-10-15 21:20:17.128692	1
9	Reply	vitalyv	Re: Favorite Movie	I love the movie Fight Club.	2024-02-16 20:00:18.128692	3
10	Reply	vitalyv	Re: Just registered	You should definitely post more.	2024-02-21 17:45:18.296916	4
11	Reply	vitalyv	Re: Your favorite TV show	Honestly, I really like House Md.	2024-02-21 17:45:35.501472	1
12	Topic	vitalyv	Hello World!	Hello world!	2024-02-21 17:45:43.828851	\N
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, username, hashed_pwd) FROM stdin;
1	ali87	$2a$12$tpBRAWAxHXp3Xlj0SRfkxeCi..UJ0ePWLkGqoJuWx9TR.rRg2/3AW
2	ricq7	$2a$12$BHG7Bc3q7qzVu9aAGM5aveMMHR8Dk5rEKoqqvsdSW7NfmmevCjS7K
3	messi	$2a$12$oLrZz5M4xMtr1xQ71LrUPuqzJnoe4p3Ri2IrMu3Gg.CluSqMPr2H.
4	mike	$2a$12$oHQppzObnHERQEjbnzcV9.0WVMvppjxCuEOgbE7Dqm14uJtjZ16.u
5	jenny	$2a$12$23By41iikAqYW/NEJRmrZO40kUDGr6h1SwMBMkX2sX2YSZ.btXV/S
6	vitalyv	$2a$12$OtZqnZvccgw3x1fAXrq/xOwjJm/KRQvwLJDhQiP3x1aHp6Die0oBm
7	testuser123	$2a$12$VGFqrRT09uy.FSjk1VSypeJXquLhhMGyYHOwttfrv4uiZ0D15.No2
\.


--
-- Name: messages_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.messages_id_seq', 12, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 7, true);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: messages fk_parent; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES public.messages(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

