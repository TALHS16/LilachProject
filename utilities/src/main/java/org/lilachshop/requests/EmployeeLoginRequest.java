package org.lilachshop.requests;

public class EmployeeLoginRequest extends Request{
    String userName;
    String password;


    public EmployeeLoginRequest(String request, String userName, String password ) {
        super(request);
        this.userName= userName;
        this.password= password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
