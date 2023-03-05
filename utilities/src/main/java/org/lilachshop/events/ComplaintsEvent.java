package org.lilachshop.events;

import org.lilachshop.entities.Complaint;

import java.io.Serializable;
import java.util.List;

public class ComplaintsEvent implements Serializable {
    List<Complaint> allComplaints;

    public ComplaintsEvent(List<Complaint> allComplaints) {
        this.allComplaints = allComplaints;
    }

    public List<Complaint> getAllComplaints() {
        return allComplaints;
    }
}
