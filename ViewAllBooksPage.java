//package src.gui;
import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
//import src.db.DatabaseConnection;

public class ViewAllBooksPage extends JFrame {

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    public ViewAllBooksPage() {
        super("View All Books");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadAllBooks();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel("All Books in Library", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Table for displaying all books
        String[] columnNames = {"Book ID", "Title", "Author", "Genre", "Availability"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table read-only
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // South panel for the back button
        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Back");
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Add action listener for the back button
        backButton.addActionListener(e -> {
            this.dispose(); // Close this window
            new AdminPage().setVisible(true); // Re-open the AdminPage
        });
    }

    private void loadAllBooks() {
        tableModel.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT bookId, bookTitle, bookAuthor, bookGenre, bookQuantity FROM books";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String bookId = rs.getString("bookId");
                    String title = rs.getString("bookTitle");
                    String author = rs.getString("bookAuthor");
                    String genre = rs.getString("bookGenre");
                    int quantity = rs.getInt("bookQuantity");

                    tableModel.addRow(new Object[]{bookId, title, author, genre, quantity});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewAllBooksPage());
    }
}