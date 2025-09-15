package rescuelink;

import javax.swing.*;
import java.awt.*;

public class StartPage extends JFrame {

    public StartPage() {
        setTitle("RescueLink - Start Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Logo / Title
        JLabel title = new JLabel("RescueLink", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        JLabel tagline = new JLabel("Connecting Help with Hope", SwingConstants.CENTER);
        tagline.setFont(new Font("Arial", Font.ITALIC, 14));
        add(tagline, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton victimBtn = new JButton("Sign in as Victim");
        JButton volunteerBtn = new JButton("Sign in as Volunteer");
        JButton adminBtn = new JButton("Sign in as Admin");

        buttonPanel.add(victimBtn);
        buttonPanel.add(volunteerBtn);
        buttonPanel.add(adminBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        victimBtn.addActionListener(e -> {
            new VictimGUI().setVisible(true);
            dispose();
        });

        volunteerBtn.addActionListener(e -> {
    new VolunteerForm().setVisible(true);
    dispose(); // closes StartPage window if you want
});


        adminBtn.addActionListener(e -> {
            new AdminDashboard().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartPage().setVisible(true));
    }
}
