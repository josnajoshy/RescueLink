package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VolunteerForm extends JFrame {
    private JTextField nameField, locationField, phoneField, skillField;
    private JCheckBox availabilityBox;
    private final JButton registerButton;
    private final JButton viewAlertsButton;
    private final JButton reportCompletionButton;
    private final JLabel statusLabel;

    // Virtual gamified fields
    private int virtualPoints = 0;
    private final List<String> virtualBadges = new ArrayList<>();
    private boolean rescueReported = false;

    public VolunteerForm() {
        setTitle("Volunteer Registration");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Location:"));
        locationField = new JTextField();
        panel.add(locationField);

        panel.add(new JLabel("Phone No:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Skill:"));
        skillField = new JTextField();
        panel.add(skillField);

        panel.add(new JLabel("Available:"));
        availabilityBox = new JCheckBox();
        panel.add(availabilityBox);

        registerButton = new JButton("Register");
        viewAlertsButton = new JButton("View Alerts");
        reportCompletionButton = new JButton("Report Rescue Completion");
        statusLabel = new JLabel("Status: Active");

        panel.add(registerButton);
        panel.add(viewAlertsButton);
        panel.add(reportCompletionButton);
        panel.add(statusLabel);

        add(panel);

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            String phone = phoneField.getText().trim();
            String skill = skillField.getText().trim();
            boolean available = availabilityBox.isSelected();

            if (name.isEmpty() || location.isEmpty() || phone.isEmpty() || skill.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields.");
                return;
            }

            Volunteer v = new Volunteer(name, location, phone, skill, available);
            VolunteerDAO dao = new VolunteerDAO();
            boolean success = dao.addVolunteer(v);

            if (success) {
                JOptionPane.showMessageDialog(null, "Volunteer registered successfully.");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(null, "Registration failed.");
            }
        });

        viewAlertsButton.addActionListener(e -> {
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Enter phone number to fetch alerts.");
                return;
            }

            VolunteerDAO dao = new VolunteerDAO();
            List<Volunteer> volunteers = dao.getVolunteerList();

            Volunteer matched = volunteers.stream()
                .filter(v -> v.getPhoneNo().equals(phone))
                .findFirst()
                .orElse(null);

            if (matched != null) {
                AlertDAO alertDAO = new AlertDAO();
                List<Alert> alerts = alertDAO.getAlertsForUser(matched);

                if (alerts.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No alerts found.");
                } else {
                    StringBuilder sb = new StringBuilder("Alerts:\n\n");
                    for (Alert alert : alerts) {
                        sb.append(alert.getMessage()).append("\n---\n");
                    }

                    String badgeList = String.join(", ", virtualBadges);
                    sb.append("\nPoints: ").append(virtualPoints);
                    sb.append("\nBadges: ").append(badgeList.isEmpty() ? "None" : badgeList);

                    JOptionPane.showMessageDialog(null, sb.toString());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Volunteer not found. Please register first.");
            }
        });

        reportCompletionButton.addActionListener(e -> {
            String phone = phoneField.getText().trim();
            String assignmentId = JOptionPane.showInputDialog("Enter Assignment ID:");
            String outcome = JOptionPane.showInputDialog("Briefly describe the rescue outcome:");

            VolunteerDAO dao = new VolunteerDAO();
            List<Volunteer> volunteers = dao.getVolunteerList();
            Volunteer matched = volunteers.stream()
                .filter(v -> v.getPhoneNo().equals(phone))
                .findFirst()
                .orElse(null);

            if (matched != null) {
                String adminMessage = """
                    Rescue Completion Report Received:
                    Volunteer: %s
                    Assignment ID: %s
                    Outcome: %s
                    """.formatted(matched.getName(), assignmentId, outcome);

                Victim adminRecipient = new Victim(0, "Admin", "HQ", "Admin Inbox", "Other", "Mild", 0, false, "Pending");
                AlertDAO alertDAO = new AlertDAO();
                Alert alert = new Alert(adminRecipient, adminMessage);
                boolean sent = alertDAO.sendAlert(alert);

                if (sent) {
                    rescueReported = true;
                    statusLabel.setText("Status: Rescue reported (pending admin verification)");

                    virtualPoints += 10;
                    if (!virtualBadges.contains("Rescue Hero")) {
                        virtualBadges.add("Rescue Hero");

                        Alert badgeAlert = new Alert(matched, "You've earned a new badge: Rescue Hero.");
                        alertDAO.sendAlert(badgeAlert);
                    }

                    JOptionPane.showMessageDialog(null, "Rescue report submitted. Admin will verify and update your status.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to send report.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Volunteer not found. Please register first.");
            }
        });
    }

    private void clearForm() {
        nameField.setText("");
        locationField.setText("");
        phoneField.setText("");
        skillField.setText("");
        availabilityBox.setSelected(false);
        statusLabel.setText("Status: Active");
        rescueReported = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VolunteerForm().setVisible(true));
    }
}
