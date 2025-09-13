package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminBadgePanel extends JFrame {
    private final JComboBox<Volunteer> volunteerDropdown;
    private final JComboBox<Badge> badgeDropdown;
    private final JTextField pointsField;
    private final JButton assignButton, refreshButton, clearButton;

    public AdminBadgePanel() {
        setTitle("Admin Badge Assignment");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Select Volunteer:"));
        volunteerDropdown = new JComboBox<>();
        panel.add(volunteerDropdown);

        panel.add(new JLabel("Select Badge:"));
        badgeDropdown = new JComboBox<>();
        panel.add(badgeDropdown);

        panel.add(new JLabel("Reward Points:"));
        pointsField = new JTextField("10");
        panel.add(pointsField);

        assignButton = new JButton("Assign Badge");
        refreshButton = new JButton("Refresh Lists");
        clearButton = new JButton("Clear");

        panel.add(refreshButton);
        panel.add(clearButton);
        panel.add(new JLabel()); // spacer
        panel.add(assignButton);

        add(panel);
        loadDropdowns();

        assignButton.addActionListener(e -> assignBadge());
        refreshButton.addActionListener(e -> loadDropdowns());
        clearButton.addActionListener(e -> clearForm());
    }

    private void loadDropdowns() {
        volunteerDropdown.removeAllItems();
        badgeDropdown.removeAllItems();

        VolunteerDAO volunteerDAO = new VolunteerDAO();
        List<Volunteer> volunteers = volunteerDAO.getVolunteerList();
        for (Volunteer v : volunteers) {
            volunteerDropdown.addItem(v);
        }

        BadgeDAO badgeDAO = new BadgeDAO();
        List<Badge> badges = badgeDAO.getAllBadges();
        for (Badge b : badges) {
            badgeDropdown.addItem(b);
        }
    }

    private void assignBadge() {
        Volunteer volunteer = (Volunteer) volunteerDropdown.getSelectedItem();
        Badge badge = (Badge) badgeDropdown.getSelectedItem();
        int points;

        try {
            points = Integer.parseInt(pointsField.getText().trim());
            if (points < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive number for points.");
            return;
        }

        if (volunteer != null && badge != null) {
            AdminDAO adminDAO = new AdminDAO();
            boolean success = adminDAO.assignBadgeToVolunteer(volunteer.getId(), badge.getBadgeId(), points);

            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Badge assigned to " + volunteer.getName() + " with " + points + " points.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to assign badge.");
            }
        }
    }

    private void clearForm() {
        pointsField.setText("10");
        volunteerDropdown.setSelectedIndex(0);
        badgeDropdown.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminBadgePanel().setVisible(true));
    }
}
