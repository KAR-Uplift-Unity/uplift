# before you use the seeder file
# step 1: use the uplift-db-migration file (if you already have the db fallow step 2 then drop the DB as well.)
# step 2: (drop all tables in db in order 1.post_categories, 2.categories, 3.comments, 4.hearts, 5.images, 6.tags, 7.updates, 8.posts, 9.users)
# step 2 continued: if you dont have tables in the db run the application (NOT THE SQL SEEDER) so that the tables are generated.
# step 3: run this seeder file!
# step 4; Please make sure that the "flagged" column on both the comments, and post are set to false. and on post set days to 0. all should be good to go!

USE uplift_db;

INSERT INTO categories (id ,cat) VALUES
(1,'Animals'),
(2,'Community'),
(3,'Education'),
(4,'Emergencies'),
(5,'Environment'),
(6,'Family'),
(7,'Medical'),
(8,'Monthly Bills'),
(9,'Recovery'),
(10,'Travel'),
(11,'Volunteer'),
(12,'Youth'),
(13,'Event'),
(14,'Advice'),
(15,'Other');

insert into users (archive, email, password, username, admin, profile_image_url, archive_date) values
(false, 'JeyNoir1@gmail.com', '$2a$10$dZVoT/ClmHqeqgDmSf7qlOBm7IIWOYQcWI1W3W1nalWjQHIzt0LUm', 'JemalKnowles1', true, 'https://cdn.filestackcontent.com/LxaFGP7ERAm8kt7J6efL', null),
(false, 'robert.j.mendez3@gmail.com', '$2a$10$a7La9jCqVG/YTPIiS2G6m.Pw1.qR2x8vT8hNCu1X/rWWAQBB./mJO', 'rjmendez', true, 'https://cdn.filestackcontent.com/XwB1S9aoR0Smkk9RQyB5', null),
(false, 'aaron.reid.bond@gmail.com', '$2a$10$4WL6toxwoYnUbnHNULZiEufdnkN9N.EBkk63g2IS084MklIgVR7wi', 'Aaron', true, null, null),
(false, 'rtucker0@over-blog.com', '$2a$10$ufpvPGP47iFdFFGDA1maBOqiGSQ.DTYl888Vis6zp1cUUStdkWMmu', 'dharms0', false, 'https://robohash.org/eiusquaeratexplicabo.png?size=250x250&set=set1', null),
(false, 'awalasik1@imgur.com', '$2a$10$U0voc6OXWn5OnKzpe3aleOeXJ3EYIHbUKKmRMXg8okuSghTKOCzSG', 'hharmson1', false, 'https://robohash.org/idetvoluptatem.png?size=250x250&set=set1', null),
(false, 'coxlade2@canalblog.com', '$2a$10$4S0TexH2pu5mw0/Ec4rTQ.HB1aG70w/EXHllwbj6mIMQjetVLfuxC', 'callonby2', false, null, null),
(false, 'bburcombe3@squarespace.com', '$2a$10$UtfPS05aca9Sjtx4qTKix.ZcCI55r3qdJS6iIUQRFtCyFYgVGzXKS', 'visitor', false, 'https://robohash.org/earumimpeditet.png?size=250x250&set=set1', null);
