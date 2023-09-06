package com.biblio.dao;

import com.biblio.app.model.Book;
import com.biblio.app.model.Role;
import com.biblio.app.model.User;

import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class UserDao extends User {


    public boolean create() throws SQLException {
        this.id = Integer.parseInt(super.create(this.getUser()));
        return this.id > 0;
    }

    public User read() {
        Map<String, String> User = super.read(new String[]{String.valueOf(this.id)});

        this.setUser(
                User.get("firstName"),
                User.get("lastName"),
                User.get("email"),
                User.get("password"),
                User.get("gender"),
                User.get("phone")
        );

        return this;
    }

    public boolean update() {
        return super.update(this.getUser(), new String[]{String.valueOf(this.id)});
    }

    public boolean delete() {
        return super.softDelete(new String[]{String.valueOf(this.id)});
    }

    public User getUserWithRoles() {
        boolean flag = true;
        try {
            String query = "SELECT u.*, r.role, r.id r_id " +
                    "FROM users u " +
                    "LEFT JOIN users_roles ur ON u.id = ur.user " +
                    "LEFT JOIN roles r ON ur.role = r.id " +
                    "WHERE u.id = ? AND u.delete_at IS NULL";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, this.id);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Role> roles = new ArrayList<>();

            while (resultSet.next()) {
                if (flag) {
                    this.setUser(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("gender"),
                            resultSet.getString("phone")
                    );
                    flag = false;
                }

                int id = resultSet.getInt("r_id");
                String roleTitle = resultSet.getString("role");

                System.out.println(id + " " + roleTitle);

                if (roleTitle != null && id > 0) {
                    Role role = (Role) new RoleDao();
                        role.setRole(
                                id,
                                roleTitle
                        );
                        roles.add(role);
                }
            }

            this.hasRoles(roles);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public User getByEmailWithRoles() {
        boolean flag = true;
        try {
            String query = "SELECT u.*, r.role " +
                    "FROM users u " +
                    "LEFT JOIN users_roles ur ON u.id = ur.user " +
                    "LEFT JOIN roles r ON ur.role = r.id " +
                    "WHERE u.email = ? AND u.delete_at IS NULL";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, this.email);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Role> roles = new ArrayList<>();

            while (resultSet.next()) {
                if (flag) {
                    this.setUser(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("gender"),
                            resultSet.getString("phone")
                    );
                    flag = false;
                }

                int id = resultSet.getInt("id");
                String roleTitle = resultSet.getString("role");

                if (roleTitle != null && id > 0) {
                    Role role = (Role) new RoleDao();
                    role.setRole(
                            id,
                            roleTitle
                    );
                    roles.add(role);
                }
            }

            this.hasRoles(roles);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public User getByPhoneWithRoles() {
        try {
            boolean flag = true;

            String query = "SELECT u.*, r.role " +
                    "FROM users u " +
                    "LEFT JOIN users_roles ur ON u.id = ur.user " +
                    "LEFT JOIN roles r ON ur.role = r.id " +
                    "WHERE u.phone = ? AND u.delete_at IS NULL";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, this.phone);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Role> roles = new ArrayList<>();

            while (resultSet.next()) {
                if (flag) {
                    this.setUser(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("gender"),
                            resultSet.getString("phone")
                    );
                    flag = false;
                }

                int id = resultSet.getInt("id");
                String roleTitle = resultSet.getString("role");

                if (roleTitle != null && id > 0) {
                    Role role = (Role) new RoleDao();
                    role.setRole(
                            id,
                            roleTitle
                    );
                    roles.add(role);
                }
            }

            this.hasRoles(roles);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

}
