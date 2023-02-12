DROP SCHEMA IF EXISTS pharmacy;
CREATE SCHEMA pharmacy DEFAULT CHARACTER SET utf8;
USE pharmacy;

CREATE TABLE users (
id BIGINT AUTO_INCREMENT,
username VARCHAR(20) NOT NULL,
password VARCHAR(20) NOT NULL,
email VARCHAR(99) NOT NULL,
name VARCHAR(20) NOT NULL,
surname VARCHAR(20) NOT NULL,
birthDate DATE NOT NULL,
street VARCHAR(50) NOT NULL,
streetNumber VARCHAR(10) NOT NULL,
city VARCHAR(20) NOT NULL,
country VARCHAR(30) NOT NULL,
phoneNumber VARCHAR(15) NOT NULL,
userRole VARCHAR(20) NOT NULL,
PRIMARY KEY(id)
);

CREATE TABLE medicineCategories (
medicineID BIGINT AUTO_INCREMENT,
medicineName VARCHAR(50) NOT NULL,
medicinePurpose VARCHAR(50) NOT NULL,
medicineDescription VARCHAR(50) NOT NULL,
PRIMARY KEY(medicineID)
);


insert into users (username, password, email, name, surname, birthDate, street, streetNumber, city, country, phoneNumber, userRole) VALUES ('Pera123', 'Pera321', 'pera.pera@gmail.com', 'Petar', 'Petrovic', '2002-11-11', 'Rajiceva', 4, 'Novi Sad', 'Srbija', '021777888', 'ADMINISTRATOR');
insert into users (username, password, email, name, surname, birthDate, street, streetNumber, city, country, phoneNumber, userRole) VALUES ('Mika123', 'Mika321', 'mika.mika@gmail.com', 'Mika', 'Markovic', '2002-07-03', 'Kralja Petra', 8, 'Beograd', 'Srbija', '021724588', 'PHARMACIST');
insert into users (username, password, email, name, surname, birthDate, street, streetNumber, city, country, phoneNumber, userRole) VALUES ('Jova123', 'Jova321', 'jova.jova@gmail.com', 'Jova', 'Jovic', '2002-07-05', 'Kosovska', 20, 'Novi Sad', 'Srbija', '021763358', 'CUSTOMER');

insert into medicineCategories(medicineName, medicinePurpose, medicineDescription) VALUES ('Brufen', 'Smanjenje bolova', 'Vrlo dobro pokazan!');
insert into medicineCategories(medicineName, medicinePurpose, medicineDescription) VALUES ('Bromazepam HF', 'Smirenje', 'U umerenim kolicinama!');
insert into medicineCategories(medicineName, medicinePurpose, medicineDescription) VALUES ('Vitamin C narandza', 'Bolovi u grlu', 'Sumece tablete u pakovanju od po 10 komada!');