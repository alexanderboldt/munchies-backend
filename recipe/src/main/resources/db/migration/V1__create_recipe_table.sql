CREATE TABLE IF NOT EXISTS db_model_recipe (

    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id varchar(100) not null,
    title varchar(100) not null,
    description text,
    created_at bigint not null,
    updated_at bigint not null

)ENGINE=InnoDB DEFAULT CHARSET=UTF8;