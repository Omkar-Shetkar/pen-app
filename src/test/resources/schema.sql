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

create TABLE IF NOT EXISTS exam (
    id INTEGER  PRIMARY KEY AUTO_INCREMENT,
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

create TABLE IF NOT EXISTS country (
    id INTEGER  PRIMARY KEY,
    name VARCHAR(250)
);


create TABLE IF NOT EXISTS state (
    id INTEGER  PRIMARY KEY,
    name VARCHAR(250),
    country INTEGER,
    FOREIGN KEY (country) REFERENCES country(id)
);


create TABLE IF NOT EXISTS city (
    id INTEGER  PRIMARY KEY,
    name VARCHAR(250),
    state INTEGER,
    FOREIGN KEY (state) REFERENCES state(id)
);

create TABLE IF NOT EXISTS address (
    id INTEGER  PRIMARY KEY,
    city INTEGER,
    state INTEGER,
    country INTEGER,
    pincode VARCHAR(25),
    gps_location VARCHAR(200),
    FOREIGN KEY (city) REFERENCES city(id),
    FOREIGN KEY (state) REFERENCES state(id),
    FOREIGN KEY (country) REFERENCES country(id)
);

create TABLE IF NOT EXISTS exam_schedule (
    id INTEGER  PRIMARY KEY AUTO_INCREMENT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    exam INTEGER,
    language INTEGER,
    address INTEGER,
    FOREIGN KEY (exam) REFERENCES exam(id),
    FOREIGN KEY (language) REFERENCES language(id),
    FOREIGN KEY (address) REFERENCES address(id)
);

create TABLE IF NOT EXISTS request (
    id INTEGER  PRIMARY KEY AUTO_INCREMENT,
    candidate INTEGER,
    scribe INTEGER,
    exam_schedule INTEGER,
    status VARCHAR(200),
    FOREIGN KEY (candidate) REFERENCES candidate(id),
    FOREIGN KEY (scribe) REFERENCES scribe(id),
    FOREIGN KEY (exam_schedule) REFERENCES exam_schedule(id)
);

create TABLE IF NOT EXISTS exam_event (
    id INTEGER  PRIMARY KEY AUTO_INCREMENT,
    request INTEGER,
    scribe INTEGER,
    candidate INTEGER,
    otp_generated BOOLEAN,
    otp_verified BOOLEAN,
    exam_status VARCHAR(30),
    exam_schedule INTEGER,

    FOREIGN KEY (request) REFERENCES request(id),
    FOREIGN KEY (scribe) REFERENCES scribe(id),
    FOREIGN KEY (candidate) REFERENCES candidate(id),
    FOREIGN KEY (exam_schedule) REFERENCES exam_schedule(id)
);

create TABLE IF NOT EXISTS feedback (
    rating INTEGER,
    comment VARCHAR,
    exam_event INTEGER REFERENCES exam_event(id)
);
