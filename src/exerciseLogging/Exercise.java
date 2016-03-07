package exerciseLogging;

public class Exercise {

    private final int id;
    private final String name, description;

    public Exercise(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
