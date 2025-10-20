package rescuelink;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VictimLogin extends JFrame {

    private final JTextField phoneField;
    private final JButton loginButton;
    private final VictimModule vm;

    public VictimLogin() throws SQLException {
        vm = new VictimModule();

        setTitle("Victim Login");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Enter Phone Number:"), gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(15);
        add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login / Register");
        add(loginButton, gbc);

        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Phone Number.");
            return;
        }

        // Check if victim exists
        Victim victim = vm.getVictimByPhone(phone);

        if (victim == null) {
            try {
          
                JOptionPane.showMessageDialog(this, "First-time login detected. Please fill your details.");
                VictimGUI registrationGUI = new VictimGUI(phone, vm); // pass phone & module
                registrationGUI.setVisible(true);
            } catch (SQLException ex) {
                System.getLogger(VictimLogin.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        } else {
 
            VictimDashboard dashboard = new VictimDashboard(victim);
            dashboard.setVisible(true);
        }

        this.dispose(); // Close login window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new VictimLogin().setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}
