-- Insertion de données de base
USE mpaudb;

INSERT INTO mpaudb.typeinter (type_libelle) VALUES ('SOS');
INSERT INTO mpaudb.typeinter (type_libelle) VALUES ('15');

INSERT INTO mpaudb.soustypeinter (soustype_libelle) VALUES ('Détresse Cardiologique');
INSERT INTO mpaudb.soustypeinter (soustype_libelle) VALUES ('Détresse Neurologique');
INSERT INTO mpaudb.soustypeinter (soustype_libelle) VALUES ('Détresse Respiratoire');
INSERT INTO mpaudb.soustypeinter (soustype_libelle) VALUES ('Petit Bobo');
INSERT INTO mpaudb.soustypeinter (soustype_libelle) VALUES ('Plaie');
INSERT INTO mpaudb.soustypeinter (soustype_libelle) VALUES ('Psychiatrie');
INSERT INTO mpaudb.soustypeinter (soustype_libelle) VALUES ('Traumatologie');
INSERT INTO mpaudb.soustypeinter (soustype_libelle) VALUES ('Autre');

INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('-10');
INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('10-20');
INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('20-30');
INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('30-40');
INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('40-50');
INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('50-60');
INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('60-70');
INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('70-80');
INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('80-90');
INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('90-100');
INSERT INTO mpaudb.agepatient (age_libelle) VALUES ('+100');
