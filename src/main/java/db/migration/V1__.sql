-- the first script for migration
CREATE TABLE Book (
 id bigint PRIMARY KEY,
 book_title varchar(20),
 book_price varchar(20)
);
CREATE TABLE Article (
 id bigint PRIMARY KEY,
 content varchar(5000),
 postedAt timestamp,
 title varchar(255)
);
 
CREATE TABLE Article_authorIds (
 Article_id bigint  not null,
 authorIds bigint
);
CREATE TABLE Book_authorIds (
 Book_id bigint not null,
 authorIds bigint
);
 
CREATE TABLE AppUser (
 id bigint PRIMARY KEY,
 fullname varchar(255),
 isAdmin boolean not null,
 password varchar(255),
 username varchar(255)
);
ALTER TABLE Article_authorIds
add foreign key (Article_id)
references Article(id);
 

ALTER TABLE Book_authorIds
add foreign key (Book_id)
references Book(id);
