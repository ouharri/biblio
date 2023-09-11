package com.biblio.libs;

import com.biblio.core.database;
import com.biblio.interfaces.CRUD;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The `Model` class provides a generic model for interacting with a database table. It implements CRUD (Create, Read, Update, Delete) operations
 * and supports transactions. This class should be extended by specific models for individual database tables.
 */
public class Model implements AutoCloseable, CRUD {
    protected Connection connection = null;
    protected String _table = null;
    protected String[] _primaryKey = {"id"};
    protected String _foreignKey = null;
    protected Boolean _softDelete = true;
    private boolean inTransaction = false;

    public Model(String tableName, String[] primaryKey) {
        this.connection = database.getConnection();
        this._table = tableName;
        if (primaryKey != null && primaryKey.length > 0) {
            this._primaryKey = primaryKey;
        }
    }


    /**
     * Begins a database transaction. Call this method before performing a series of database operations
     * that should be treated as a single transaction. This allows for rolling back changes in case of errors.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void beginTransaction() throws SQLException {
        if (!inTransaction) {
            this.connection.setAutoCommit(false);
            this.inTransaction = true;
        }
    }

    /**
     * Commits the current database transaction. This should be called after successfully completing a series
     * of database operations within a transaction.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void commitTransaction() throws SQLException {
        if (inTransaction) {
            this.connection.commit();
            this.connection.setAutoCommit(true);
            this.inTransaction = false;
        }
    }

    /**
     * Rolls back the current database transaction. This should be called in case of an error or when you want
     * to discard changes made within the current transaction.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void rollbackTransaction() throws SQLException {
        if (inTransaction) {
            this.connection.rollback();
            this.connection.setAutoCommit(true);
            this.inTransaction = false;
        }
    }

    /**
     * Retrieves all records from the associated database table.
     *
     * @return A list of maps, where each map represents a row of data with column names as keys.
     */
    public List<Map<String, String>> getAll() {
        List<Map<String, String>> resultList = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + this._table;

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

    /**
     * Searches for records in the associated database table that match a given keyword in specified columns.
     *
     * @param keyword The search keyword to match.
     * @param columns The columns in which to perform the search.
     * @return A list of maps, where each map represents a row of data with column names as keys.
     */
    public List<Map<String, String>> search(String keyword, String[] columns) {
        List<Map<String, String>> resultList = new ArrayList<>();
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT * FROM ").append(this._table).append(" WHERE ");

            if (this._softDelete) {
                queryBuilder.append("delete_at IS NULL AND (");
            } else {
                queryBuilder.append("(");
            }

            for (int i = 0; i < columns.length; i++) {
                queryBuilder.append(columns[i]).append(" LIKE ?");
                if (i < columns.length - 1) {
                    queryBuilder.append(" OR ");
                }
            }
            queryBuilder.append(")");

            PreparedStatement preparedStatement = this.connection.prepareStatement(queryBuilder.toString());

            for (int i = 0; i < columns.length; i++) {
                preparedStatement.setString(i + 1, "%" + keyword + "%");
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

    /**
     * Creates a new record in the associated database table with the provided data.
     *
     * @param data A map of column names and their corresponding values for the new record.
     * @return The primary key value (e.g., ID) of the newly created record, or null if the operation fails.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public String create(Map<String, String> data) throws SQLException {
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

        if (rowsAffected > 0) {
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getString(1);
            } else {
                return data.get(this._primaryKey[0]);
            }
        }
        return null;
    }

    /**
     * Reads a single record from the associated database table based on the specified primary key values.
     *
     * @param ids An array of primary key values used to identify the record.
     * @return A map representing the retrieved record's data, or null if the record is not found.
     */
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

    /**
     * Reads a single record from the associated database table based on a specific column value.
     *
     * @param columnName The name of the column to search.
     * @param value      The value to search for in the specified column.
     * @return A map representing the retrieved record's data, or null if the record is not found.
     */
    @Override
    public Map<String, String> read(String columnName, String value) {
        try {
            String query = "SELECT * FROM " + this._table + " WHERE " + columnName + " = ?";

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

    /**
     * Retrieves the count of records in the associated database table that match a specific column value.
     *
     * @param WhereColumnName The name of the column to search.
     * @param value           The value to search for in the specified column.
     * @return The count of records matching the specified column value.
     */
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

    /**
     * Retrieves all records from the associated database table that match a specific column value.
     *
     * @param columnName The name of the column to search.
     * @param value      The value to search for in the specified column.
     * @return A list of maps, where each map represents a row of data with column names as keys.
     */
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

    /**
     * Retrieves all records from the associated database table based on the specified primary key values.
     *
     * @param ids An array of primary key values used to identify the records.
     * @return A list of maps, where each map represents a row of data with column names as keys.
     */
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

    /**
     * Updates records in the associated database table with the provided data based on specified primary key values.
     *
     * @param data A map of column names and their corresponding values for the update.
     * @param ids  An array of primary key values used to identify the records to be updated.
     * @return True if the update operation is successful; otherwise, false.
     */
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

    /**
     * Deletes records from the associated database table based on specified primary key values.
     *
     * @param ids An array of primary key values used to identify the records to be deleted.
     * @return True if the delete operation is successful; otherwise, false.
     */
    @Override
    public boolean delete(String[] ids) {
        try {
            StringBuilder whereClause = new StringBuilder();

            int ids_length = ids.length;
            int primaryKey_length = _primaryKey.length;

            for (int i = 0; i < ids_length && i < primaryKey_length; i++) {
                whereClause.append(_primaryKey[i]).append(" = ?");
                if (i < ids_length - 1 && i < primaryKey_length - 1) {
                    whereClause.append(" AND ");
                }
            }

            String query = "DELETE FROM " + _table + " WHERE " + whereClause.toString();
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            for (int i = 0; i < ids_length && i < primaryKey_length; i++) {
                preparedStatement.setString(i + 1, ids[i]);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Performs a soft delete operation on the records identified by the given IDs.
     * Soft delete sets the "delete_at" column to the current timestamp.
     *
     * @param ids The array of IDs of records to be soft-deleted.
     * @return True if the records were successfully soft-deleted; otherwise, false.
     */
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
//        if (this.connection != null) {
//            try {
//                this.connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    }
}