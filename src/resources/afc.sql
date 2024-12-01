DROP DATABASE IF EXISTS `afc`;
CREATE DATABASE `afc`;
USE `afc`;

CREATE TABLE immigrant (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  a_number VARCHAR(50) NOT NULL UNIQUE,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  dob DATE NOT NULL,
  email VARCHAR(50) NOT NULL,
  phone VARCHAR(50) NOT NULL,
  address VARCHAR(50) NOT NULL
);

INSERT INTO immigrant (a_number, first_name, last_name, dob, email, phone, address)
VALUES
("A123456789", "Pedro", "Vasquez", "1985-01-15", "pedro.vasquez@gmail.com", "(202) 123-4567", "1234 Main St, New York, NY, 10001"),
("A987654321", "Maria", "Gomez", "1990-06-22", "maria.gomez@yahoo.com", "(703) 987-6543", "5678 Elm St, Los Angeles, CA, 90001"),
("A112233445", "Johannes", "Dohen", "1982-08-14", "jo.do@aol.com", "(555) 555-5555", "7890 Pine St, Boston, MA, 02115"),
("A135792468", "Trinh Phuong", "Le", "1971-02-18", "letrinhphuong@hotmail.com", "(214) 478-1111", "4771 Poco Mas Drive, Dallas, TX, 75212"),
("A998877665", "Rabeea", "Malouf", "1943-01-21", "rnmalouf@gmail.com", "(562) 791-7544", "1623 Kerry Way, Los Angeles, CA, 90017"),
("A975318642", "Joao", "Azevedo Oliveira", "1964-07-03", "jao64@ymail.com", "(713) 795-7574", "4641 Michael Street, Houston, TX, 77030"),
("A000011111", "Menegilda", "Puddifoot", "1999-10-20", "puddifoot@aol.com", "(618) 784-1441", "878 Carter Street, St Louis, IL, 63101"),
("A102030405", "Li Wei", "Cheng", "1956-12-06", "cheng7171@outlook.com", "(248) 286-8041", "2409 Tennessee Avenue, Southfield, MI, 48075");

SELECT * from immigrant;

CREATE TABLE dependent (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  a_number VARCHAR(50) NOT NULL UNIQUE,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  dob DATE NOT NULL,
  country VARCHAR(50) NOT NULL,
  relationship VARCHAR(50) NOT NULL,
  FOREIGN KEY (a_number) REFERENCES immigrant(a_number)
);

INSERT INTO dependent (a_number, first_name, last_name, dob, country, relationship)
VALUES
("A987654321", "Jocelin", "Gomez", "1987-03-21", "Mexico", "Sibling");

SELECT dependent.a_number, dependent.first_name, dependent.last_name, immigrant.first_name, immigrant.last_name, dependent.relationship
FROM dependent
INNER JOIN immigrant ON dependent.a_number=immigrant.a_number;