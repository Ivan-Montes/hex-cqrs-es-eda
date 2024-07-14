DROP DATABASE IF EXISTS reservationSqlDb;

CREATE DATABASE reservationSqlDb;

DROP TABLE IF EXISTS public.reservations;
DROP TABLE IF EXISTS public.seats;

CREATE TABLE public.reservations (
	client_id uuid NOT NULL,
	flight_id uuid NOT NULL,
	reservation_id uuid NOT NULL,
	CONSTRAINT reservations_pkey PRIMARY KEY (reservation_id)
);

CREATE TABLE public.seats (
	reservation_id uuid NOT NULL,
	seat_id uuid NULL,
	CONSTRAINT fk75gc19fxjtg1o15ucvsyxlu80 FOREIGN KEY (reservation_id) REFERENCES public.reservations(reservation_id)
);