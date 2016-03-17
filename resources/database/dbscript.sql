DROP SCHEMA IF EXISTS TrainingDiary;
CREATE SCHEMA IF NOT EXISTS TrainingDiary;
use TrainingDiary;

CREATE TABLE IF NOT EXISTS Template(
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(400),
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Exercise(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(400),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS ExerciseReplacement(
    exercise_id INT NOT NULL,
    replacement_id INT NOT NULL,
    PRIMARY KEY(exercise_id, replacement_id),
    FOREIGN KEY(exercise_id) REFERENCES Exercise(id),
    FOREIGN KEY(replacement_id) REFERENCES Exercise(id)
);

CREATE TABLE IF NOT EXISTS Goal(
    weight FLOAT,
    reps INT,
    sets INT,
    distance INT,
    duration INT,
    created DATETIME DEFAULT NOW(),
    achieved DATETIME,
    exercise_id INT NOT NULL,
    PRIMARY KEY (exercise_id, created),
    FOREIGN KEY(exercise_id) REFERENCES Exercise(id)
);

CREATE TABLE IF NOT EXISTS TemplateExercise(
	template_id INT NOT NULL,
	exercise_id INT NOT NULL,
	PRIMARY KEY(template_id, exercise_id)
);

CREATE TABLE IF NOT EXISTS Category(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(200),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS SubCategory(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(200),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS CategoryRelations(
    category_id INT NOT NULL,
    subcategory_id INT NOT NULL,
    PRIMARY KEY(category_id, subcategory_id),
    FOREIGN KEY(category_id) REFERENCES Category(id),
    FOREIGN KEY(subcategory_id)  REFERENCES SubCategory(id)
);

CREATE TABLE IF NOT EXISTS ExerciseSubCategory(
    exercise_id INT NOT NULL,
    subcategory_id INT NOT NULL,
    PRIMARY KEY(exercise_id, subcategory_id),
    FOREIGN KEY(exercise_id) REFERENCES Exercise(id),
    FOREIGN KEY(subcategory_id) REFERENCES SubCategory(id)
);

CREATE TABLE IF NOT EXISTS Indoor(
    id INT NOT NULL AUTO_INCREMENT,
    air_condition VARCHAR(100),
    audience INT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Outdoor(
    id INT NOT NULL AUTO_INCREMENT,
    weather VARCHAR(200),
    temperature INT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS ConditionType(
    id INT NOT NULL AUTO_INCREMENT,
    outdoor_id INT NOT NULL,
    indoor_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(outdoor_id) REFERENCES Outdoor(id),
    FOREIGN KEy(indoor_id) REFERENCES Indoor(id)
);

CREATE TABLE IF NOT EXISTS Workout(
    time_of_exercise DATETIME NOT NULL,
    duration INT NOT NULL,
    shape INT,
    performance INT,
    note VARCHAR(400),
    condition_id INT NOT NULL,
    template_id INT NOT NULL,
    PRIMARY KEY(time_of_exercise),
    FOREIGN KEY(condition_id) REFERENCES ConditionType(id),
    FOREIGN KEY(template_id) REFERENCES Template(id)
);

CREATE TABLE IF NOT EXISTS Result(
    workout_id DATETIME NOT NULL,
    exercise_id INT NOT NULL,
    weight FLOAT,
    reps INT,
    sets INT,
    distance INT,
    duration INT,
    PRIMARY KEY(workout_id, exercise_id),
    FOREIGN KEY(workout_id) REFERENCES Workout(time_of_exercise),
    FOREIGN KEY(exercise_id) REFERENCES Exercise(id)
);

INSERT INTO indoor
VALUES (1, "void", 0);

INSERT INTO outdoor
VALUES (1, "void", 0);