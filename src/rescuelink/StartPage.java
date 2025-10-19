package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StartPage extends JFrame {

    public StartPage() {
        setTitle("RescueLink - Start Page");
        setSize(400, 400); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Header ---
        JLabel title = new JLabel("RescueLink", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        JLabel tagline = new JLabel("Connecting Help with Hope", SwingConstants.CENTER);
        tagline.setFont(new Font("Arial", Font.ITALIC, 14));
        add(tagline, BorderLayout.CENTER);

        
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10)); 

        JButton victimBtn = new JButton("Sign in as Victim");
        JButton volunteerBtn = new JButton("Sign in as Volunteer");
        JButton adminBtn = new JButton("Sign in as Admin");
        JButton donationBtn = new JButton("Donate / View Donations");
        JButton rescueBtn = new JButton("Sign in as Rescue Team"); 

        buttonPanel.add(victimBtn);
        buttonPanel.add(volunteerBtn);
        buttonPanel.add(adminBtn);
        buttonPanel.add(donationBtn);
        buttonPanel.add(rescueBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        

       
        victimBtn.addActionListener(e -> {
            JTextField phoneField = new JTextField();
            int result = JOptionPane.showConfirmDialog(
                    this,
                    phoneField,
                    "Enter your phone number",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (result == JOptionPane.OK_OPTION) {
                String phone = phoneField.getText().trim();
                if (!phone.isEmpty()) {
                    try {
                        VictimModule vm = new VictimModule();
                        new VictimGUI(phone, vm).setVisible(true);
                        dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error loading victim interface.");
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Phone number cannot be empty.");
                }
            }
        });

        // Volunteer login
        volunteerBtn.addActionListener(e -> {
            new VolunteerForm().setVisible(true);
            dispose();
        });

        // Admin login
        adminBtn.addActionListener(e -> {
            new AdminLoginScreen().setVisible(true);
            dispose();
        });

        // Donation page
        donationBtn.addActionListener(e -> {
            new DonationForm().setVisible(true);
            dispose();
        });

        // Rescue team login
        rescueBtn.addActionListener(e -> {
            try {
                new RescueLogin().setVisible(true);
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading rescue login.");
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartPage().setVisible(true));
    }
}
