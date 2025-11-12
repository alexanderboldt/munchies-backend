alter table db_model_recipe add label_id bigint;
alter table db_model_recipe add foreign key (label_id) references db_model_label(id) on delete set null;