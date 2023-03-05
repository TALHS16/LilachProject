package org.lilachshop.entities;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Transactional
@Entity
@Table(name = "catalog")
public class Catalog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    Store store;

    public void setStore(Store store) {
        this.store = store;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalog", orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Item> items = new LinkedList<>();

    public List<Item> getItems() {
        return items;
    }

    public int getId() {
        return id;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        items.add(item);
        item.setCatalog(this);
    }

    public Store getStore() {
        return store;
    }

    @Override
    public String toString() {
        return this.store.storeName;
    }
}
