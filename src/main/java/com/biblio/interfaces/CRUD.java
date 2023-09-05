package com.biblio.interfaces;

import java.util.Map;
import java.sql.*;


public interface CRUD {
    String create(Map<String, String> data) throws SQLException;

    Map<String, String> read(String[] ids);

    Map<String, String> read(String columnName, String value);

    boolean update(Map<String, String> data, String[] ids);

    boolean delete(String[] ids);
}
