package org.lilachshop.entities;


import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Transactional

@Entity
@Table(name = "Complaint")
public class Complaint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    LocalDateTime creationDate;
    LocalDateTime endOfHandleDate;
    ComplaintStatus status;
    String content;
    String reply;

    @OneToOne(cascade = CascadeType.ALL)
    Order order;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Store getStore() {
        return store;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Complaint() {}

    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Complaint(LocalDateTime endOfHandleDate, ComplaintStatus status, String content, LocalDateTime creationDate, String reply) {
        this.endOfHandleDate = endOfHandleDate;
        this.status = status;
        this.content = content;
        this.creationDate = creationDate;
        this.reply = reply;
    }

    public LocalDateTime getCreationDate() {
        return endOfHandleDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.endOfHandleDate = creationDate;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {return reply; }
}
