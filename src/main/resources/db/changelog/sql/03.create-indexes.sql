CREATE INDEX airplane_model_id ON airplane USING HASH (airplane_model_id);

CREATE INDEX aircompany_id ON airplane USING HASH (aircompany_id);

CREATE INDEX airplane_id ON flight USING HASH (airplane_id);

CREATE INDEX arrival_airport_id ON flight USING HASH (arrival_airport_id);

CREATE INDEX departure_airport_id ON flight USING HASH (departure_airport_id);

CREATE INDEX flight_id ON ticket USING HASH (flight_id);

