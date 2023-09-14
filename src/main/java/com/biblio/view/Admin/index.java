package com.biblio.view.Admin;

import com.biblio.app.Controllers.AnalysisController;
import com.biblio.app.Controllers.BookController;
import com.biblio.app.Enums.Language;
import com.biblio.app.Models.*;
import com.biblio.dao.AuthorDao;
import com.biblio.dao.CategoryDao;
import com.biblio.dao.UserDao;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;

import com.toedter.calendar.JDateChooser;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class index extends JFrame implements ActionListener {

    private User User;
    private BookController bc;
    private AnalysisController ac;
    private JButton statistic, all_book, search, available_book, loan_book, brr_book, lost_book, user, awaiting_list, logout;
    private JButton add_book_button, edit_book_button, delete_book_button, search_book_button, loan_book_button;
    private JTable bookTable;
    private JScrollPane scrollPane;
    private JLabel book_label;
    private DefaultTableModel tableModel;
    private JTextField search_field;

    String CurrentBookClicked = null,CurrentCnieClicked= null,CurrentBookReferenceClicked= null;
    int CurrentBookClickedRow = -1;

    private AddBookDialog addBookDialog;
    private AddLoanDialog loanBookDialog;
    private EditBookDialog editBookDialog;
    private deleteBookDialog deleteBookDialog;
    private returnloanBookDialog returnloanBookDialog;


    public index(User u) throws SQLException {
        bc = new BookController();
        ac = new AnalysisController();
        User = u;

        setTitle("Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        ImageIcon logoIcon = new ImageIcon("assets/images/app/user.png");
        Image UserLogo = logoIcon.getImage();

        ImageIcon Statistic_icon = new ImageIcon("assets/icon/statistics.png");
        ImageIcon Book_icon = new ImageIcon("assets/icon/book.png");
        ImageIcon Search_icon = new ImageIcon("assets/icon/search.png");
        ImageIcon Available_icon = new ImageIcon("assets/icon/available.png");
        ImageIcon Loan_icon = new ImageIcon("assets/icon/loan.png");
        ImageIcon Borrowed_icon = new ImageIcon("assets/icon/borrow.png");
        ImageIcon Lost_icon = new ImageIcon("assets/icon/lost.png");
        ImageIcon Users_icon = new ImageIcon("assets/icon/user.png");
        ImageIcon Awaiting_icon = new ImageIcon("assets/icon/waiting.png");
        ImageIcon Logout_icon = new ImageIcon("assets/icon/logout.png");

        statistic = new JButton("  Statistic", Statistic_icon);
        all_book = new JButton("  All books", Book_icon);
        search = new JButton(" Search", Search_icon);
        available_book = new JButton("  Available books", Available_icon);
        loan_book = new JButton(" Loan books", Loan_icon);
        brr_book = new JButton("  Borrowed books", Borrowed_icon);
        lost_book = new JButton("  Lost books", Lost_icon);
        user = new JButton("  Users", Users_icon);
        awaiting_list = new JButton("  Awaiting list", Awaiting_icon);
        logout = new JButton("  Logout", Logout_icon);

        setButtons(statistic);
        setButtons(all_book);
        setButtons(search);
        setButtons(available_book);
        setButtons(loan_book);
        setButtons(brr_book);
        setButtons(lost_book);
        setButtons(user);
        setButtons(awaiting_list);
        setButtons(logout);

        int y = 110;
        int y2 = 10;

        JLabel logoLabel = new JLabel(new ImageIcon(UserLogo));
        logoLabel.setBounds(50, 50 + y2, 64, 64);

        StringBuilder user_roles = new StringBuilder();
        user_roles.append(" [");
        u.getRoles().forEach(role -> {
            user_roles.append(role.getRole()).append(", ");
        });
        user_roles.setLength(user_roles.length() - 1);
        user_roles.append("]");

        JLabel user_name = new JLabel(u.getFullName());
        user_name.setFont(new Font("Arial", Font.PLAIN, 25));
        user_name.setBounds(127, 70 + y2, 200, 17);

        JLabel user_contact = new JLabel((u.getEmail() != null ? u.getEmail() : u.getPhone() != null ? u.getPhone() : u.getCnie()));
        user_contact.setFont(new Font("Arial", Font.PLAIN, 13));
        user_contact.setBounds(127, 90 + y2, 300, 15);

//        JLabel user_role = new JLabel(user_roles.toString());
//        user_role.setFont(new Font("Arial", Font.PLAIN, 9));
//        user_role.setBounds(127, 110, 300,15);

        statistic.setBounds(50, 100 + y, 250, 30);
        all_book.setBounds(50, 155 + y, 250, 30);
        search.setBounds(50, 210 + y, 200, 30);
        available_book.setBounds(50, 265 + y, 250, 30);
        loan_book.setBounds(50, 320 + y, 250, 30);
        brr_book.setBounds(50, 375 + y, 250, 30);
        lost_book.setBounds(50, 430 + y, 250, 30);
        user.setBounds(50, 485 + y, 250, 30);
        awaiting_list.setBounds(50, 540 + y, 250, 30);
        logout.setBounds(50, 595 + y, 250, 30);

        statistic.addActionListener(this);
        search.addActionListener(this);
        all_book.addActionListener(this);
        available_book.addActionListener(this);
        loan_book.addActionListener(this);
        brr_book.addActionListener(this);
        lost_book.addActionListener(this);
        user.addActionListener(this);
        awaiting_list.addActionListener(this);
        logout.addActionListener(this);

        add(user_name);
        add(user_contact);
//        add(user_role);

        add(logoLabel);
        add(statistic);
        add(all_book);
        add(search);
        add(available_book);
        add(loan_book);
        add(brr_book);
        add(lost_book);
        add(user);
        add(awaiting_list);
        add(logout);

        displayStatistics();

        setVisible(true);

        // ouharrioutman@gmail.com
        // 68767498739879
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == statistic) {
            refresh();
            displayStatistics();
        } else if (e.getSource() == all_book) {
            refresh();
            try {
                displayAllBook();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == search) {
            refresh();
            try {
                displaySearchBook();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == available_book) {
            refresh();
            try {
                displayAvailableBook();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == loan_book) {
            refresh();
            try {
                displayLoanBook();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == brr_book) {
            refresh();
            try {
                displayBorrowedBook();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == lost_book) {
            refresh();

            try {
                displayLostBook();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == user) {
            refresh();
        } else if (e.getSource() == awaiting_list) {
            refresh();
        } else if (e.getSource() == logout) {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new com.biblio.view.Authentication.Signing();
            }
        }
    }

    private void displayStatistics() {
        book_label = new JLabel("Statistics :");
        book_label.setFont(new Font("Arial", Font.PLAIN, 25));
        book_label.setBounds(400, 140, 300, 50);

        JPanel totalBooksPanel = createStatPanel("Total Books", ac.getTotalBooks());
        JPanel totalAuthorsPanel = createStatPanel("Total Authors", ac.getTotalAuthors());
        JPanel totalCategoriesPanel = createStatPanel("Total Categories", ac.getTotalCategories());
        JPanel totalUsersPanel = createStatPanel("Total Users", ac.getTotalUsers());
        JPanel totalBorrowedBooksPanel = createStatPanel("Total Borrowed Books", ac.getTotalBorrowedBooks());
        JPanel totalAvailableBooksPanel = createStatPanel("Total Available Books", ac.getTotalAvailableBooks());
        JPanel totalLostBooksPanel = createStatPanel("Total Lost Books", ac.getTotalLostBooks());
        JPanel totalBorrowedNotReturnedBooksPanel = createStatPanel("Total Borrowed Not Returned Books", ac.getTotalBorrowedNotReturnedBooks());
        JPanel totalBorrowedUsersPanel = createStatPanel("Total Borrowed Users", ac.getTotalBorrowedUsers());
        JPanel totalAvailableUsersPanel = createStatPanel("Total Available Users", ac.getTotalAvailableUsers());
        JPanel totalStockPanel = createStatPanel("Total Stock", ac.getTotalStock());
        JPanel totalReturnedBooksPanel = createStatPanel("Total Returned Books", ac.getTotalReturnedBooks());

        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(new GridLayout(4, 3));
        statisticsPanel.add(totalBooksPanel);
        statisticsPanel.add(totalAuthorsPanel);
        statisticsPanel.add(totalCategoriesPanel);
        statisticsPanel.add(totalUsersPanel);
        statisticsPanel.add(totalBorrowedBooksPanel);
        statisticsPanel.add(totalAvailableBooksPanel);
        statisticsPanel.add(totalLostBooksPanel);
        statisticsPanel.add(totalBorrowedNotReturnedBooksPanel);
        statisticsPanel.add(totalBorrowedUsersPanel);
        statisticsPanel.add(totalAvailableUsersPanel);
        statisticsPanel.add(totalStockPanel);
        statisticsPanel.add(totalReturnedBooksPanel);

        scrollPane = new JScrollPane(statisticsPanel);
        scrollPane.setBounds(400, 200, 1070, 550);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(book_label);
        add(scrollPane);

        revalidate();
        repaint();
    }

    private void displayAllBook() throws SQLException {
        book_label = new JLabel("All Books :");
        book_label.setFont(new Font("Arial", Font.PLAIN, 25));
        book_label.setBounds(400, 140, 300, 50);

        tableModel = new DefaultTableModel(new String[]{"ISBN", "Title", "Pages", "Edition", "Quantity", "Language", "Author", "Category", "Description"}, 0);
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(30);

        bookTable.getColumnModel().getColumn(3).setPreferredWidth(20);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(3);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(25);

        ImageIcon add_book_icon = new ImageIcon("assets/icon/add_book.png");
        ImageIcon edit_book_icon = new ImageIcon("assets/icon/edit_book.png");
        ImageIcon delete_book_icon = new ImageIcon("assets/icon/delete_book.png");

        add_book_button = new JButton("", add_book_icon);
        edit_book_button = new JButton("", edit_book_icon);
        delete_book_button = new JButton("", delete_book_icon);


        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(Color.WHITE);
        bookTable.setDefaultRenderer(Object.class, renderer);

        setButtons(add_book_button);
        setButtons(edit_book_button);
        setButtons(delete_book_button);

        add_book_button.setBounds(1350, 150, 50, 24);
        edit_book_button.setBounds(1300, 150, 50, 24);
        delete_book_button.setBounds(1250, 150, 50, 24);

        Frame owner = this;

        add_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addBookDialog = new AddBookDialog(owner);
                        addBookDialog.setVisible(true);
                    }
                }
        );
        edit_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            editBookDialog = new EditBookDialog(owner);
                            editBookDialog.display();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        delete_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            deleteBookDialog = new deleteBookDialog();
                            deleteBookDialog.display();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );

        scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(400, 190, 1000, 700);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        List<Book> books = bc.getAlBooks();

        addBooksToTable(books);

        add(book_label);
        add(add_book_button);
        add(edit_book_button);
        add(delete_book_button);
        add(scrollPane);


        revalidate();
        repaint();
    }

    private void displaySearchBook() throws SQLException {
        book_label = new JLabel("Search In Books :");
        book_label.setFont(new Font("Arial", Font.PLAIN, 25));
        book_label.setBounds(400, 140, 300, 50);

        tableModel = new DefaultTableModel(new String[]{"ISBN", "Title", "Pages", "Edition", "Quantity", "Language", "Author", "Category", "Description"}, 0);
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(30);

        bookTable.getColumnModel().getColumn(3).setPreferredWidth(20);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(3);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(25);

        search_field = new JTextField();

        ImageIcon search_book_icon = new ImageIcon("assets/icon/loupe.png");
        ImageIcon edit_book_icon = new ImageIcon("assets/icon/edit_book.png");
        ImageIcon delete_book_icon = new ImageIcon("assets/icon/delete_book.png");

        search_book_button = new JButton("", search_book_icon);
        edit_book_button = new JButton("", edit_book_icon);
        delete_book_button = new JButton("", delete_book_icon);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(Color.WHITE);
        bookTable.setDefaultRenderer(Object.class, renderer);

        setButtons(search_book_button);
        setButtons(edit_book_button);
        setButtons(delete_book_button);

        search_field.setBounds(1050, 145, 200, 30);

        search_book_button.setBounds(1250, 150, 50, 24);
        edit_book_button.setBounds(1300, 150, 50, 24);
        delete_book_button.setBounds(1350, 150, 50, 24);

        Frame owner = this;

        search_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String search = search_field.getText();
                        List<Book> books = null;
                        try {
                            books = bc.searchBook(search);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        removeAllRowFromTable();
                        addBooksToTable(books);
                    }
                }
        );
        edit_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            editBookDialog = new EditBookDialog(owner);
                            editBookDialog.display();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        delete_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            deleteBookDialog = new deleteBookDialog();
                            deleteBookDialog.display();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );

        scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(400, 190, 1000, 700);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());


        List<Book> books = bc.getAlBooks();

        addBooksToTable(books);


        add(book_label);
        add(search_field);
        add(search_book_button);
        add(edit_book_button);
        add(delete_book_button);
        add(scrollPane);


        revalidate();
        repaint();
        setVisible(true);
    }

    private void displayAvailableBook() throws SQLException {
        book_label = new JLabel("All Books :");
        book_label.setFont(new Font("Arial", Font.PLAIN, 25));
        book_label.setBounds(400, 140, 300, 50);

        tableModel = new DefaultTableModel(new String[]{"ISBN", "Title", "Pages", "Edition", "Quantity", "Language", "Author", "Category", "Description"}, 0);
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(30);

        bookTable.getColumnModel().getColumn(3).setPreferredWidth(20);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(3);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(25);

        ImageIcon add_book_icon = new ImageIcon("assets/icon/add_book.png");
        ImageIcon edit_book_icon = new ImageIcon("assets/icon/edit_book.png");
        ImageIcon delete_book_icon = new ImageIcon("assets/icon/delete_book.png");

        add_book_button = new JButton("", add_book_icon);
        edit_book_button = new JButton("", edit_book_icon);
        delete_book_button = new JButton("", delete_book_icon);


        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(Color.WHITE);
        bookTable.setDefaultRenderer(Object.class, renderer);

        setButtons(add_book_button);
        setButtons(edit_book_button);
        setButtons(delete_book_button);

        add_book_button.setBounds(1350, 150, 50, 24);
        edit_book_button.setBounds(1300, 150, 50, 24);
        delete_book_button.setBounds(1250, 150, 50, 24);

        Frame owner = this;

        add_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addBookDialog = new AddBookDialog(owner);
                        addBookDialog.setVisible(true);
                    }
                }
        );
        edit_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            editBookDialog = new EditBookDialog(owner);
                            editBookDialog.display();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        delete_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            deleteBookDialog = new deleteBookDialog();
                            deleteBookDialog.display();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );

        scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(400, 190, 1000, 700);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        List<Book> books = bc.getAlBooks();

        addBooksToTable(books);

        add(book_label);
        add(add_book_button);
        add(edit_book_button);
        add(delete_book_button);
        add(scrollPane);


        revalidate();
        repaint();
    }

    private void displayLoanBook() throws SQLException {
        book_label = new JLabel("Loan Book :");
        book_label.setFont(new Font("Arial", Font.PLAIN, 25));
        book_label.setBounds(400, 140, 300, 50);

        tableModel = new DefaultTableModel(new String[]{"ISBN", "Title", "Pages", "Edition", "Quantity", "Language", "Author", "Category", "Description"}, 0);
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(30);

        bookTable.getColumnModel().getColumn(3).setPreferredWidth(20);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(3);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(25);

        ImageIcon loan_book_icon = new ImageIcon("assets/icon/add_book.png");

        loan_book_button = new JButton("", loan_book_icon);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(Color.WHITE);
        bookTable.setDefaultRenderer(Object.class, renderer);

        setButtons(loan_book_button);

        List<Book> books = bc.getAlBooks();

        addBooksToTable(books);

        loan_book_button.setBounds(1350, 150, 50, 24);

        Frame owner = this;

        loan_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            loanBookDialog = new AddLoanDialog(owner);
                            loanBookDialog.display();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );

        scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(400, 190, 1000, 700);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(book_label);
        add(loan_book_button);
        add(scrollPane);


        revalidate();
        repaint();
    }

    private void displayBorrowedBook() throws SQLException {
        book_label = new JLabel("Brrowed Book :");
        book_label.setFont(new Font("Arial", Font.PLAIN, 25));
        book_label.setBounds(400, 140, 300, 50);

        tableModel = new DefaultTableModel(new String[]{"cnie", "Name", "ISBN", "Title", "Book reference", "Loan date", "Excepted return date", "Return Date", "Requested date"}, 0);
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(30);

        bookTable.getColumnModel().getColumn(3).setPreferredWidth(20);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(3);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(25);

        ImageIcon loan_book_icon = new ImageIcon("assets/icon/return_book.png");

        loan_book_button = new JButton("", loan_book_icon);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(Color.WHITE);
        bookTable.setDefaultRenderer(Object.class, renderer);

        setButtons(loan_book_button);

        List<Loan> books = bc.getBorrowedBooks();

        addBorrowedToTable(books);

        loan_book_button.setBounds(1350, 150, 50, 24);

        loan_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            returnloanBookDialog = new returnloanBookDialog();
                        try {
                            returnloanBookDialog.display();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );

        scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(400, 190, 1000, 700);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(book_label);
        add(loan_book_button);
        add(scrollPane);

        revalidate();
        repaint();
    }

    private void displayLostBook()  throws SQLException {
        book_label = new JLabel("Lost Book :");
        book_label.setFont(new Font("Arial", Font.PLAIN, 25));
        book_label.setBounds(400, 140, 300, 50);

        tableModel = new DefaultTableModel(new String[]{"isbn","Book reference", "title", "lost Date", "Description", "actual status"}, 0);
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(30);

        bookTable.getColumnModel().getColumn(3).setPreferredWidth(20);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(3);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(25);

        ImageIcon loan_book_icon = new ImageIcon("assets/icon/add_book.png");

        loan_book_button = new JButton("", loan_book_icon);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(Color.WHITE);
        bookTable.setDefaultRenderer(Object.class, renderer);

        setButtons(loan_book_button);

        List<Lost> books = bc.getLostBooks();

        addLostToTable(books);

        loan_book_button.setBounds(1350, 150, 50, 24);

        loan_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        returnloanBookDialog = new returnloanBookDialog();
                        try {
                            returnloanBookDialog.display();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );

        scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(400, 190, 1000, 700);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(book_label);
        add(loan_book_button);
        add(scrollPane);

        revalidate();
        repaint();
    }

    private void refresh() {
        if (book_label != null) remove(book_label);
        if (add_book_button != null) remove(add_book_button);
        if (edit_book_button != null) remove(edit_book_button);
        if (delete_book_button != null) remove(delete_book_button);
        if (loan_book_button != null) remove(loan_book_button);
        if (scrollPane != null) remove(scrollPane);

        if (search_field != null) remove(search_field);
        if (search_book_button != null) remove(search_book_button);

        if (book_label != null) remove(book_label);
        if (scrollPane != null) remove(scrollPane);

        if (addBookDialog != null) addBookDialog.dispose();
        if (loanBookDialog != null) loanBookDialog.dispose();
        if (editBookDialog != null) editBookDialog.dispose();
        if (deleteBookDialog != null) deleteBookDialog.dispose();


        revalidate();
        repaint();
    }

    private JPanel createStatPanel(String label, int value) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setLayout(new GridLayout(2, 1));

        JLabel statLabel = new JLabel(label);
        statLabel.setHorizontalAlignment(JLabel.CENTER);
        statLabel.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel statValue = new JLabel(String.valueOf(value));
        statValue.setHorizontalAlignment(JLabel.CENTER);
        statValue.setForeground(Color.BLUE);
        statValue.setFont(new Font("Arial", Font.BOLD, 25));

        panel.add(statLabel);
        panel.add(statValue);

        return panel;
    }

    private class AddBookDialog extends JDialog {
        public AddBookDialog(Frame owner) {
            super(owner, "Add Book", true);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(500, 750);
            setLocationRelativeTo(null);
            setLayout(null);

            String[] categories = {null};
            String[] authors = {null};

            int categories_size = 0;
            int authors_size = 0;

            try (AuthorDao authorDao = new AuthorDao()) {
                authors = authorDao.getAllAuthors();
                categories_size = 20 * categories.length;
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }

            try (CategoryDao categoryDao = new CategoryDao()) {
                categories = categoryDao.getAllCategories();
                authors_size = 20 * categories.length;
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }


            JLabel isbn, quantity, pages, title, edition, language, description, author, category;
            JTextField isbn_field, quantity_field, pages_field, title_field, edition_field;
            JTextArea description_field;

            JComboBox<Language> language_field;
            JList<String> author_field;
            JList<String> category_field;

            BasicComboBoxRenderer renderer = new BasicComboBoxRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value != null) {
                        setText(value.toString());
                        setBackground(Color.WHITE);
                    }
                    return this;
                }
            };

            isbn = new JLabel("ISBN");
            quantity = new JLabel("Quantity");
            pages = new JLabel("Pages");
            title = new JLabel("Title");
            edition = new JLabel("Edition");
            language = new JLabel("Language");
            description = new JLabel("Description");
            author = new JLabel("Author");
            category = new JLabel("Category");

            isbn_field = new JTextField();
            quantity_field = new JTextField();
            pages_field = new JTextField();
            title_field = new JTextField();
            edition_field = new JTextField();
            description_field = new JTextArea();

            language_field = new JComboBox<Language>(Language.values());
            author_field = new JList<>(authors);
            category_field = new JList<>(categories);

            JScrollPane authorScrollPane = new JScrollPane(author_field);
            JScrollPane categoryScrollPane = new JScrollPane(category_field);

            JButton add_book = new JButton("Add Book");

            isbn.setBounds(50, 50, 400, 30);
            isbn_field.setBounds(50, 80, 400, 30);

            title.setBounds(50, 110, 400, 30);
            title_field.setBounds(50, 140, 400, 30);

            pages.setBounds(50, 170, 400, 30);
            pages_field.setBounds(50, 200, 400, 30);

            edition.setBounds(50, 230, 400, 30);
            edition_field.setBounds(50, 260, 400, 30);

            quantity.setBounds(50, 290, 400, 30);
            quantity_field.setBounds(50, 320, 400, 30);

            language.setBounds(50, 350, 400, 30);
            language_field.setBounds(50, 380, 400, 30);

            description.setBounds(50, 410, 400, 30);
            description_field.setBounds(50, 440, 400, 30);

            author.setBounds(50, 470, 400, 30);
            authorScrollPane.setBounds(50, 500, 400, 60);

            category.setBounds(50, 570, 400, 30);
            categoryScrollPane.setBounds(50, 600, 400, 60);

            add_book.setBounds(350, 670, 100, 30);
            add_book.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            add_book.setContentAreaFilled(false);
            add_book.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            add_book.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String isbn = isbn_field.getText();
                    String title = title_field.getText();
                    int pages = Integer.parseInt(pages_field.getText());
                    String edition = edition_field.getText();
                    int quantity = Integer.parseInt(quantity_field.getText());
                    Language language = (Language) language_field.getSelectedItem();
                    String description = description_field.getText();
                    String[] selectedAuthors = author_field.getSelectedValuesList().toArray(new String[0]);
                    String[] selectedCategories = category_field.getSelectedValuesList().toArray(new String[0]);

                    String[] authorIds = new String[selectedAuthors.length];
                    for (int i = 0; i < selectedAuthors.length; i++) {
                        authorIds[i] = extractId(selectedAuthors[i]);
                    }

                    String[] categoryIds = new String[selectedCategories.length];
                    for (int i = 0; i < selectedCategories.length; i++) {
                        categoryIds[i] = extractId(selectedCategories[i]);
                    }

                    try {
                        Book b = bc.addBook(isbn, title, description, language, quantity, pages, edition, authorIds, categoryIds);
                        if (b != null) {
                            tableModel.addRow(new Object[]{isbn, title, pages, edition, quantity, language, concatenateTexts(selectedAuthors), concatenateTexts(selectedCategories), description});
                            JOptionPane.showMessageDialog(null, "Book added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            setVisible(false);
//                            addBookDialog.removeAll();
                            revalidate();
                            repaint();
                            addBookDialog.dispose();
//                            remove(addBookDialog);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });

            add(isbn);
            add(isbn_field);
            add(title);
            add(title_field);
            add(pages);
            add(pages_field);
            add(edition);
            add(edition_field);
            add(quantity);
            add(quantity_field);
            add(language);
            add(language_field);
            add(description);
            add(description_field);
            add(author);
            add(authorScrollPane);
            add(category);
            add(categoryScrollPane);
            add(add_book);
        }
    }

    private class EditBookDialog extends JDialog {
        public EditBookDialog(Frame owner) {
            super(owner, "Edit Book", true);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(500, 750);
            setLocationRelativeTo(null);
            setLayout(null);
        }

        public void display() throws SQLException {
            if (CurrentBookClicked == null) {
                JOptionPane.showMessageDialog(null, "Please select a book to edit", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book b = bc.find(CurrentBookClicked);

            if (b == null) return;

            String[] categories;
            String[] authors;

            try (AuthorDao authorDao = new AuthorDao()) {
                authors = authorDao.getAllAuthors();
            } catch (Exception throwables) {
                throwables.printStackTrace();
                authors = new String[0];
            }

            try (CategoryDao categoryDao = new CategoryDao()) {
                categories = categoryDao.getAllCategories();
            } catch (Exception throwables) {
                throwables.printStackTrace();
                categories = new String[0];
            }

            JLabel isbn, quantity, pages, title, edition, language, description, author, category;
            JTextField isbn_field, quantity_field, pages_field, title_field, edition_field;
            JTextArea description_field;

            JComboBox<Language> language_field;
            JList<String> author_field;
            JList<String> category_field;

            isbn = new JLabel("ISBN");
            quantity = new JLabel("Quantity");
            pages = new JLabel("Pages");
            title = new JLabel("Title");
            edition = new JLabel("Edition");
            language = new JLabel("Language");
            description = new JLabel("Description");
            author = new JLabel("Author");
            category = new JLabel("Category");

            isbn_field = new JTextField();
            quantity_field = new JTextField();
            pages_field = new JTextField();
            title_field = new JTextField();
            edition_field = new JTextField();
            description_field = new JTextArea();

            isbn_field.setText(b.getIsbn());
            quantity_field.setText(String.valueOf(b.getQuantities()));
            pages_field.setText(String.valueOf(b.getPages()));
            title_field.setText(b.getTitle());
            edition_field.setText(b.getEdition());
            description_field.setText(b.getDescription());

            language_field = new JComboBox<>(Language.values());
            author_field = new JList<>(authors);
            category_field = new JList<>(categories);

            JScrollPane authorScrollPane = new JScrollPane(author_field);
            JScrollPane categoryScrollPane = new JScrollPane(category_field);

            String[] selectedAuthors = b.getAuthors().stream()
                    .map(Author::getFullName)
                    .toArray(String[]::new);
            author_field.setSelectedValue(selectedAuthors, true);

            String[] selectedCategories = b.getCategories().stream()
                    .map(Category::getCat)
                    .toArray(String[]::new);
            category_field.setSelectedValue(selectedCategories, true);

            JButton update_book = new JButton("Update Book");

            isbn.setBounds(50, 50, 400, 30);
            isbn_field.setBounds(50, 80, 400, 30);

            title.setBounds(50, 110, 400, 30);
            title_field.setBounds(50, 140, 400, 30);

            pages.setBounds(50, 170, 400, 30);
            pages_field.setBounds(50, 200, 400, 30);

            edition.setBounds(50, 230, 400, 30);
            edition_field.setBounds(50, 260, 400, 30);

            quantity.setBounds(50, 290, 400, 30);
            quantity_field.setBounds(50, 320, 400, 30);

            language.setBounds(50, 350, 400, 30);
            language_field.setBounds(50, 380, 400, 30);

            description.setBounds(50, 410, 400, 30);
            description_field.setBounds(50, 440, 400, 30);

            author.setBounds(50, 470, 400, 30);
            authorScrollPane.setBounds(50, 500, 400, 60);

            category.setBounds(50, 570, 400, 30);
            categoryScrollPane.setBounds(50, 600, 400, 60);

            update_book.setBounds(350, 670, 100, 30);
            update_book.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            update_book.setContentAreaFilled(false);
            update_book.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JComboBox<Language> finalLanguage_field = language_field;
            JList<String> finalAuthor_field = author_field;
            JList<String> finalCategory_field = category_field;
            update_book.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String isbn = isbn_field.getText();
                    String title = title_field.getText();
                    int pages = Integer.parseInt(pages_field.getText());
                    String edition = edition_field.getText();
                    int quantity = Integer.parseInt(quantity_field.getText());
                    Language language = (Language) finalLanguage_field.getSelectedItem();
                    String description = description_field.getText();
                    String[] selectedAuthors = finalAuthor_field.getSelectedValuesList().toArray(new String[0]);
                    String[] selectedCategories = finalCategory_field.getSelectedValuesList().toArray(new String[0]);

                    String[] authorIds = new String[selectedAuthors.length];
                    for (int i = 0; i < selectedAuthors.length; i++) {
                        authorIds[i] = extractId(selectedAuthors[i]);
                    }

                    String[] categoryIds = new String[selectedCategories.length];
                    for (int i = 0; i < selectedCategories.length; i++) {
                        categoryIds[i] = extractId(selectedCategories[i]);
                    }

                    try {
                        Book b = bc.updateBook(isbn, title, description, language, quantity, pages, edition, authorIds, categoryIds);
                        if (b != null) {
                            tableModel.removeRow(CurrentBookClickedRow);
                            tableModel.addRow(new Object[]{CurrentBookClicked, title, pages, edition, quantity, language, concatenateTexts(selectedAuthors), concatenateTexts(selectedCategories), description});
                            CurrentBookClicked = null;
                            CurrentBookClickedRow = -1;
                            JOptionPane.showMessageDialog(null, "Book added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            setVisible(false);
//                            editBookDialog.removeAll();
                            revalidate();
                            repaint();
                            editBookDialog.dispose();
                            remove(editBookDialog);
                        } else System.out.println("Book not updated");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    CurrentBookClicked = null;
                    CurrentBookClickedRow = -1;

                    setVisible(false);
//                    editBookDialog.removeAll();
                    revalidate();
                    repaint();
                    editBookDialog.dispose();
                    remove(editBookDialog);
                    dispose();
                }
            });

            add(isbn);
            add(isbn_field);
            add(title);
            add(title_field);
            add(pages);
            add(pages_field);
            add(edition);
            add(edition_field);
            add(quantity);
            add(quantity_field);
            add(language);
            add(language_field);
            add(description);
            add(description_field);
            add(author);
            add(authorScrollPane);
            add(category);
            add(categoryScrollPane);
            add(update_book);

            setVisible(true);
        }
    }

    private class deleteBookDialog extends JDialog {
        public deleteBookDialog() {
        }

        public void display() throws SQLException {
            if (CurrentBookClicked == null) {
                JOptionPane.showMessageDialog(null, "Please select a book to delete", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Book (ISBN:" + CurrentBookClicked + ") ?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                if (bc.deleteBook(CurrentBookClicked)) {
                    tableModel.removeRow(CurrentBookClickedRow);
                }
            }
            CurrentBookClicked = null;
            CurrentBookClickedRow = -1;
            setVisible(false);
//            deleteBookDialog.removeAll();
            revalidate();
            repaint();
            deleteBookDialog.dispose();
            remove(deleteBookDialog);
        }

    }

    private class AddLoanDialog extends JDialog {
        public AddLoanDialog(Frame owner) {
            super(owner, "Loan Book", true);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(500, 450);
            setLocationRelativeTo(null);
            setLayout(null);
        }

        public void display() throws SQLException {
            if (CurrentBookClicked == null) {
                JOptionPane.showMessageDialog(null, "Please select a book to Loan", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book b = bc.find(CurrentBookClicked);

            if (b == null) return;

            String[] users;

            try (UserDao user = new UserDao()) {
                users = user.getAllUser();
            } catch (Exception throwables) {
                throwables.printStackTrace();
                users = new String[0];
            }

            JLabel book_reference, member, book, exepted_return_date;
            JTextField book_isbn, book_reference_field;
            JDateChooser exepted_return_date_field = new JDateChooser();

            JComboBox<String> user_field;

            book_reference = new JLabel("Book reference :");
            book = new JLabel("Book :");
            member = new JLabel("Member :");

            book_reference_field = new JTextField();
            book_isbn = new JTextField();
            user_field = new JComboBox<>(users);

            Date today = new Date();
            exepted_return_date = new JLabel("Exepted return date :");
            exepted_return_date_field.setBounds(50, 260, 400, 30);
            exepted_return_date_field.setMinSelectableDate(today);

            book_isbn.setText(CurrentBookClicked);

            JButton Loan_book_user = new JButton("Loan Book");

            book.setBounds(50, 50, 400, 30);
            book_isbn.setBounds(50, 80, 400, 30);

            book_reference.setBounds(50, 110, 400, 30);
            book_reference_field.setBounds(50, 140, 400, 30);

            member.setBounds(50, 170, 400, 30);
            user_field.setBounds(50, 200, 400, 30);

            exepted_return_date.setBounds(50, 230, 400, 30);
            exepted_return_date_field.setBounds(50, 260, 400, 30);

            Loan_book_user.setBounds(350, 350, 100, 30);
            Loan_book_user.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            Loan_book_user.setContentAreaFilled(false);
            Loan_book_user.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


            Loan_book_user.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Date selectedDate = exepted_return_date_field.getDate();

                    if (selectedDate == null) {
                        JOptionPane.showMessageDialog(null, "Please select a valid date", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    java.sql.Timestamp today_TimeStamp = new java.sql.Timestamp(today.getTime());
                    java.sql.Timestamp excepted = new java.sql.Timestamp(selectedDate.getTime());
                    String user = extractId(Objects.requireNonNull(user_field.getSelectedItem()).toString());
                    String book_reference = book_reference_field.getText();

                    if (excepted.before(today_TimeStamp)) {
                        JOptionPane.showMessageDialog(null, "Please select a valid date", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (user == null || book_reference == null) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid data", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean valid = false;
                    try {
                        valid = bc.loanBook(CurrentBookClicked, book_reference, user, today_TimeStamp, excepted);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                    if (valid) {
                        JOptionPane.showMessageDialog(null, "Book loaned successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        CurrentBookClicked = null;
                        CurrentBookClickedRow = -1;
                        setVisible(false);
                        loanBookDialog.dispose();
                        revalidate();
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error when loan a book", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    CurrentBookClicked = null;
                    CurrentBookClickedRow = -1;
                    setVisible(false);
                    loanBookDialog.dispose();
                    revalidate();
                    repaint();
                }
            });


            add(Loan_book_user);
            add(user_field);
            add(book_reference_field);
            add(book_isbn);
            add(book);
            add(member);
            add(book_reference);
            add(exepted_return_date_field);

            setVisible(true);
        }
    }

    private class returnloanBookDialog extends JDialog {
        public returnloanBookDialog(){

        }

        public void display() throws Exception {
            if (CurrentBookClicked == null) {
                JOptionPane.showMessageDialog(null, "Please select a book to delete", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Book (ISBN:" + CurrentBookClicked + ") ?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                if (bc.returnBook(CurrentBookClicked,CurrentCnieClicked,CurrentBookReferenceClicked)) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String strDate = timestamp.toLocalDateTime().format(formatter);
                    tableModel.setValueAt(strDate,CurrentBookClickedRow,7);

                    JOptionPane.showMessageDialog(null, "Book returned successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            CurrentBookClicked = null;
            CurrentBookClickedRow = -1;
            setVisible(false);
            revalidate();
            repaint();
            deleteBookDialog.dispose();
            remove(deleteBookDialog);
        }
    }

    private String extractId(String authorString) {
        String[] parts = authorString.split(",");

        if (parts.length == 2) {
            try {
                return parts[0].trim();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private String concatenateTexts(String[] textsWithId) {
        StringBuilder concatenatedText = new StringBuilder();

        for (String text : textsWithId) {
            String[] parts = text.split(",");
            if (parts.length == 2) {
                if (concatenatedText.length() > 0) {
                    concatenatedText.append(", ");
                }
                concatenatedText.append(parts[1].trim());
            }
        }

        return concatenatedText.toString();
    }

    private void removeAllRowFromTable() {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

    private void addBooksToTable(List<Book> books) {
        removeAllRowFromTable();
        for (Book book : books) {
            String isbn = book.getIsbn();
            String title = book.getTitle();
            int Pages = book.getPages();
            String Edition = book.getEdition();
            int Quantity = book.getQuantities();
            String Language = book.getLanguage().toString();
            String Description = book.getDescription();
            String author = book.getAuthors().stream().map(Author::getFullName).collect(Collectors.joining(", "));
            String category = book.getCategories().stream().map(Category::getCat).collect(Collectors.joining(", "));

            tableModel.addRow(new Object[]{isbn, title, Pages, Edition, Quantity, Language, author, category, Description});
        }

        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookTable.rowAtPoint(e.getPoint());
                Object cellValue = bookTable.getValueAt(row, 0);
                CurrentBookClicked = cellValue.toString();
                CurrentBookClickedRow = row;
            }
        });
    }

    private void addBorrowedToTable(List<Loan> books) {
        removeAllRowFromTable();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Loan book : books) {
            String isbn = book.getIsbn();
            String title = book.getTitle();
            String Book_reference = book.getBook_reference();
            String Loan_date = (book.getLoan_date() != null) ? book.getLoan_date().toLocalDateTime().format(formatter) : "N/A";
            String Excepted_return_date = (book.getExpected_return_date() != null) ? book.getExpected_return_date().toLocalDateTime().format(formatter) : "N/A";
            String Return_date = (book.getReturn_date() != null) ? book.getReturn_date().toLocalDateTime().format(formatter) : "N/A";
            String Requested_date = (book.getRequested_at() != null) ? book.getRequested_at().toLocalDateTime().format(formatter) : "N/A";
            String Cnie = (book.getUser() != null) ? book.getUser().getCnie() : "N/A";
            String Name = (book.getUser() != null) ? book.getUser().getFullName() : "N/A";

            tableModel.addRow(new Object[]{Cnie, Name, isbn, title, Book_reference, Loan_date, Excepted_return_date, Return_date, Requested_date});
        }

        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookTable.rowAtPoint(e.getPoint());
                Object cellValue = bookTable.getValueAt(row, 2);
                CurrentBookClicked = cellValue.toString();

                cellValue = bookTable.getValueAt(row, 4);
                CurrentBookReferenceClicked = cellValue.toString();

                cellValue = bookTable.getValueAt(row, 0);
                CurrentCnieClicked = cellValue.toString();

                CurrentBookClickedRow = row;
            }
        });
    }

    private void addLostToTable(List<Lost> books) {
        removeAllRowFromTable();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

////        new DefaultTableModel(new String[]{"isbn","Book reference", "title", "lost Date", "Description", "actual status"}, 0);
//
//        for (Lost book : books) {
//            String isbn = book.getIsbn();
//            String title = book.getTitle();
//            String Book_reference = book.getBook_reference();
//            String Loan_date = (book.getLoan_date() != null) ? book.getLoan_date().toLocalDateTime().format(formatter) : "N/A";
//            String Excepted_return_date = (book.getExpected_return_date() != null) ? book.getExpected_return_date().toLocalDateTime().format(formatter) : "N/A";
//            String Return_date = (book.getReturn_date() != null) ? book.getReturn_date().toLocalDateTime().format(formatter) : "N/A";
//            String Requested_date = (book.getRequested_at() != null) ? book.getRequested_at().toLocalDateTime().format(formatter) : "N/A";
//            String Cnie = (book.getUser() != null) ? book.getUser().getCnie() : "N/A";
//            String Name = (book.getUser() != null) ? book.getUser().getFullName() : "N/A";
//
//            tableModel.addRow(new Object[]{Cnie, Name, isbn, title, Book_reference, Loan_date, Excepted_return_date, Return_date, Requested_date});
//        }

        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookTable.rowAtPoint(e.getPoint());
                Object cellValue = bookTable.getValueAt(row, 2);
                CurrentBookClicked = cellValue.toString();

                cellValue = bookTable.getValueAt(row, 4);
                CurrentBookReferenceClicked = cellValue.toString();

                cellValue = bookTable.getValueAt(row, 0);
                CurrentCnieClicked = cellValue.toString();

                CurrentBookClickedRow = row;
            }
        });
    }

    private void setButtons(JButton butt) {
        butt.setBorderPainted(false);
        butt.setHorizontalAlignment(SwingConstants.LEFT);
        butt.setContentAreaFilled(false);
        butt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Font police = new Font("SansSerif", Font.BOLD, 15);
        butt.setFont(police);
    }

}
