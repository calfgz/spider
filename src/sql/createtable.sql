create table author(
	id int primary key auto_increment,
	wechat varchar(50) not null,
	open_id varchar(255) not null,
	create_at datetime not null
)default charset 'utf8' ENGINE='innodb';

create table article(
	id int primary key auto_increment,
	docid varchar(255) not null,
	title varchar(255) ,
	pdate varchar(20),
	url varchar(255),
	content text
)default charset 'utf8' ENGINE='innodb';