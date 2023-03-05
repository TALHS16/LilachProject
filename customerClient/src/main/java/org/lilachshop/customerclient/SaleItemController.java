/**
 * Sample Skeleton for 'saleItem.fxml' Controller Class
 */

package org.lilachshop.customerclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.lilachshop.entities.Item;
import org.w3c.dom.events.MouseEvent;

public class SaleItemController {
    private MyListener myListener;
    private Item flower;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="flowerName"
    private Text flowerName; // Value injected by FXMLLoader

    @FXML // fx:id="price"
    private Text price; // Value injected by FXMLLoader

    @FXML // fx:id="price1"
    private Text price1; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

    @FXML
    private void click(MouseEvent mouseEvent){
        myListener.onClickListener(flower);
    }

    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        myListener.onClickListener(flower);
    }



    /**
     * upload the data of the item and set as sale item
     */
    public void setData(Item flower, MyListener myListener)
    {
        this.flower = flower;
        this.myListener = myListener;
        flowerName.setText(flower.getName());
        price.setText(String.valueOf(flower.getPrice()*(100-flower.getPercent())/100));
    }

}
