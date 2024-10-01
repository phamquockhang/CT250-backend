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

-- --ROUTE
DROP SEQUENCE IF EXISTS routes_seq;
CREATE SEQUENCE routes_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.routes
    ALTER COLUMN route_id SET DEFAULT nextval('routes_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;


