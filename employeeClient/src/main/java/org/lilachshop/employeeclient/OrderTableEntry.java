package org.lilachshop.employeeclient;

import javafx.scene.control.cell.PropertyValueFactory;
import org.lilachshop.commonUtils.OrderType;
import org.lilachshop.entities.DeliveryDetails;
import org.lilachshop.entities.Order;
import org.lilachshop.entities.OrderStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderTableEntry {
    Order order;

    public Order getOrder() {
        return order;
    }



    public void setOrder(Order order) {
        this.order = order;
    }

    public LocalDateTime getDeliveryTime()
    {
        if (order.getDeliveryDetails() != null)
            return order.getDeliveryDetails().getDeliveryTime();
        else
            return order.getPickUpDetails().getPickUptime();
    }

    public OrderType getMethod()
    {
        if (order.getDeliveryDetails() != null)
            return OrderType.DELIVERY;
        else
            return OrderType.PICKUP;
    }

    public LocalDateTime getCreateDate()
    {
        return order.getCreationDate();
    }

    public Long getId()
    {
        return order.getId();
    }

    public String getOwnerName()
    {
        return order.getCustomer().getName();
    }

    public double getRefund()
    {
        return order.getRefund();
    }

    public String getAddress()
    {
        if (order.getDeliveryDetails() != null)
            return order.getDeliveryDetails().getAddress();
        else
            return order.getStore().getAddress();
    }

    public OrderStatus getStatus()
    {
        return order.getOrderStatus();
    }

    public double getTotalPrice()
    {
        return order.getTotalPrice();
    }
}
