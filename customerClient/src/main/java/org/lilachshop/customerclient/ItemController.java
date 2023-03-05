package org.lilachshop.customerclient;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.text.Text;
import org.lilachshop.commonUtils.Utilities;
import org.lilachshop.entities.Item;
import org.w3c.dom.events.MouseEvent;

import java.util.Objects;

public class ItemController {

    @FXML // fx:id="Img"
    private ImageView Img; // Value injected by FXMLLoader

    @FXML // fx:id="Name"
    private Label Name; // Value injected by FXMLLoader

    @FXML // fx:id="Price"
    private Text Price; // Value injected by FXMLLoader

    @FXML // fx:id="Price1"
    private Label Price1; // Value injected by FXMLLoader

    @FXML // fx:id="oldPrice"
    private Text oldPrice; // Value injected by FXMLLoader

    @FXML // fx:id="oldPrice1"
    private Text oldPrice1; // Value injected by FXMLLoader

    @FXML // fx:id="saleImage"
    private ImageView saleImage; // Value injected by FXMLLoader

    @FXML
    private void click(MouseEvent mouseEvent){
        myListener.onClickListener(flower);
    }

    private MyListener myListener;
    private Item flower;


    /**
     * upload the data of the item in the catalog
     */
    public void setData(Item flower, MyListener myListener) {
        this.flower = flower;
        this.myListener = myListener;
        Name.setText(flower.getName());

        Price.setText(String.valueOf(flower.getPrice()));
        try{
//            Image image = new Image((Objects.requireNonNull(getClass().getResourceAsStream(flower.getImage()))));
            Image image = Utilities.bytesToImageConverter(flower.getImageBlob());
            Img.setImage(image);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        //if the item is on sale
        if (flower.getPercent()>0)
        {
            oldPrice.setVisible(true);
            oldPrice.setText(String.valueOf(flower.getPrice()));
            Price.setText(String.valueOf(flower.getPrice()*(100-flower.getPercent())/100));
            oldPrice1.setVisible(true);
            saleImage.setVisible(true);

        }
    }
    //set the flower in the left side
    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        myListener.onClickListener(flower);
    }

    void setPriceInCatalog(Item flower){
        this.flower.setPrice(flower.getPrice());
        Price.setText(String.valueOf(flower.getPrice()) );





    }

}
