import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProfilePage extends JFrame {

    private String username;
    private String userRole;
    private String userid;

    // This is the primary constructor that accepts all three arguments
    public ProfilePage(String username, String userRole, String userid) {
        this.username = username;
        this.userRole = userRole;
        this.userid = userid;

        setTitle("User Profile");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    // This is the new, overloaded constructor that accepts only two arguments.
    // It calls the main constructor with a default value for the userid.
    public ProfilePage(String username, String userRole) {
        this(username, userRole, "Not Provided");
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder("Personal Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font valueFont = new Font("Arial", Font.PLAIN, 14);

        gbc.gridx = 0; gbc.gridy = 0;
        profilePanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JLabel usernameValue = new JLabel(username);
        usernameValue.setFont(valueFont);
        profilePanel.add(usernameValue, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        profilePanel.add(new JLabel("User Role:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JLabel roleValue = new JLabel(userRole);
        roleValue.setFont(valueFont);
        profilePanel.add(roleValue, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        profilePanel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JLabel idValue = new JLabel(userid);
        idValue.setFont(valueFont);
        profilePanel.add(idValue, gbc);

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.setFont(labelFont);
        buttonPanel.add(backButton);

        backButton.addActionListener(e -> {
            this.dispose();
            if (userRole.equals("Administrator")) {
                new AdminPage();
            } else {
                new UserPage();
            }
        });

        mainPanel.add(profilePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}