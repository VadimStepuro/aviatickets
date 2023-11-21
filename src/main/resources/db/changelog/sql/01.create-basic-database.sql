CREATE TABLE public.aircompany (
	id uuid NOT NULL DEFAULT gen_random_uuid (),
	"name" varchar NOT NULL,
	country varchar NULL,
	CONSTRAINT aircompany_pkey PRIMARY KEY (id)
);


-- public.airplane_model definition

-- Drop table

-- DROP TABLE public.airplane_model;

CREATE TABLE public.airplane_model (
	id uuid NOT NULL DEFAULT gen_random_uuid (),
	"name" varchar NOT NULL,
	weight int4 NOT NULL CONSTRAINT airplane_model_check_weight CHECK (weight > 0),
	economy_seats int4 NOT NULL CONSTRAINT airplane_model_check_economy_seats CHECK (economy_seats > 0),
	business_seats int4 NULL CONSTRAINT airplane_model_check_business_seats CHECK (business_seats >= 0),
	CONSTRAINT airplane_model_pkey PRIMARY KEY (id)
);


-- public.airport definition

-- Drop table

-- DROP TABLE public.airport;

CREATE TABLE public.airport (
	id uuid NOT NULL DEFAULT gen_random_uuid (),
	city varchar NOT NULL,
	"name" varchar NOT NULL,
	capacity int4 NULL CONSTRAINT airport_check_capacity CHECK (capacity >= 0),
	runway_num int4 NULL CONSTRAINT airport_check_runway_num CHECK (runway_num > 0),
	country varchar NOT NULL,
	CONSTRAINT airport_pkey PRIMARY KEY (id)
);


-- public.privilege definition

-- Drop table

-- DROP TABLE public.privilege;

CREATE TABLE public.privilege (
	id uuid NOT NULL DEFAULT gen_random_uuid (),
	"name" varchar NOT NULL,
	CONSTRAINT privilege_pkey PRIMARY KEY (id)
);


-- public."role" definition

-- Drop table

-- DROP TABLE public."role";

CREATE TABLE public."role" (
	id uuid NOT NULL DEFAULT gen_random_uuid (),
	"name" varchar NOT NULL,
	CONSTRAINT role_pkey PRIMARY KEY (id)
);


-- public."user" definition

-- Drop table

-- DROP TABLE public."user";

CREATE TABLE public."user" (
	id uuid NOT NULL DEFAULT gen_random_uuid (),
	"name" varchar NOT NULL,
	login varchar NOT NULL,
	email varchar NULL,
	"password" varchar NOT NULL,
	birthdate date NULL,
	CONSTRAINT user_pkey PRIMARY KEY (id)
);


-- public.airplane definition

-- Drop table

-- DROP TABLE public.airplane;

CREATE TABLE public.airplane (
	id uuid NOT NULL DEFAULT gen_random_uuid (),
	airplane_model_id uuid NULL,
	aircompany_id uuid NOT NULL,
	CONSTRAINT airplane_pkey PRIMARY KEY (id),
	CONSTRAINT aircompany_fk FOREIGN KEY (aircompany_id) REFERENCES public.aircompany(id),
	CONSTRAINT airplane_fk FOREIGN KEY (airplane_model_id) REFERENCES public.airplane_model(id)
);


-- public.flight definition

-- Drop table

-- DROP TABLE public.flight;

CREATE TABLE public.flight (
	id uuid NOT NULL DEFAULT gen_random_uuid (),
	airplane_id uuid NOT NULL,
	arrival_airport_id uuid NOT NULL,
	departure_airport_id uuid NOT NULL,
	status varchar NULL,
	arrival_date timestamp NOT NULL,
	departure_date timestamp NOT NULL,
	CONSTRAINT flight_pkey PRIMARY KEY (id),
	CONSTRAINT airplane_fk FOREIGN KEY (airplane_id) REFERENCES public.airplane(id),
	CONSTRAINT arrival_airport_fk FOREIGN KEY (arrival_airport_id) REFERENCES public.airport(id),
	CONSTRAINT departure_airport_fk FOREIGN KEY (departure_airport_id) REFERENCES public.airport(id)
);


-- public.roles_privileges definition

-- Drop table

-- DROP TABLE public.roles_privileges;

CREATE TABLE public.roles_privileges (
	role_id uuid NOT NULL,
	privilege_id uuid NOT NULL,
	CONSTRAINT roles_privileges_pkey PRIMARY KEY (role_id, privilege_id),
	CONSTRAINT privileges_fk FOREIGN KEY (privilege_id) REFERENCES public.privilege(id),
	CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES public."role"(id)
);


-- public.ticket definition

-- Drop table

-- DROP TABLE public.ticket;

CREATE TABLE public.ticket (
	id uuid NOT NULL DEFAULT gen_random_uuid (),
	flight_id uuid NOT NULL,
	flight_class varchar NULL,
	flight_cost numeric NOT NULL CONSTRAINT ticket_check_flight_cost CHECK (flight_cost > 0),
	CONSTRAINT ticket_pkey PRIMARY KEY (id),
	CONSTRAINT flight_fk FOREIGN KEY (flight_id) REFERENCES public.flight(id)
);


-- public.users_roles definition

-- Drop table

-- DROP TABLE public.users_roles;

CREATE TABLE public.users_roles (
	user_id uuid NOT NULL,
	role_id uuid NOT NULL,
	CONSTRAINT users_roles_pkey PRIMARY KEY (role_id, user_id),
	CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES public."role"(id),
	CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES public."user"(id)
);
