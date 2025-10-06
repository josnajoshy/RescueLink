package rescuelink;

public class RescueTeam {
    private int teamId;
    private String teamName;
    private String specialization;
    private String phoneNo;
    private String password;

    public RescueTeam(int teamId, String teamName, String specialization, String phoneNo, String password) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.specialization = specialization;
        this.phoneNo = phoneNo;
        this.password = password;
    }

    // Getters
    public int getTeamId() { return teamId; }
    public String getTeamName() { return teamName; }
    public String getSpecialization() { return specialization; }
    public String getPhoneNo() { return phoneNo; }
    public String getPassword() { return password; }
    public Volunteer toVolunteer() {
    return new Volunteer(teamId, teamName, "Unknown", phoneNo, specialization, true);
}
}