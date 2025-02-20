CREATE TABLE locators
(
    id            UUID PRIMARY KEY,
    encoded       BIGINT UNIQUE  NOT NULL,
    original_name VARCHAR NOT NULL
);

CREATE TABLE ref
(
    id      SERIAL PRIMARY KEY,
    counter BIGINT NOT NULL,
    version BIGINT NOT NULL
);

INSERT INTO ref (counter, version)
VALUES (0, 1) ON CONFLICT (id) DO NOTHING;
