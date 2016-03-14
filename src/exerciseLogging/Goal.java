package exerciseLogging;

import java.util.Date;

public class Goal {

    private final int reps, sets, distance, duration, exercise_id;
    private final float weight;
    private final Date created, achieved;
    private final String name;

    public Goal(int reps, int sets, int distance, int duration, int exercise_id, float weight, Date created, Date achieved, String name){
        this.reps = reps;
        this.sets = sets;
        this.distance = distance;
        this.duration = duration;
        this.exercise_id = exercise_id;
        this.weight = weight;
        this.created = created;
        this.achieved = achieved;
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }

    public int getDuration() {
        return duration;
    }

    public int getExercise_id() {
        return exercise_id;
    }

    public float getWeight() {
        return weight;
    }

    public Date getCreated() {
        return created;
    }

    public Date getAchieved() {
        return achieved;
    }

    @Override
    public String toString() {
        return created.toString()+ " " + name;
    }
}
