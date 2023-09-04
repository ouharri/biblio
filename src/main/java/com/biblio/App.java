package com.biblio;

import com.biblio.model.Book;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {

        try (Book book = new Book()) {

            book.setBook("98990", 15, 100, "Le titre du livre 2", "1", "fr", "La description du livre");

            if (book.save()) {
                System.out.println("Le livre a été inséré avec succès !" + book.toString());
            } else {
                System.out.println("Erreur lors de l'insertion du livre.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}