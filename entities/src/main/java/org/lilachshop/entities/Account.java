package org.lilachshop.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class Account implements Serializable{

    LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    AccountType accountType;

    protected Account() {}

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public  Account(AccountType accountType){
        this.creationDate = LocalDate.now();
        this.accountType = accountType;
    }

    public Account(LocalDate creationDate, AccountType accountType) {
        this.creationDate = creationDate;
        this.accountType = accountType;
    }
}
