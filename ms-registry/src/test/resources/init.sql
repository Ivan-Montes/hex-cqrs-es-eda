DROP DATABASE IF EXISTS registrySqlDb;

CREATE DATABASE registrySqlDb;

DROP TABLE IF EXISTS public.registries;

CREATE TABLE public.registries (
	"sequence" int8 NOT NULL,
	time_instant timestamptz(6) NOT NULL,
	event_id uuid NOT NULL,
	event_category varchar(255) NOT NULL,
	event_type varchar(255) NOT NULL,
	event_data jsonb NOT NULL,
	CONSTRAINT registries_pkey PRIMARY KEY (event_id)
);