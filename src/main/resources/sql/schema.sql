--USER
ALTER TABLE public.users
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--COUNTRY
DROP SEQUENCE IF EXISTS countries_seq;
CREATE SEQUENCE countries_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.countries
    ALTER COLUMN country_id SET DEFAULT nextval('countries_seq');


--AIRPORT
DROP SEQUENCE IF EXISTS airports_seq;
CREATE SEQUENCE airports_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.airports
    ALTER COLUMN airport_id SET DEFAULT nextval('airports_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;


--PERMISSION
DROP SEQUENCE IF EXISTS permissions_seq;
CREATE SEQUENCE permissions_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.permissions
    ALTER COLUMN permission_id SET DEFAULT nextval('permissions_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;


--ROLE
DROP SEQUENCE IF EXISTS roles_seq;
CREATE SEQUENCE roles_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.roles
    ALTER COLUMN role_id SET DEFAULT nextval('roles_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--AIRPLANE
DROP SEQUENCE IF EXISTS airplanes_seq;
CREATE SEQUENCE airplanes_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.airplanes
    ALTER COLUMN airplane_id SET DEFAULT nextval('airplanes_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- MODEL
DROP SEQUENCE IF EXISTS models_seq;
CREATE SEQUENCE models_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.models
    ALTER COLUMN model_id SET DEFAULT nextval('models_seq');


-- -- --ROUTE
DROP SEQUENCE IF EXISTS routes_seq;
CREATE SEQUENCE routes_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.routes
    ALTER COLUMN route_id SET DEFAULT nextval('routes_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- -- --FLIGHT
-- DROP SEQUENCE IF EXISTS flights_seq;
-- CREATE SEQUENCE flights_seq
--     START WITH 1
--     INCREMENT BY 1;
-- ALTER TABLE public.flights
--     ALTER COLUMN flight_id SET DEFAULT nextval('flights_seq'),
-- ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
-- ALTER COLUMN flight_status SET DEFAULT 'SCHEDULED';
--
-- -- FLIGHT_PRICING
-- DROP SEQUENCE IF EXISTS flight_pricing_seq;
-- CREATE SEQUENCE flight_pricing_seq
--     START WITH 1
--     INCREMENT BY 1;
-- ALTER TABLE public.flight_pricing
--     ALTER COLUMN flight_pricing_id SET DEFAULT nextval('flight_pricing_seq'),
-- ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
--
-- -- SEAT
-- DROP SEQUENCE IF EXISTS seat_seq;
-- CREATE SEQUENCE seat_seq
--     START WITH 1
--     INCREMENT BY 1;
-- ALTER TABLE public.seats
--     ALTER COLUMN seat_id SET DEFAULT nextval('seat_seq');

-- -- SEAT-AVAILABILITY
-- DROP SEQUENCE IF EXISTS seat_availability_seq;
-- CREATE SEQUENCE seat_availability_seq
--     START WITH 1
--     INCREMENT BY 1;
-- ALTER TABLE public.seat_availability
--     ALTER COLUMN seat_availability_id SET DEFAULT nextval('seat_availability_seq');
--
--
