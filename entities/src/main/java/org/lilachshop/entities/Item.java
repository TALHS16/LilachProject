package org.lilachshop.entities;

import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Locale;
import java.util.Objects;


@Entity
@Table(name = "Items")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "item_name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    public Item(String name, String description, int percent, int price, ItemType itemType, Color color, byte[] imageBlob) {
        this.name = name;
        this.description = description;
        this.percent = percent;
        this.price = price;
        this.itemType = itemType;
        this.color = color;
        this.imageBlob = imageBlob;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) throws Exception {
        if ((percent > 100) || (percent < 0)) {
            throw new Exception("הנחה לא יכולה להיות יותר מ-100 אחוז");
        } else {
            this.percent = percent;
        }
    }

    String description;

    private int percent = 0;

    private int price;


    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Lob
    private byte[] imageBlob;

    public Item() {
    }

    @Override
    public String toString() {
        return "item name: " + name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item(String name, int price, String imagePath, int percent) {
        super();
        this.name = name;
        this.price = price;
        this.percent = percent;
        try {
            setImageBlob(imagePath);
        } catch (Exception e) {
            System.out.println("Load image failed for " + imagePath);
            e.printStackTrace();
        }
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Color getColor() {
        return color;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    private void setImageBlobInternal(BufferedImage image, String extension) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, extension, out);
        imageBlob = out.toByteArray();
    }

    private void setImageBlob(String imgPath) throws IOException, NullPointerException {
        File imageFile = new File(imgPath);
        BufferedImage image = ImageIO.read(imageFile);
        setImageBlobInternal(image, imageFile.getName().split("\\.")[1].toUpperCase(Locale.ROOT));
    }

    /*public BufferedImage getImageBlob() throws IOException {
        InputStream in = new ByteArrayInputStream(imageBlob);
        return ImageIO.read(in);
    }*/

    public InputStream getImageInputStream() {
        return new ByteArrayInputStream(imageBlob);
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id;
    }

    public void setAsCopyOf(Item item) {
        this.name = item.getName();
        this.price = item.getPrice();
        this.percent = item.getPercent();
        this.description = item.getDescription();
        this.itemType = item.getItemType();
        this.color = item.getColor();
        this.imageBlob = item.getImageBlob();
    }
}
