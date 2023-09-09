package com.biblio.app.Exceptions;

import com.biblio.app.Models.User;

public class existingUserException  extends Exception {
    public existingUserException(User user) {
        super("User already exists: " + user.getFirst_name() + " " + user.getLast_name());
    }
}
