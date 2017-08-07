# url_alias SCHEMA

# --- !Ups

CREATE TABLE url_alias (
  id varchar(100) NOT NULL,
  alias varchar(100) NOT NULL,
  destination_url varchar(1000) NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE url_alias;