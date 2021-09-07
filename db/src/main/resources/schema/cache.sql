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

CREATE TABLE IF NOT EXISTS `preference`(
    `key`      VARCHAR(64)         PRIMARY KEY,
    `content`  VARCHAR(256)        NOT NULL DEFAULT ''
);

INSERT or REPLACE INTO `preference`(`key`, `content`) VALUES ('init', 'init');