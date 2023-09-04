package com.biblio.model;

import com.biblio.dao.WaitingListDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class waitingList extends WaitingListDao {

    public int id;
    public int book;
    public int user;
    public int loan;
    public java.sql.Date create_at;
}
