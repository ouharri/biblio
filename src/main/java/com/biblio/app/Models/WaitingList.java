package com.biblio.app.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.Model;


@Data
@EqualsAndHashCode(callSuper = false)
public class WaitingList{


    protected int id;
    protected java.sql.Date create_at;

    protected User user;
    protected Loan loan;
    protected Book book;


}