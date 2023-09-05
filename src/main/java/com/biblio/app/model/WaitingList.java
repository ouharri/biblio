package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.db;


@Data
@EqualsAndHashCode(callSuper = true)
public abstract class WaitingList extends db {


    public int id;
    public Loan loan;
    public Book book;
    public User user;
    public java.sql.Date create_at;

    public WaitingList() {
        super("waitinglists", new String[]{"id"});
    }
}