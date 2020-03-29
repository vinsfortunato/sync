-- Template database generation script
-- Can be used to generate template.sqlite which is used
-- by jooq gradle task to generate schema sources

CREATE TABLE IF NOT EXISTS songs (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    pack            TEXT NOT NULL,
    directory       TEXT NOT NULL,
    hash            TEXT NOT NULL,
    sim_path        TEXT NOT NULL,
    format          TEXT NOT NULL,
    title           TEXT NOT NULL,
    subtitle        TEXT,
    artist          TEXT,
    genre           TEXT,
    banner_path     TEXT,
    background_path TEXT,
    cd_title        TEXT,
    music_path      TEXT NOT NULL,
    sample_start    REAL DEFAULT 0,
    sample_length   REAL,
    UNIQUE (pack, directory)
);

CREATE TABLE IF NOT EXISTS charts (
    id                  TEXT PRIMARY KEY,
    song_id             INTEGER NOT NULL,
    hash TEXT           NOT NULL,
    difficulty_class    TEXT NOT NULL,
    difficulty_meter    INTEGER NOT NULL,
    display_bpm         TEXT NOT NULL,
    name                TEXT,
    description         TEXT,
    credit              TEXT,
    notes               TEXT,
    FOREIGN KEY (song_id)
        REFERENCES songs (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);





