ALTER TABLE public.aircompany ADD CONSTRAINT aircompany_un_name UNIQUE ("name");

ALTER TABLE public.airplane_model ADD CONSTRAINT airplane_model_un_name UNIQUE ("name");

ALTER TABLE public.airport ADD CONSTRAINT airport_un_name UNIQUE ("name");

ALTER TABLE public.privilege ADD CONSTRAINT privilege_un_name UNIQUE ("name");

ALTER TABLE public."role" ADD CONSTRAINT role_un_name UNIQUE ("name");


