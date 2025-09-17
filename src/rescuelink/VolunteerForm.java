package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VolunteerForm extends JFrame {
    private JTextField nameField, locationField, phoneField;
    private JComboBox<String> skillDropdown; // ✅ Changed from JTextField to JComboBox
    private JCheckBox availabilityBox;
    private final JButton registerButton;
    private final JButton viewAlertsButton;
    private final JButton reportCompletionButton;
    private final JButton checkStatusButton;
    private final JLabel statusLabel;

    public VolunteerForm() {
        setTitle("Volunteer Registration");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

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
        skillDropdown = new JComboBox<>(new String[]{
                "First Aid",
                "Rescue Ops",
                "Medical Support",
                "Food Distribution",
                "Shelter Management"
        });
        panel.add(skillDropdown);

        panel.add(new JLabel("Available:"));
        availabilityBox = new JCheckBox();
        panel.add(availabilityBox);

        registerButton = new JButton("Register");
        viewAlertsButton = new JButton("View Alerts");
        reportCompletionButton = new JButton("Report Rescue Completion");
        checkStatusButton = new JButton("Check Assignment Status");
        statusLabel = new JLabel("Status: Active");

        panel.add(registerButton);
        panel.add(viewAlertsButton);
        panel.add(reportCompletionButton);
        panel.add(checkStatusButton);
        panel.add(statusLabel);

        add(panel);

        // ✅ Register volunteer
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            String phone = phoneField.getText().trim();
            String skill = (String) skillDropdown.getSelectedItem(); // ✅ from dropdown
            boolean available = availabilityBox.isSelected();

            if (name.isEmpty() || location.isEmpty() || phone.isEmpty() || skill == null) {
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

        // ✅ View alerts and badges
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

                StringBuilder sb = new StringBuilder();
                if (alerts.isEmpty()) {
                    sb.append("No alerts found.\n");
                } else {
                    sb.append("Alerts:\n\n");
                    for (Alert alert : alerts) {
                        sb.append(alert.getMessage()).append("\n---\n");
                    }
                }

                sb.append("\nReward Points: ").append(matched.getRewardPoints());
                sb.append("\nAttended Requests: ").append(matched.getAttendedRequests());
                sb.append("\nBadges:\n");
                for (Badge badge : matched.getBadges()) {
                    sb.append("- ").append(badge.getName())
                            .append(": ").append(badge.getDescription()).append("\n");
                }

                JOptionPane.showMessageDialog(null, sb.toString());
            } else {
                JOptionPane.showMessageDialog(null, "Volunteer not found. Please register first.");
            }
        });

        // ✅ Report rescue completion
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

                AlertDAO alertDAO = new AlertDAO();
                Alert alert = new Alert(matched, adminMessage); 
                boolean sent = alertDAO.sendAlert(alert);

                if (sent) {
                    statusLabel.setText("Status: Rescue reported (pending admin verification)");
                    JOptionPane.showMessageDialog(null, "Rescue report submitted. Admin will verify and assign rewards.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to send report.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Volunteer not found. Please register first.");
            }
        });

        // ✅ Check assignment status
        checkStatusButton.addActionListener(e -> {
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Enter phone number to check status.");
                return;
            }

            VolunteerDAO dao = new VolunteerDAO();
            List<Volunteer> volunteers = dao.getVolunteerList();
            Volunteer matched = volunteers.stream()
                    .filter(v -> v.getPhoneNo().equals(phone))
                    .findFirst()
                    .orElse(null);

            if (matched != null) {
                String status = dao.getAssignmentStatus(matched.getId());
                JOptionPane.showMessageDialog(null, "Current Assignment Status: " + status);
            } else {
                JOptionPane.showMessageDialog(null, "Volunteer not found. Please register first.");
            }
        });
    }

    private void clearForm() {
        nameField.setText("");
        locationField.setText("");
        phoneField.setText("");
        skillDropdown.setSelectedIndex(0); // reset to default
        availabilityBox.setSelected(false);
        statusLabel.setText("Status: Active");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VolunteerForm().setVisible(true));
    }
}
