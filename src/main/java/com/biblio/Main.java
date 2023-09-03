package com.biblio;

import com.biblio.model.Book;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {

        try (Book book = new Book()) {

            book.setBook(123456789, 14, 100, "Le titre du livre 2", "1", "fr", "La description du livre");

            if (book.save()) {
                System.out.println("Le livre a été inséré avec succès !" + book.toString());
                Map<String, String> bookData = new HashMap<>();
                bookData.put("isbn", "123456888");
                book.update(book.getIsbn(),bookData);
            } else {
                System.out.println("Erreur lors de l'insertion du livre.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}