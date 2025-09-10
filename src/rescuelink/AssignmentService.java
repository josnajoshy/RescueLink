
package rescuelink;



import java.time.LocalDateTime;
public class AssignmentService {
    private final AssignmentDAO assignmentDAO;
    private final AlertDAO alertDAO;

    public AssignmentService() {
        assignmentDAO = new AssignmentDAO();
        alertDAO = new AlertDAO();
    }

    // Assign volunteer to victim and notify both
    public boolean assignAndNotify(Volunteer volunteer, Victim victim) {
        // Step 1: Create assignment
        boolean assigned = assignmentDAO.assignVolunteerToVictim(volunteer, victim);
        if (!assigned) return false;

        // Step 2: Generate full alert for volunteer
        String volunteerMessage = """
                You have been assigned to a victim.
                Victim Name: %s
                Location: %s
                Condition: %s
                Number of people involved: %d
                """.formatted(
                victim.getName(),
                victim.getLocation(),
                victim.getCondition(),
                victim.getPeopleCount()
        );

        Alert volunteerAlert = new Alert(volunteer, volunteerMessage);
        alertDAO.sendAlert(volunteerAlert);

        // Step 3: Generate full alert for victim
        String victimMessage = """
                A volunteer has been assigned to help you.
                Volunteer Name: %s
                Location: %s
                Phone: %s
                Skill: %s
                Availability: %s
                """.formatted(
                volunteer.getName(),
                volunteer.getLocation(),
                volunteer.getPhoneNo(),
                volunteer.getSkill(),
                volunteer.isAvailability() ? "Yes" : "No"
        );

        Alert victimAlert = new Alert(victim, victimMessage);
        alertDAO.sendAlert(victimAlert);

        return true;
    }
}
