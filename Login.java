import java.awt.*;
import javax.swing.*; // Don't forget this import!
import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;

// 1. Make Login extend JFrame
public class Login extends JFrame {

    // Declare components as instance variables so they are accessible throughout the class
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // 2. Create the public constructor
    public Login() {
        // Call the superclass (JFrame) constructor to set the title
        super("Library Management System Login");

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Constants for frame dimensions
        final int FRAME_WIDTH = 400;
        final int FRAME_HEIGHT = 300;
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null); // Center the window

        // Create a main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Set up constraints for GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and configure font
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        // Username components
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        mainPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15); // Initialize instance variable
        usernameField.setFont(fieldFont);
        mainPanel.add(usernameField, gbc);

        // Password components
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        mainPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15); // Initialize instance variable
        passwordField.setFont(fieldFont);
        mainPanel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span two columns
        gbc.fill = GridBagConstraints.CENTER;
        loginButton = new JButton("Login"); // Initialize instance variable
        loginButton.setFont(labelFont);
        mainPanel.add(loginButton, gbc);

        // 3. Add action listener to the button (now it's attached to the instance's button)
        loginButton.addActionListener(e -> {
            attemptLogin(); // Call the instance method
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 10, 10);
        JCheckBox rememberCheckBox = new JCheckBox("Remember Me");
        rememberCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        rememberCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(rememberCheckBox, gbc);

        // Add main panel to frame (since Login now IS a JFrame)
        add(mainPanel);
        pack(); // Adjusts frame to preferred size of components
        // setVisible(true) will be called from main
    }

    // 4. Move the core login logic into an instance method
    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT userid, role FROM users WHERE username = ? AND pass = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userRole = rs.getString("role");
                String userId = rs.getString("userid");

                JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                if ("admin".equals(userRole)) {
                    new AdminPage();
                } else {
                    new UserPage();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Username or Password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
/*
        // 3. Role-Based Access Control & Open New Page
        if ("admin".equals(userRole)) {
            JOptionPane.showMessageDialog(null, "Admin Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new AdminPage();
            dispose(); // Close current login window
        } else if ("member".equals(userRole)) {
            JOptionPane.showMessageDialog(null, "User Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new UserPage();
            dispose(); // Close current login window
        }
    }
*/
    // 5. Update the main method to create an instance of Login
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login loginFrame = new Login(); // Create an instance of Login
            loginFrame.setVisible(true);   // Make the instance visible
        });
    }
}