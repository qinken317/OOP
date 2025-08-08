import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AdminPage extends JFrame {

    public AdminPage() {
        // Now, we are setting properties on 'this' (the AdminPage JFrame instance)
        super("Admin Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 800));

        // Create the main content panel with a BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());

        // North - Header with centered title
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);

        // West - Navigation (Buttons filling the width)
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
        toolbar.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton profileButton = new JButton("Profile");
        JButton bookButton = new JButton("AddBook");
        JButton removeButton = new JButton("Remove Book");
        JButton ViewBookButton = new JButton("View All Books");
        JButton logoutbutton = new JButton("LogOut");

        profileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ViewBookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutbutton.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, profileButton.getPreferredSize().height));
        bookButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, bookButton.getPreferredSize().height));
        removeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, removeButton.getPreferredSize().height));
        ViewBookButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, ViewBookButton.getPreferredSize().height));
        logoutbutton.setMaximumSize(new Dimension(Integer.MAX_VALUE, logoutbutton.getPreferredSize().height));

        toolbar.add(profileButton);
        toolbar.add(Box.createRigidArea(new Dimension(0, 10)));
        toolbar.add(bookButton);
        toolbar.add(Box.createRigidArea(new Dimension(0, 10)));
        toolbar.add(removeButton);
        toolbar.add(Box.createRigidArea(new Dimension(0, 10)));
        toolbar.add(ViewBookButton);
        toolbar.add(Box.createVerticalGlue());
        toolbar.add(logoutbutton);

        profileButton.addActionListener(e -> {
            this.dispose();
            // You could pass real data here if you had it
            // For now, we use a placeholder
            new ProfilePage("Sim Zi Ming", "Administrator", "DCS2410004");
        });

        bookButton.addActionListener(e -> {
            this.dispose();
            new addbook();
        });

        removeButton.addActionListener(e -> {
            this.dispose(); // Closes the current AdminPage frame if you want to
            new removebook().setVisible(true); // Opens the new removebook frame
        });

        ViewBookButton.addActionListener(e ->{
            dispose();
            new ViewAllBooksPage();
        });

        logoutbutton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to log out?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                SwingUtilities.invokeLater(() -> new Login().setVisible(true));
            }
        });
        mainContentPanel.add(toolbar, BorderLayout.WEST);

        // Center - Library Statistics
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BorderLayout());
        JLabel statsTitle = new JLabel("Library Statistics");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statsTitle.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsPanel.add(statsTitle, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 2, 10, 10));
        gridPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        // Borrowed Panel
        JPanel borrowedPanel = new JPanel(new BorderLayout());
        borrowedPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        JLabel borrowedTitle = new JLabel("Borrowed");
        borrowedTitle.setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel borrowedValue = new JLabel("14");
        borrowedValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        borrowedValue.setHorizontalAlignment(SwingConstants.LEFT);
        borrowedValue.setBorder(new EmptyBorder(0, 5, 5, 5));
        borrowedPanel.add(borrowedTitle, BorderLayout.NORTH);
        borrowedPanel.add(borrowedValue, BorderLayout.CENTER);
        gridPanel.add(borrowedPanel);

        // Overdue Panel
        JPanel overduePanel = new JPanel(new BorderLayout());
        overduePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        JLabel overdueTitle = new JLabel("Overdue");
        overdueTitle.setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel overdueValue = new JLabel("8");
        overdueValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        overdueValue.setHorizontalAlignment(SwingConstants.LEFT);
        overdueValue.setBorder(new EmptyBorder(0, 5, 5, 5));
        overduePanel.add(overdueTitle, BorderLayout.NORTH);
        overduePanel.add(overdueValue, BorderLayout.CENTER);
        gridPanel.add(overduePanel);

        // Visitors Panel
        JPanel visitorsPanel = new JPanel(new BorderLayout());
        visitorsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        JLabel visitorsTitle = new JLabel("Visitors");
        visitorsTitle.setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel visitorsValue = new JLabel("6");
        visitorsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        visitorsValue.setHorizontalAlignment(SwingConstants.LEFT);
        visitorsValue.setBorder(new EmptyBorder(0, 5, 5, 5));
        visitorsPanel.add(visitorsTitle, BorderLayout.NORTH);
        visitorsPanel.add(visitorsValue, BorderLayout.CENTER);
        gridPanel.add(visitorsPanel);

        // New Members Panel
        JPanel newMembersPanel = new JPanel(new BorderLayout());
        newMembersPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        JLabel newMembersTitle = new JLabel("New members");
        newMembersTitle.setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel newMembersValue = new JLabel("4");
        newMembersValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        newMembersValue.setHorizontalAlignment(SwingConstants.LEFT);
        newMembersValue.setBorder(new EmptyBorder(0, 5, 5, 5));
        newMembersPanel.add(newMembersTitle, BorderLayout.NORTH);
        newMembersPanel.add(newMembersValue, BorderLayout.CENTER);
        gridPanel.add(newMembersPanel);

        statsPanel.add(gridPanel, BorderLayout.CENTER);
        mainContentPanel.add(statsPanel, BorderLayout.CENTER);

        // Add the main content panel to the frame
        add(mainContentPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // The main method is now simpler
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPage());
    }
}