CREATE TABLE filenames (
    hash    INTEGER NOT NULL,
    name    STRING,
    used    INTEGER NOT NULL DEFAULT '0',
    archive STRING,
    version INTEGER,
    PRIMARY KEY ( hash )
);

CREATE TABLE folders (
    hash    INTEGER NOT NULL,
    path    STRING,
    used    INTEGER NOT NULL DEFAULT '0',
    archive STRING,
    version INTEGER,
    PRIMARY KEY ( hash )
);

DROP TABLE IF EXISTS `version`;
CREATE TABLE `version`(
    `version`        VARCHAR(20)         NOT NULL,
    PRIMARY KEY ( version )
);

DROP TABLE IF EXISTS `CACHE`;
CREATE TABLE `CACHE`(
    `KEY`            VARCHAR(64)        PRIMARY KEY,
    `CONTENT`        VARCHAR(64)        NOT NULL DEFAULT '',
    `CACHE_TIME`     INT                NOT NULL,
    `EXPIRE_TIME`    INT                NOT NULL
);

INSERT INTO `CACHE`(`KEY`, CONTENT, EXPIRE_TIME, CACHE_TIME) VALUES ('init', 'init', 0, 0);
