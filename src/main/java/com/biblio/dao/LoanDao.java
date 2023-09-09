package com.biblio.dao;

import com.biblio.app.Models.Book;
import com.biblio.app.Models.Loan;
import com.biblio.app.Models.User;
import com.biblio.libs.Model;

import java.sql.*;
import java.util.Map;

public final class LoanDao extends Model {

    private Loan loan = null;

    public LoanDao() throws SQLException {
        super("borrowed_books", new String[]{"id"});
        this._softDelete = false;
        this.loan = new Loan();
    }

    public Loan insert(String isbn, String book_reference, String cnie, java.sql.Timestamp loan_date, java.sql.Timestamp expected_return_date) throws Exception {

        User user;
        Book book;

        Loan loan = new Loan();

        try (UserDao userDao = new UserDao()) {
            user = userDao.find(cnie);
        }

        try (BookDao bookDao = new BookDao()) {
            book = bookDao.find(isbn);
        }

        loan.setBook(
                book.getIsbn(),
                book.getQuantities(),
                book.getPages(),
                book.getTitle(),
                book.getEdition(),
                book.getLanguage(),
                book.getDescription()
        );

        loan.setLoan(
                user,
                book_reference,
                loan_date,
                expected_return_date
        );

        System.out.println(loan.toString());

        Map<String, String> LoanData = loan.getLoan();

        System.out.println("\n"+LoanData.toString()+"\n");

        System.out.println(super.create(LoanData));

//        if (super.create(LoanData) != null) {
//            return loan;
//        }

        return null;
    }


}
