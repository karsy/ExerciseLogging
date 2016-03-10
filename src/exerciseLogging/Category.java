package exerciseLogging;


public class Category {

    private int id;
    private String name;

    public Category(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public String toString(){
        return this.id + " : " + this.name;
    }
}
