--Student
INSERT INTO student(grade, year_of_study, created_date, last_modified_date, user_id, created_by, email, faculty_section, last_modified_by, name, password, role)
Values (10, 1, CURRENT_DATE, CURRENT_DATE, nextval('student_user_id_seq'), 'Roberto', 'roberto.klotzl@gmail.com', 'IR', 'Roberto', 'Klotzl Roberto', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'STUDENT');

INSERT INTO student(grade, year_of_study, created_date, last_modified_date, user_id, created_by, email, faculty_section, last_modified_by, name, password, role)
Values (10, 1, CURRENT_DATE, CURRENT_DATE, nextval('student_user_id_seq'), 'Roberto', 'kiss.sergiu@gmail.com', 'IR', 'Roberto', 'Kiss Sergiu', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'STUDENT');

INSERT INTO student(grade, year_of_study, created_date, last_modified_date, user_id, created_by, email, faculty_section, last_modified_by, name, password, role)
Values (9, 1, CURRENT_DATE, CURRENT_DATE, nextval('student_user_id_seq'), 'Roberto', 'lazar.catalin@gmail.com', 'IR', 'Roberto', 'Lazar Catalin', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'STUDENT');

INSERT INTO student(grade, year_of_study, created_date, last_modified_date, user_id, created_by, email, faculty_section, last_modified_by, name, password, role)
Values (8, 1, CURRENT_DATE, CURRENT_DATE, nextval('student_user_id_seq'), 'Roberto', 'ilea.robert@gmail.com', 'IR', 'Roberto', 'Ilea Robert', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'STUDENT');

INSERT INTO student(grade, year_of_study, created_date, last_modified_date, user_id, created_by, email, faculty_section, last_modified_by, name, password, role)
Values (10, 1, CURRENT_DATE, CURRENT_DATE, nextval('student_user_id_seq'), 'Roberto', 'iacob.alex@gmail.com', 'IR', 'Roberto', 'Iacob Alexandru', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'STUDENT');

INSERT INTO student(grade, year_of_study, created_date, last_modified_date, user_id, created_by, email, faculty_section, last_modified_by, name, password, role)
Values (5, 1, CURRENT_DATE, CURRENT_DATE, nextval('student_user_id_seq'), 'Roberto', 'gamidiov.djahid@gmail.com', 'IR', 'Roberto', 'Gamidov Djahid', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'STUDENT');

INSERT INTO student(grade, year_of_study, created_date, last_modified_date, user_id, created_by, email, faculty_section, last_modified_by, name, password, role)
Values (5, 1, CURRENT_DATE, CURRENT_DATE, nextval('student_user_id_seq'), 'Roberto', 'elvis@gmail.com', 'IR', 'Roberto', 'Elvis', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'STUDENT');

--Admin
INSERT INTO admin(created_date, last_modified_date, user_id, created_by, email, last_modified_by, name, password, role)
VALUES (CURRENT_DATE, CURRENT_DATE, nextval('admin_user_id_seq'), 'Roberto', 'ionel@gmail.com', 'Roberto', 'Ionel', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'ADMIN');

INSERT INTO admin(created_date, last_modified_date, user_id, created_by, email, last_modified_by, name, password, role)
VALUES (CURRENT_DATE, CURRENT_DATE, nextval('admin_user_id_seq'), 'Roberto', 'ion@gmail.com', 'Roberto', 'Ion', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'ADMIN');

--Course
INSERT INTO course(maximum_students_allowed, year_of_study, course_id, created_date, last_modified_date, category, course_name, created_by, description, faculty_section, last_modified_by, teacher_name)
VALUES (100, 2, nextval('course_course_id_seq'), CURRENT_DATE, CURRENT_DATE, 'Programming', 'C++', 'Roberto', 'c++ 101', 'IR', 'Roberto', 'Dan Popescu');

INSERT INTO course(maximum_students_allowed, year_of_study, course_id, created_date, last_modified_date, category, course_name, created_by, description, faculty_section, last_modified_by, teacher_name)
VALUES (100, 2, nextval('course_course_id_seq'), CURRENT_DATE, CURRENT_DATE, 'Programming', 'Java', 'Roberto', 'java 101', 'IR', 'Roberto', 'Fred');

INSERT INTO course(maximum_students_allowed, year_of_study, course_id, created_date, last_modified_date, category, course_name, created_by, description, faculty_section, last_modified_by, teacher_name)
VALUES (100, 2, nextval('course_course_id_seq'), CURRENT_DATE, CURRENT_DATE, 'Programming', 'Python', 'Roberto', 'python 101', 'IR', 'Roberto', 'Morris');

INSERT INTO course(maximum_students_allowed, year_of_study, course_id, created_date, last_modified_date, category, course_name, created_by, description, faculty_section, last_modified_by, teacher_name)
VALUES (100, 2, nextval('course_course_id_seq'), CURRENT_DATE, CURRENT_DATE, 'Databases', 'MySql', 'Roberto', 'sql', 'IR', 'Roberto', 'George');

INSERT INTO course(maximum_students_allowed, year_of_study, course_id, created_date, last_modified_date, category, course_name, created_by, description, faculty_section, last_modified_by, teacher_name)
VALUES (100, 2, nextval('course_course_id_seq'), CURRENT_DATE, CURRENT_DATE, 'Databases', 'NoSql', 'Roberto', '!sql', 'IR', 'Roberto', 'Alex');

INSERT INTO course(maximum_students_allowed, year_of_study, course_id, created_date, last_modified_date, category, course_name, created_by, description, faculty_section, last_modified_by, teacher_name)
VALUES (100, 1, nextval('course_course_id_seq'), CURRENT_DATE, CURRENT_DATE, 'Programming', 'Algorithms', 'Roberto', 'djikstra', 'IR', 'Roberto', 'Dorel');

--Course Schedule
INSERT INTO course_schedule(end_time, start_time, course_id, created_date, last_modified_date, created_by, day, last_modified_by)
VALUES ('9:30', '8:00', 1, CURRENT_DATE, CURRENT_DATE, 'Roberto', 'MONDAY', 'Roberto');

--Enrollment
INSERT INTO enrollment(priority, course_id, created_date, enrollment_id, last_modified_date, student_id, created_by, last_modified_by, status)
VALUES (1, 1, CURRENT_DATE, nextval('enrollment_enrollment_id_seq'), CURRENT_DATE, 1, 'Roberto', 'Roberto', 'ACCEPTED');

INSERT INTO enrollment(priority, course_id, created_date, enrollment_id, last_modified_date, student_id, created_by, last_modified_by, status)
VALUES (2, 2, CURRENT_DATE, nextval('enrollment_enrollment_id_seq'), CURRENT_DATE, 1, 'Roberto', 'Roberto', 'PENDING');

INSERT INTO enrollment(priority, course_id, created_date, enrollment_id, last_modified_date, student_id, created_by, last_modified_by, status)
VALUES (3, 3, CURRENT_DATE, nextval('enrollment_enrollment_id_seq'), CURRENT_DATE, 1, 'Roberto', 'Roberto', 'PENDING');

INSERT INTO enrollment(priority, course_id, created_date, enrollment_id, last_modified_date, student_id, created_by, last_modified_by, status)
VALUES (1, 4, CURRENT_DATE, nextval('enrollment_enrollment_id_seq'), CURRENT_DATE, 1, 'Roberto', 'Roberto', 'ACCEPTED');

INSERT INTO enrollment(priority, course_id, created_date, enrollment_id, last_modified_date, student_id, created_by, last_modified_by, status)
VALUES (2, 5, CURRENT_DATE, nextval('enrollment_enrollment_id_seq'), CURRENT_DATE, 1, 'Roberto', 'Roberto', 'PENDING');

INSERT INTO enrollment(priority, course_id, created_date, enrollment_id, last_modified_date, student_id, created_by, last_modified_by, status)
VALUES (1, 1, CURRENT_DATE, nextval('enrollment_enrollment_id_seq'), CURRENT_DATE, 2, 'Roberto', 'Roberto', 'PENDING');

INSERT INTO enrollment(priority, course_id, created_date, enrollment_id, last_modified_date, student_id, created_by, last_modified_by, status)
VALUES (3, 2, CURRENT_DATE, nextval('enrollment_enrollment_id_seq'), CURRENT_DATE, 2, 'Roberto', 'Roberto', 'PENDING');

INSERT INTO enrollment(priority, course_id, created_date, enrollment_id, last_modified_date, student_id, created_by, last_modified_by, status)
VALUES (2, 3, CURRENT_DATE, nextval('enrollment_enrollment_id_seq'), CURRENT_DATE, 2, 'Roberto', 'Roberto', 'PENDING');

INSERT INTO enrollment(priority, course_id, created_date, enrollment_id, last_modified_date, student_id, created_by, last_modified_by, status)
VALUES (2, 4, CURRENT_DATE, nextval('enrollment_enrollment_id_seq'), CURRENT_DATE, 3, 'Roberto', 'Roberto', 'ACCEPTED');

INSERT INTO enrollment(priority, course_id, created_date, enrollment_id, last_modified_date, student_id, created_by, last_modified_by, status)
VALUES (1, 5, CURRENT_DATE, nextval('enrollment_enrollment_id_seq'), CURRENT_DATE, 3, 'Roberto', 'Roberto', 'PENDING');
