package exerciseLogging;

import java.util.Date;

public class Result {

    private final Date workout_id;
    private final int exercise_id, reps, sets, distance, duration;
    private final float weight;

    public Result(Date dateTime, int exercise_id, float weight, int reps, int sets, int distance, int duration){
        this.workout_id = dateTime;
        this.exercise_id = exercise_id;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.distance = distance;
        this.duration = duration;
    }

    public Date getDateTime() {
        return workout_id;
    }

    public int getExercise_id() {
        return exercise_id;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public float getWeight() {
        return weight;
    }
}
