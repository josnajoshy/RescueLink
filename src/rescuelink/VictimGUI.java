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
    

    private final VictimModule vm;
    private Victim lastReportedVictim;

    public VictimGUI(String phone, VictimModule vm1) throws SQLException {
        this.vm = vm1;

        setTitle("Report Victim Incident - ResQLink");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // =========================
        // HEADER
        // =========================
        JLabel title = new JLabel("Report Victim Incident", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // =========================
        // FORM PANEL (GridLayout)
        // =========================
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Labels and Fields
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        formPanel.add(locationField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(phone); // pre-filled
        phoneField.setEditable(false);
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Condition:"));
        conditionCombo = new JComboBox<>(new String[]{"Stable", "Critical"});
        formPanel.add(conditionCombo);

        formPanel.add(new JLabel("Incident Type:"));
        incidentCombo = new JComboBox<>(new String[]{
                "Flood", "Fire", "Accident", "Landslide", "Earthquake",
                "Cyclone", "Building Collapse", "Medical Emergency", "Other"
        });
        formPanel.add(incidentCombo);

        formPanel.add(new JLabel("Severity:"));
        severityCombo = new JComboBox<>(new String[]{"Mild", "Moderate", "Severe"});
        formPanel.add(severityCombo);

        formPanel.add(new JLabel("People Affected:"));
        peopleField = new JTextField();
        formPanel.add(peopleField);

        formPanel.add(new JLabel("Immediate Rescue Needed:"));
        immediateCheck = new JCheckBox("Yes");
        formPanel.add(immediateCheck);

        formPanel.add(new JLabel("Status:"));
        JTextField statusField = new JTextField("Pending");
        statusField.setEditable(false);
        formPanel.add(statusField);

        add(formPanel, BorderLayout.CENTER);

        // =========================
        // BUTTON PANEL
        // =========================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        reportButton = new JButton("Report Incident");
        
        buttonPanel.add(reportButton);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // =========================
        // BUTTON ACTIONS
        // =========================
        reportButton.addActionListener(e -> reportIncident());
        
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

    public static void main(String[] args) throws SQLException {
        VictimModule vm = new VictimModule();
        SwingUtilities.invokeLater(() -> {
            try {
                new VictimGUI("9876543210", vm).setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
