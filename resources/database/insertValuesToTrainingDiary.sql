INSERT INTO exercise
VALUES (1, "Squat", "Put a barbell on your back, and squat down and back up again."),
(2, "Deadlift", "Lift a barbell up from the ground."),
(3, "Bench press", "Push a barbell up and lower it back down above your chest."),
(4, "Jogging", "Jog at a nice pace");

INSERT INTO category
VALUES (1, "Strength Training"), (2, "Cardiovascular Training");

INSERT INTO subcategory 
VALUES (1, "Lower Body"), (2, "Upper Body"), (3, "Running");

INSERT INTO categoryrelations
VALUES (1, 1), (1, 2), (2, 3);

INSERT INTO exercisesubcategory
VALUES (1, 1), (2, 1), (3, 2), (4,3);

INSERT INTO goal
VALUES (100.0, 5, 5, null, null, "2016-03-02", "2016-03-07", 1),
(null, null, null, 5, 30, "2016-03-01", null, 4),
(140.0, 5, 5, null, null, "2016-03-07", null, 1);

INSERT INTO indoor
VALUES (2, "Decent", 5);

INSERT INTO outdoor
VALUES (2, "Sunny, hot", 30);

INSERT INTO indoor
VALUES (1, "void", 0);

INSERT INTO outdoor
VALUES (1, "void", 0);

INSERT INTO conditiontype
VALUES (1, 2, 2), (2, 2, 2);

INSERT INTO template
VALUES (1, "Full Body Strength", "A 5x5 Strength workout."), (2, "Jog", "A template for jogging.");

INSERT INTO templateexercise
VALUES (1, 1), (1, 2), (1, 3), (2, 4);

INSERT INTO workout
VALUES ("2016-03-03", 60, 7, 7, "Did pretty well, want to lift more weights.", 1, 1), 
("2016-03-04", 30, 8, 8, "Im pretty good at jogging.", 2, 2), ("2016-03-07", 60, 10, 10, "ACHIEVED MY GOAL!", 1, 1);

INSERT INTO result
VALUES ("2016-03-03", 1, 90.0, 5, 5, null, null), ("2016-03-03", 2, 120.0, 5, 5, null, null), ("2016-03-03", 3, 60.0, 5, 5, null, null),
("2016-03-04", 4, null, null, null, 5, 30), ("2016-03-07", 1, 100.0, 5, 5, null, null), ("2016-03-07", 2, 125.0, 5, 5, null, null), ("2016-03-07", 3, 63.0, 5, 5, null, null);