package org.lilachshop.events;


//Event to pass from SignUp2 to final stage to create new costumer
public class Signup2Event {
    String firstName;
    String lastName;
    String phoneNumber;
    String city;
    String address;
    String email;


    public Signup2Event(String firstName, String lastName, String phoneNumber, String city, String address, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.address = address;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
