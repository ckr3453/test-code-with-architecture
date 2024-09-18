insert into users (id, email, nickname, address, certification_code, status, last_login_at)
values (1, 'david3453@naver.com', 'ckr', 'Seoul, Gunja', '123123-123123-123-123-123123123', 'ACTIVE', 0);

insert into posts (id, content, created_at, modified_at, user_id)
values (1, 'hello, world!', 1678530673958, 1678530673958, 1);

insert into posts (id, content, created_at, modified_at, user_id)
values (2, 'hello, test world?!!', 1678530673999, 1678530673999, 1);