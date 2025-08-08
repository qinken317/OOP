import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;

public class SearchBookPage extends JFrame {

    private JTextField searchField;
    private JButton borrowButton;
    private JButton searchButton;
    private JButton backButton;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public SearchBookPage() {
        // Set up the main JFrame
        super("Search Books");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Main panel with a BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // North panel for search bar and button
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(30);
        searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search by Title or Author: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Center panel for displaying results in a table
        String[] columnNames = {"Book ID", "Title", "Author", "Genre", "Availability"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Override isCellEditable to make the table read-only
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        // South panel for the back button
        JPanel buttonPanel = new JPanel();
        borrowButton = new JButton("Borrow");
        backButton = new JButton("Back");
        buttonPanel.add(backButton);
        buttonPanel.add(borrowButton);

        // Add components to the main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Add action listeners
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch()); // Allows searching by pressing Enter
        borrowButton.addActionListener(e -> borrowSelectedBook());
        backButton.addActionListener(e -> {
            this.dispose();
            new UserPage().setVisible(true);
        });
    }

    private void borrowSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to borrow.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookId = tableModel.getValueAt(selectedRow, 0).toString();
        int quantity = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "This book is currently unavailable.", "Unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Insert into loans table (You need to have current user's ID)
            int userId = getCurrentUserId(); // Replace with your actual method to get logged-in user
            String insertLoan = "INSERT INTO loans (userId, bookId, borrowedDate, dueDate, returned) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), false)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertLoan)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, bookId);
                pstmt.executeUpdate();
            }

            // 2. Update book quantity
            String updateBook = "UPDATE books SET bookQuantity = bookQuantity - 1 WHERE bookId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateBook)) {
                pstmt.setString(1, bookId);
                pstmt.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
            performSearch(); // Refresh table
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error borrowing book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getCurrentUserId(){
        return 1;
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim().toLowerCase();

        // Clear previous results
        tableModel.setRowCount(0);

        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT bookId, bookTitle, bookAuthor, bookGenre, bookQuantity FROM books " +
                    "WHERE LOWER(bookTitle) LIKE ? OR LOWER(bookAuthor) LIKE ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + searchTerm + "%");
                pstmt.setString(2, "%" + searchTerm + "%");

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String bookId = rs.getString("bookId");
                        String title = rs.getString("bookTitle");
                        String author = rs.getString("bookAuthor");
                        String genre = rs.getString("bookGenre");
                        int quantity = rs.getInt("bookQuantity");

                        tableModel.addRow(new Object[]{bookId, title, author, genre, quantity});
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No books found matching your search.", "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchBookPage());
    }
}