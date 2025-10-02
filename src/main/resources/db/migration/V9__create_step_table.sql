CREATE TABLE IF NOT EXISTS step_entity (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id varchar(100) not null,
    recipe_id bigint not null,
    number int not null,
    title varchar(255) not null,
    description varchar(255) not null,
    created_at bigint not null,
    updated_at bigint not null,
    foreign key (recipe_id) references recipe_entity(id) on delete cascade,
    unique (recipe_id, number)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;