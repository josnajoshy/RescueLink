package rescuelink;

import java.util.ArrayList;
import java.util.List;

public class Volunteer extends User {
    private final String phoneNo;
    private final String skill;
    private boolean availability;
    private int rewardPoints;
    private int attendedRequests;
    private List<Badge> badges = new ArrayList<>();

    // Constructor for full data (DB load)
    public Volunteer(int id, String name, String location, String phoneNo, String skill,
                     boolean availability, int rewardPoints, int attendedRequests) {
        super(id, name, location);
        this.phoneNo = phoneNo;
        this.skill = skill;
        this.availability = availability;
        this.rewardPoints = rewardPoints;
        this.attendedRequests = attendedRequests;
    }

    // Constructor for new registration (default points & requests)
    public Volunteer(String name, String location, String phoneNo, String skill, boolean availability) {
        super(0, name, location);
        this.phoneNo = phoneNo;
        this.skill = skill;
        this.availability = availability;
        this.rewardPoints = 0;
        this.attendedRequests = 0;
    }

    // Optional constructor: partial data including id (default points)
    public Volunteer(int id, String name, String location, String phoneNo, String skill, boolean availability) {
        super(id, name, location);
        this.phoneNo = phoneNo;
        this.skill = skill;
        this.availability = availability;
        this.rewardPoints = 0;
        this.attendedRequests = 0;
    }

    // Getters
    public String getPhoneNo() { return phoneNo; }
    public String getSkill() { return skill; }
    public boolean isAvailability() { return availability; }
    public int getRewardPoints() { return rewardPoints; }
    public int getAttendedRequests() { return attendedRequests; }
    public List<Badge> getBadges() { return badges; }

    // Setters
    public void setAvailability(boolean availability) { this.availability = availability; }
    public void setRewardPoints(int rewardPoints) { this.rewardPoints = rewardPoints; }
    public void setAttendedRequests(int attendedRequests) { this.attendedRequests = attendedRequests; }
    public void setBadges(List<Badge> badges) { this.badges = badges; }

    // Display for debugging / admin view
    @Override
    public void displayInfo() {
        System.out.println("Volunteer ID: " + getId() +
                " | Name: " + getName() +
                " | Location: " + getLocation() +
                " | Phone: " + phoneNo +
                " | Skill: " + skill +
                " | Availability: " + (availability ? "Yes" : "No") +
                " | Reward Points: " + rewardPoints +
                " | Attended Requests: " + attendedRequests +
                " | Badges: " + (badges.isEmpty() ? "None" : badgeSummary()));
    }

    private String badgeSummary() {
        StringBuilder sb = new StringBuilder();
        for (Badge badge : badges) {
            sb.append(badge.getName()).append(", ");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "";
    }

    // For dropdowns or logs
    @Override
    public String toString() {
        return getName() + " (" + phoneNo + ")";
    }

    // DAO compatibility
    public int getVolunteerId() {
        return getId();
    }
}
