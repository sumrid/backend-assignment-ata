CREATE TABLE jobs
(
    id                  INT AUTO_INCREMENT NOT NULL,
    job_title           varchar(255) NULL,
    salary_number       DECIMAL NULL default 0,
    gender              varchar(24) NULL,
    employer            varchar(255) NULL,
    location            varchar(255) NULL,
    years_at_employer   varchar(255) NULL,
    years_of_experience varchar(255) NULL,
    signing_bonus       varchar(255) NULL,
    annual_bonus        varchar(255) NULL,
    salary              varchar(255) NULL,
    currency            varchar(5) NULL
);