CREATE TABLE customers
(
    subscription_date DATE,
    city              CHARACTER VARYING(225),
    company           CHARACTER VARYING(225),
    country           CHARACTER VARYING(225),
    customer_id       CHARACTER VARYING(225) NOT NULL,
    email             CHARACTER VARYING(225),
    first_name        CHARACTER VARYING(225),
    last_name         CHARACTER VARYING(225),
    phone1            CHARACTER VARYING(225),
    phone2            CHARACTER VARYING(225),
    website           CHARACTER VARYING(225),
    PRIMARY KEY (customer_id)
)