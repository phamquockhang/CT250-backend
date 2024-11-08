CREATE EXTENSION IF NOT EXISTS unaccent;

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

--ROLE
DROP SEQUENCE IF EXISTS roles_seq;
CREATE SEQUENCE roles_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.roles
    ALTER COLUMN role_id SET DEFAULT nextval('roles_seq'),
ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--PERMISSION
DROP SEQUENCE IF EXISTS permissions_seq;
CREATE SEQUENCE permissions_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.permissions
    ALTER COLUMN permission_id SET DEFAULT nextval('permissions_seq'),
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

-- TICKET_CLASS
DROP SEQUENCE IF EXISTS ticket_class_seq;
CREATE SEQUENCE ticket_class_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.ticket_class
    ALTER COLUMN ticket_class_id SET DEFAULT nextval('ticket_class_seq'),
ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;


-- -- --ROUTE
DROP SEQUENCE IF EXISTS routes_seq;
CREATE SEQUENCE routes_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.routes
    ALTER COLUMN route_id SET DEFAULT nextval('routes_seq'),
ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- --FLIGHT
DROP SEQUENCE IF EXISTS flights_seq;
CREATE SEQUENCE flights_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.flights
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN flight_status SET DEFAULT 'SCHEDULED';


-- FLIGHT_PRICING
DROP SEQUENCE IF EXISTS flight_pricing_seq;
CREATE SEQUENCE flight_pricing_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.flight_pricing
    ALTER COLUMN flight_pricing_id SET DEFAULT nextval('flight_pricing_seq');

-- SEAT
DROP SEQUENCE IF EXISTS seat_seq;
CREATE SEQUENCE seat_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.seats
    ALTER COLUMN seat_id SET DEFAULT nextval('seat_seq');

-- -- SEAT-AVAILABILITY
DROP SEQUENCE IF EXISTS seat_availability_seq;
CREATE SEQUENCE seat_availability_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.seat_availability
    ALTER COLUMN seat_availability_id SET DEFAULT nextval('seat_availability_seq');

--FEE
DROP SEQUENCE IF EXISTS fees_seq;
CREATE SEQUENCE fees_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.fees
    ALTER COLUMN fee_id SET DEFAULT nextval('fees_seq'),
ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

DROP SEQUENCE IF EXISTS fee_pricing_seq;
CREATE SEQUENCE fee_pricing_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.fee_pricing
    ALTER COLUMN fee_pricing_id SET DEFAULT nextval('fee_pricing_seq');

DROP SEQUENCE IF EXISTS fee_group_seq;
CREATE SEQUENCE fee_group_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.fee_groups
    ALTER COLUMN fee_group_id SET DEFAULT nextval('fee_group_seq'),
ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- MEALS
DROP SEQUENCE IF EXISTS meals_seq;
CREATE SEQUENCE meals_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.meals
    ALTER COLUMN meal_id SET DEFAULT nextval('meals_seq'),
ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;


-- MEAL_PRICING
DROP SEQUENCE IF EXISTS meal_pricing_seq;
CREATE SEQUENCE meal_pricing_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.meal_pricing
    ALTER COLUMN meal_pricing_id SET DEFAULT nextval('meal_pricing_seq'),
ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- BAGGAGE
DROP SEQUENCE IF EXISTS baggage_seq;
CREATE SEQUENCE baggage_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.baggages
    ALTER COLUMN baggage_id SET DEFAULT nextval('baggage_seq'),
ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- BAGGAGE_PRICING
DROP SEQUENCE IF EXISTS baggage_pricing_seq;
CREATE SEQUENCE baggage_pricing_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.baggage_pricing
    ALTER COLUMN baggage_pricing_id SET DEFAULT nextval('baggage_pricing_seq'),
ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--PAYMENT-METHOD
DROP SEQUENCE IF EXISTS payment_methods_seq;
CREATE SEQUENCE payment_methods_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.payment_methods
    ALTER COLUMN payment_method_id SET DEFAULT nextval('payment_methods_seq');