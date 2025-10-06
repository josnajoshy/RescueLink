package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RescueLogin extends JFrame {
    private final JTextField idField;
    private final JTextField nameField;
    private final JButton loginButton;
    private final RescueModule rm;

    public RescueLogin() throws SQLException {
        rm = new RescueModule();

        setTitle("Rescue Team Login");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Team ID:"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("Team Name:"));
        nameField = new JTextField();
        add(nameField);

        loginButton = new JButton("Login");
        add(new JLabel());
        add(loginButton);

        loginButton.addActionListener(e -> login());
    }

    private void login() {
        try {
            int teamId = Integer.parseInt(idField.getText().trim());
            String teamName = nameField.getText().trim();

            RescueTeam team = rm.getTeamByCredentials(teamId, teamName);

            if (team != null) {
                JOptionPane.showMessageDialog(this, "Login Successful! 🚒");
                new RescueDashboard(team, rm).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID or Name!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new RescueLogin().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}