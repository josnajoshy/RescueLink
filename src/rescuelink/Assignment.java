
package rescuelink;



import java.time.LocalDateTime;

public class Assignment {
    private int id;
    private Volunteer volunteer;
    private Victim victim;
    private LocalDateTime assignedAt;
    private boolean isActive;

    public Assignment(int id, Volunteer volunteer, Victim victim, LocalDateTime assignedAt, boolean isActive) {
        this.id = id;
        this.volunteer = volunteer;
        this.victim = victim;
        this.assignedAt = assignedAt;
        this.isActive = isActive;
    }

    public Assignment(Volunteer volunteer, Victim victim) {
        this(0, volunteer, victim, LocalDateTime.now(), true);
    }

    // Getters and setters
    public int getId() { return id; }
    public Volunteer getVolunteer() { return volunteer; }
    public Victim getVictim() { return victim; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public boolean isActive() { return isActive; }

    public void setId(int id) { this.id = id; }
    public void setVolunteer(Volunteer volunteer) { this.volunteer = volunteer; }
    public void setVictim(Victim victim) { this.victim = victim; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public void setActive(boolean active) { isActive = active; }
}
