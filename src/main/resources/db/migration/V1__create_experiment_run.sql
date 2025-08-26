## Migration SQL (Flyway) src/main/resources/db/migration/V1__create_experiment_run.sql

create table if not exists experiment_run (
  id bigserial primary key,
  variant varchar(16) not null,
  rows int not null,
  cols int not null,
  result int not null,
  micros bigint not null,
  created_at timestamptz not null default now(),
  client_ip text
);

create index if not exists idx_experiment_run_variant on experiment_run(variant);
create index if not exists idx_experiment_run_created_at on experiment_run(created_at);