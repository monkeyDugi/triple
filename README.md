# 실행 방법

## 애플리케이션 실행 방법

- 프로젝트 클론

```sql
https://github.com/monkeyDugi/triple.git
```

- 애플리케이션 실행

```sql
./gradlew clean build
./gradlew copyDocument
./gradlew bootRun
```

- API 문서 참고하여 요청

```sql
http://localhost:8080/docs/index.html
```

## 테스트 방법

### 사전 데이터 생성

[h2 console 접속](http://localhost:8080/h2-console) 후 아래 쿼리를 실행

- 회원 생성

```sql
SET NON_KEYWORDS user;

// 장소 등록자
insert into user(id, account_id, deleted, create_date, modified_date) values('69b408c4-f5fe-11ec-b637-1bfda337292b', 'account_id_1', false, now(), now());
insert into user(id, account_id, deleted, create_date, modified_date) values('6cbecbda-f5fe-11ec-b637-1bfda337292b', 'account_id_2', false, now(), now());

// 리뷰 등록자
insert into user(id, account_id, deleted, create_date, modified_date) values('6d07a26a-f5fe-11ec-b637-1bfda337292b', 'account_id_3', false, now(), now());
insert into user(id, account_id, deleted, create_date, modified_date) values('6d49d07c-f5fe-11ec-b637-1bfda337292b', 'account_id_4', false, now(), now());

select * from user;
```

- 장소 생성

```sql

// 장소 등록자 account_id_1
insert into place(id, user_id, address, name, content, deleted, create_date, modified_date) values('7711e300-f5ff-11ec-b637-1bfda337292b', '69b408c4-f5fe-11ec-b637-1bfda337292b', '장소1', '장소명', '내용', false, now(), now());
insert into place(id, user_id, address, name, content, deleted, create_date, modified_date) values('97d1288a-f5ff-11ec-b637-1bfda337292b', '69b408c4-f5fe-11ec-b637-1bfda337292b', '장소2', '장소명', '내용', false, now(), now());
insert into place(id, user_id, address, name, content, deleted, create_date, modified_date) values('9834e4a6-f5ff-11ec-b637-1bfda337292b', '69b408c4-f5fe-11ec-b637-1bfda337292b', '장소3', '장소명', '내용', false, now(), now());
insert into place(id, user_id, address, name, content, deleted, create_date, modified_date) values('988578e4-f5ff-11ec-b637-1bfda337292b', '69b408c4-f5fe-11ec-b637-1bfda337292b', '장소4', '장소명', '내용', false, now(), now());
insert into place(id, user_id, address, name, content, deleted, create_date, modified_date) values('98d0e36a-f5ff-11ec-b637-1bfda337292b', '69b408c4-f5fe-11ec-b637-1bfda337292b', '장소5', '장소명', '내용', false, now(), now());

// 장소 등록자 account_id_2
insert into place(id, user_id, address, name, content, deleted, create_date, modified_date) values('9919a50a-f5ff-11ec-b637-1bfda337292b', '6cbecbda-f5fe-11ec-b637-1bfda337292b', '장소6', '장소명', '내용', false, now(), now());
insert into place(id, user_id, address, name, content, deleted, create_date, modified_date) values('995c9c3e-f5ff-11ec-b637-1bfda337292b', '6cbecbda-f5fe-11ec-b637-1bfda337292b', '장소7', '장소명', '내용', false, now(), now());
insert into place(id, user_id, address, name, content, deleted, create_date, modified_date) values('99a126f6-f5ff-11ec-b637-1bfda337292b', '6cbecbda-f5fe-11ec-b637-1bfda337292b', '장소8', '장소명', '내용', false, now(), now());
insert into place(id, user_id, address, name, content, deleted, create_date, modified_date) values('99e377b8-f5ff-11ec-b637-1bfda337292b', '6cbecbda-f5fe-11ec-b637-1bfda337292b', '장소9', '장소명', '내용', false, now(), now());
insert into place(id, user_id, address, name, content, deleted, create_date, modified_date) values('9a281bca-f5ff-11ec-b637-1bfda337292b', '6cbecbda-f5fe-11ec-b637-1bfda337292b', '장소10', '장소명', '내용', false, now(), now());

select * from place;
```

- 리뷰 생성

```sql
// 리뷰 등록자 account_id_3
insert into review(id, user_id, place_id, content, deleted, create_date, modified_date) values('c64e048a-f5ff-11ec-b637-1bfda337292b', '6d07a26a-f5fe-11ec-b637-1bfda337292b', '7711e300-f5ff-11ec-b637-1bfda337292b', '내용1', false, now(), now());
insert into review(id, user_id, place_id, content, deleted, create_date, modified_date) values('c6b50a22-f5ff-11ec-b637-1bfda337292b', '6d07a26a-f5fe-11ec-b637-1bfda337292b', '97d1288a-f5ff-11ec-b637-1bfda337292b', '내용2', false, now(), now());
insert into review(id, user_id, place_id, content, deleted, create_date, modified_date) values('c700acde-f5ff-11ec-b637-1bfda337292b', '6d07a26a-f5fe-11ec-b637-1bfda337292b', '9834e4a6-f5ff-11ec-b637-1bfda337292b', '내용3', false, now(), now());
insert into review(id, user_id, place_id, content, deleted, create_date, modified_date) values('c749cda6-f5ff-11ec-b637-1bfda337292b', '6d07a26a-f5fe-11ec-b637-1bfda337292b', '988578e4-f5ff-11ec-b637-1bfda337292b', '내용4', false, now(), now());
insert into review(id, user_id, place_id, content, deleted, create_date, modified_date) values('c7910720-f5ff-11ec-b637-1bfda337292b', '6d07a26a-f5fe-11ec-b637-1bfda337292b', '98d0e36a-f5ff-11ec-b637-1bfda337292b', '내용5', false, now(), now());

// 리뷰 등록자 account_id_4
insert into review(id, user_id, place_id, content, deleted, create_date, modified_date) values('c7dc7688-f5ff-11ec-b637-1bfda337292b', '6d49d07c-f5fe-11ec-b637-1bfda337292b', '9919a50a-f5ff-11ec-b637-1bfda337292b', '내용6', false, now(), now());
insert into review(id, user_id, place_id, content, deleted, create_date, modified_date) values('c828c560-f5ff-11ec-b637-1bfda337292b', '6d49d07c-f5fe-11ec-b637-1bfda337292b', '995c9c3e-f5ff-11ec-b637-1bfda337292b', '내용7', false, now(), now());
insert into review(id, user_id, place_id, content, deleted, create_date, modified_date) values('c86eece8-f5ff-11ec-b637-1bfda337292b', '6d49d07c-f5fe-11ec-b637-1bfda337292b', '99a126f6-f5ff-11ec-b637-1bfda337292b', '내용8', false, now(), now());
insert into review(id, user_id, place_id, content, deleted, create_date, modified_date) values('c8b245ce-f5ff-11ec-b637-1bfda337292b', '6d49d07c-f5fe-11ec-b637-1bfda337292b', '99e377b8-f5ff-11ec-b637-1bfda337292b', '내용9', false, now(), now());
insert into review(id, user_id, place_id, content, deleted, create_date, modified_date) values('c8f6b43e-f5ff-11ec-b637-1bfda337292b', '6d49d07c-f5fe-11ec-b637-1bfda337292b', '9a281bca-f5ff-11ec-b637-1bfda337292b', '내용10', false, now(), now());
```

### 테스트 방법

1. 사용자와 장소가 미리 생성되어 있어야 하기 때문에 위의 `사용자 생성 쿼리`와 `장소 생성 쿼리`를 모두 실행한다.
2. 리뷰 이벤트를 위해 위의 `리뷰 생성 쿼리` 1개를 실행한다.
3. 등록한 리뷰의 값으로 포인트 적립 요청을 한다.
4. point, point_history 테이블을 확인한다.
5. 포인트 조회 API를 호출하여 현재 포인트를 확인한다.

## 데이터베이스 설계

- MySQL8 버전을 사용했습니다.
- DDL

point, point_history가 포인트 관리 테이블이고, 나머지 테이블은 리뷰와 관련된 테이블입니다.

forein key는 생성 시 자동으로 index가 생성되어 DDL을 첨부하지 않았습니다.

```sql
/* 회원 테이블 생성 */
create table user (
	id varchar(255),
	account_id varchar(255) unique not null,
	deleted tinyint not null,
	create_date timestamp not null,
	modified_date timestamp not null,
	primary key (id)
) engine=innodb;

/* 장소 테이블 생성 */
create table place (
	id varchar(255),
	user_id varchar(255) not null,
	address varchar(255) unique not null,
	name varchar(255) not null,
	content mediumtext not null,
	deleted tinyint not null,
	create_date timestamp not null,
	modified_date timestamp not null,
	primary key (id),
	foreign key (user_id) references user(id)
) engine=innodb;

/* 리뷰 테이블 생성 */
create table review (
	id varchar(255),
	user_id varchar(255) not null,
	place_id varchar(255) not null,
	content mediumtext not null,
	deleted tinyint not null,
	create_date timestamp not null,
	modified_date timestamp not null,
	primary key (id),
	foreign key (user_id) references user(id),
	foreign key (place_id) references place(id)	
) engine=innodb;

/* 적립금 테이블 생성 */
create table point (
	id varchar(255),
	user_id varchar(255) unique not null,
	score int not null,
	create_date timestamp not null,
	modified_date timestamp not null,
	primary key (id),
	foreign key (user_id) references user(id)
) engine=innodb;

/* 적립금 부여 히스토리 테이블 생성 */
create table point_history (
	id varchar(255) not null,
	user_id varchar(255) not null,
	place_id varchar(255) not null,
	review_id varchar(255) not null,
	action varchar(255) not null,
	photo_count int not null,
	score int not null,
	create_date timestamp not null,
	modified_date timestamp not null,
	primary key (id)
) engine=innodb;
create index user_id on point_history (user_id);
create index place_id on point_history (place_id);
create index review_id on point_history (review_id);
create index create_date on point_history (create_date);

/* 적립금 부여 히스토리 테이블 생성 */
create table photo (
	id varchar(255) not null,
	review_id binary(16) not null,
	origin_file_name varchar(255) not null,
	store_file_name varchar(255) not null,
	create_date timestamp not null,
	modified_date timestamp not null,
	primary key (id),
	foreign key (review_id) references review(id)
) engine=innodb;
```