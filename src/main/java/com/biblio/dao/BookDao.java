package com.biblio.dao;

import com.biblio.app.model.Author;
import com.biblio.app.model.Book;
import com.biblio.app.model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookDao extends Book {

    public boolean save() throws SQLException {
        this.isbn = this.create(this.getBook());
        return this.isbn != null;
    }

    public boolean create() throws SQLException {
        Map<String, String> bookData = this.getBook();

        this.beginTransaction();

        if (super.create(bookData) != null) {
            this.rollbackTransaction();
            return false;
        }

        this.commitTransaction();


        return true;
    }

    public Book getBookWithDetails() {

        boolean flag = true;
        try {
            String query = "SELECT b.isbn, b.quantities, b.pages, b.title, b.edition, b.language, b.description,a.id, a.firstName, a.lastName, c.category " +
                    "FROM books b " +
                    "LEFT JOIN books_authors ba ON b.isbn = ba.book " +
                    "LEFT JOIN authors a ON ba.author = a.id " +
                    "LEFT JOIN categories_books cb ON b.isbn = cb.book " +
                    "LEFT JOIN categories c ON cb.category = c.id " +
                    "WHERE b.isbn = ?";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, this.getIsbn());

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Author> authors = new ArrayList<>();
            List<Category> categories = new ArrayList<>();

            while (resultSet.next()) {
                if(flag){
                    this.setBook(
                            resultSet.getString("isbn"),
                            resultSet.getInt("quantities"),
                            resultSet.getInt("pages"),
                            resultSet.getString("title"),
                            resultSet.getString("edition"),
                            resultSet.getString("language"),
                            resultSet.getString("description")
                    );
                    flag = false;
                }

                int authorId = resultSet.getInt("id");
                String authorFirstName = resultSet.getString("firstName");
                String authorLastName = resultSet.getString("lastName");

                if (authorFirstName != null && authorLastName != null && authorId != 0) {
                    Author author = (Author) new AuthorDao();
                    author.setAuthor(
                            authorId,
                            authorFirstName,
                            authorLastName
                    );
                    authors.add(author);
                }

                String categoryStr = resultSet.getString("category");
                String categoryDescription = resultSet.getString("description");

                if (categoryStr != null) {
                    Category category = (Category) new CategoryDao();
                    category.setCategory(categoryStr);
                    category.setDescription(categoryDescription);
                    categories.add(category);
                }
            }

            this.hasAuthors(authors.toArray(new Author[0]));
            this.hasCategories(categories.toArray(new Category[0]));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    public Book read() {
        Map<String, String> Book = super.read(new String[]{this.isbn});

        System.out.println(Book.toString());

        this.setBook(
                Book.get("isbn"),
                Integer.parseInt(Book.get("quantities")),
                Integer.parseInt(Book.get("pages")),
                Book.get("title"),
                Book.get("edition"),
                Book.get("language"),
                Book.get("description")
        );

        return this;
    }

    public boolean update() {
        return super.update(this.getBook(), new String[]{this.isbn});
    }

    public boolean delete() {
        return super.softDelete(new String[]{this.isbn});
    }

}
