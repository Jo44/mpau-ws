-- Création des tables
-- (MySQL v5.6.5 minimum : plusieurs DEFAULT CURRENT_TIMESTAMP par table)
-- (MariaDB)
USE mpaudb;

DROP TABLE mpaudb.workperiods;
DROP TABLE mpaudb.workdays;
DROP TABLE mpaudb.agepatient;
DROP TABLE mpaudb.soustypeinter;
DROP TABLE mpaudb.typeinter;
DROP TABLE mpaudb.interventions;
DROP TABLE mpaudb.users;

CREATE TABLE mpaudb.users
(
    user_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    user_pass VARCHAR(100) NOT NULL,
    user_email VARCHAR(50) NOT NULL UNIQUE,
    user_nb_inter INT NOT NULL DEFAULT 0,
    user_inter_id_max INT NOT NULL DEFAULT 0,
    user_inscription_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE mpaudb.interventions
(
	inter_id INT NOT NULL,
    inter_user_id INT NOT NULL,
	inter_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	inter_duree INT NOT NULL,
    inter_secteur VARCHAR(100) NOT NULL,
    inter_smur TINYINT(1) NOT NULL,
    inter_type_id INT NOT NULL,
    inter_soustype_id INT NOT NULL,
	inter_agepatient_id INT NOT NULL,
    inter_commentaire TEXT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci',
    inter_date_insert TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    inter_date_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(inter_id, inter_user_id)
);

CREATE TABLE mpaudb.typeinter
(
	type_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    type_libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE mpaudb.soustypeinter
(
	soustype_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    soustype_libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE mpaudb.agepatient
(
	age_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    age_libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE mpaudb.workdays
(
	wd_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    wd_user_id INT NOT NULL,
    wd_start TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    wd_stop TIMESTAMP NULL DEFAULT NULL,
    wd_finished TINYINT(1) NOT NULL DEFAULT '0',
    wd_date_insert TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    wd_date_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE mpaudb.workperiods
(
	wp_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    wp_wd_id INT NOT NULL,
    wp_start TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    wp_stop TIMESTAMP NULL DEFAULT NULL,
    wp_finished TINYINT(1) NOT NULL DEFAULT '0',
    wp_date_insert TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    wp_date_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);