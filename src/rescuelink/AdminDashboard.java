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
        setSize(1000, 550);
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
                return false;
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
                return false;
            }
        };

        volunteerTable = new JTable(volunteerModel);
        JScrollPane volunteerScroll = new JScrollPane(volunteerTable);

        JButton updateVolunteerBtn = new JButton("Update Volunteer Availability");
        updateVolunteerBtn.addActionListener(e -> updateVolunteerAvailability());

        JButton assignBtn = new JButton("Assign Volunteer to Victim");
        assignBtn.addActionListener(e -> assignSelectedVolunteerToVictim());

        JPanel volunteerPanel = new JPanel(new BorderLayout());
        JPanel volunteerButtonPanel = new JPanel(new FlowLayout());
        volunteerButtonPanel.add(updateVolunteerBtn);
        volunteerButtonPanel.add(assignBtn);

        volunteerPanel.add(volunteerScroll, BorderLayout.CENTER);
        volunteerPanel.add(volunteerButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Volunteers", volunteerPanel);

        add(tabbedPane);
        
        // Inside the AdminDashboard constructor, after setting up tabbedPane and before loadVictims()/loadVolunteers()
        JButton backBtn = new JButton("Back to Control Panel");
        backBtn.addActionListener(e -> {
            new AdminControlPanel().setVisible(true);
            dispose(); // close the dashboard
        });

        // Add the back button at the bottom of the JFrame
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);


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

    private void assignSelectedVolunteerToVictim() {
        int volunteerRow = volunteerTable.getSelectedRow();
        int victimRow = victimTable.getSelectedRow();

        if (volunteerRow == -1 || victimRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select both a volunteer and a victim.");
            return;
        }

        Volunteer volunteer = new Volunteer(
                Integer.parseInt(volunteerModel.getValueAt(volunteerRow, 0).toString()),
                volunteerModel.getValueAt(volunteerRow, 1).toString(),
                volunteerModel.getValueAt(volunteerRow, 2).toString(),
                volunteerModel.getValueAt(volunteerRow, 3).toString(),
                volunteerModel.getValueAt(volunteerRow, 4).toString(),
                volunteerModel.getValueAt(volunteerRow, 5).toString().equalsIgnoreCase("Yes"),
                0, 0
        );

        Victim victim = new Victim(
                Integer.parseInt(victimModel.getValueAt(victimRow, 0).toString()),
                victimModel.getValueAt(victimRow, 1).toString(),
                victimModel.getValueAt(victimRow, 2).toString(),
                victimModel.getValueAt(victimRow, 3).toString(),
                victimModel.getValueAt(victimRow, 4).toString(),
                victimModel.getValueAt(victimRow, 5).toString(),
                Integer.parseInt(victimModel.getValueAt(victimRow, 6).toString()),
                victimModel.getValueAt(victimRow, 7).toString().equalsIgnoreCase("Yes"),
                victimModel.getValueAt(victimRow, 8).toString()
        );

        AssignmentService service = new AssignmentService();
        boolean success = service.assignAndNotify(volunteer, victim);

        if (success) {
            JOptionPane.showMessageDialog(this, "Volunteer assigned and notified!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to assign volunteer.");
        }
    }

    // ✅ Missing method inserted here
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
