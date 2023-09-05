package com.biblio.libs;

import com.biblio.core.database;
import com.biblio.interfaces.CRUD;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class db implements AutoCloseable, CRUD {
    protected Connection connection = null;
    protected String _table = null;
    protected String[] _primaryKey = {"id"};
    protected String _foreignKey = null;
    protected Boolean _softDelete = true;
    private boolean inTransaction = false;

    public db(String tableName, String[] primaryKey) {
        this.connection = database.getConnection();
        this._table = tableName;
        if (primaryKey != null && primaryKey.length > 0) {
            this._primaryKey = primaryKey;
        }
    }

    public void beginTransaction() throws SQLException {
        if (!inTransaction) {
            this.connection.setAutoCommit(false);
            this.inTransaction = true;
        }
    }

    public void commitTransaction() throws SQLException {
        if (inTransaction) {
            this.connection.commit();
            this.connection.setAutoCommit(true);
            this.inTransaction = false;
        }
    }

    public void rollbackTransaction() throws SQLException {
        if (inTransaction) {
            this.connection.rollback();
            this.connection.setAutoCommit(true);
            this.inTransaction = false;
        }
    }

    public List<Map<String, String>> getAll() {
        List<Map<String, String>> resultList = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + this._table;

            // Ajoutez la condition pour le soft delete si _softDelete est true
            if (this._softDelete) {
                query += " WHERE delete_at IS NULL";
            }

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    rowData.put(columnName, resultSet.getString(columnName));
                }

                resultList.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    @Override
    public String create(Map<String, String> data) throws SQLException {
        try {
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();

            for (Map.Entry<String, String> entry : data.entrySet()) {
                columns.append(entry.getKey()).append(",");
                values.append("?").append(",");
            }

            columns.setLength(columns.length() - 1);
            values.setLength(values.length() - 1);

            String query = "INSERT INTO " + _table + " (" + columns.toString() + ") VALUES (" + values.toString() + ")";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            int index = 1;
            for (String value : data.values()) {
                preparedStatement.setString(index++, value);
            }

            int rowsAffected = preparedStatement.executeUpdate();

            // Vérifiez s'il y a une clé générée
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Récupérez la clé primaire générée (peut être int ou String)
                    String generatedKey = generatedKeys.getString(1); // Vous pouvez utiliser getInt(1) si c'est un int
                    return generatedKey;
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return null; // Retourne null si l'insertion a échoué ou s'il n'y a pas de clé primaire générée
    }


    @Override
    public Map<String, String> read(String[] ids) {
        try {
            StringBuilder whereClause = new StringBuilder();
            for (int i = 0; i < _primaryKey.length; i++) {
                whereClause.append(_primaryKey[i]).append(" = ?");
                if (i < _primaryKey.length - 1) {
                    whereClause.append(" AND ");
                }
            }

            String query = "SELECT * FROM " + this._table + " WHERE " + whereClause.toString();

            if (this._softDelete) {
                query += " AND delete_at IS NULL";
            }

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            for (int i = 0; i < ids.length; i++) {
                preparedStatement.setString(i + 1, ids[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    rowData.put(columnName, resultSet.getString(columnName));
                }

                return rowData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> read(String columnName, String value) {
        try {
            String query = "SELECT * FROM " + this._table + " WHERE " + columnName + " = ?";

            // Ajoutez la condition pour le soft delete si _softDelete est true
            if (this._softDelete) {
                query += " AND delete_at IS NULL";
            }

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, value);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();

                // Remplir la map avec les données de la ligne
                // Utilisez resultSet.getString("nom_de_la_colonne") pour récupérer les valeurs

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String column = metaData.getColumnName(i);
                    rowData.put(column, resultSet.getString(column));
                }

                return rowData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getColumnCount(String WhereColumnName, String value) {
        System.out.println(this._softDelete);
        try {
            String query = "SELECT count(*) AS count FROM " + this._table + " WHERE " + WhereColumnName + " = ?";

            if (this._softDelete) {
                query += " AND delete_at IS NULL";
            }

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, value);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public List<Map<String, String>> readAll(String columnName, String value) {
        List<Map<String, String>> resultList = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + this._table + " WHERE " + columnName + " = ?";

            if (this._softDelete) {
                query += " AND delete_at IS NULL";
            }

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, value);

            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String column = metaData.getColumnName(i);
                    rowData.put(column, resultSet.getString(column));
                }

                resultList.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public List<Map<String, String>> readAll(String[] ids) {
        List<Map<String, String>> resultList = new ArrayList<>();
        try {
            StringBuilder whereClause = new StringBuilder();
            for (int i = 0; i < _primaryKey.length; i++) {
                whereClause.append(_primaryKey[i]).append(" = ?");
                if (i < _primaryKey.length - 1) {
                    whereClause.append(" AND ");
                }
            }

            String query = "SELECT * FROM " + this._table + " WHERE " + whereClause.toString();

            if (this._softDelete) {
                query += " AND delete_at IS NULL";
            }

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            for (int i = 0; i < ids.length; i++) {
                preparedStatement.setString(i + 1, ids[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    rowData.put(columnName, resultSet.getString(columnName));
                }

                resultList.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    @Override
    public boolean update(Map<String, String> data,String[] ids) {
        try {
            StringBuilder setClause = new StringBuilder();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                setClause.append(entry.getKey()).append(" = ?,");
            }
            setClause.setLength(setClause.length() - 1);

            StringBuilder whereClause = new StringBuilder();
            for (int i = 0; i < _primaryKey.length; i++) {
                whereClause.append(_primaryKey[i]).append(" = ?");
                if (i < _primaryKey.length - 1) {
                    whereClause.append(" AND ");
                }
            }

            String query = "UPDATE " + _table + " SET " + setClause.toString() + " WHERE " + whereClause.toString();
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            int index = 1;
            for (String value : data.values()) {
                preparedStatement.setString(index++, value);
            }

            for (String id : ids) {
                preparedStatement.setString(index++, id);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String[] ids) {
        try {
            StringBuilder whereClause = new StringBuilder();
            for (int i = 0; i < _primaryKey.length; i++) {
                whereClause.append(_primaryKey[i]).append(" = ?");
                if (i < _primaryKey.length - 1) {
                    whereClause.append(" AND ");
                }
            }

            String query = "DELETE FROM " + _table + " WHERE " + whereClause.toString();
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            for (int i = 0; i < ids.length; i++) {
                preparedStatement.setString(i + 1, ids[i]);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean softDelete(String[] ids) {
        try {

            String query = "UPDATE " + _table + " SET " + "delete_at = ?" + " WHERE ";

            StringBuilder whereClause = new StringBuilder();
            for (int i = 0; i < _primaryKey.length; i++) {
                whereClause.append(_primaryKey[i]).append(" = ?");
                if (i < _primaryKey.length - 1) {
                    whereClause.append(" AND ");
                }
            }

            query += whereClause.toString();
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(1, currentTimestamp);

            for (int i = 0; i < ids.length; i++) {
                preparedStatement.setString(i + 2, ids[i]);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void close() throws Exception {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}