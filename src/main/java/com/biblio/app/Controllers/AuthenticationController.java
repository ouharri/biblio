package com.biblio.app.Controllers;

import com.biblio.app.Enums.Gender;
import com.biblio.app.Exceptions.existingUserException;
import com.biblio.app.Models.User;
import com.biblio.dao.UserDao;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationController {

    private final User user;

    /**
     * Constructs a new AuthenticationController.
     */
    public AuthenticationController() {
        this.user = new User();
    }

    /**
     * Registers a new user.
     *
     * @param cnie      The CNIE (user identifier).
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email address of the user.
     * @param password  The user's password.
     * @param gender    The gender of the user.
     * @param phone     The user's phone number.
     * @return The newly registered user.
     * @throws existingUserException If a user with the same CNIE, email, or phone number already exists.
     * @throws Exception             If a database error occurs during user registration.
     */
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
            throw e;
        }
    }

    /**
     * Authenticates a user.
     *
     * @param cnieOrEmailOrPhone The CNIE, email, or phone number used for authentication.
     * @param password           The user's password.
     * @return `true` if authentication is successful, `false` otherwise.
     */
    public boolean authenticate(String cnieOrEmailOrPhone, String password) {

        User user = getUserByCnieOrEmailOrPhone(cnieOrEmailOrPhone);

        if (user != null) {
            return this.checkPassword(password, user.getPassword());
        }

        return false;
    }

    /**
     * Retrieves a user by CNIE, email, or phone number.
     *
     * @param cnieOrEmailOrPhone The CNIE, email, or phone number of the user.
     * @return The user if found, or `null` if not found.
     * @throws RuntimeException If a database error occurs during the user retrieval.
     */
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

    private String hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    private boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}