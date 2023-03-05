/**
 * Sample Skeleton for 'main.fxml' Controller Class
 */

package org.lilachshop.customerclient;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.commonUtils.Utilities;
import org.lilachshop.entities.*;
import org.lilachshop.panels.*;


public class CatalogController {
    private static Panel panel;

    @FXML
    public HBox resetFilterBtn;

    Boolean switchFlag = false;
    private Item flowerShown;

    private MyListener myListener;
    private List<myOrderItem> myFlowers = null;
    int count = 0;
    private static final int MAX_ON_SALE = 10;
    static boolean onStartUp = true;

    private static ObservableList<Store> allStores = FXCollections.observableArrayList();

    @FXML
    private Label history;

    @FXML
    private Text itemId;

    @FXML
    private Text itemType;

    @FXML
    private Text textDesc;


    @FXML
    private ImageView historyImg;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private HBox itemLayout;

    @FXML // fx:id="FlowerImg"
    private ImageView FlowerImg; // Value injected by FXMLLoader

    @FXML // fx:id="FlowerNameLabel"
    private Label FlowerNameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="FlowerPrice"
    private Label FlowerPrice; // Value injected by FXMLLoader

    @FXML // fx:id="addToCart"
    private Button addToCart; // Value injected by FXMLLoader

    @FXML // fx:id="chosenFlower"
    public VBox chosenFlower; // Value injected by FXMLLoader

    @FXML // fx:id="countItems"
    private Label countItems; // Value injected by FXMLLoader


    @FXML
    private HBox logout;

    @FXML
    private FlowPane grid;

    @FXML // fx:id="name"
    private Label name; // Value injected by FXMLLoader

    @FXML // fx:id="oldPrice"
    private Text oldPrice; // Value injected by FXMLLoader

    @FXML // fx:id="oldPrice1"
    private Text oldPrice1; // Value injected by FXMLLoader

    @FXML // fx:id="saleImag"
    private ImageView saleImag; // Value injected by FXMLLoader

    @FXML // fx:id="scroll"
    private ScrollPane scroll; // Value injected by FXMLLoader

    @FXML // fx:id="shopList"
    private ChoiceBox<Store> storeChoiceBox; // Value injected by FXMLLoader
    private List<Item> flowerList;


    /**
     * set the new scene cart
     * and sent the order list
     */
    @FXML
    void gotoCart(MouseEvent event) {
        if (myFlowers != null && myFlowers.size() > 0) {
            Stage stage = CustomerApp.getStage();
            try {
                CustomerApp.setMyFlowers(myFlowers);
                FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("cart.fxml"));
                Parent root = fxmlLoader.load();
                stage.setScene(new Scene(root));
                stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, CustomerApp::onCloseWindowEvent);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            if (!switchFlag) {
                ButtonType button = new ButtonType("אישור");
                a.getButtonTypes().setAll(button);
                a.setHeaderText("נא התחבר או הרשם למערכת");
                a.setTitle("הוספה לסל");
            } else {
                ButtonType button = new ButtonType("אישור");
                a.getButtonTypes().setAll(button);
                a.setHeaderText("הסל ריק נא הוסף מוצרים לסל");
                a.setTitle("הוספה לסל");
            }
            a.setContentText("");
            a.show();
        }
    }

    @FXML
    void openPersonalArea(ActionEvent event) {

    }

    public void gotoHistory(MouseEvent event) {
        ((StoreCustomerPanel) CustomerApp.getPanel()).sendGetAllOrdersToServer(CustomerApp.getMyCustomer().getId());

    }

    @FXML
    void gotoSignUp(MouseEvent event) throws IOException {
        if (!switchFlag) {
            Stage stage = CustomerApp.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("SignupLogin.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, CustomerApp::onCloseWindowEvent);
            stage.show();

        }
    }

    /**
     * add the item to the list
     * if exit increase the amount else create new
     */
    @FXML
    void onAddToCart(ActionEvent event) {
        boolean addFlag = false;
        if (switchFlag) {
            for (myOrderItem flower : myFlowers) {
                if (flowerShown == flower.getItem()) {
                    flower.setCount(flower.getCount() + 1);
                    addFlag = true;
                }
            }
            if (!addFlag) {
                myOrderItem newFlower = new myOrderItem(flowerShown, 1);
                myFlowers.add(newFlower);
            }
            count++;
            countItems.setText(String.valueOf(count));
        } else {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            ButtonType button = new ButtonType("אישור");
            a.getButtonTypes().setAll(button);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("נא התחבר או הרשם למערכת");
            a.setTitle("הוספה לסל");
            a.setContentText("");
            a.show();

        }
    }

    @FXML
    void onFilter(MouseEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("filter.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {

        }
    }

    @FXML
// This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        count = 0;
        storeChoiceBox.setDisable(true);
        countItems.setText(String.valueOf(count));
    }

    /**
     * set the chosen item from the catalog in the fields on the left side of the scene
     */
    public void setChosenItem(Item flower) {
        FlowerNameLabel.setText(flower.getName());
        itemId.setText(Integer.toString(flower.getId()));
        if (flower.getItemType() == null) {
            itemType.setText("");
        } else {
            itemType.setText(flower.getItemType().toString());
        }
        textDesc.setText(flower.getDescription());

        if (flower.getPercent() > 0) {
            System.out.println(flower.getPercent() > 0);
            oldPrice.setVisible(true);
            oldPrice.setText(String.valueOf(flower.getPrice()));
            FlowerPrice.setText(String.valueOf(flower.getPrice() * (100 - flower.getPercent()) / 100));
            oldPrice1.setVisible(true);
            saleImag.setVisible(true);

        } else {
            FlowerPrice.setText(String.valueOf(flower.getPrice()));
            oldPrice.setVisible(false);
            oldPrice1.setVisible(false);
            saleImag.setVisible(false);
        }
        try {
            FlowerImg.setImage(Utilities.bytesToImageConverter(flower.getImageBlob()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        flowerShown = flower;
    }

    public void showInfo(List<Item> flowerList, CustomerApp customerApp) {

        this.myFlowers = CustomerApp.getMyFlowers();
        if (myFlowers != null && myFlowers.size() > 0) {
            for (myOrderItem flower : myFlowers)
                count += flower.getCount();
        }
        countItems.setText(String.valueOf(count));
        CustomerApp.setApp(customerApp);
        this.flowerList = flowerList;
        if (CustomerApp.getMyCustomer() != null) {
            switchFlag = true;
            name.setText("שלום, " + CustomerApp.getMyCustomer().getName());
            history.setVisible(true);
            historyImg.setVisible(true);

        } else {
            switchFlag = false;
            name.setText("התחבר/הרשם");
            logout.setVisible(false);
            history.setVisible(false);
            historyImg.setVisible(false);
        }
        int countOnSale = 0;
        System.out.println("catalogController recieved message from server");
        if (flowerList.size() > 0) {
            setChosenItem(flowerList.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(Item flower) {
                    setChosenItem(flower);
                }
            };
        }
        grid.getChildren().clear();
        itemLayout.getChildren().clear();
        try {
            if (flowerList.size() > 0) {
                for (Item flower : flowerList) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();

                    ItemController itemController = fxmlLoader.getController();
                    itemController.setData(flower, myListener);
                    grid.getChildren().add(anchorPane);
                    grid.setVgap(30);
                    grid.setHgap(45);
                    grid.setAlignment(Pos.CENTER);

                    if (countOnSale <= MAX_ON_SALE && flower.getPercent() > 0) {
                        try {
                            FXMLLoader fxmlLoaderSale = new FXMLLoader();
                            fxmlLoaderSale.setLocation(getClass().getResource("saleItem.fxml"));
                            AnchorPane anchorPaneSale = fxmlLoaderSale.load();
                            SaleItemController saleItemController = fxmlLoaderSale.getController();
                            saleItemController.setData(flower, myListener);

                            itemLayout.getChildren().add(anchorPaneSale);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                chosenFlower.setVisible(false);
            }
        } catch (IOException e) {

        }

        if (CustomerApp.getMyCustomer() != null && !CustomerApp.getMyCustomer().getAccount().getAccountType().equals(AccountType.STORE_ACCOUNT)) {

            storeChoiceBox.setDisable(false);
            if (panel != null) {
                panel.closeConnection();
                panel = null;
            }
            panel = OperationsPanelFactory.createPanel(CustomerApp.panelEnum, CustomerApp.getSocket(), this);
            if (CatalogController.allStores.isEmpty())
                ((ChainCustomerPanel) panel).getAllStores();
            else {
                storeChoiceBox.setItems(CatalogController.allStores);
                storeChoiceBox.setOnAction((event) -> {
                    System.out.println("hhaaa");
                    Store selectedItem = storeChoiceBox.getSelectionModel().getSelectedItem();
                    CustomerApp.setMyStore(selectedItem);
                    CustomerApp.setStoreId(selectedItem.getId());
                    CustomerApp.setMyFlowers(new ArrayList<myOrderItem>());
                    grid.getChildren().clear();
                    chosenFlower.setVisible(false);
                    CustomerApp.getCustomerCatalog();
                });
            }
        }
    }

    public ChoiceBox<Store> getStoreChoiceBox() {
        return storeChoiceBox;
    }

    @Subscribe
    public void onGetAllStores(List<Store> allStores) {
        Platform.runLater(() -> {
            CatalogController.allStores = FXCollections.observableArrayList(allStores);
            CatalogController.allStores.remove(0);
            storeChoiceBox.setItems(CatalogController.allStores);
//            storeChoiceBox.getItems().remove(0);
            storeChoiceBox.getSelectionModel().selectFirst();
            storeChoiceBox.setOnAction((event) -> {
                System.out.println("hhaaa");
                Store selectedItem = storeChoiceBox.getSelectionModel().getSelectedItem();
                CustomerApp.setMyStore(selectedItem);
                CustomerApp.setStoreId(selectedItem.getId());
                CustomerApp.setMyFlowers(new ArrayList<myOrderItem>());
                grid.getChildren().clear();
                chosenFlower.setVisible(false);
                CustomerApp.getCustomerCatalog();
            });

        });
    }

    @FXML
    void onLogOut(MouseEvent event) {
        ((StoreCustomerPanel) CustomerApp.getPanel()).sendSignOutRequestToServer((User) CustomerApp.getMyCustomer());
        CustomerApp.setMyCustomer(null);
        CustomerApp.setMyFlowers(new LinkedList<myOrderItem>());
        CustomerApp.createPanel();

    }

    @FXML
    public void onClickReset(MouseEvent event) {
        if (CustomerApp.getMyCustomer() == null) {
            ((CustomerAnonymousPanel) CustomerApp.getPanel()).sendGetGeneralCatalogRequestToServer();
        } else {
            ((StoreCustomerPanel) CustomerApp.getPanel()).sendGetCatalogRequestToServer(CustomerApp.getMyStore().getId());
        }
    }

}
