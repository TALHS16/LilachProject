package org.lilachshop.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Customer extends User implements Serializable {

    public Customer(String userName, String userPassword, String name, String address, String phoneNumber, CreditCard card, List<Order> orders, Store store, Account account, String email, ActiveDisabledState... accountState) {
        super(userName, userPassword);
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.card = card;
        this.orders = orders;
        this.store = store;
        this.account = account;
        this.email = email;
        this.accountState = accountState.length > 0 ? accountState[0] : ActiveDisabledState.ACTIVE;
    }

    @Embedded
    Account account;
    String name;
    String address;
    String phoneNumber;

    String email;

    @Enumerated(EnumType.STRING)
    ActiveDisabledState accountState;


    @Embedded
    CreditCard card;

    @OneToMany
    List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "store_id")
    Store store;

    public List<Order> getOrders() {
        return orders;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Customer() {
        this.accountState = ActiveDisabledState.ACTIVE;
    }

    public void addOrderToList(Order order) {
        this.orders.add(order);
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws Exception {
        if (phoneNumber == null)
            return;
        Pattern pattern = Pattern.compile("^[0-9]\\d*$");
        int length = phoneNumber.length();
        if (pattern.matcher(phoneNumber).matches() &&
                ((length == 10 && phoneNumber.startsWith("05")) || (length == 9 && phoneNumber.startsWith("0"))))
            this.phoneNumber = phoneNumber;
        else
            throw new Exception("מספר טלפון אינו תקין");
    }

    public ActiveDisabledState getAccountState() {
        return accountState;
    }

    public void setAccountState(ActiveDisabledState accountState) {
        this.accountState = accountState;
    }

    public CreditCard getCard() {
        return card;
    }

    public void setCard(CreditCard card) {
        this.card = card;
    }

    public String getEmail() {
        return email;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public void setEmail(String email) throws RuntimeException {
        if (!isValidEmail(email)) {
            throw new RuntimeException("כתובת דואר אלקטרוני אינה תקינה. אנא הכנס כתובת דואר אלקטרוני תקינה.");
        }
        this.email = email;
    }

    public AccountType getAccountType() {
        return account.accountType;
    }

    public LocalDate getCreationDate() {
        return account.creationDate;
    }

    public String getCardExpDate() {
        return card.getExpDateStringFormat();
    }

    public String getCardThreeDigits() {
        return card.getThreeDigits();
    }

    public String getCardOwnerName() {
        return card.getOwnerName();
    }

    public String getCardOwnerId() {
        return card.getCardOwnerId();
    }

    public String getCardNumber() {
        return card.getNumber();
    }

    @Override
    public String toString() {
        return this.name;
    }

}

