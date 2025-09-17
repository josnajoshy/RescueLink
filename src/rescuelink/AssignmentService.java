package rescuelink;

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

        // Step 2: Generate alert for volunteer
        String volunteerMessage = String.format(
                "You have been assigned to a victim.\n" +
                "Victim Name: %s\n" +
                "Location: %s\n" +
                "Condition: %s\n" +
                "Number of people involved: %d",
                victim.getName(),
                victim.getLocation(),
                victim.getCondition(),
                victim.getPeopleAffected()   // ✅ corrected
        );

        Alert volunteerAlert = new Alert(volunteer, volunteerMessage);
        alertDAO.sendAlert(volunteerAlert);

        // Step 3: Generate alert for victim
        String victimMessage = String.format(
                "A volunteer has been assigned to help you.\n" +
                "Volunteer Name: %s\n" +
                "Location: %s\n" +
                "Phone: %s\n" +
                "Skill: %s\n" +
                "Availability: %s",
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
