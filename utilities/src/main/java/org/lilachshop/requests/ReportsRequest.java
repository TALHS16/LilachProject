package org.lilachshop.requests;

public class ReportsRequest extends Request {
    long storeID;
    public ReportsRequest(String request) {
        super(request);
    }

    public ReportsRequest(String request, long storeID) {
        super(request);
        this.storeID = storeID;
    }

    public long getStoreID() {
        return storeID;
    }
}
