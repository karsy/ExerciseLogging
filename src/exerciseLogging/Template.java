package exerciseLogging;

import java.util.HashSet;
import java.util.Set;

public class Template{

    private final int id;
    private String name, description;
    private Set<Exercise> exercises;
    private boolean updated = false;

    public Template(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exercises = new HashSet<>();
    }

    public Template(int id, String name, String description, Set<Exercise> exercises){
        this.id = id;
        this.name = name;
        this.description = description;
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public String toString(){
        return this.name;
    }
}
