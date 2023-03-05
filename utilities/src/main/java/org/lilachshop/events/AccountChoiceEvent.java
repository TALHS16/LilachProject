package org.lilachshop.events;

import org.lilachshop.entities.AccountType;
import org.lilachshop.entities.Store;


//Event to pass from SignUp3 to final stage to create new costumer
public class AccountChoiceEvent {
    AccountType chosenAccount;
    Store chosenStore;


    public AccountChoiceEvent(AccountType chosenAccount, Store store) {
        this.chosenAccount = chosenAccount;
        this.chosenStore = store;
    }

    public AccountChoiceEvent() {
    }

    public AccountType getChosenAccount() {
        return chosenAccount;
    }

    public Store getStore() {
        return chosenStore;
    }
}
