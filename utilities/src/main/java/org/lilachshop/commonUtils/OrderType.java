package org.lilachshop.commonUtils;

public enum OrderType {
    DELIVERY("משלוח"),
    PICKUP("איסוף עצמי");

    private String string;
    OrderType(String str){
        this.string =str;
    }

    @Override
    public String toString() {
        return string;
    }


}
