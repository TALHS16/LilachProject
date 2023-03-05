package org.lilachshop.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Employee extends User implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL)
    Store store;

    @Enumerated(EnumType.STRING)
    Role role;

    public Employee(Store store, Role role, String userName, String userPassword) {
        super(userName, userPassword);
        this.store = store;
        this.role = role;
    }

    protected Employee() {
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
