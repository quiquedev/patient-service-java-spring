CREATE TABLE patients (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(150) NOT NULL,
    passport_number VARCHAR(10) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    version INT NOT NULL
)