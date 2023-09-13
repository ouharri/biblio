package com.biblio.view.Admin;

import com.biblio.app.Controllers.AnalysisController;
import com.biblio.app.Controllers.BookController;
import com.biblio.app.Enums.Language;
import com.biblio.app.Models.*;
import com.biblio.dao.AuthorDao;
import com.biblio.dao.CategoryDao;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class index extends JFrame implements ActionListener {

    private User User;
    private BookController bc;
    private AnalysisController ac;
    private JButton statistic, all_book, search, available_book, brr_book, lost_book, user, awaiting_list, logout;
    private JButton add_book_button, edit_book_button, delete_book_button, search_book_button;
    private JTable bookTable;
    private JScrollPane scrollPane;
    private JLabel book_label;
    private DefaultTableModel tableModel;
    private JTextField search_field;

    String CurrentBookClicked = null;
    int CurrentBookClickedRow = -1;

    private AddBookDialog addBookDialog, updateBookDialog;
    private EditBookDialog editBookDialog;
    private deleteBookDialog deleteBookDialog;


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
        ImageIcon Borrowed_icon = new ImageIcon("assets/icon/borrow.png");
        ImageIcon Lost_icon = new ImageIcon("assets/icon/lost.png");
        ImageIcon Users_icon = new ImageIcon("assets/icon/user.png");
        ImageIcon Awaiting_icon = new ImageIcon("assets/icon/waiting.png");
        ImageIcon Logout_icon = new ImageIcon("assets/icon/logout.png");

        statistic = new JButton("  Statistic", Statistic_icon);
        all_book = new JButton("  All books", Book_icon);
        search = new JButton(" Search", Search_icon);
        available_book = new JButton("  Available books", Available_icon);
        brr_book = new JButton("  Borrowed books", Borrowed_icon);
        lost_book = new JButton("  Lost books", Lost_icon);
        user = new JButton("  Users", Users_icon);
        awaiting_list = new JButton("  Awaiting list", Awaiting_icon);
        logout = new JButton("  Logout", Logout_icon);

        setButtons(statistic);
        setButtons(all_book);
        setButtons(search);
        setButtons(available_book);
        setButtons(brr_book);
        setButtons(lost_book);
        setButtons(user);
        setButtons(awaiting_list);
        setButtons(logout);

        int y = 120;
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
        brr_book.setBounds(50, 320 + y, 250, 30);
        lost_book.setBounds(50, 375 + y, 250, 30);
        user.setBounds(50, 430 + y, 250, 30);
        awaiting_list.setBounds(50, 485 + y, 250, 30);
        logout.setBounds(50, 540 + y, 250, 30);

        statistic.addActionListener(this);
        search.addActionListener(this);
        all_book.addActionListener(this);
        available_book.addActionListener(this);
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
        add(brr_book);
        add(lost_book);
        add(user);
        add(awaiting_list);
        add(logout);
        setVisible(true);

        // ouharrioutman@gmail.com
        // 68767498739879
    }


    private void refresh() {
        if (book_label != null) remove(book_label);
        if (add_book_button != null) remove(add_book_button);
        if (edit_book_button != null) remove(edit_book_button);
        if (delete_book_button != null) remove(delete_book_button);
        if (scrollPane != null) remove(scrollPane);

        if (search_field != null) remove(search_field);
        if (search_book_button != null) remove(search_book_button);


        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == statistic){
            refresh();
            displayStatistics();
        }else if (e.getSource() == all_book) {
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
        } else if (e.getSource() == brr_book) {
            refresh();
        } else if (e.getSource() == lost_book) {
            refresh();
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

        addBookDialog = new AddBookDialog(this);
        editBookDialog = new EditBookDialog(this);
        deleteBookDialog = new deleteBookDialog();

        add_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addBookDialog.setVisible(true);
                    }
                }
        );
        edit_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
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

        addBookDialog = new AddBookDialog(this);
        editBookDialog = new EditBookDialog(this);
        deleteBookDialog = new deleteBookDialog();

        add_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addBookDialog.setVisible(true);
                    }
                }
        );
        edit_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
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

        editBookDialog = new EditBookDialog(this);
        deleteBookDialog = new deleteBookDialog();

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

    private void displayStatistics() {
        book_label = new JLabel("Statistics :");
        book_label.setFont(new Font("Arial", Font.PLAIN, 25));
        book_label.setBounds(400, 140, 300, 50);

        int TotalBooks = ac.getTotalBooks();
        int TotalAuthors = ac.getTotalAuthors();
        int TotalCategories = ac.getTotalCategories();
        int TotalUsers = ac.getTotalUsers();
        int TotalBorrowedBooks = ac.getTotalBorrowedBooks();
        int TotalAvailableBooks = ac.getTotalAvailableBooks();
        int TotalLostBooks = ac.getTotalLostBooks();
        int TotalBorrowedNotReturnedBooks = ac.getTotalBorrowedNotReturnedBooks();
        int TotalBorrowedUsers = ac.getTotalBorrowedUsers();
        int TotalAvailableUsers = ac.getTotalAvailableUsers();
        int TotalStock = ac.getTotalStock();
        int TotalReturnedBooks = ac.getTotalReturnedBooks();





        add(book_label);
        revalidate();
        repaint();
    }


    private class AddBookDialog extends JDialog {
        public AddBookDialog(Frame owner) {
            super(owner, "Add Book", true);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(500, 720);
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
            author_field.setBounds(50, 500, 400, authors_size);

            category.setBounds(50, 500 + authors_size, 400, 30);
            category_field.setBounds(50, 530 + authors_size, 400, categories_size);

            add_book.setBounds(350, 630, 100, 30);
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

                    int[] authorIds = new int[selectedAuthors.length];
                    for (int i = 0; i < selectedAuthors.length; i++) {
                        authorIds[i] = extractId(selectedAuthors[i]);
                    }

                    int[] categoryIds = new int[selectedCategories.length];
                    for (int i = 0; i < selectedCategories.length; i++) {
                        categoryIds[i] = extractId(selectedCategories[i]);
                    }

                    try {
                        Book b = bc.addBook(isbn, title, description, language, quantity, pages, edition, authorIds, categoryIds);
                        if (b != null) {
                            tableModel.addRow(new Object[]{isbn, title, pages, edition, quantity, language, concatenateTexts(selectedAuthors), concatenateTexts(selectedCategories), description});
                            JOptionPane.showMessageDialog(null, "Book added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            setVisible(false);
                            addBookDialog.removeAll();
                            revalidate();
                            repaint();
                            addBookDialog.dispose();
                            remove(addBookDialog);
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
            add(author_field);
            add(category);
            add(category_field);
            add(add_book);
        }
    }

    private class EditBookDialog extends JDialog {
        public EditBookDialog(Frame owner) {
            super(owner, "Edit Book", true);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(500, 720);
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

            int authors_size = 20 * authors.length;
            int categories_size = 20 * categories.length;

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

            System.out.println(b.getEdition());

            isbn_field.setText(b.getIsbn());
            quantity_field.setText(String.valueOf(b.getQuantities()));
            pages_field.setText(String.valueOf(b.getPages()));
            title_field.setText(b.getTitle());
            edition_field.setText(b.getEdition());
            description_field.setText(b.getDescription());

            language_field = new JComboBox<>(Language.values());
            author_field = new JList<>(authors);
            category_field = new JList<>(categories);

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
            edition_field.setText(b.getEdition());

            quantity.setBounds(50, 290, 400, 30);
            quantity_field.setBounds(50, 320, 400, 30);

            language.setBounds(50, 350, 400, 30);
            language_field.setBounds(50, 380, 400, 30);

            description.setBounds(50, 410, 400, 30);
            description_field.setBounds(50, 440, 400, 30);

            author.setBounds(50, 470, 400, 30);
            author_field.setBounds(50, 500, 400, authors_size);

            category.setBounds(50, 500 + authors_size, 400, 30);
            category_field.setBounds(50, 530 + authors_size, 400, categories_size);

            update_book.setBounds(350, 630, 100, 30);
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

                    int[] authorIds = new int[selectedAuthors.length];
                    for (int i = 0; i < selectedAuthors.length; i++) {
                        authorIds[i] = extractId(selectedAuthors[i]);
                    }

                    int[] categoryIds = new int[selectedCategories.length];
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
                            editBookDialog.removeAll();
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
            add(author_field);
            add(category);
            add(category_field);
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
        }

    }

    private int extractId(String authorString) {
        String[] parts = authorString.split(",");

        if (parts.length == 2) {
            try {
                return Integer.parseInt(parts[0].trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return -1;
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

    private void setButtons(JButton butt) {
        butt.setBorderPainted(false);
        butt.setHorizontalAlignment(SwingConstants.LEFT);
        butt.setContentAreaFilled(false);
        butt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Font police = new Font("SansSerif", Font.BOLD, 15);
        butt.setFont(police);

    }


}
