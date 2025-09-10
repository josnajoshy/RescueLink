package rescuelink;

public class Victim extends User {
    private String condition;
    private String incidentType;
    private String severity;
    private int peopleAffected;
    private boolean immediateRescue;
    private String status; // For Admin updates (Pending, In Progress, Rescued, etc.)

    // Constructor
    public Victim(int id, String name, String location, String condition, String incidentType, String severity, int peopleAffected, boolean immediateRescue, String string5) {
        super(id, name, location);
        this.condition = condition;
        this.incidentType = incidentType;
        this.severity = severity;
        this.peopleAffected = peopleAffected;
        this.immediateRescue = immediateRescue;
        this.status = "Pending"; // default status when victim is reported
    }

    // --- Getters ---
    public String getCondition() {
        return condition;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public String getSeverity() {
        return severity;
    }

    public int getPeopleAffected() {
        return peopleAffected;
    }

    public boolean isImmediateRescue() {
        return immediateRescue;
    }

    public String getStatus() {
        return status;
    }

    // --- Setters ---
    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setPeopleAffected(int peopleAffected) {
        this.peopleAffected = peopleAffected;
    }

    public void setImmediateRescue(boolean immediateRescue) {
        this.immediateRescue = immediateRescue;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // --- Polymorphism: Display Info ---
    @Override
    public void displayInfo() {
        System.out.println("Victim ID: " + getId() +
                " | Name: " + getName() +
                " | Location: " + getLocation() +
                " | Condition: " + condition +
                " | Incident: " + incidentType +
                " | Severity: " + severity +
                " | People Affected: " + peopleAffected +
                " | Immediate Rescue: " + immediateRescue +
                " | Status: " + status);
    }

    String getPeopleCount() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
