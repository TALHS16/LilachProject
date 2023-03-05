package org.lilachshop.entities;

import java.io.Serializable;

public enum ActiveDisabledState implements Serializable {
    ACTIVE("פעיל"),
    DISABLED("מושבת");

    private String state;

    ActiveDisabledState(String string) {
        state = string;
    }

    @Override
    public String toString() {
        return state;
    }
}
