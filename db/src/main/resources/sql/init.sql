CREATE TABLE IF NOT EXISTS filenames (
    hash    INTEGER NOT NULL,
    name    STRING,
    used    INTEGER NOT NULL DEFAULT '0',
    archive STRING,
    version INTEGER,
    PRIMARY KEY ( hash )
);

CREATE TABLE IF NOT EXISTS folders (
    hash    INTEGER NOT NULL,
    path    STRING,
    used    INTEGER NOT NULL DEFAULT '0',
    archive STRING,
    version INTEGER,
    PRIMARY KEY ( hash )
);

CREATE TABLE IF NOT EXISTS `version`(
    `version`        VARCHAR(20)         NOT NULL,
    PRIMARY KEY ( version )
);

CREATE TABLE IF NOT EXISTS `CACHE`(
    `KEY`            VARCHAR(64)        PRIMARY KEY,
    `CONTENT`        VARCHAR(64)        NOT NULL DEFAULT '',
    `CACHE_TIME`     INT                NOT NULL,
    `EXPIRE_TIME`    INT                NOT NULL
);

INSERT or IGNORE INTO `CACHE`(`KEY`, CONTENT, EXPIRE_TIME, CACHE_TIME) VALUES ('init', 'init', 0, 0);

CREATE TABLE IF NOT EXISTS furniture_category (
    id int primary key,
    name varchar(64),
    short_cut varchar(8)
);

CREATE TABLE IF NOT EXISTS furniture_catalog (
    id int,
    category_id int,
    name varchar(64),
    subOrder int
);

CREATE TABLE IF NOT EXISTS furniture (
    id int primary key,
    name varchar(64),
    item_id int,
    icon_id int,
    category_id int,
    catalog_id int,
    is_dyeable int
);