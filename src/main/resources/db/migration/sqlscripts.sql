--liquibase formatted sql

--changeset dvoyevodin_17102025:1
CREATE TABLE users (
	id UUID PRIMARY KEY,
	first_name VARCHAR(255) NOT NULL,
	second_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	login VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	role VARCHAR(255) NOT NULL,
	archive_flag BOOLEAN NOT NULL,
	email VARCHAR(255) NOT NULL
);

--changeset dvoyevodin_17102025:2
CREATE TABLE hero (
	id UUID PRIMARY KEY,
	x INTEGER NOT NULL,
	y INTEGER NOT NULL,
	up_ BOOLEAN NOT NULL,
	down_ BOOLEAN NOT NULL,
	left_ BOOLEAN NOT NULL,
	right_ BOOLEAN NOT NULL,
	cadr INTEGER NOT NULL,
	active_flag BOOLEAN NOT NULL,
	player_flag BOOLEAN NOT NULL,
	user_id UUID NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

--changeset dvoyevodin_17102025:3
CREATE TABLE landscape (
	id UUID PRIMARY KEY,
	i INTEGER NOT NULL,
	j INTEGER NOT NULL,
	type VARCHAR(255) NOT NULL,
	timer_for_bomb INTEGER,
    size_for_bomb INTEGER,
    user_id UUID NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);