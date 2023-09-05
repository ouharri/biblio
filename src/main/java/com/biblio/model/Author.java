package com.biblio.model;

import com.biblio.dao.AuthorDao;
import com.biblio.dao.BooksAuthorsDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class Author extends AuthorDao {

	public int id;
//	private Book[] book = null;
	public String firstName;
	public String lastName;




	public Author[] getAuthorByBook(Book book) throws Exception {


		try(BooksAuthorsDao BooksAuthorsDao = new BooksAuthorsDao()){
			List<Map<String, String>> resultList = BooksAuthorsDao.readAll("book",book.getIsbn());
			Author[] authors = new Author[resultList.size()];

			for (int i = 0; i < resultList.size(); i++) {
				Map<String, String> rowData = resultList.get(i);
				try(Author author = new Author()) {
					author.setId(Integer.parseInt(rowData.get("id")));
					author.setFirstName(rowData.get("firstName"));
					author.setLastName(rowData.get("lastName"));
					authors[i] = author;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return authors;
		}
	}

}