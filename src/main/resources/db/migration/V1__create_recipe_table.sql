CREATE TABLE IF NOT EXISTS db_model_recipe (

    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id varchar(20) not null,
    title varchar(20) not null,
    description varchar(50),
    created_at timestamp not null,
    updated_at timestamp not null

)ENGINE=InnoDB DEFAULT CHARSET=UTF8;