CREATE TABLE IF NOT EXISTS db_model_label (

    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) not null,
    created_at timestamp not null,
    updated_at timestamp not null

)ENGINE=InnoDB DEFAULT CHARSET=UTF8;