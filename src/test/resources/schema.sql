drop table if exists promotion CASCADE;
CREATE TABLE IF NOT EXISTS promotion (
	id	BIGINT	NOT NULL AUTO_INCREMENT,
	name	VARCHAR(50)	NOT NULL,
	description	VARCHAR(255)	NOT NULL,
	started_at	DATETIME	NOT NULL,
	finished_at	DATETIME	NOT NULL,
	is_display	TINYINT	NOT NULL,
	banner_url	VARCHAR(1024)	NOT NULL,
	main_image_url	VARCHAR(1024)	NOT NULL,
	mall_type	VARCHAR(10)	NOT NULL	COMMENT 'ONLINE/OFFLINE',
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);
drop table if exists coupon_group CASCADE;
CREATE TABLE IF NOT EXISTS coupon_group (
	id	BIGINT	NOT NULL AUTO_INCREMENT,
	promotion_id	BIGINT	NOT NULL,
	exclusive_type	VARCHAR(10)	NOT NULL	COMMENT 'APP/OFFLINE',
	used_started_at	DATETIME	NOT NULL,
	used_finished_at	DATETIME	NOT NULL,
	issued_started_at	DATETIME	NOT NULL,
	issued_finished_at	DATETIME	NOT NULL,
	is_random	TINYINT	NOT NULL,
	is_issued	TINYINT	NOT NULL,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);
drop table if exists coupon CASCADE;
CREATE TABLE IF NOT EXISTS coupon (
	id	BIGINT	NOT NULL AUTO_INCREMENT,
	coupon_group_id	BIGINT	NOT NULL,
	discount_type	VARCHAR(10)	NOT NULL	COMMENT '정액/정률',
	fixed_discount_amount	DECIMAL(8,2)	NULL	COMMENT '단위(원)',
	percentage_discount_rate	DECIMAL(4,2)	NULL	COMMENT '단위(%)',
	min_puchase_amount	DECIMAL(8,2)	NOT NULL	COMMENT '단위(원)',
	max_discount_amount	DECIMAL(8,2)	NULL	COMMENT '단위(원)',
	name	VARCHAR(255)	NOT NULL,
	initial_quantity	INT	NOT NULL,
	remain_quantity	INT	NOT NULL,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);
drop table if exists member_coupon CASCADE;
CREATE TABLE IF NOT EXISTS member_coupon (
	id	BIGINT	NOT NULL	AUTO_INCREMENT,
	coupon_id	BIGINT	NOT NULL,
	coupon_group_id BIGINT NOT NULL,
	member_id	BIGINT	NOT NULL,
	issued_at	DATETIME	NOT NULL,
	used_at	DATETIME	NULL,
	state	VARCHAR(10)	NOT NULL	COMMENT '사용전/사용후/만료',
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);
drop table if exists promotion_history CASCADE;
CREATE TABLE IF NOT EXISTS promotion_history (
	id	BIGINT	NOT NULL AUTO_INCREMENT,
	member_id	BIGINT	NOT NULL,
	promotion_id	BIGINT	NOT NULL,
	coupon_group_id	BIGINT	NOT NULL,
	participated_dt	DATETIME	NOT NULL,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);
drop table if exists member CASCADE;
CREATE TABLE IF NOT EXISTS member (
	id	BIGINT	NOT NULL AUTO_INCREMENT,
	email	VARCHAR(45)	NOT NULL,
	password	VARCHAR(255) NOT NULL,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);