package rescuelink;

public class Volunteer extends User {
    private String phoneNo;
    private String skill;
    private boolean availability;

    // Constructor with ID (for existing records)
    public Volunteer(int id, String name, String location, String phoneNo, String skill, boolean availability) {
        super(id, name, location); // Inherited from User
        this.phoneNo = phoneNo;
        this.skill = skill;
        this.availability = availability;
    }

    // Constructor without ID (for new registrations)
    public Volunteer(String name, String location, String phoneNo, String skill, boolean availability) {
        super(0, name, location); // Default ID for new records
        this.phoneNo = phoneNo;
        this.skill = skill;
        this.availability = availability;
    }

    // Encapsulation: Getters and Setters
    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }

    public boolean isAvailability() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }

    public int getVolunteerId() { return id; } // Inherited from User
    public void setVolunteerId(int volunteerId) { this.id = volunteerId; }

    // Polymorphism: Override abstract method
    @Override
    public void displayInfo() {
        System.out.println("Volunteer: " + name + " | Location: " + location + " | Skill: " + skill + " | Available: " + (availability ? "Yes" : "No"));
    }
}
