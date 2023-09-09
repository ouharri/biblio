package com.biblio.app.Controllers;

import com.biblio.app.Enums.Gender;
import com.biblio.app.Exceptions.existingUserException;
import com.biblio.app.Models.User;
import com.biblio.dao.UserDao;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationController {

    private final User user;

    public AuthenticationController() {
        this.user = new User();
    }

    public User register( String cnie, String firstName, String lastName,String email, String password, Gender gender, String phone) throws Exception {

        user.setUser(
                cnie,
                firstName,
                lastName,
                gender,
                email,
                phone,
                this.hashPassword(password)
        );

        try(UserDao userDao = new UserDao(user)) {
            if (userDao.isExistedUser()) {
                throw new existingUserException(this.user);
            } else {
                    return userDao.create();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean authenticate(String cnieOrEmailOrPhone, String password) {

        User user = getUserByCnieOrEmailOrPhone(cnieOrEmailOrPhone);

        if (user != null) {
            return this.checkPassword(password, user.getPassword());
        }

        return false;
    }

    public User getUserByCnieOrEmailOrPhone(String cnieOrEmailOrPhone) {

        try(UserDao userDao = new UserDao()){
            return userDao.find(cnieOrEmailOrPhone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private boolean isEmail(String input) {
        String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$\n";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private boolean isPhoneNumber(String input) {
        return input.matches("\\d{10}");
    }

    private boolean userExists() {
        try(UserDao userDao = new UserDao(user)) {
            return userDao.isExistedUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    public boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}