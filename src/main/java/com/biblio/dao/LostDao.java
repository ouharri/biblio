package com.biblio.dao;

import com.biblio.app.Models.Book;
import com.biblio.app.Models.Lost;
import com.biblio.libs.Model;

public final class LostDao extends Model {

    private Lost lost = null;

    public LostDao() {
        super("lost_books", new String[]{"id"});
        this.lost = new Lost();
    }


}
