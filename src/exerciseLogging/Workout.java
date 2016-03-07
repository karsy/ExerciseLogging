package exerciseLogging;

import java.util.Date;

public class Workout {

    private final Date dateTime;
    private final int exercise_id, reps, sets, distance, duration;
    private final float weight;

    public Workout(Date dateTime, int exercise_id, float weight, int reps, int sets, int distance, int duration){
        this.dateTime = dateTime;
        this.exercise_id = exercise_id;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.distance = distance;
        this.duration = duration;
    }

    public Date getDateTime() {
        return dateTime;
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
