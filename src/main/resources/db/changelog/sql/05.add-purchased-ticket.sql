CREATE TABLE public.purchased_ticket (
	id uuid NOT NULL DEFAULT gen_random_uuid (),
	user_id uuid NOT NULL,
	ticket_id uuid NOT NULL,
	"date" date NOT NULL,
	CONSTRAINT purchased_ticket_pkey PRIMARY KEY (id),
	CONSTRAINT purchased_ticket_fk FOREIGN KEY (user_id) REFERENCES public."user"(id),
	CONSTRAINT purchased_ticket_fk_1 FOREIGN KEY (ticket_id) REFERENCES public.ticket(id)
);
CREATE INDEX purchased_ticket_ticket_index ON public.purchased_ticket USING hash (ticket_id);
CREATE INDEX purchased_ticket_user_index ON public.purchased_ticket USING hash (user_id);