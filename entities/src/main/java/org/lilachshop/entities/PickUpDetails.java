package org.lilachshop.entities;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Embeddable
public class PickUpDetails implements Serializable {

    LocalDateTime PickUptime;

    public PickUpDetails(LocalDateTime pickUptime) {
        PickUptime = pickUptime;
    }

    protected PickUpDetails() {

    }

    public LocalDateTime getPickUptime() {
        return PickUptime;
    }

    public void setPickUptime(LocalDateTime pickUptime) {
        PickUptime = pickUptime;
    }
}
