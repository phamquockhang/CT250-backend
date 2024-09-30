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
    ALTER COLUMN airport_id SET DEFAULT nextval('airports_seq');


--PERMISSION
DROP SEQUENCE IF EXISTS permissions_seq;
CREATE SEQUENCE permissions_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.permissions
    ALTER COLUMN permission_id SET DEFAULT nextval('permissions_seq');


--ROLE
DROP SEQUENCE IF EXISTS roles_seq;
CREATE SEQUENCE roles_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.roles
    ALTER COLUMN role_id SET DEFAULT nextval('roles_seq');


-- --MODEL
-- DROP SEQUENCE IF EXISTS models_seq;
-- CREATE SEQUENCE models_seq
--     START WITH 1
--     INCREMENT BY 1;
-- ALTER TABLE public.models
--     ALTER COLUMN model_id SET DEFAULT nextval('models_seq');


--AIRPLANE
DROP SEQUENCE IF EXISTS airplanes_seq;
CREATE SEQUENCE airplanes_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.airplanes
    ALTER COLUMN airplane_id SET DEFAULT nextval('airplanes_seq');