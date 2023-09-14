package com.biblio;

import com.biblio.app.Controllers.AuthenticationController;
import com.biblio.app.Enums.Gender;
import com.biblio.core.database;
import com.biblio.dao.UserDao;
import com.biblio.view.Authentication.Signing;

import java.sql.Connection;
import java.sql.SQLException;

public class App implements AutoCloseable{

    private Connection connection = null;

    public App() {
        try {
            this.connection = database.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {


        Signing signin = new Signing();
//        BookDao book = new BookDao();
//        System.out.println(book.search("Le titre du livre djjdn N test").toString());
//        book.setIsbn("90");
//
//        book.read();

//        BookController book = new BookController();
//
//        System.out.println(book.updateBook(
//                "23d00700",
//                "Le titre du livr djjdn N test",
//                "La description du livre apres",
//                Language.Bengali,
//                47,
//                68,
//                null,
//                new int[]{2},
//                new int[]{1}
//        ));

//        System.out.println(book.searchBook("cn").toString());
//
//        System.out.println(book.deleteBook("278s2vf0"));


//        UserDao user = new UserDao();
//        AuthenticationController user = new AuthenticationController();
//            user.setEmail("ouharrioutman@gmail.com");
//            System.out.println( ((User)user.getByEmailWithRoles()).toString() );


//        AuthenticationController user = new AuthenticationController();
//
//        if(user.authenticate(
//                "12345690",
//                "68767498739879"
//        )){
//            System.out.println("L'utilisateur a été inséré avec succès !");
//        } else {
//            System.out.println("Erreur lors de l'insertion de l'utilisateur.");
//        }

//        System.out.println(user.register(
//                "AD333647",
//                "outman",
//                "ouharri",
//                "ouharrioutman@gmail.com",
//                "68767498739879",
//                Gender.valueOf("Male"),
//                "12345690"
//        ).getEmail());

//        System.out.println(new BookController().getBooks().toString());



//        try (UserDao user = new UserDao()) {
//
////            book.setBook("98790", 15, 100, "Le titre du livre 2", "1", "fr", "La description du livre");
//            user.setUser(
//                    "outman",
//                    "ouharri",
//                    "ouharrioutman@gmail.com",
//                    "687674987398749",
//                    "hgrkgrjgrlg",
//                    "678687399298"
//            );
//
//            if (user.create()) {
//                System.out.println("L'utilisateur a été inséré avec succès !" + user.toString());
//            } else {
//                System.out.println("Erreur lors de l'insertion de l'utilisateur.");
//            }
//
//            BookDao book = new BookDao();
//
//            LoanDao loan = new LoanDao();
//
//            book.setIsbn("90");
//
//            book.getBookWithDetails();
//
//            loan.setLoan(
//                    (Book) book,
//                    (User) user
//            );
//
//            if (loan.create()) {
//                System.out.println("Le prêt a été inséré avec succès !" + loan.toString());
//            } else {
//                System.out.println("Erreur lors de l'insertion du prêt.");
//            }
//
//
//         System.out.println(((Book)book.getBookWithDetails()).toString());
//
////            if (book.delete()) {
////                System.out.println("Le livre a été inséré avec succès !" + book.toString());
////            } else {
////                System.out.println("Erreur lors de l'insertion du livre.");
////            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        System.out.println(book.returnBook(
//                "2340700",
//                "AD333647",
//                "1"
//        ));


//        System.out.println(book.loanBook(
//                "2340700",
//                "1",
//                "AD333647",
//                new java.sql.Timestamp(System.currentTimeMillis()),
//                new java.sql.Timestamp(System.currentTimeMillis())
//        ));



    }
    @Override
    public void close() throws Exception {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}