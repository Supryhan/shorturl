CREATE TABLE locators (
                       id UUID PRIMARY KEY,
                       short_name VARCHAR UNIQUE NOT NULL,
                       original_name VARCHAR UNIQUE NOT NULL
);
