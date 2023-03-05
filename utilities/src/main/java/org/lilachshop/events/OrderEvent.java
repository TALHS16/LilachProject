package org.lilachshop.events;

import org.lilachshop.entities.Catalog;
import org.lilachshop.entities.Item;
import org.lilachshop.entities.Order;

import java.io.Serializable;
import java.util.List;

public class OrderEvent implements Serializable {
    List<Order> orders;
    List<Item> items;
    List<Catalog> catalogs;
    public List<Order> getOrders() {
        return orders;
    }

    public List<Item> getItems() {
        return items;
    }

    public OrderEvent(List<Order> orders) {
        this.orders = orders;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }
}