DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
  id SERIAL PRIMARY KEY,
  original_url TEXT NOT NULL,
  short_url TEXT UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  deleted_at TIMESTAMP
);

INSERT INTO urls (original_url, short_url, created_at, updated_at, deleted_at)
VALUES
('http://localhost:8080/3', 'http://localhost:8080/DNWr6RR', '2022-05-12 16:20:00', '2022-05-12 16:20:00', null),
('http://localhost:8080/2', 'http://localhost:8080/DNWozoH', '2022-05-11 13:45:00', '2022-05-11 13:45:00', null),
('http://localhost:8080/1', 'http://localhost:8080/DNVIHZn', '2022-05-10 09:30:00', '2022-05-10 09:30:00', null);

