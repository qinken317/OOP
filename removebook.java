import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

public class removebook extends JFrame {
    private JButton removeButton , cancelButton;
    private JTextField idField;

    public removebook() {
        // Set up the main JFrame
        setTitle("Remove Book");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Call a method to create and add the GUI components
        initComponents();

        // Make the frame visible
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        JLabel titleLabel = new JLabel("Remove a Book");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter Book Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Book ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idLabel = new JLabel("Book ID:");
        idLabel.setFont(labelFont);
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        idField = new JTextField(15);
        idField.setFont(fieldFont);
        formPanel.add(idField, gbc);

        // Panel for the remove and cancel button
        JPanel buttonPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(labelFont);
        buttonPanel.add(cancelButton);

        removeButton = new JButton("Remove Book");
        removeButton.setFont(labelFont);
        buttonPanel.add(removeButton);

        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                new AdminPage();
                dispose();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRemove();
            }
        });

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleRemove() {
        String bookId = idField.getText().trim();

        // Input validation
        if (bookId.isEmpty()) {
            showError("Please enter a Book ID");
            return;
        }

        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove book with ID: " + bookId + "?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Database operation
        try (Connection conn = DatabaseConnection.getConnection()) {
            // First check if book exists
            if (!bookExists(conn, bookId)) {
                showError("No book found with ID: " + bookId);
                return;
            }

            // Then delete the book
            String sql = "DELETE FROM books WHERE bookId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, bookId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Book successfully removed!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    idField.setText("");
                }
            }
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean bookExists(Connection conn, String bookId) throws SQLException {
        String sql = "SELECT 1 FROM books WHERE bookId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new removebook());
    }
}