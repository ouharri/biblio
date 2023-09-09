package com.biblio.dao;

import com.biblio.app.Models.WaitingList;
import com.biblio.libs.Model;

public final class WaitingListDao extends Model {


    public WaitingListDao() {
        super("waitinglists", new String[]{"id"});
    }

}
