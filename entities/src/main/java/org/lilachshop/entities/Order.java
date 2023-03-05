package org.lilachshop.entities;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Transactional
@Entity
@Table(name = "Orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
    private Long id;
    private double refund;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus = OrderStatus.PENDING;

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public String getGreetingCard() {
        return greetingCard;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public PickUpDetails getPickUpDetails() {
        return pickUpDetails;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Order(List<myOrderItem> items, Double totalPrice, int amountOfProducts, Customer customer) {
        this.creationDate = LocalDateTime.now();
        this.items = items;
        this.totalPrice = totalPrice;
        this.amountOfProducts = amountOfProducts;
        this.customer = customer;
        this.refund = 0.0;
        this.orderStatus = OrderStatus.PENDING;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

    public int getAmountOfProducts() {
        return amountOfProducts;
    }

    public Order(LocalDateTime creationDate, String greetingCard, List<myOrderItem> items, Double totalPrice, int amountOfProducts, DeliveryDetails deliveryDetails, PickUpDetails pickUpDetails, Complaint complaint, Customer customer) {
        this.creationDate = creationDate;
        this.greetingCard = greetingCard;
        this.items = items;
        this.totalPrice = totalPrice;
        this.amountOfProducts = amountOfProducts;
        this.deliveryDetails = deliveryDetails;
        this.pickUpDetails = pickUpDetails;
        this.complaint = complaint;
        this.customer = customer;
        this.refund = 0.0;
        this.orderStatus = OrderStatus.PENDING;
    }

    public Order(String greetingCard, List<myOrderItem> items, Double totalPrice, int amountOfProducts, DeliveryDetails deliveryDetails, PickUpDetails pickUpDetails, Complaint complaint, Customer customer) {
        this.creationDate = LocalDateTime.now();
        this.greetingCard = greetingCard;
        this.items = items;
        this.totalPrice = totalPrice;
        this.amountOfProducts = amountOfProducts;
        this.deliveryDetails = deliveryDetails;
        this.pickUpDetails = pickUpDetails;
        this.complaint = complaint;
        this.customer = customer;
        this.refund = 0.0;
        this.orderStatus = OrderStatus.PENDING;
    }


    public Complaint getComplaint() {
        return complaint;
    }

    public void setPickUpDetails(PickUpDetails pickUpDetails) {
        this.pickUpDetails = pickUpDetails;
    }

    public List<myOrderItem> getItems() {
        return items;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Store getStore() {
        return store;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @ManyToOne
    Store store;

    LocalDateTime creationDate;

    String greetingCard;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<myOrderItem> items;

    Double totalPrice;

    int amountOfProducts;

    @Embedded
    DeliveryDetails deliveryDetails;

    @Embedded
    PickUpDetails pickUpDetails;

    @OneToOne(cascade = CascadeType.ALL)
    Complaint complaint;

    @ManyToOne
    Customer customer;

    @Embedded
    CreditCard creditCard;


    public void setGreetingCard(String greetingCard) {
        this.greetingCard = greetingCard;
    }

    public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
    }

    protected Order() {
    }


    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}
