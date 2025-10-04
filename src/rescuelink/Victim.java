package rescuelink;

public class Victim extends User {
    private String condition;
    private String incidentType;
    private String severity;
    private int peopleAffected;
    private boolean immediateRescue;
    private String status; // Pending, In Progress, Rescued, etc.
    private String phone;  // Added phone number

    // Constructor matching DB schema
    public Victim(int id, String name, String location,
                  String condition, String incidentType,
                  String severity, int peopleAffected,
                  boolean immediateRescue, String status) {
        super(id, name, location);
        this.condition = condition;
        this.incidentType = incidentType;
        this.severity = severity;
        this.peopleAffected = peopleAffected;
        this.immediateRescue = immediateRescue;
        this.status = (status == null || status.isEmpty()) ? "Pending" : status;
    }

    // Overloaded constructor (when phone is added later)
    public Victim(int id, String name, String location,
                  String condition, String incidentType,
                  String severity, int peopleAffected,
                  boolean immediateRescue, String status, String phone) {
        this(id, name, location, condition, incidentType, severity, peopleAffected, immediateRescue, status);
        this.phone = phone;
    }

    // --- Getters ---
    public String getCondition() { return condition; }
    public String getIncidentType() { return incidentType; }
    public String getSeverity() { return severity; }
    public int getPeopleAffected() { return peopleAffected; }
    public boolean isImmediateRescue() { return immediateRescue; }
    public String getStatus() { return status; }
    public String getPhone() { return phone; }

    // --- Setters ---
    public void setCondition(String condition) { this.condition = condition; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setPeopleAffected(int peopleAffected) { this.peopleAffected = peopleAffected; }
    public void setImmediateRescue(boolean immediateRescue) { this.immediateRescue = immediateRescue; }
    public void setStatus(String status) { this.status = status; }
    public void setPhone(String phone) { this.phone = phone; }

    // --- Polymorphism: Display Info ---
    @Override
    public void displayInfo() {
        System.out.println("Victim ID: " + getId() +
                " | Name: " + getName() +
                " | Location: " + getLocation() +
                " | Phone: " + (phone != null ? phone : "N/A") +
                " | Condition: " + condition +
                " | Incident: " + incidentType +
                " | Severity: " + severity +
                " | People Affected: " + peopleAffected +
                " | Immediate Rescue: " + immediateRescue +
                " | Status: " + status);
    }
}