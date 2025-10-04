package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VictimDashboard extends JFrame {
    private final Victim victim;

    public VictimDashboard(Victim victim) {
        this.victim = victim;

        setTitle("Victim Dashboard - " + victim.getName());
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Title / Welcome panel
        JLabel welcomeLabel = new JLabel("Welcome, " + victim.getName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.NORTH);

        // Center buttons panel
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton viewAlertsBtn = new JButton("View My Alerts");
        viewAlertsBtn.addActionListener(e -> new VictimAlerts(victim).setVisible(true));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            try {
                new VictimLogin().setVisible(true);
            } catch (SQLException ex) {
                System.getLogger(VictimDashboard.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });

        centerPanel.add(viewAlertsBtn);
        centerPanel.add(logoutBtn);

        add(centerPanel, BorderLayout.CENTER);
    }
}