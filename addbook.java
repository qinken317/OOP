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
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class addbook extends JFrame {
    private JButton submitButton, cancelButton;
    private JTextField titleField, idField, authorField, genreField, quantityField;

    public addbook() {
        // Set up the main JFrame
        setTitle("Add Book");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Call a method to create and add the GUI components
        initComponents();

        // Make the frame visible
        setVisible(true);
    }

    private void initComponents() {
        // Main panel to hold everything, using a BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding around the whole panel

        // Header Panel for the title
        JPanel headerPanel = new JPanel();
        JLabel titleLabel = new JLabel("Add New Book");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel for the form fields, using GridBagLayout for flexibility
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // --- Create and add form components ---

        // Book Title
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel titleLabelForm = new JLabel("Book Title:");
        titleLabelForm.setFont(labelFont);
        formPanel.add(titleLabelForm, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        titleField = new JTextField(20);
        titleField.setFont(fieldFont);
        formPanel.add(titleField, gbc);

        // Book ID
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel idLabel = new JLabel("Book ID:");
        idLabel.setFont(labelFont);
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        idField = new JTextField();
        idField.setFont(fieldFont);
        formPanel.add(idField, gbc);

        // Book Author
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel authorLabel = new JLabel("Book Author:");
        authorLabel.setFont(labelFont);
        formPanel.add(authorLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        authorField = new JTextField();
        authorField.setFont(fieldFont);
        formPanel.add(authorField, gbc);

        // Book Genre
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel genreLabel = new JLabel("Book Genre:");
        genreLabel.setFont(labelFont);
        formPanel.add(genreLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        genreField = new JTextField();
        genreField.setFont(fieldFont);
        formPanel.add(genreField, gbc);

        // Book Quantity
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel quantityLabel = new JLabel("Book Quantity:");
        quantityLabel.setFont(labelFont);
        formPanel.add(quantityLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        quantityField = new JTextField();
        quantityField.setFont(fieldFont);
        formPanel.add(quantityField, gbc);

        // Create a panel for the submit button
        JPanel buttonPanel = new JPanel(); // FlowLayout by default
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(labelFont);
        buttonPanel.add(cancelButton);

        submitButton = new JButton("Submit");
        submitButton.setFont(labelFont);
        buttonPanel.add(submitButton);

        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                new AdminPage();
                dispose();
            }
        });

        // Add action listener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        // Add the panels to the main frame
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleSubmit() {
        String getTitle = titleField.getText();
        String getId = idField.getText();
        String getAuthor = authorField.getText();
        String getGenre = genreField.getText();

        int getQuantity = 0;

        try {
            getQuantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Quantity!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO books (bookId, bookTitle, bookAuthor, bookGenre, bookQuantity) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, getId);
            pstmt.setString(2, getTitle);
            pstmt.setString(3, getAuthor);
            pstmt.setString(4, getGenre);
            pstmt.setInt(5, getQuantity);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear fields after successful submission
                titleField.setText("");
                idField.setText("");
                authorField.setText("");
                genreField.setText("");
                quantityField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving to database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to check if book already exists
    private boolean bookExists(Connection conn, String bookId) throws SQLException {
        String sql = "SELECT 1 FROM books WHERE bookId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            try (var rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if book exists
            }
        }
    }

    public static void main(String[] args) {
        // Ensures the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new addbook());
    }
}