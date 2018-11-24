CREATE TABLE news(
        news_id serial,
        title text,
        tag text,
        publish_date timestamp with time zone,
        source_url text
);