package com.biblio.dao;

import com.biblio.app.Enums.Gender;
import com.biblio.app.Models.Role;
import com.biblio.app.Models.User;
import com.biblio.libs.Model;

import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class UserDao extends Model {

    private User user = null;

    public UserDao() {
        super("users", new String[]{"cnie"});
        this.user = new User();
    }
    public UserDao(User user) {
        super("users", new String[]{"cnie"});
        this.user = user;
    }

    public User create() throws SQLException {
        super.create(this.user.getUser());
        return this.read();
    }

    public User read() {
        Map<String, String> User = super.read(new String[]{String.valueOf(this.user.getCnie())});

        this.user.setUser(
                User.get("cnie"),
                User.get("first_name"),
                User.get("last_name"),
                Gender.valueOf(User.get("gender")),
                User.get("email"),
                User.get("phone"),
                User.get("password")
        );

        return this.user;
    }


    public boolean update() {
        return super.update(this.user.getUser(), new String[]{String.valueOf(this.user.getCnie())});
    }

    public boolean delete() {
        return super.softDelete(new String[]{String.valueOf(this.user.getCnie())});
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
            preparedStatement.setString(1, this.user.getCnie());

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Role> roles = new ArrayList<>();

            while (resultSet.next()) {
                if (flag) {
                    this.user.setUser(
                            resultSet.getString("cnie"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            Gender.valueOf(resultSet.getString("gender")),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getString("password")
                    );
                    flag = false;
                }

                int id = resultSet.getInt("r_id");
                String roleTitle = resultSet.getString("role");

                System.out.println(id + " " + roleTitle);

                if (roleTitle != null && id > 0) {
                    Role role = new Role();
                        role.setRole(
                                id,
                                roleTitle
                        );
                        roles.add(role);
                }
            }

            this.user.hasRoles(roles);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.user;
    }

    public Boolean isExistedUser() {
        try {
            String query;
            String param;

            if (this.user.getEmail() != null) {
                query = "SELECT COUNT(*) AS exist FROM " + this._table + " WHERE email = ? AND delete_at IS NULL";
                param = this.user.getEmail();
            } else if (this.user.getPhone() != null) {
                query = "SELECT * FROM " + this._table + " WHERE phone = ? AND delete_at IS NULL";
                param = this.user.getPhone();
            } else {
                return false;
            }

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, param);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("exist") > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public User find(String keyword) {

        boolean flag = true;
        User user = new User();

        try {
            String query = "SELECT u.*, r.role , r.id " +
                    "FROM " + this._table + " u " +
                    "LEFT JOIN users_roles ur ON u.cnie = ur.user " +
                    "LEFT JOIN roles r ON ur.role = r.id " +
                    "WHERE ( u.email = ? OR u.phone = ? OR u.cnie = ? ) AND u.delete_at IS NULL";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            preparedStatement.setString(1, keyword);
            preparedStatement.setString(2, keyword);
            preparedStatement.setString(3, keyword);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Role> roles = new ArrayList<>();

            while (resultSet.next()) {

                    if (resultSet.getString("cnie") != null && flag) {
                        user.setUser(
                                resultSet.getString("cnie"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                Gender.valueOf(resultSet.getString("gender")),
                                resultSet.getString("email"),
                                resultSet.getString("phone"),
                                resultSet.getString("password")
                        );
                        flag = false;
                    } else return null;

                    int id = resultSet.getInt("id");
                    String roleTitle = resultSet.getString("role");

                    if (roleTitle != null && id > 0) {
                        Role role = new Role();
                        role.setRole(
                                id,
                                roleTitle
                        );
                        roles.add(role);
                    }

                }

            if(user.getCnie() == null) return null;

            user.hasRoles(roles);

            return user;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public User getByEmailWithRoles() {
        boolean flag = true;
        try {
            String query = "SELECT u.*, r.role, r.id " +
                    "FROM " + this._table + " u " +
                    "LEFT JOIN users_roles ur ON u.cnie = ur.user " +
                    "LEFT JOIN roles r ON ur.role = r.id " +
                    "WHERE u.email = ? AND u.delete_at IS NULL";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, this.user.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Role> roles = new ArrayList<>();

            while (resultSet.next()) {
                if (flag) {
                    this.user.setUser(
                            resultSet.getString("cnie"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            Gender.valueOf(resultSet.getString("gender")),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getString("password")
                    );
                    flag = false;
                }

                int id = resultSet.getInt("id");
                String roleTitle = resultSet.getString("role");

                if (roleTitle != null && id > 0) {
                    Role role = new Role();
                    role.setRole(
                            id,
                            roleTitle
                    );
                    roles.add(role);
                }
            }

            this.user.hasRoles(roles);

            return this.user;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public User getByPhoneWithRoles() {
        try {
            boolean flag = true;

            String query = "SELECT u.*, r.role, r.id " +
                    "FROM users u " +
                    "LEFT JOIN users_roles ur ON u.cnie = ur.user " +
                    "LEFT JOIN roles r ON ur.role = r.cnie " +
                    "WHERE u.phone = ? AND u.delete_at IS NULL";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, this.user.getPhone());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Role> roles = new ArrayList<>();

            while (resultSet.next()) {
                if (flag) {
                    this.user.setUser(
                            resultSet.getString("cnie"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            Gender.valueOf(resultSet.getString("gender")),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getString("password")
                    );
                    flag = false;
                }

                int id = resultSet.getInt("id");
                String roleTitle = resultSet.getString("role");

                if (roleTitle != null && id > 0) {
                    Role role = new Role();
                    role.setRole(
                            id,
                            roleTitle
                    );
                    roles.add(role);
                }
            }

            this.user.hasRoles(roles);

            return this.user;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
