DROP FUNCTION IF EXISTS insert_permission_role CASCADE;
CREATE OR REPLACE FUNCTION insert_permission_role() RETURNS TRIGGER
AS $$
BEGIN
    INSERT INTO permission_role (permission_id, role_id)
    VALUES (NEW.permission_id, 1);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER insert_permission_role
    AFTER INSERT ON permissions
    FOR EACH ROW
EXECUTE FUNCTION insert_permission_role();

DROP FUNCTION IF EXISTS insert_flight_fees CASCADE;
CREATE OR REPLACE FUNCTION insert_flight_fees() RETURNS TRIGGER
AS $$
BEGIN
    INSERT INTO flight_fee (flight_id, fee_id)
    SELECT NEW.flight_id, fee_id
    FROM fees;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER insert_flight_fees
    AFTER INSERT ON flights
    FOR EACH ROW
EXECUTE FUNCTION insert_flight_fees();