package org.lilachshop.entities;


import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Transactional
@Entity
@Table(name = "Stores")
public class Store implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected Store() {
    }

    public Long getId() {
        return id;
    }

    public Store(String address, String storeName, Catalog catalog, List<Complaint> complaints, List<Order> orders) {
        this.address = address;
        this.storeName = storeName;
        this.catalog = catalog;
        this.complaints = complaints;
        this.orders = orders;
    }

    public String getAddress() {
        return address;
    }

    public String getStoreName() {
        return storeName;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public List<Order> getOrders() {
        return orders;
    }

    String address;
    String storeName;

    @OneToOne(cascade = CascadeType.ALL)
    Catalog catalog;

    @OneToMany(cascade = CascadeType.ALL)
    List<Complaint> complaints;

    @OneToMany(cascade = CascadeType.ALL)
    List<Order> orders;

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public void addComplaint(Complaint complaint) {
        this.complaints.add(complaint);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    @Override
    public String toString() {
        return storeName;
    }

    @Override
    public boolean equals(Object obj) {
        // self check
        if (this == obj)
            return true;
        // null check
        if (obj == null)
            return false;
        // type check and cast
        if (getClass() != obj.getClass())
            return false;
        Store store = (Store) obj;
        // field comparison
        return Objects.equals(storeName, store.storeName);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
