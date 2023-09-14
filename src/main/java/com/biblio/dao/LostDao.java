package com.biblio.dao;

import com.biblio.app.Enums.Language;
import com.biblio.app.Enums.LostStatus;
import com.biblio.app.Models.Author;
import com.biblio.app.Models.Book;
import com.biblio.app.Models.Category;
import com.biblio.app.Models.Lost;
import com.biblio.libs.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class LostDao extends Model {


    public LostDao() {
        super("lost_books", new String[]{"book","book_reference","lost_date"});
    }

    public Lost add(String isbn,String book_reference,String description) throws Exception {

        Book book;
        Lost lost = new Lost();

        try (BookDao bookDao = new BookDao()) {
            book = bookDao.find(isbn);
        }

        lost.setBook(
                book.getIsbn(),
                book.getQuantities(),
                book.getPages(),
                book.getTitle(),
                book.getEdition(),
                book.getLanguage(),
                book.getDescription()
        );

        lost.setLost(
                book_reference,
                description
        );

        Map<String, String> LostData = lost.getLost();

        if (super.create(LostData) != null) {
            return lost;
        }

        return null;

    }

    public Lost updateLost(String isbn, String book_reference,String description, LostStatus status,java.sql.Timestamp requested_ad) throws Exception {

        Book book;
        Lost lost = new Lost();

        try (BookDao bookDao = new BookDao()) {
            book = bookDao.find(isbn);
        }

        lost.setBook(
                book.getIsbn(),
                book.getQuantities(),
                book.getPages(),
                book.getTitle(),
                book.getEdition(),
                book.getLanguage(),
                book.getDescription()
        );

        lost.setLost(
                book_reference,
                description,
                status
        );

        Map<String, String> LostData = lost.getLost();

        if (super.update(LostData, new String[]{isbn, book_reference, String.valueOf(requested_ad)})) {
            return lost;
        }

        return null;

    }

    public List<Lost> getLostBooks() {
        List<Lost> lostBooks = new ArrayList<>();

        try {
            String query = "SELECT DISTINCT " +
                    "    b.isbn, " +
                    "    b.title, " +
                    "    c.category, " +
                    "    c.description AS category_description, " +
                    "    lb.loast_date, " +
                    "    lb.book_reference, " +
                    "    lb.description, " +
                    "    lb.actual_statut " +
                    "FROM books b " +
                    "LEFT JOIN books_authors ba ON b.isbn = ba.book " +
                    "LEFT JOIN authors a ON ba.author = a.id " +
                    "LEFT JOIN categories_books cb ON b.isbn = cb.book " +
                    "LEFT JOIN categories c ON cb.category = c.id " +
                    "INNER JOIN lost_books lb ON b.isbn = lb.book " +
                    "WHERE b.delete_at IS NULL " +
                    "AND lb.actual_statut = 'STILL_LOST' " +
                    "ORDER BY b.isbn;";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            String currentIsbn = null;
            Lost currentBook = null;
            List<Author> authors = new ArrayList<>();
            List<Category> categories = new ArrayList<>();

            while (resultSet.next()) {
                String isbn = resultSet.getString("isbn");

                if (currentIsbn == null || !currentIsbn.equals(isbn)) {

                    if (currentBook != null) {
                        currentBook.hasAuthors(authors);
                        currentBook.hasCategories(categories);
                        lostBooks.add(currentBook);
                    }

                    currentBook = new Lost();
                    currentBook.setBook(
                            isbn,
                            resultSet.getInt("quantities"),
                            resultSet.getInt("pages"),
                            resultSet.getString("title"),
                            resultSet.getString("edition"),
                            Language.valueOf(resultSet.getString("language")),
                            resultSet.getString("description")
                    );

                    currentBook.setLost(
                            resultSet.getString("book_reference"),
                            resultSet.getDate("loast_date"),
                            resultSet.getString("description"),
                            LostStatus.valueOf(resultSet.getString("actual_statut"))
                    );

                    currentIsbn = isbn;
                }

                lostBooks.add(currentBook);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lostBooks;
    }


}
