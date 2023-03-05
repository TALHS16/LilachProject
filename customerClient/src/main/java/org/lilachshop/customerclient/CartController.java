/**
 * Sample Skeleton for 'cart.fxml' Controller Class
 */

package org.lilachshop.customerclient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.entities.AccountType;
import org.lilachshop.entities.Order;
import org.lilachshop.entities.myOrderItem;
import org.lilachshop.events.UpdateCartEvent;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.RegisteredCustomerPanel;
import org.lilachshop.panels.StoreCustomerPanel;

public class CartController implements Initializable {
    List<myOrderItem> myFlowers = new ArrayList<>();

    int countItem = 0;
    Panel panel = null;
    Double sum = Double.valueOf(0);

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML
    private VBox itemLayout;

    @FXML
    private Label storeName;

    @FXML
    private Label finalPrice;

    @FXML
    private Label count;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="chosenFlower"
    private VBox chosenFlower; // Value injected by FXMLLoader

    @FXML // fx:id="createOrder"
    private Button createOrder; // Value injected by FXMLLoader

    @FXML // fx:id="emptyCart"
    private Button emptyCart; // Value injected by FXMLLoader

    @FXML // fx:id="name"
    private Label name; // Value injected by FXMLLoader

    /**
     * go from cart.fxml to main.fxml
     * load the catalog
     */
    @FXML
    void returnToCatalog(MouseEvent event) {
        CustomerApp.setMyFlowers(myFlowers);
        CustomerApp.getCustomerCatalog();

    }

    @FXML
    void gotoSignUpOrPersonalArea(MouseEvent event) {

    }

    @FXML
    void onCreateOrder(ActionEvent event) {
        if(Integer.parseInt(count.getText()) > 0)
        {

            if (CustomerApp.getMyCustomer().getAccount().getAccountType().equals(AccountType.ANNUAL_SUBSCRIPTION) && sum > 50)
            {
                sum -= sum*0.1;
            }
            sum += CustomerApp.getShipPrice();
            Stage stage = CustomerApp.getStage();
            try {
                FXMLLoader fxmlLoader1 = new FXMLLoader(CartController.class.getResource("OrderStage1.fxml"));
                Parent root = fxmlLoader1.load();
                OrderStage1Controller orderStage1Controller = fxmlLoader1.getController();
                Order myOrder = new Order(myFlowers,sum,countItem, CustomerApp.getMyCustomer());
                orderStage1Controller.showInfo(myOrder);
                stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, CustomerApp::onCloseWindowEvent);
                stage.setScene(new Scene(root));
                stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, CustomerApp::onCloseWindowEvent);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            alert.setHeaderText("ביצוע הזמנה:");
            alert.setContentText("הסל הינו ריק אנא מלא אותו כדי לבצע הזמנה");
            alert.show();
        }

    }

    /**
     * empty the list of the order
     * clean the board and the text fields
     */
    @FXML
    void onEmptyCart(ActionEvent event) {
        itemLayout.getChildren().clear();
        this.myFlowers.clear();
        this.count.setText("0");
        this.finalPrice.setText("0");

    }

    /**
     * upload the data of the order
     */
    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setText("שלום, " + CustomerApp.getMyCustomer().getName());
        sum = Double.valueOf(0);
        EventBus.getDefault().register(this);
        this.myFlowers = CustomerApp.getMyFlowers();
        if(panel != null){
            panel.closeConnection();
            panel = null;
        }
        panel = OperationsPanelFactory.createPanel(CustomerApp.panelEnum, CustomerApp.getSocket(), this);
        ((StoreCustomerPanel) panel).refreshCart(this.myFlowers);

        finalPrice.setText(String.valueOf(sum));
        count.setText(String.valueOf(countItem));

    }

    /**
     * handle the eventbus for remove and add the items
     * update the external fields of the item in case of remove and add
     */
    @Subscribe
    public void handleRemoveObject(CartEvent cartEvent) {
        if (cartEvent.action == "remove")
        {
            Platform.runLater(() -> {
                if (cartEvent.object != null) {
                    itemLayout.getChildren().remove(cartEvent.object);
                    myFlowers.remove(cartEvent.updateFlower);
                }
                count.setText(String.valueOf(Integer.parseInt(count.getText())-1));
                countItem--;
                if (cartEvent.updateFlower.getItem().getPercent() > 0)
                    sum -= cartEvent.updateFlower.getItem().getPrice()*(100- cartEvent.updateFlower.getItem().getPercent())/100;
                else
                    sum -= cartEvent.updateFlower.getItem().getPrice();
                finalPrice.setText(String.valueOf(sum));
            });
        }
        else if(cartEvent.action == "add")
        {
            count.setText(String.valueOf(Integer.parseInt(count.getText())+1));
            countItem++;
            if (cartEvent.updateFlower.getItem().getPercent() > 0)
                sum += cartEvent.updateFlower.getItem().getPrice()*(100- cartEvent.updateFlower.getItem().getPercent())/100;
            else
                sum += cartEvent.updateFlower.getItem().getPrice();
            finalPrice.setText(String.valueOf(sum));
        }
    }
    @Subscribe
    public void handleCartRefreshFromClient(UpdateCartEvent cartEvent){
        if(cartEvent.getAction().equals("updated item list success")){
            this.myFlowers = cartEvent.getMyOrderItemList();
        }
        Platform.runLater(()->{

            for (int i = 0; i < myFlowers.size(); i++) {
                //calculate the sum price of the order
                sum += (myFlowers.get(i).getItem().getPercent() > 0 ? myFlowers.get(i).getItem().getPrice() * (100 - myFlowers.get(i).getItem().getPercent()) / 100 : myFlowers.get(i).getItem().getPrice())*myFlowers.get(i).getCount();
                //calculate the amount of items that has in the order
                countItem += myFlowers.get(i).getCount();
                //load the item fxml
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("item_cart.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    ItemCartController itemCartController = fxmlLoader.getController();
                    //set the photo,name,price and amount from this flower
                    itemCartController.setData(myFlowers.get(i));
                    itemLayout.getChildren().add(anchorPane);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //set the total price and total amount of products
            finalPrice.setText(String.valueOf(sum));
            count.setText(String.valueOf(countItem));
        });
    }
}