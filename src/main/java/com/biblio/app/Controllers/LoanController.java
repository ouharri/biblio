package com.biblio.app.Controllers;
import com.biblio.app.Exceptions.NoQuantityBookException;
import com.biblio.app.Models.Book;
import com.biblio.app.Models.User;
import com.biblio.dao.BookDao;
import com.biblio.dao.LoanDao;

import java.sql.SQLException;

public class LoanController {

    BookDao BookDao = new BookDao();
    LoanDao loanDao = new LoanDao();


    public LoanController() throws SQLException {
    }

    public boolean createLoan(String isbn,String book_reference,String cnie,java.sql.Timestamp loan_date,java.sql.Timestamp expected_return_date) throws Exception {
        if(BookDao.existsBookQuantity(isbn)){
            return this.loanDao.insert(isbn,book_reference,cnie,loan_date,expected_return_date) != null;
        } else {
            throw new NoQuantityBookException();
        }
    }

}
