package com.biblio;

import com.biblio.app.controller.AuthenticationController;

public class App {
    public static void main(String[] args) throws Exception {


//        BookDao book = new BookDao();
//        book.setIsbn("90");
//
//        book.read();



//        UserDao user = new UserDao();
//            user.setEmail("ouharrioutman@gmail.com");
//            System.out.println( ((User)user.getByEmailWithRoles()).toString() );


        AuthenticationController user = new AuthenticationController();

        if(user.authenticateUser(
                "483957485",
                "687674987398749"
        )){
            System.out.println("L'utilisateur a été inséré avec succès !");
        } else {
            System.out.println("Erreur lors de l'insertion de l'utilisateur.");
        }



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

    }
}