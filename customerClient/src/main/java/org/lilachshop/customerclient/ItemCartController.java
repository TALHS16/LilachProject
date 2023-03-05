/**
 * Sample Skeleton for 'item_cart.fxml' Controller Class
 */

package org.lilachshop.customerclient;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.lilachshop.commonUtils.Utilities;
import org.lilachshop.entities.myOrderItem;

public class ItemCartController {

    @FXML
    private AnchorPane removeObject;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML
    private Label flowerName;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="item_img"
    private ImageView item_img; // Value injected by FXMLLoader

    @FXML // fx:id="oldPrice"
    private Text oldPrice; // Value injected by FXMLLoader

    @FXML // fx:id="oldPrice1"
    private Text oldPrice1; // Value injected by FXMLLoader

    @FXML // fx:id="add"
    private ImageView add; // Value injected by FXMLLoader

    @FXML // fx:id="amount"
    private TextField amount; // Value injected by FXMLLoader

    @FXML // fx:id="price"
    private Label price; // Value injected by FXMLLoader

    @FXML // fx:id="price1"
    private Label price1; // Value injected by FXMLLoader

    @FXML // fx:id="remove"
    private ImageView remove; // Value injected by FXMLLoader

    @FXML // fx:id="saleImage"
    private ImageView saleImage; // Value injected by FXMLLoader
    private myOrderItem flower;

    /**
     * on add item increase the amount from that flower
     * sent event bus to update the external fields
     */
    @FXML
    void addItem(MouseEvent event) {
        amount.setText(String.valueOf(Integer.parseInt(amount.getText()) + 1));
        flower.setCount(flower.getCount() + 1);
        EventBus.getDefault().post(new CartEvent(null, flower, "add"));
    }

    /**
     * on remove item increase the amount from that flower
     * sent event bus to update the external fields
     * if we have 0 items from this flower sent request by eventbus to remove this object from the list
     */
    @FXML
    void removeItem(MouseEvent event) {
        if (flower.getCount() - 1 > 0) {
            EventBus.getDefault().post(new CartEvent(null, flower, "remove"));
            amount.setText(String.valueOf(Integer.parseInt(amount.getText()) - 1));
            flower.setCount(flower.getCount() - 1);
        } else {
            EventBus.getDefault().post(new CartEvent(removeObject, flower, "remove"));
        }

    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

    /**
     * upload the data of the cart item
     */
    public void setData(myOrderItem flower) {
        this.flower = flower;
        amount.setText(String.valueOf(flower.getCount()));
        price.setText(String.valueOf(flower.getItem().getPrice()));
        flowerName.setText(flower.getItem().getName());
        if (flower.getItem().getPercent() > 0) {
            oldPrice.setVisible(true);
            oldPrice.setText(String.valueOf(flower.getItem().getPrice()));
            price.setText(String.valueOf(flower.getItem().getPrice() * (100 - flower.getItem().getPercent()) / 100));
            oldPrice1.setVisible(true);
            saleImage.setVisible(true);

        }
        try {
            Image image = Utilities.bytesToImageConverter(flower.getItem().getImageBlob());
            item_img.setImage(image);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
