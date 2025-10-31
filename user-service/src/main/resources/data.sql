CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Unique user id
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,  -- Unique email
    password VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    address VARCHAR(100) NOT NULL,
    joining_date DATE NOT NULL
);

INSERT INTO users (first_name, last_name, email, password, age, address, joining_date)
VALUES
('John', 'Doe', 'john.doe@example.com', 'password123', 30, 'Israel,123,haifa,1,2', '2025-01-15'),
('Alice', 'Smith', 'alice.smith@example.com', 'alicePass!45', 25, 'Israel,123,haifa,1,2', '2025-03-22');


