package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminControlPanel extends JFrame {

    private final VolunteerDAO volunteerDAO = new VolunteerDAO();
    private final VictimDAO victimDAO = new VictimDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final AlertDAO alertDAO = new AlertDAO();
    private final BadgeDAO badgeDAO = new BadgeDAO();

    public AdminControlPanel() {
        setTitle("RescueLink Admin Control Panel");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 15, 15));

        JButton assignBadgeBtn = new JButton("Assign Badge to Volunteer");
        JButton viewVictimsBtn = new JButton("View All Victims");
        JButton viewVolunteersBtn = new JButton("View All Volunteers");
        JButton sendAlertBtn = new JButton("Send Alert to Volunteer");
        JButton viewAlertsBtn = new JButton("View Alerts for Volunteer");
        JButton viewDonationsBtn = new JButton("View Donations Dashboard"); // ✅ NEW BUTTON
        JButton logoutBtn = new JButton("Logout");

        add(assignBadgeBtn);
        add(viewVictimsBtn);
        add(viewVolunteersBtn);
        add(sendAlertBtn);
        add(viewAlertsBtn);
        add(viewDonationsBtn);  // ✅ Added here
        add(logoutBtn);

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

        // View all victims
        viewVictimsBtn.addActionListener(e -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
            dashboard.getTabbedPane().setSelectedIndex(0); // Victims tab
        });

        // View all volunteers
        viewVolunteersBtn.addActionListener(e -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
            dashboard.getTabbedPane().setSelectedIndex(1); // Volunteers tab
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

        // ✅ View donation dashboard
        viewDonationsBtn.addActionListener(e -> {
            DonationDashboard donationDashboard = new DonationDashboard();
            donationDashboard.setVisible(true);
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
