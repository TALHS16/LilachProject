package org.lilachshop.requests;

import org.lilachshop.entities.Color;
import org.lilachshop.entities.Item;
import org.lilachshop.entities.ItemType;

public class CatalogRequest extends Request {
    private int minPrice;
    private int maxPrice;
    private Color color = null;
    private ItemType type = null;
    long Id;
    Item item;

    public CatalogRequest(String request, long catalogId, int minPrice, int maxPrice, Color color, ItemType type) {
        super(request);
        Id = catalogId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.color = color;
        this.type = type;
    }

    public CatalogRequest(String request, long catalogId, Item item) {
        super(request);
        Id = catalogId;
        this.item = item;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public Color getColor() {
        return color;
    }

    public ItemType getType() {
        return type;
    }

    public CatalogRequest(String request, long catalogId) {
        super(request);
        this.Id = catalogId;
    }

    public CatalogRequest(String get_all_catalogs) {
        super(get_all_catalogs);
    }

    public long getId() {
        return Id;
    }

    public Item getItem() {
        return item;
    }
}
