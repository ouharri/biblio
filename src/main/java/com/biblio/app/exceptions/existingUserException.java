package com.biblio.app.exceptions;

import com.biblio.app.model.User;

public class existingUserException  extends Exception {
    public existingUserException(User user) {
        super("User already exists: " + user.getFirstName() + " " + user.getLastName());
    }
}
