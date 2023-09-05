package com.biblio.model;

import com.biblio.dao.BookDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends BookDao {

	public int quantities;
	public int pages;
	public String isbn;
	public String title;
	public String edition;
	public String language;
	public String description;
	public java.sql.Timestamp delete_at;

	public Category[] category;
	public Author[] author;

	public void setBook(String isbn, int quantities, int pages, String title, String edition, String language, String description) {
		this.isbn = isbn;
		this.quantities = quantities;
		this.pages = pages;
		this.title = title;
		this.edition = edition;
		this.language = language;
		this.description = description;
	}


	public Map<String, String> getBook() {
		Map<String, String> bookData = new HashMap<>();
		bookData.put("isbn", this.isbn);
		bookData.put("quantities", String.valueOf(this.quantities));
		bookData.put("pages", String.valueOf(this.pages));
		bookData.put("title", this.title);
		bookData.put("edition", this.edition);
		bookData.put("language", this.language);
		bookData.put("description", this.description);
		return bookData;
	}


	@Override
	public boolean create() throws SQLException {
		Map<String, String> bookData = this.getBook();

		this.beginTransaction();

		if (!super.create(bookData)) {
			this.rollbackTransaction();
			return false;
		}

		this.commitTransaction();


		return true;
	}

	public boolean save() throws SQLException {
		return this.create(this.getBook());
	}

	public Book hasAuthor(Author[] author) {
		this.author = author;
		return this;
	}

	public Book hasCategory(Category[] category) {
		this.category = category;
		return this;
	}

	public Book withAuthor() {
		try( Author author = new Author() ) {
			this.author = author.getAuthorByBook(this);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public Book getBookWithDetails() {

		boolean flag = true;
		try {
			String query = "SELECT b.isbn, b.quantities, b.pages, b.title, b.edition, b.language, b.description, a.firstName, a.lastName, c.category " +
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

			System.out.println(resultSet.toString());
			while (resultSet.next()) {
				if(flag){
					this.setIsbn(resultSet.getString("isbn"));
					this.setQuantities(resultSet.getInt("quantities"));
					this.setPages(resultSet.getInt("pages"));
					this.setTitle(resultSet.getString("title"));
					this.setEdition(resultSet.getString("edition"));
					this.setLanguage(resultSet.getString("language"));
					this.setDescription(resultSet.getString("description"));
					flag = false;
				}

				int authorId = resultSet.getInt("id");
				String authorFirstName = resultSet.getString("firstName");
				String authorLastName = resultSet.getString("lastName");

				if (authorFirstName != null && authorLastName != null && authorId != 0) {
					Author author = new Author();
					author.setId(authorId);
					author.setFirstName(authorFirstName);
					author.setLastName(authorLastName);
					authors.add(author);
				}

				String categoryStr = resultSet.getString("category");
				String categoryDescription = resultSet.getString("description");

				if (categoryStr != null) {
					Category category = new Category();
					category.setCategory(categoryStr);
					category.setDescription(categoryDescription);
					categories.add(category);
				}
			}

            this.setAuthors(authors.toArray(new Author[0]));
            this.setCategories(categories.toArray(new Category[0]));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return this;
	}

	private void setCategories(Category[] categories) {
		this.category = categories;
	}

	private void setAuthors(Author[] authors) {
		this.author = authors;
	}

	@Override
	public Book read() {
		Map<String, String> Book = super.read(new String[]{this.isbn});

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

	@Override
	public boolean update() throws SQLException {
		return super.update(this.getBook(), new String[]{this.isbn});
	}

	@Override
	public boolean delete() {
		return super.softDelete(new String[]{this.isbn});
	}
}