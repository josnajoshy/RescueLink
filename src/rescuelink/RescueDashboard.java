package rescuelink;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RescueDashboard extends JFrame {
    private final RescueTeam team;
    private final RescueModule rm;
    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private JTable victimTable;

    public RescueDashboard(RescueTeam team, RescueModule rm) {
        this.team = team;
        this.rm = rm;

        setTitle("Rescue Dashboard - " + team.getTeamName());
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Welcome, " + team.getTeamName() + " 🚒", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        victimTable = new JTable();
        refreshVictimData();

        JScrollPane scrollPane = new JScrollPane(victimTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Reported Victims"));
        add(scrollPane, BorderLayout.CENTER);

        JButton rescueBtn = new JButton("Rescue Selected Victim");
        rescueBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        rescueBtn.setBackground(Color.ORANGE);
        rescueBtn.setForeground(Color.BLACK);
        add(rescueBtn, BorderLayout.SOUTH);

        rescueBtn.addActionListener(e -> {
            int selectedRow = victimTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a victim to rescue.");
                return;
            }

            int victimId = (int) victimTable.getValueAt(selectedRow, 0); // Column 0 = victim ID
            Victim selectedVictim = rm.getVictimById(victimId);

            if (selectedVictim != null) {
                Volunteer volunteer = team.toVolunteer();

                // Ensure volunteer exists in DB
                assignmentDAO.ensureVolunteerExists(volunteer);

                // Assign to victim
                boolean success = assignmentDAO.assignVolunteerToVictim(volunteer, selectedVictim);

                if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Victim assigned successfully!");
                    refreshVictimData();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Assignment failed. Check console for details.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Victim not found.");
            }
        });
    }

    private void refreshVictimData() {
        List<Victim> victims = rm.getAllVictims();

        DefaultTableModel model = new DefaultTableModel(
            new Object[]{
                "ID", "Name", "Location", "Condition", "Incident Type",
                "Severity", "People Affected", "Immediate Rescue", "Status", "Phone"
            }, 0
        );

        for (Victim v : victims) {
            model.addRow(new Object[]{
                v.getId(), v.getName(), v.getLocation(),
                v.getCondition(), v.getIncidentType(), v.getSeverity(),
                v.getPeopleAffected(), v.isImmediateRescue() ? "Yes" : "No",
                v.getStatus(), v.getPhone()
            });
        }

        victimTable.setModel(model);
        victimTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        victimTable.setRowHeight(25);
    }
}