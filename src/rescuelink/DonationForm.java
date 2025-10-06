package rescuelink;

import javax.swing.*;
import java.awt.*;

public class DonationForm extends JFrame {

    private final DonationDAO donationDAO = new DonationDAO();

    public DonationForm() {
        setTitle("Make a Donation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Labels and fields
        JLabel nameLabel = new JLabel("Your Name:");
        JTextField nameField = new JTextField();

        JLabel typeLabel = new JLabel("Donation Type:");
        JTextField typeField = new JTextField();

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();

        JLabel statusLabel = new JLabel("Status:");
        JTextField statusField = new JTextField("Pending"); // default

        JButton submitBtn = new JButton("Submit Donation");
        JButton backBtn = new JButton("Back");

        // Add to frame
        add(nameLabel);
        add(nameField);
        add(typeLabel);
        add(typeField);
        add(amountLabel);
        add(amountField);
        add(statusLabel);
        add(statusField);
        add(submitBtn);
        add(backBtn);

        // Submit action
        submitBtn.addActionListener(e -> {
            String donorName = nameField.getText().trim();
            String type = typeField.getText().trim();
            String amountStr = amountField.getText().trim();
            String status = statusField.getText().trim();

            if (donorName.isEmpty() || type.isEmpty() || amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid number for amount.");
                return;
            }

            Donation donation = new Donation(0, donorName, type, amount, status);

            if (donationDAO.registerDonation(donation)) {
                JOptionPane.showMessageDialog(this, "Donation submitted successfully!");
                nameField.setText("");
                typeField.setText("");
                amountField.setText("");
                statusField.setText("Pending");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit donation.");
            }
        });

        // Back action
        backBtn.addActionListener(e -> {
            new StartPage().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DonationForm().setVisible(true));
    }
}