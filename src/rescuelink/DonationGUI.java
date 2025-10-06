package rescuelink;

import javax.swing.*;
import java.awt.*;

public class DonationGUI extends JFrame {
    private final JTextField donorNameField = new JTextField();
    private final JTextField typeField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final DonationDAO donationDAO = new DonationDAO();

    public DonationGUI() {
        setTitle("Donation Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Donor Name:"));
        panel.add(donorNameField);
        panel.add(new JLabel("Donation Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        JButton registerBtn = new JButton("Register Donation");
        panel.add(new JLabel(""));
        panel.add(registerBtn);

        add(panel);

        registerBtn.addActionListener(e -> registerDonation());
    }

    private void registerDonation() {
        String donor = donorNameField.getText().trim();
        String type = typeField.getText().trim();
        String amtStr = amountField.getText().trim();

        if (donor.isEmpty() || type.isEmpty() || amtStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amtStr);
            Donation donation = new Donation(donor, type, amount);
            if (donationDAO.registerDonation(donation)) {
                JOptionPane.showMessageDialog(this, "Donation registered successfully!");
                donorNameField.setText("");
                typeField.setText("");
                amountField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save donation.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid number.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DonationGUI().setVisible(true));
    }
}