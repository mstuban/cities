CREATE SEQUENCE seq_city_id;

CREATE TABLE city
(
    city_id             BIGINT PRIMARY KEY NOT NULL,
    name                TEXT UNIQUE        NOT NULL,
    population          INTEGER            NOT NULL,
    description         TEXT               NOT NULL,
    created             timestamptz        NOT NULL,
    number_of_favorites INTEGER NOT NULL DEFAULT 0
);

INSERT INTO city (city_id, name, population, description, created, number_of_favorites)
VALUES (nextval('seq_city_id'), 'Zagreb', 806341, 'Croatia''s northwestern capital', now(), 1);

INSERT INTO city (city_id, name, population, description, created, number_of_favorites)
VALUES (nextval('seq_city_id'),
        'Varazdin',
        46946,
        'City on the Drava River, in northern Croatia',
        now() - interval '1 day',
        10);

INSERT INTO city (city_id, name, population, description, created, number_of_favorites)
VALUES (nextval('seq_city_id'), 'Osijek', 107784, 'Fourth largest city in Croatia', now() - interval '2 day', 52);

INSERT INTO city (city_id, name, population, description, created, number_of_favorites)
VALUES (nextval('seq_city_id'), 'Vukovar', 27683, 'A city in Eastern Croatia', now() - interval '3 day', 22);

INSERT INTO city (city_id, name, population, description, created, number_of_favorites)
VALUES (nextval('seq_city_id'), 'Rijeka', 128624, 'Croatian port city on Kvarner bay', now() - interval '4 day', 34);

INSERT INTO city (city_id, name, population, description, created, number_of_favorites)
VALUES (nextval('seq_city_id'),
        'Pula',
        57460,
        'Seafront city on the tip of Croatia''s Istrian Peninsula',
        now() - interval '5 day',
        104);

INSERT INTO city (city_id, name, population, description, created, number_of_favorites)
VALUES (nextval('seq_city_id'),
        'Zadar',
        168031,
        'Known for its Venetian ruins in Old Town',
        now() - interval '6 day',
        152);

INSERT INTO city (city_id, name, population, description, created, number_of_favorites)
VALUES (nextval('seq_city_id'),
        'Sibenik', 34302,
        'Known as a gateway to Kornati Islands',
        now() - interval '7 day',
        242);

INSERT INTO city (city_id, name, population, description, created, number_of_favorites)
VALUES (nextval('seq_city_id'),
        'Split',
        178192,
        'Croatia''s second largest city and the largest city in the Dalmatiaregion',
        now() - interval '8 day',
        6);

INSERT INTO city (city_id, name, population, description, created, number_of_favorites)
VALUES (nextval('seq_city_id'),
        'Dubrovnik',
        42615,
        'City in southern Croatia fronting the Adriatic Sea',
        now() - interval '9 day',
        242);
