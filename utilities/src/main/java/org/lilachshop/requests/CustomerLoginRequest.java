package org.lilachshop.requests;

public class CustomerLoginRequest extends Request{
    String userName;
    String userPassword;

    public CustomerLoginRequest(String request, String userName, String userPassword) {
        super(request);
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
