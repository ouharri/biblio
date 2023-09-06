package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.Model;


@Data
@EqualsAndHashCode(callSuper = true)
public abstract class WaitingList extends Model {


    protected int id;
    protected java.sql.Date create_at;

    protected Loan loan;
    protected Book book;
    protected User user;


    public WaitingList() {
        super("waitinglists", new String[]{"id"});
    }
}