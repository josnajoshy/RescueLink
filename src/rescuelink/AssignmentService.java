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
        String volunteerMessage = String.format("""
                                                You have been assigned to a victim.
                                                Victim Name: %s
                                                Location: %s
                                                Condition: %s
                                                Number of people involved: %d""",
                victim.getName(),
                victim.getLocation(),
                victim.getCondition(),
                victim.getPeopleAffected()   // ✅ corrected
        );

        Alert volunteerAlert = new Alert(volunteer, volunteerMessage);
        alertDAO.sendAlert(volunteerAlert);
        System.out.println("Alert sent to volunteer ID: " + volunteer.getVolunteerId());

        // Step 3: Generate alert for victim
        String victimMessage;
        victimMessage = String.format("""
                                      A volunteer has been assigned to help you.
                                      Volunteer Name: %s
                                      Location: %s
                                      Phone: %s
                                      Skill: %s
                                      Availability: %s""",
                volunteer.getName(),
                volunteer.getLocation(),
                volunteer.getPhoneNo(),
                volunteer.getSkill(),
                volunteer.isAvailability() ? "Yes" : "No"
        );

        Alert victimAlert = new Alert(victim, victimMessage);
        alertDAO.sendAlert(victimAlert);
        System.out.println("Alert sent to victim ID: " + volunteer.getVolunteerId());

        return true;
    }
}
