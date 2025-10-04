package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VolunteerDashboard extends JFrame {
    private Volunteer loggedInVolunteer;
    private final VolunteerDAO volunteerDAO = new VolunteerDAO();
    private final AlertDAO alertDAO = new AlertDAO();

    private final JTextArea alertArea = new JTextArea();
    private final JPanel badgePanel = new JPanel(new GridLayout(0, 1, 5, 5));
    private final JLabel rescueStatusLabel = new JLabel();

    public VolunteerDashboard() {
        setTitle("Volunteer Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JButton loginBtn = new JButton("Login");

        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        loginPanel.add(new JLabel("Name:"));
        loginPanel.add(nameField);
        loginPanel.add(new JLabel("Phone No:"));
        loginPanel.add(phoneField);
        loginPanel.add(new JLabel(""));
        loginPanel.add(loginBtn);

        add(loginPanel);

        loginBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            loggedInVolunteer = volunteerDAO.login(name, phone);

            if (loggedInVolunteer != null) {
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Check name and phone.");
            }
        });
    }

    private void showDashboard() {
        getContentPane().removeAll();
        setTitle("Volunteer Dashboard - " + loggedInVolunteer.getName());
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));

        // Rescue Status
        rescueStatusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        rescueStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadRescueStatus();

        // Alerts
        alertArea.setEditable(false);
        alertArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        alertArea.setBorder(BorderFactory.createTitledBorder("Alerts"));
        loadAlerts();

        // Badges
        badgePanel.setBorder(BorderFactory.createTitledBorder("Badges Earned"));
        loadBadges();

        // Layout Panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(rescueStatusLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(alertArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(badgePanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void loadRescueStatus() {
        // This method should return a count of victims with status = 'Rescued'
        int rescuedCount = volunteerDAO.getRescuedVictimCount(loggedInVolunteer.getVolunteerId());
        rescueStatusLabel.setText("Rescue Operations Completed: " + rescuedCount);
    }

    private void loadAlerts() {
        alertArea.setText("");
        List<Alert> alerts = alertDAO.getAlertsForUser(loggedInVolunteer);
        if (alerts.isEmpty()) {
            alertArea.setText("No alerts available.");
        } else {
            for (Alert a : alerts) {
                alertArea.append("[" + a.getSentAt() + "] " + a.getMessage() + "\n\n");
            }
        }
    }

    private void loadBadges() {
        badgePanel.removeAll();
        List<Badge> badges = loggedInVolunteer.getBadges();
        if (badges.isEmpty()) {
            badgePanel.add(new JLabel("No badges earned yet."));
        } else {
            for (Badge b : badges) {
                badgePanel.add(new JLabel("🏅 " + b.getName() + ": " + b.getDescription()));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VolunteerDashboard().setVisible(true));
    }
}