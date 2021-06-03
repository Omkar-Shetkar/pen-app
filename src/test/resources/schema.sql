create TABLE IF NOT EXISTS candidate (
    id INTEGER  PRIMARY KEY,
    first_name VARCHAR(200),
    last_name VARCHAR(200),
    gender VARCHAR(200),
    phone VARCHAR(200)
);

-------------------------------------------------------------

create TABLE IF NOT EXISTS language (
    id INTEGER  PRIMARY KEY,
    name VARCHAR(200)
);
-------------------------------------------------------------

create TABLE IF NOT EXISTS scribe (
    id INTEGER  PRIMARY KEY,
    first_name VARCHAR(200),
    last_name VARCHAR(200),
    gender VARCHAR(200),
    phone VARCHAR(200),
    email VARCHAR(200),
    location VARCHAR(200)
);

create TABLE IF NOT EXISTS availability (
    available_from TIMESTAMP,
    available_to TIMESTAMP,
    scribe INTEGER REFERENCES scribe(id)
);

-------------------------------------------------------------

create TABLE IF NOT EXISTS scribe_language (
    scribe INTEGER,
    language INTEGER,
    FOREIGN KEY (scribe) REFERENCES scribe(id),
    FOREIGN KEY (language) REFERENCES language(id),
    UNIQUE (scribe,language)
);

-------------------------------------------------------------
