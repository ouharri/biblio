package com.biblio.model;

import com.biblio.dao.WaitingListDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class waitingList extends WaitingListDao {


    public int id;
    public Loans loan;
    public Book book;
    public User user;
    public java.sql.Date create_at;
}