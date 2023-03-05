package org.lilachshop.employeeclient;

public class ItemSalesObservable {
    int numOfSales;
    int price;
    String itemName;

    public ItemSalesObservable(int numOfSales, int price, String itemName) {
        this.numOfSales = numOfSales;
        this.price = price;
        this.itemName = itemName;
    }

    public int getNumOfSales() {
        return numOfSales;
    }

    public int getPrice() {
        return price;
    }

    public String getItemName() {
        return itemName;
    }
}
