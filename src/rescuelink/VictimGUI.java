package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VictimGUI extends JFrame {

    private final JTextField nameField;
    private final JTextField locationField;
    private final JTextField phoneField;
    private final JComboBox<String> conditionCombo;
    private final JComboBox<String> incidentCombo;
    private final JComboBox<String> severityCombo;
    private final JTextField peopleField;
    private final JCheckBox immediateCheck;
    private final JButton reportButton;
    private final JButton alertsButton;

    private final VictimModule vm;
    private Victim lastReportedVictim;

    public VictimGUI(String phone, VictimModule vm1) throws SQLException {
        this.vm = vm1;

        setTitle("Report Victim Incident");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 6, 10, 10));

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(new JLabel("Condition:"));
        inputPanel.add(new JLabel("Incident Type:"));
        inputPanel.add(new JLabel("Severity:"));
        inputPanel.add(new JLabel("People Affected:"));
        inputPanel.add(new JLabel("Status:"));

        nameField = new JTextField();
        locationField = new JTextField();
        phoneField = new JTextField(phone); // pre-filled from login

        conditionCombo = new JComboBox<>(new String[]{"Stable", "Critical"});
        incidentCombo = new JComboBox<>(new String[]{
                "Flood", "Fire", "Accident", "Landslide", "Earthquake",
                "Cyclone", "Building Collapse", "Medical Emergency", "Other"
        });
        severityCombo = new JComboBox<>(new String[]{"Mild", "Moderate", "Severe"});
        peopleField = new JTextField();

        JTextField statusField = new JTextField("Pending");
        statusField.setEditable(false);

        inputPanel.add(nameField);
        inputPanel.add(locationField);
        inputPanel.add(phoneField);
        inputPanel.add(conditionCombo);
        inputPanel.add(incidentCombo);
        inputPanel.add(severityCombo);
        inputPanel.add(peopleField);
        inputPanel.add(statusField);

        add(inputPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        immediateCheck = new JCheckBox("Immediate Rescue");
        reportButton = new JButton("Report Incident");
        alertsButton = new JButton("View My Alerts");

        bottomPanel.add(immediateCheck);
        bottomPanel.add(reportButton);
        bottomPanel.add(alertsButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions
        reportButton.addActionListener(e -> reportIncident());
        alertsButton.addActionListener(e -> openAlerts());
    }

    private void reportIncident() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String phone = phoneField.getText().trim();
        String condition = (String) conditionCombo.getSelectedItem();
        String incident = (String) incidentCombo.getSelectedItem();
        String severity = (String) severityCombo.getSelectedItem();
        int peopleAffected;

        if (name.isEmpty() || location.isEmpty() || phone.isEmpty() || peopleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            peopleAffected = Integer.parseInt(peopleField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number for People Affected.");
            return;
        }

        boolean immediate = immediateCheck.isSelected();
        String status = "Pending";

        Victim v = new Victim(
                0,
                name,
                location,
                condition,
                incident,
                severity,
                peopleAffected,
                immediate,
                status,
                phone
        );

        try {
            vm.addVictim(v);
            lastReportedVictim = v;
            JOptionPane.showMessageDialog(this, "Incident reported successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inserting victim: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.setText("");
        locationField.setText("");
        // phoneField stays pre-filled
        conditionCombo.setSelectedIndex(0);
        incidentCombo.setSelectedIndex(0);
        severityCombo.setSelectedIndex(0);
        peopleField.setText("");
        immediateCheck.setSelected(false);
    }

    private void openAlerts() {
        if (lastReportedVictim == null) {
            JOptionPane.showMessageDialog(this, "No victim reported yet!");
            return;
        }
        VictimAlerts alertsPanel = new VictimAlerts(lastReportedVictim);
        alertsPanel.setVisible(true);
    }
}