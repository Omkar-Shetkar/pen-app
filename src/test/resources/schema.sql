create TABLE IF NOT EXISTS candidate (
    id INTEGER IDENTITY PRIMARY KEY,
    first_name VARCHAR(200),
    last_name VARCHAR(200),
    gender VARCHAR(200),
    phone VARCHAR(200)
);

-------------------------------------------------------------