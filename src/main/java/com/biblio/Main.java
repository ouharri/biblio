package com.biblio;

import com.biblio.model.Books;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {


        Books books = new Books();

        // Créez un objet Map pour contenir les données du livre que vous souhaitez insérer
        Map<String, String> bookData = new HashMap<>();
        bookData.put("title", "Le titre du livre");
        bookData.put("isbn", "123456789");
        bookData.put("quantities", "10");
        bookData.put("pages", "100");
        bookData.put("edition", "1");
        bookData.put("language", "fr");
        bookData.put("description", "La description du livre");

        // Utilisez la méthode create pour insérer les données dans la table
        boolean success = books.create(bookData);

        if (success) {
            System.out.println("Le livre a été inséré avec succès !");
        } else {
            System.out.println("Erreur lors de l'insertion du livre.");
        }

    }
}