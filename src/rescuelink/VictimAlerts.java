package rescuelink;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
        setSize(800, 400); // wider window for better visibility
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Table model
        alertModel = new DefaultTableModel(new String[]{"Message", "Created At", "Read"}, 0);
        alertTable = new JTable(alertModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Set row height and column width
        alertTable.setRowHeight(60); // taller rows for wrapped text
        alertTable.getColumnModel().getColumn(0).setPreferredWidth(500); // wider message column

        // Custom renderer for wrapped text in message column
        alertTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JTextArea textArea = new JTextArea(value != null ? value.toString() : "");
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setOpaque(true);
                textArea.setFont(table.getFont());
                textArea.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                textArea.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                return textArea;
            }
        });

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
            @Override
            public void run() {
                SwingUtilities.invokeLater(VictimAlerts.this::loadAlerts);
            }
        }, 10000, 10000);
    }

    private void loadAlerts() {
        alertModel.setRowCount(0);
        List<Alert> alerts = alertDAO.getAlertsForUser(victim);
        if (alerts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No alerts yet!");
        }

        for (Alert a : alerts) {
            alertModel.addRow(new Object[]{
                    a.getMessage(),
                    a.getSentAt(),
                    a.isRead() ? "Yes" : "No"
            });
        }
    }
}