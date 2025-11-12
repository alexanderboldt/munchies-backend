CREATE TABLE IF NOT EXISTS db_model_label (

    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(100) not null,
    created_at bigint not null,
    updated_at bigint not null

)ENGINE=InnoDB DEFAULT CHARSET=UTF8;