-- the first script for migration
CREATE TABLE Book (
 id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 book_title varchar(20),
 book_price varchar(20)
);
CREATE TABLE Article (
 id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 content varchar(5000),
 postedAt timestamp,
 title varchar(255)
);
 
CREATE TABLE Article_authorIds (
 Article_id bigint UNSIGNED not null,
 authorIds bigint UNSIGNED
);
CREATE TABLE Book_authorIds (
 Book_id bigint UNSIGNED not null,
 authorIds bigint UNSIGNED
);
 
CREATE TABLE User (
 id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
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
