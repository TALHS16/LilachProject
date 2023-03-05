package org.lilachshop.entities;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    CANCELED("בוטלה"),
    PENDING("בהכנה"),
    DELIVERED("סופקה");

private String string;

OrderStatus(String str){
    this.string =str;
}

    @Override
    public String toString() {
        return string;
    }
}
