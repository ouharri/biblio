package com.biblio.dao;

import com.biblio.app.model.Loan;

import java.sql.SQLException;

public class LoanDao extends Loan {

    public boolean create() throws SQLException {
        return super.create(this.getLoan()) != null;
    }


}
