package org.lilachshop.events;


//Event to pass from SignUp3 to final stage to create new costumer
public class Signup1Event {
    String username;
    String password;

    public Signup1Event(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
