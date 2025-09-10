package rescuelink;

public abstract class User {
    protected int id;
    protected String name;
    protected String location;

    // Constructor
    public User(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    // Encapsulation: Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    // Polymorphism: Abstract method to be overridden by subclasses
    public abstract void displayInfo();
}
