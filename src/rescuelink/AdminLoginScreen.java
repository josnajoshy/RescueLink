package rescuelink;

import javax.swing.*;

public class AdminLoginScreen extends JFrame {

    public AdminLoginScreen() {
        setTitle("Admin Login");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            AdminDAO adminDAO = new AdminDAO();
            if (adminDAO.validateAdmin(username, password)) {
                dispose(); // close login screen
                new AdminControlPanel().setVisible(true); // open control panel
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        });
    }
}