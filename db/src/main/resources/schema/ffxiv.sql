
CREATE TABLE IF NOT EXISTS terr (
    id int primary key,
    name varchar(8),
    place_name varchar(32) not null,
    model varchar(128) not null
);

CREATE TABLE IF NOT EXISTS `dye` (
    `id` int primary key,
    `name` varchar(16),
    `item_id` int,
    `shade` int,
    `order` int,
    `red` int,
    `green` int,
    `blue` int
);

CREATE TABLE IF NOT EXISTS housing_category (
    id int primary key,
    name varchar(64),
    short_cut varchar(8)
);

-- indoor

CREATE TABLE IF NOT EXISTS furniture_catalog (
    id int primary key,
    category int,
    name varchar(64),
    `order` int
);

CREATE TABLE IF NOT EXISTS `interior` (
    `id` int primary key,
    `name` varchar(16),
    `item_id` int,
    `category` int,
    `order` int,
    `path` varchar(128),
    `icon` varchar(128)
);

CREATE TABLE IF NOT EXISTS furniture (
    id int primary key,
    name varchar(64),
    item_id int,
    model varchar(128),
    icon varchar(128),
    `category` int,
    `catalog` int,
    is_dyeable int
);

-- outdoor

CREATE TABLE IF NOT EXISTS yard_catalog (
    id int primary key,
    category int,
    name varchar(64),
    `order` int
);

CREATE TABLE IF NOT EXISTS `exterior` (
    `id` int primary key,
    `name` varchar(16),
    `item_id` int,
    `size` int,
    `category` int,
    `order` int,
    `path` varchar(128),
    `icon` varchar(128),
    `is_united` int
);

CREATE TABLE IF NOT EXISTS yard_object (
    id int primary key,
    name varchar(64),
    item_id int,
    model varchar(128),
    icon varchar(128),
    `category` int,
    `catalog` int,
    is_dyeable int
);

-- common

CREATE TABLE IF NOT EXISTS `preference`(
    `key`      VARCHAR(64)         PRIMARY KEY,
    `content`  VARCHAR(256)        NOT NULL DEFAULT ''
);

INSERT or REPLACE INTO `preference`(`key`, `content`) VALUES ('init', 'init');