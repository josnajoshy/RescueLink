package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminControlPanel extends JFrame {

    private final VolunteerDAO volunteerDAO = new VolunteerDAO();
    private final VictimDAO victimDAO = new VictimDAO();
    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final AlertDAO alertDAO = new AlertDAO();
    private final BadgeDAO badgeDAO = new BadgeDAO();

    public AdminControlPanel() {
        setTitle("RescueLink Admin Control Panel");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 15, 15));

        JButton assignVolunteerBtn = new JButton("Assign Volunteer to Victim");
        JButton assignBadgeBtn = new JButton("Assign Badge to Volunteer");
        JButton updateAssignmentBtn = new JButton("View Assigned Victims");
        JButton updateVictimStatusBtn = new JButton("Update Victim Status");
        JButton viewVictimsBtn = new JButton("View All Victims");
        JButton viewVolunteersBtn = new JButton("View All Volunteers");
        JButton sendAlertBtn = new JButton("Send Alert to Volunteer");
        JButton viewAlertsBtn = new JButton("View Alerts for Volunteer");
        JButton logoutBtn = new JButton("Logout");

        add(assignVolunteerBtn);
        add(assignBadgeBtn);
        add(updateAssignmentBtn);
        add(updateVictimStatusBtn);
        add(viewVictimsBtn);
        add(viewVolunteersBtn);
        add(sendAlertBtn);
        add(viewAlertsBtn);
        add(logoutBtn);

        // Assign volunteer to victim
        assignVolunteerBtn.addActionListener(e -> {
            List<Volunteer> volunteers = volunteerDAO.getVolunteerList();
            List<Victim> victims = victimDAO.getAllVictims();

            JComboBox<Volunteer> volunteerBox = new JComboBox<>(volunteers.toArray(new Volunteer[0]));
            JComboBox<Victim> victimBox = new JComboBox<>(victims.toArray(new Victim[0]));

            int result = JOptionPane.showConfirmDialog(this, new Object[]{
                "Select Volunteer:", volunteerBox,
                "Select Victim:", victimBox
            }, "Assign Volunteer", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                Volunteer v = (Volunteer) volunteerBox.getSelectedItem();
                Victim vt = (Victim) victimBox.getSelectedItem();
                boolean success = assignmentDAO.assignVolunteerToVictim(v, vt);
                JOptionPane.showMessageDialog(this, success ? "Volunteer assigned!" : "Assignment failed.");
            }
        });

        // Assign badge to volunteer
        assignBadgeBtn.addActionListener(e -> {
            List<Volunteer> volunteers = volunteerDAO.getVolunteerList();
            List<Badge> badges = badgeDAO.getAllBadges();

            JComboBox<Volunteer> volunteerBox = new JComboBox<>(volunteers.toArray(new Volunteer[0]));
            JComboBox<Badge> badgeBox = new JComboBox<>(badges.toArray(new Badge[0]));
            JTextField pointsField = new JTextField();

            int result = JOptionPane.showConfirmDialog(this, new Object[]{
                "Select Volunteer:", volunteerBox,
                "Select Badge:", badgeBox,
                "Points to Add:", pointsField
            }, "Assign Badge", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                Volunteer v = (Volunteer) volunteerBox.getSelectedItem();
                Badge b = (Badge) badgeBox.getSelectedItem();
                int points = Integer.parseInt(pointsField.getText().trim());

                boolean success = adminDAO.assignBadgeToVolunteer(v.getVolunteerId(), b.getBadgeId(), points);
                JOptionPane.showMessageDialog(this, success ? "Badge assigned!" : "Failed to assign badge.");
            }
        });

        // View assigned victims
        updateAssignmentBtn.addActionListener(e -> {
            List<Volunteer> volunteers = volunteerDAO.getVolunteerList();
            JComboBox<Volunteer> volunteerBox = new JComboBox<>(volunteers.toArray(new Volunteer[0]));

            int result = JOptionPane.showConfirmDialog(this, new Object[]{
                "Select Volunteer:", volunteerBox
            }, "View Assignments", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                Volunteer v = (Volunteer) volunteerBox.getSelectedItem();
                List<Victim> assignedVictims = assignmentDAO.getAssignedVictims(v.getVolunteerId());

                StringBuilder sb = new StringBuilder("Assigned Victims:\n");
                for (Victim vt : assignedVictims) {
                    sb.append("ID: ").append(vt.getId())
                      .append(", Name: ").append(vt.getName())
                      .append(", Status: ").append(vt.getStatus()).append("\n");
                }

                JOptionPane.showMessageDialog(this, sb.toString());
            }
        });

        // Update victim status
        updateVictimStatusBtn.addActionListener(e -> {
            List<Victim> victims = victimDAO.getAllVictims();
            JComboBox<Victim> victimBox = new JComboBox<>(victims.toArray(new Victim[0]));
            String[] statuses = {"Pending", "In Progress", "Rescued"};
            JComboBox<String> statusBox = new JComboBox<>(statuses);

            int result = JOptionPane.showConfirmDialog(this, new Object[]{
                "Select Victim:", victimBox,
                "New Status:", statusBox
            }, "Update Victim Status", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                Victim v = (Victim) victimBox.getSelectedItem();
                String newStatus = (String) statusBox.getSelectedItem();
                boolean success = victimDAO.updateVictimStatus(v.getId(), newStatus);
                JOptionPane.showMessageDialog(this, success ? "Status updated!" : "Update failed.");
            }
        });

        // View all victims
        viewVictimsBtn.addActionListener(e -> {
            List<Victim> victims = victimDAO.getAllVictims();
            StringBuilder sb = new StringBuilder("All Victims:\n");
            for (Victim v : victims) {
                sb.append("ID: ").append(v.getId())
                  .append(", Name: ").append(v.getName())
                  .append(", Location: ").append(v.getLocation())
                  .append(", Status: ").append(v.getStatus()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        // View all volunteers
        viewVolunteersBtn.addActionListener(e -> {
            List<Volunteer> volunteers = volunteerDAO.getVolunteerList();
            StringBuilder sb = new StringBuilder("All Volunteers:\n");
            for (Volunteer v : volunteers) {
                sb.append("ID: ").append(v.getVolunteerId())
                  .append(", Name: ").append(v.getName())
                  .append(", Location: ").append(v.getLocation())
                  .append(", Skill: ").append(v.getSkill()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        // Send alert to volunteer
        sendAlertBtn.addActionListener(e -> {
            List<Volunteer> volunteers = volunteerDAO.getVolunteerList();
            JComboBox<Volunteer> volunteerBox = new JComboBox<>(volunteers.toArray(new Volunteer[0]));
            JTextField messageField = new JTextField();

            int result = JOptionPane.showConfirmDialog(this, new Object[]{
                "Select Volunteer:", volunteerBox,
                "Message:", messageField
            }, "Send Alert", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                Volunteer v = (Volunteer) volunteerBox.getSelectedItem();
                Alert alert = new Alert(0, v, messageField.getText(), java.time.LocalDateTime.now(), false);
                boolean success = alertDAO.sendAlert(alert);
                JOptionPane.showMessageDialog(this, success ? "Alert sent!" : "Failed to send alert.");
            }
        });

        // View alerts for volunteer
        viewAlertsBtn.addActionListener(e -> {
            List<Volunteer> volunteers = volunteerDAO.getVolunteerList();
            JComboBox<Volunteer> volunteerBox = new JComboBox<>(volunteers.toArray(new Volunteer[0]));

            int result = JOptionPane.showConfirmDialog(this, new Object[]{
                "Select Volunteer:", volunteerBox
            }, "View Alerts", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                Volunteer v = (Volunteer) volunteerBox.getSelectedItem();
                List<Alert> alerts = alertDAO.getAlertsForUser(v);

                StringBuilder sb = new StringBuilder("Alerts:\n");
                for (Alert a : alerts) {
                    sb.append("[").append(a.getSentAt()).append("] ").append(a.getMessage()).append("\n");
                }

                JOptionPane.showMessageDialog(this, sb.toString());
            }
        });

        // Logout
        logoutBtn.addActionListener(e -> {
            dispose();
            new AdminLoginScreen().setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminControlPanel().setVisible(true));
    }
}