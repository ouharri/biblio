package com.biblio.view.Admin;

import com.biblio.app.Controllers.BookController;
import com.biblio.app.Enums.Language;
import com.biblio.app.Models.*;
import com.biblio.dao.AuthorDao;
import com.biblio.dao.CategoryDao;
import com.biblio.view.core.ButtonEditor;
import com.biblio.view.core.ButtonRenderer;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class index extends JFrame implements ActionListener {

    private User User;
    private BookController bc; // Book Controller

    private List<JButton> deleteTableButtons = new ArrayList<JButton>(), updateTableButtons = new ArrayList<JButton>();
    private JButton statistic, all_book, available_book, brr_book, lost_book, user, awaiting_list, logout;
    private JButton add_book_button, edit_book_button, delete_book_button;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    String CurrentBookClicked = "";

    private AddBookDialog addBookDialog;


    public index(User u) throws SQLException {
        bc = new BookController();
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
        ImageIcon Available_icon = new ImageIcon("assets/icon/available.png");
        ImageIcon Borrowed_icon = new ImageIcon("assets/icon/borrow.png");
        ImageIcon Lost_icon = new ImageIcon("assets/icon/lost.png");
        ImageIcon Users_icon = new ImageIcon("assets/icon/user.png");
        ImageIcon Awaiting_icon = new ImageIcon("assets/icon/waiting.png");
        ImageIcon Logout_icon = new ImageIcon("assets/icon/logout.png");

        statistic = new JButton("  Statistic", Statistic_icon);
        all_book = new JButton("  All books", Book_icon);
        available_book = new JButton("  Available books", Available_icon);
        brr_book = new JButton("  Borrowed books", Borrowed_icon);
        lost_book = new JButton("  Lost books", Lost_icon);
        user = new JButton("  Users", Users_icon);
        awaiting_list = new JButton("  Awaiting list", Awaiting_icon);
        logout = new JButton("  Logout", Logout_icon);

        setButtons(statistic);
        setButtons(all_book);
        setButtons(available_book);
        setButtons(brr_book);
        setButtons(lost_book);
        setButtons(user);
        setButtons(awaiting_list);
        setButtons(logout);

        int y = 120;
        int y2 = 10;

        System.out.println(screenHeight);

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
        available_book.setBounds(50, 210 + y, 200, 30);
        brr_book.setBounds(50, 265 + y, 250, 30);
        lost_book.setBounds(50, 320 + y, 250, 30);
        user.setBounds(50, 375 + y, 250, 30);
        awaiting_list.setBounds(50, 430 + y, 250, 30);
        logout.setBounds(50, 485 + y, 250, 30);

        statistic.addActionListener(this);
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

    private void displayBook() {
        JLabel book_label = new JLabel("All Books :");
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

        add_book_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addBookDialog.setVisible(true);
                    }
                }
        );
        edit_book_button.addActionListener(this);
        delete_book_button.addActionListener(this);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(400, 190, 1000, 700);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        List<Book> books = bc.getAlBooks();

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
            }
        });


        add(book_label);
        add(add_book_button);
        add(edit_book_button);
        add(delete_book_button);
        add(scrollPane);

        revalidate();
        repaint();
    }


    private int incrementY(int y) {
        y = y + 100;
        return y + 100;
    }

    private JButton setButtons(JButton butt) {
        butt.setBorderPainted(false);
        butt.setHorizontalAlignment(SwingConstants.LEFT);
        butt.setContentAreaFilled(false);
        butt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Font police = new Font("SansSerif", Font.BOLD, 15);
        butt.setFont(police);

        return butt;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("jnf");
        if (e.getSource() == all_book) {
            displayBook();
        } else if (e.getSource() == logout) {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new com.biblio.view.Authentication.Signing();
            }

        } else {
            for (JButton updateButton : updateTableButtons) {
                if (e.getSource() == updateButton) {
                    System.out.println("update");
                    // Ajoutez ici la logique de mise à jour
                }
            }
            for (JButton deleteButton : deleteTableButtons) {
                if (e.getSource() == deleteButton) {
                    System.out.println("delete");
                    // Ajoutez ici la logique de mise à jour
                }
            }
        }
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
            JButton cancel = new JButton("Cancel");

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

            category.setBounds(50, 530, 400, 30);
            category_field.setBounds(50, 560, 400, categories_size);

            add_book.setBounds(350, 630, 100, 30);

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
                        if(b!=null){
                            tableModel.addRow(new Object[]{isbn, title, pages, edition, quantity, language, concatenateTexts(selectedAuthors), concatenateTexts(selectedCategories), description});
                            JOptionPane.showMessageDialog(null, "Book added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            revalidate();
                            repaint();
                            addBookDialog.dispose();
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
//            add(cancel);
        }
    }

    private int extractId(String authorString) {
        String[] parts = authorString.split(",");

        if (parts.length == 2) {
            try {
                int id = Integer.parseInt(parts[0].trim());
                return id;
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





}
