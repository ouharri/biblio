package com.biblio;

import com.biblio.dao.BookDao;
import com.biblio.dao.LoanDao;
import com.biblio.dao.UserDao;
import com.biblio.app.model.Book;
import com.biblio.app.model.User;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {


        BookDao book = new BookDao();
        book.setIsbn("90");

        book.read();
        System.out.println(book.toString());

        try (UserDao user = new UserDao()) {

//            book.setBook("98790", 15, 100, "Le titre du livre 2", "1", "fr", "La description du livre");
            user.setUser(
                    "outmane",
                    "ouharri",
                    "ouharrioutman@gmail.com",
                    "687674987398749",
                    "hgrkgrjgrlg",
                    "678687399298"
            );

            if (user.create()) {
                System.out.println("L'utilisateur a été inséré avec succès !" + user.toString());
            } else {
                System.out.println("Erreur lors de l'insertion de l'utilisateur.");
            }

            book = new BookDao();

            LoanDao loan = new LoanDao();

            book.setIsbn("90");

            book.read();

            loan.setLoan(
                    (Book) book,
                    (User) user
            );

            if (loan.create()) {
                System.out.println("Le prêt a été inséré avec succès !" + loan.toString());
            } else {
                System.out.println("Erreur lors de l'insertion du prêt.");
            }


//            System.out.println(((Book)book.getBookWithDetails()).toString());

//            if (book.delete()) {
//                System.out.println("Le livre a été inséré avec succès !" + book.toString());
//            } else {
//                System.out.println("Erreur lors de l'insertion du livre.");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}