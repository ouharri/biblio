package com.biblio.libs;

import com.biblio.core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class db implements AutoCloseable {
    private Connection connection = null;
    protected String _table = null;
    protected String _primaryKey = "id";
    protected String _foreignKey = null;

    public db(String tableName, String primaryKey) {
        this.connection = database.getConnection();
        this._table = tableName;
        this._primaryKey = primaryKey;
    }

    public boolean create(Map<String, String> data) throws SQLException {
        try {
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();

            for (Map.Entry<String, String> entry : data.entrySet()) {
                columns.append(entry.getKey()).append(",");
                values.append("?").append(",");
            }

            columns.setLength(columns.length() - 1); // Supprimer la virgule finale
            values.setLength(values.length() - 1); // Supprimer la virgule finale

            String query = "INSERT INTO " + _table + " (" + columns.toString() + ") VALUES (" + values.toString() + ")";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            int index = 1;
            for (String value : data.values()) {
                preparedStatement.setString(index++, value);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw e;
        }
    }


    public Map<String, String> read(int id) {
        try {
            String query = "SELECT * FROM " + this._table + " WHERE "+ this._primaryKey +" = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();
                // Remplir la map avec les données de la ligne
                // Utilisez resultSet.getString("nom_de_la_colonne") pour récupérer les valeurs
                return rowData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(int id, Map<String, String> data) {
        try {
            StringBuilder setClause = new StringBuilder();

            for (Map.Entry<String, String> entry : data.entrySet()) {
                setClause.append(entry.getKey()).append(" = ?,");
            }

            setClause.setLength(setClause.length() - 1); // Supprimer la virgule finale

            String query = "UPDATE " + _table + " SET " + setClause.toString() + " WHERE "+ this._primaryKey +" = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            int index = 1;
            for (String value : data.values()) {
                preparedStatement.setString(index++, value);
            }
            preparedStatement.setInt(index, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean delete(int id) {
        try {
            String query = "DELETE FROM " + _table + " WHERE "+ this._primaryKey +" = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

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
