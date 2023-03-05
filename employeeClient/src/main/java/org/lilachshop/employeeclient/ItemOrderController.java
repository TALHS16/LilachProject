/**
 * Sample Skeleton for 'item_cart.fxml' Controller Class
 */

package org.lilachshop.employeeclient;

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

import java.net.URL;
import java.util.ResourceBundle;

public class ItemOrderController {

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


    @FXML
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
