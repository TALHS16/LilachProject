package org.lilachshop.events;

import org.lilachshop.entities.Store;

import java.io.Serializable;
import java.util.List;

public class StoreEvent implements Serializable {
    private final List<Store> stores;

    public StoreEvent(List<Store> stores) {
        this.stores = stores;
    }

    public List<Store> getStores() {
        return stores;
    }
}
