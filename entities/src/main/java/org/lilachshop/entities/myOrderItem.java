package org.lilachshop.entities;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;

@Entity
@Table(name = "OrderItems")
public class myOrderItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    private int count;

    public myOrderItem() {
    }
    public myOrderItem(Item flower,int count) {
        this.item = flower;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item flower) {
        this.item = flower;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return item.getName();
    }
}