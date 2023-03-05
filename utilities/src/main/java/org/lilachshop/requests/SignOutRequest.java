package org.lilachshop.requests;

import org.lilachshop.entities.User;

public class SignOutRequest extends Request {

    public enum Messages {
        SIGN_OUT,
        CHECK_SIGNED_OUT;
    }

    User userToSignOut;

    public SignOutRequest(Messages message, User user) {
        super(message.name());
        userToSignOut = user;
    }

    public User getUser() {
        return userToSignOut;
    }
}
