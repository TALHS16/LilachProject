package org.lilachshop.events;

import org.lilachshop.entities.Store;

import java.io.Serializable;

public class RefreshCatalogEvent implements Serializable {
    int id;

    public RefreshCatalogEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
