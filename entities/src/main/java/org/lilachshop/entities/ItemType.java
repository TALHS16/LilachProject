package org.lilachshop.entities;

import java.io.Serializable;

public enum ItemType implements Serializable {
    BOUQUET("זר פרחים"),
    VASE("אגרטל"),
    ARRANGEMENT("סידור פרחים"),
    PLANTS("עציץ"),
    BRIDAL("זר כלה"),
    OTHER("אחר");

    private String string;

    ItemType(String name) {
        {
            string = name;
        }
    }

    @Override
    public String toString() {
        return string;
    }
}
