begin transaction;

--drop views
drop view if exists vwu_card;

--drop tables
drop TABLE if exists `card_tag`;
drop TABLE if exists `card_lang`;
drop TABLE if exists `tag`;
drop TABLE if exists `lang`;
drop TABLE if exists `card`;

--create tables
CREATE TABLE `tag` (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT NOT NULL UNIQUE
);

CREATE TABLE `lang` (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT NOT NULL UNIQUE
);

CREATE TABLE `card` (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`learned` INTEGER default 0
);

CREATE TABLE `card_tag` (
	`card_id`	INTEGER,
	`tag_id`	INTEGER,
	PRIMARY KEY(card_id,tag_id),
	FOREIGN KEY(card_id) REFERENCES card(id),
	FOREIGN KEY(tag_id) REFERENCES tag(id)
);

CREATE TABLE `card_lang` (
	`card_id`	INTEGER,
	`lang_id`	INTEGER,
	`text`	TEXT NOT NULL,
	PRIMARY KEY(card_id,lang_id),
	FOREIGN KEY(card_id) REFERENCES card(id),
	FOREIGN KEY(lang_id) REFERENCES lang(id)
);


-- create views

create view vwu_card as select c.id as `id`, cl.lang_id as `lang_id`, l.name as `lang_name`, cl.text as `text`, t.id as `tag_id`, t.name as `tag_name`, c.learning from card c
	left outer join card_tag ct on ct.card_id=c.id
	left outer join tag t on t.id=ct.tag_id
	left outer join card_lang cl on cl.card_id=c.id
	left outer join lang l on l.id=cl.lang_id
order by c.id
;
--select * from vwu_card;

--initialize data
insert into `lang` (name)values('En');
insert into `lang` (name)values('De');
insert into `lang` (name)values('Ru');

insert into tag (`name`)values('Hochsprache');
insert into tag (`name`)values('Umgangsprach');
insert into tag (`name`)values('Geschäfte');
insert into tag (`name`)values('Offene Verkehr');
insert into tag (`name`)values('Hause');
insert into tag (`name`)values('Straße');
insert into tag (`name`)values('Arbeit');

insert into card (learned)values(0);
insert into card (learned)values(0);
insert into card (learned)values(0);
insert into card (learned)values(0);
insert into card (learned)values(0);

--insert into card_tag (card_id,tag_id)values(1,1);
insert into card_tag (card_id,tag_id)values(2,2);
insert into card_tag (card_id,tag_id)values(2,5);
insert into card_tag (card_id,tag_id)values(3,7);

insert into card_lang(card_id,lang_id,text)values(1,1,'All roads lead to Rome');
insert into card_lang(card_id,lang_id,text)values(1,2,'Alle Wege führen nach Rom');
insert into card_lang(card_id,lang_id,text)values(1,3,'Все дороги ведут в Рим');

insert into card_lang(card_id,lang_id,text)values(2,1,'Cause time fun hour');
insert into card_lang(card_id,lang_id,text)values(2,3,'Делу время, потехе час');

insert into card_lang(card_id,lang_id,text)values(5,3,'test-123');

commit;
--rollback
--PRAGMA foreign_keys = on;
select * from vwu_card;
