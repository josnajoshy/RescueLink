package rescuelink;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminDashboard extends JFrame {
    private final JTabbedPane tabbedPane;
    private final JTable victimTable;
    private final JTable volunteerTable;
    private final DefaultTableModel victimModel;
    private final DefaultTableModel volunteerModel;

    public AdminDashboard() {
        setTitle("Admin Dashboard - ResQLink");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // Victim Tab
        victimModel = new DefaultTableModel(new Object[]{
                "ID", "Name", "Location", "Condition", "Incident Type",
                "Severity", "People Affected", "Immediate Rescue", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table non-editable
            }
        };

        victimTable = new JTable(victimModel);
        JScrollPane victimScroll = new JScrollPane(victimTable);

        JButton updateVictimBtn = new JButton("Update Victim Status");
        updateVictimBtn.addActionListener(e -> updateVictimStatus());

        JPanel victimPanel = new JPanel(new BorderLayout());
        victimPanel.add(victimScroll, BorderLayout.CENTER);
        victimPanel.add(updateVictimBtn, BorderLayout.SOUTH);

        tabbedPane.addTab("Victims", victimPanel);

        // Volunteer Tab
        volunteerModel = new DefaultTableModel(new Object[]{
                "ID", "Name", "Location", "Phone", "Skill", "Availability"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // non-editable
            }
        };

        volunteerTable = new JTable(volunteerModel);
        JScrollPane volunteerScroll = new JScrollPane(volunteerTable);

        JButton updateVolunteerBtn = new JButton("Update Volunteer Availability");
        updateVolunteerBtn.addActionListener(e -> updateVolunteerAvailability());

        JPanel volunteerPanel = new JPanel(new BorderLayout());
        volunteerPanel.add(volunteerScroll, BorderLayout.CENTER);
        volunteerPanel.add(updateVolunteerBtn, BorderLayout.SOUTH);

        tabbedPane.addTab("Volunteers", volunteerPanel);

        add(tabbedPane);

        // Load data initially
        loadVictims();
        loadVolunteers();
    }

    private void loadVictims() {
    victimModel.setRowCount(0);
    try {
        VictimModule vm = new VictimModule();
        List<Victim> victims = vm.getAllVictims();

        for (Victim v : victims) {
            victimModel.addRow(new Object[]{
                    v.getId(),
                    v.getName(),
                    v.getLocation(),
                    v.getCondition(),
                    v.getIncidentType(),
                    v.getSeverity(),
                    v.getPeopleAffected(),
                    v.isImmediateRescue() ? "Yes" : "No",
                    v.getStatus()
            });
        }
        vm.closeConnection();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading victims: " + e.getMessage());
    }
}


    private void loadVolunteers() {
        volunteerModel.setRowCount(0);
        VolunteerDAO dao = new VolunteerDAO();
        List<Volunteer> volunteers = dao.getVolunteerList();

        for (Volunteer v : volunteers) {
            volunteerModel.addRow(new Object[]{
                    v.getVolunteerId(),
                    v.getName(),
                    v.getLocation(),
                    v.getPhoneNo(),
                    v.getSkill(),
                    v.isAvailability() ? "Yes" : "No"
            });
        }
    }

    private void updateVictimStatus() {
        int row = victimTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a victim to update.");
            return;
        }

        int id = Integer.parseInt(victimModel.getValueAt(row, 0).toString());
        String[] statuses = {"Pending", "In Progress", "Rescued"};
        String newStatus = (String) JOptionPane.showInputDialog(
                this, "Select new status:", "Update Victim Status",
                JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);

        if (newStatus != null) {
    try {
        VictimModule vm = new VictimModule();
        boolean success = vm.updateVictimStatus(id, newStatus);
        if (success) {
            JOptionPane.showMessageDialog(this, "Victim status updated!");
            loadVictims();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update status.");
        }
        vm.closeConnection();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
    }
}
    }

    private void updateVolunteerAvailability() {
        int row = volunteerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a volunteer to update.");
            return;
        }

        int id = Integer.parseInt(volunteerModel.getValueAt(row, 0).toString());
        String[] options = {"Yes", "No"};
        String choice = (String) JOptionPane.showInputDialog(
                this, "Set availability:", "Update Volunteer Availability",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            boolean available = choice.equals("Yes");
            VolunteerDAO dao = new VolunteerDAO();
            boolean success = dao.updateAvailability(id, available);
            if (success) {
                JOptionPane.showMessageDialog(this, "Volunteer availability updated!");
                loadVolunteers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update availability.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
