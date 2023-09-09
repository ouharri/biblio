package com.biblio.dao;

import com.biblio.app.Models.Log;
import com.biblio.libs.Model;

public final class LogDao extends Model {

    public LogDao() {
        super("logs", new String[]{"id"});
    }

}
