import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserPage extends JFrame {

    public UserPage() {
        // Set properties directly on 'this' (the UserPage JFrame instance)
        super("User Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 800));

        // Create the main content panel with a BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());

        // North - Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);

        // West - Navigation Toolbar
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
        toolbar.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton profileButton = new JButton("Profile");
        JButton searchButton = new JButton("Search Book");
        JButton myBooksButton = new JButton("My Books");
        JButton logoutbutton = new JButton("LogOut");

        // Button alignment and sizing for a clean look
        profileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        myBooksButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutbutton.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, profileButton.getPreferredSize().height));
        searchButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchButton.getPreferredSize().height));
        myBooksButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, myBooksButton.getPreferredSize().height));
        logoutbutton.setMaximumSize(new Dimension(Integer.MAX_VALUE, logoutbutton.getPreferredSize().height));

        toolbar.add(profileButton);
        toolbar.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        toolbar.add(searchButton);
        toolbar.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        toolbar.add(myBooksButton);
        toolbar.add(Box.createVerticalGlue()); // Pushes buttons to the top
        toolbar.add(logoutbutton);

        // Action Listener for 'Logout'
        logoutbutton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, // Dialog is now centered over 'this' frame
                    "Are you sure you want to log out?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose(); // Correctly closes this frame
                new Login().setVisible(true); // Re-opens the login frame
            }
        });

        profileButton.addActionListener(e -> {
            // You could pass real data here if you had it
            // For now, we use a placeholder
            dispose();
            new ProfilePage("Chan Qin Ken", "Member", "DCS2410006");
        });

        // Action Listener for 'Search Book'
        searchButton.addActionListener(e -> {
            // This is the placeholder you had before
            // JOptionPane.showMessageDialog(this, "Opening Search Book page...", "Info",
            // JOptionPane.INFORMATION_MESSAGE);

            // Replace the placeholder with the new functionality
            this.dispose(); // Close the current UserPage
            new SearchBookPage(); // Open the SearchBookPage
        });

        myBooksButton.addActionListener(e -> {
            this.dispose(); // Close the current UserPage
            // Pass a user ID string to the MyBooksPage constructor
            new MyBooksPage("sampleUserId"); // Correct way to call the constructor
        });

        mainContentPanel.add(toolbar, BorderLayout.WEST);

        // Center - User specific content
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(1, 1));
        userPanel.setBorder(new TitledBorder("Welcome User!"));

        JLabel welcomeLabel = new JLabel("Welcome to the Library Management System.", JLabel.CENTER);
        userPanel.add(welcomeLabel);

        mainContentPanel.add(userPanel, BorderLayout.CENTER);

        // Add the main content panel to the frame
        add(mainContentPanel);

        pack();
        setLocationRelativeTo(null); // Centers the frame
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserPage());
    }
}