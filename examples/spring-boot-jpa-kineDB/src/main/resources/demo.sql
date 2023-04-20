
DROP TABLE mysql_jpa_demo.employees;
DROP TABLE mysql_jpa_demo.company;
DROP TABLE mysql_jpa_demo.address;
DROP TABLE mysql_jpa_demo.roles;
DROP TABLE mysql_jpa_demo.salary;

DROP DATABASE mysql_jpa_demo;

CREATE DATABASE mysql_jpa_demo WITH PROPERTIES (CATALOG = 'mysql',HOST = '127.0.0.1',PORT = 3306,
    DATABASENAME = 'mysql_jpa_demo',SCHEMA = '',INDEXNAME = '',USERNAME = 'root',PASSWORD = '123456');

CREATE TABLE mysql_jpa_demo.employees (
    id BIGINT PRIMARY key NOT NULL,
    first_name varchar(20),
    last_name varchar(20),
    username varchar(32),
    password varchar(20),
    email   varchar(32),
    age     tinyint,
    days_of_annual_leave smallint,
    address_id BIGINT,
    phone varchar(20),
    website varchar(256),
    entry_date date,
    entry_time time,
    company_id bigint,
    created_at timestamp
    );


CREATE TABLE mysql_jpa_demo.company (
      id BIGINT PRIMARY key NOT NULL,
      name varchar(256),
      catch_phrase varchar(20),
      bs varchar(32),
      created_at timestamp
);

CREATE TABLE mysql_jpa_demo.address (
        id BIGINT PRIMARY key NOT NULL,
        street varchar(256),
        suite varchar(32),
        city varchar(32),
        zipcode varchar(10),
        created_at timestamp
);

CREATE TABLE mysql_jpa_demo.user_role (
        user_id BIGINT NOT NULL,
        role_id BIGINT NOT NULL
);

CREATE TABLE mysql_jpa_demo.roles (
      id BIGINT PRIMARY key NOT NULL,
      role_type varchar(20) NOT NULL,
      created_at timestamp
);

CREATE TABLE mysql_jpa_demo.salary (
      id BIGINT PRIMARY key NOT NULL,
      release_year_month varchar(20) NOT NULL,
      salary_amount decimal(10,2),
      subsidy double,
      employee_id BIGINT NOT NULL,
      created_at timestamp
);
