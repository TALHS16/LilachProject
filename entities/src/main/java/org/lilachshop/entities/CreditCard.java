package org.lilachshop.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Embeddable
public class CreditCard implements Serializable {

    String number;

    @Convert(converter = YearMonthDateAttributeConverter.class)
    YearMonth expDate;
    String threeDigits;
    String ownerName;

    String cardOwnerId;

    public CreditCard() {
    }


    public String getNumber() {
        return number;
    }

    public CreditCard(String number, YearMonth expDate, String ownerName, String cardOwnerID, String threeDigits) {
        this.number = number;
        this.expDate = expDate;
        this.ownerName = ownerName;
        this.cardOwnerId = cardOwnerID;
        this.threeDigits = threeDigits;
    }

    public YearMonth getExpDate() {
        return expDate;
    }

    public String getExpDateStringFormat() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMyy");
        return expDate.format(dateFormatter);
    }

    private static boolean isNonNegativeNumeric(String number) {
        Pattern pattern = Pattern.compile("^[0-9]\\d*$");
        if (number == null)
            return false;
        return pattern.matcher(number).matches();
    }

    public void setNumber(String number) throws Exception {
        if (isNonNegativeNumeric(number) && number.length() == 16) {
            this.number = number;
        } else {
            throw new Exception("כרטיס האשראי שמולא איננו תקין");
        }
    }

    public void setExpDate(YearMonth expDate) throws Exception {
        if (!expDate.isBefore(YearMonth.now())) {
            this.expDate = expDate;
        } else {
            throw new Exception("תוקף אינו תקין, אנא מלא שוב");
        }
    }

    public void setExpDate(String expDate) throws Exception {
        System.out.println("Date given to credit card" + expDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
        YearMonth date;
        try {
            date = YearMonth.parse(expDate, formatter);
            setExpDate(date);
        } catch (Exception e) {
            throw new Exception("תוקף אינו תקין");
        }
    }

    public void setThreeDigits(String threeDigits) throws Exception {
        if ((threeDigits.length() == 3) && (isNonNegativeNumeric(threeDigits))) {
            this.threeDigits = threeDigits;
        } else {
            throw new Exception("קוד אימות הכרטיס אינו תקין, אנא מלא מחדש");
        }
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) throws Exception {
        if(ownerName.isBlank()){
            throw new Exception("שם המשתמש ריק, אנא מלא שנית");
        }
        this.ownerName = ownerName;
    }

    public String getCardOwnerId() {
        return cardOwnerId;
    }

    public void setCardOwnerId(String cardOwnerId) throws Exception {
        if ((cardOwnerId.length() == 9) && (isNonNegativeNumeric(cardOwnerId))) {
            this.cardOwnerId = cardOwnerId;
        } else {
            throw new Exception("תעודת זהות אינה תקינה, אנא מלא שוב");
        }
    }

    public String getThreeDigits() {
        return threeDigits;
    }

    private static boolean validateCreditCardNumber(String str) {

        int[] ints = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        if (sum % 10 == 0) {
            System.out.println(str + " is a valid credit card number");
            return true;
        } else {
            System.out.println(str + " is an invalid credit card number");
            return false;
        }
    }

}

