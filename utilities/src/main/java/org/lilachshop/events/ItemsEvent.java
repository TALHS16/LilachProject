package org.lilachshop.events;


import org.lilachshop.entities.Item;

import java.io.Serializable;
import java.util.List;

//Event to pass from SignUp3 to final stage to create new costumer
public class ItemsEvent implements Serializable {
    List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public ItemsEvent(List<Item> items) {
        this.items = items;
    }
}