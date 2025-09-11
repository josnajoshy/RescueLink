package rescuelink;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VictimAlerts extends JFrame {
    private final Victim victim;
    private final AlertDAO alertDAO;
    private final DefaultTableModel alertModel;
    private final JTable alertTable;

    public VictimAlerts(Victim victim) {
        this.victim = victim;
        this.alertDAO = new AlertDAO();

        setTitle("My Alerts - " + victim.getName());
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Table
        alertModel = new DefaultTableModel(new String[]{"Message", "Created At", "Read"}, 0);
        alertTable = new JTable(alertModel);
        JScrollPane scrollPane = new JScrollPane(alertTable);

        // Refresh Button
        JButton refreshBtn = new JButton("Refresh Alerts");
        refreshBtn.addActionListener(e -> loadAlerts());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(refreshBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load initial alerts
        loadAlerts();

        // Auto-refresh every 10 seconds
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                SwingUtilities.invokeLater(VictimAlerts.this::loadAlerts);
            }
        }, 10000, 10000);
    }

    private void loadAlerts() {
        alertModel.setRowCount(0);
        List<Alert> alerts = alertDAO.getAlertsForUser(victim);

        for (Alert a : alerts) {
            alertModel.addRow(new Object[]{
                    a.getMessage(),
                    a.getCreatedAt(),
                    a.isRead() ? "Yes" : "No"
            });
        }
    }
}
