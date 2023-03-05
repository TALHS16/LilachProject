package org.lilachshop.customerclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.commonUtils.Socket;
import org.lilachshop.events.ItemsEvent;
import org.lilachshop.entities.*;
import org.lilachshop.events.OrderEvent;
import org.lilachshop.panels.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * JavaFX App
 */
public class CustomerApp extends Application {
    static Socket socket = null;
    private static Panel panel;
    private static CustomerApp controller;
    private static final int shipPrice = 15;
    private static Customer myCustomer = null;
    private static Store myStore = null;
    static PanelEnum panelEnum = PanelEnum.CUSTOMER_ANONYMOUS;

    public static Panel getPanel() {
        return panel;
    }

    public static Store getMyStore() {
        return myStore;
    }

    public static void setPanel(Panel panel) {
        CustomerApp.panel = panel;
    }

    public static Customer getMyCustomer() {
        return myCustomer;
    }

    public static void setMyCustomer(Customer myCustomer) {
        CustomerApp.myCustomer = myCustomer;
    }

    public static List<myOrderItem> getMyFlowers() {
        return myFlowers;
    }

    public static void setMyStore(Store myStore) {
        CustomerApp.myStore = myStore;
    }

    public static void setMyFlowers(List<myOrderItem> myFlowers) {
        CustomerApp.myFlowers = myFlowers;
    }

    public static void setStoreId(long storeId) {
        CustomerApp.storeId = storeId;
    }

    private static Scene scene;
    private static Stage stage;

    public static List<Order> getMyOrders() {
        return myOrders;
    }

    public static void setMyOrders(List<Order> myOrders) {
        CustomerApp.myOrders = myOrders;
    }

    private static List<myOrderItem> myFlowers;
    private static List<Order> myOrders;
    private static long storeId;

    public static Stage getStage() {
        return stage;
    }

    public static void createPanel() {
        if (myCustomer == null) {
            panel = OperationsPanelFactory.createPanel(PanelEnum.CUSTOMER_ANONYMOUS, getSocket(), controller);
            ((CustomerAnonymousPanel) panel).getAllStores();
            CustomerApp.panelEnum = PanelEnum.CUSTOMER_ANONYMOUS;
        } else {
            myStore = myCustomer.getStore();
            AccountType userAccountType = myCustomer.getAccount().getAccountType();
            if (userAccountType.equals(AccountType.CHAIN_ACCOUNT)) {
                panel = OperationsPanelFactory.createPanel(PanelEnum.CHAIN_CUSTOMER, getSocket(), controller);
                storeId = myCustomer.getStore().getId();
                CustomerApp.panelEnum = PanelEnum.CHAIN_CUSTOMER;
            } else if (userAccountType.equals(AccountType.STORE_ACCOUNT)) {
                panel = OperationsPanelFactory.createPanel(PanelEnum.STORE_CUSTOMER, getSocket(), controller);
                storeId = myCustomer.getStore().getId();
                CustomerApp.panelEnum = PanelEnum.STORE_CUSTOMER;
            } else {
                panel = OperationsPanelFactory.createPanel(PanelEnum.ANNUAL_CUSTOMER, getSocket(), controller);
                storeId = myCustomer.getStore().getId();
                CustomerApp.panelEnum = PanelEnum.ANNUAL_CUSTOMER;
            }
            getCustomerCatalog();
        }
    }


    public static void getCustomerCatalog() {
        ((StoreCustomerPanel) panel).sendGetCatalogRequestToServer(storeId);
    }

    public static CustomerApp getApp() {
        return controller;
    }

    public static void setApp(CustomerApp customerApp) {
        CustomerApp.controller = customerApp;
    }

    public static int getShipPrice() {
        return shipPrice;
    }

    @Override
    public void init() throws Exception {
        System.out.println("Starting customer application...");
        panel = OperationsPanelFactory.createPanel(PanelEnum.CUSTOMER_ANONYMOUS, getSocket(), this);
    }

    @Override
    public void start(Stage stage) throws IOException {
        CustomerApp.stage = stage;
        ((CustomerAnonymousPanel) panel).getAllStores();
    }

    @Subscribe
    public void handleMessageReceivedFromClient(List<Store> msg) {
        if (!msg.isEmpty()) {
            CustomerApp.myStore = msg.get(0);
            ((CustomerAnonymousPanel) panel).sendGetGeneralCatalogRequestToServer();
        } else
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                alert.setHeaderText("קטלוג לא נמצא.");
                alert.setContentText("אנא נסה שנית במועד מאוחר יותר.");
                alert.show();
                System.exit(-1);
            });
    }

    @Subscribe
    public void handleMessageReceivedFromClient(ItemsEvent msg) {
        Platform.runLater(() -> {
            try {
                List<Item> flowerList = msg.getItems();
                FXMLLoader fxmlLoader = new FXMLLoader(CustomerApp.class.getResource("main.fxml"));
                Parent root = fxmlLoader.load();
                CatalogController controller = fxmlLoader.getController();
                controller.chosenFlower.setVisible(true);
                if (myCustomer == null || myCustomer.getAccount().getAccountType().equals(AccountType.STORE_ACCOUNT)) {
                    controller.getStoreChoiceBox().setItems(FXCollections.observableArrayList(myStore));
                    controller.getStoreChoiceBox().getSelectionModel().selectFirst();
                } else //
                    controller.getStoreChoiceBox().getSelectionModel().select(myStore);
                controller.showInfo(flowerList, this);
                stage.setScene(new Scene(root));
                stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, CustomerApp::onCloseWindowEvent);
                stage.show();
            } catch (IOException e) {

            }
        });
    }

    @Subscribe
    public void handleReceivedOrderList(OrderEvent msg) {
        if (msg.getOrders().size() > 0) {
            Platform.runLater(() -> {
                try {
                    Stage stage = CustomerApp.getStage();
                    CustomerApp.setMyOrders(msg.getOrders());
                    FXMLLoader fxmlLoader = new FXMLLoader(CustomerApp.class.getResource("history.fxml"));
                    Parent root = fxmlLoader.load();
                    stage.setScene(new Scene(root));
                    stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, CustomerApp::onCloseWindowEvent);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Platform.runLater(() -> {
                Alert a = new Alert(Alert.AlertType.NONE);
                ButtonType button = new ButtonType("אישור");
                a.getButtonTypes().setAll(button);
                a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setHeaderText("הסטוריית הזמנות ריקה");
                a.setTitle("הסטוריית הזמנות");
                a.setContentText("");
                a.show();
            });
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CustomerApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
        setSocket(args);
        launch(args);
    }

    static void onCloseWindowEvent(WindowEvent event) {
        if (myCustomer != null)
            ((StoreCustomerPanel) CustomerApp.getPanel()).sendSignOutRequestToServer((User) CustomerApp.getMyCustomer());
        System.out.println("Graceful termination, goodbye ;)");
        System.exit(0);
    }

    public static Socket getSocket() {
        return socket;
    }

    private static void setSocket(String[] args) {
        try {
            int port = Socket.DEFAULT_PORT;
            try {
                port = Integer.parseInt(args[1]);
                socket = new Socket(args[0], port);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                socket = new Socket(args[0]);
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host.");
        } catch (IndexOutOfBoundsException e) {
            socket = new Socket();
        }
    }

}
