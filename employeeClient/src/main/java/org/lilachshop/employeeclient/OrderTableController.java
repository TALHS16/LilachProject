/**
 * Sample Skeleton for 'OrderTable.fxml' Controller Class
 */

package org.lilachshop.employeeclient;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.commonUtils.OrderType;
import org.lilachshop.entities.*;
import org.lilachshop.events.OrderEvent;
import org.lilachshop.events.RefreshCatalogEvent;
import org.lilachshop.panels.*;

public class OrderTableController {

    private static Stage stage = null;
    private static Parent root = null;
    private static FXMLLoader fxmlLoader = null;
    private static Scene scene = null;
    private long currentStoreID;
    public static Panel panel;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="DeliveryMethodCol"
    private TableColumn<OrderTableEntry, OrderType> DeliveryMethodCol; // Value injected by FXMLLoader

    @FXML // fx:id="StoreChoiceBox"
    private ChoiceBox<Store> StoreChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="creationDateCol"
    private TableColumn<OrderTableEntry, LocalDateTime> creationDateCol; // Value injected by FXMLLoader

    @FXML // fx:id="customerNameCol"
    private TableColumn<OrderTableEntry, String> customerNameCol; // Value injected by FXMLLoader

    @FXML // fx:id="deliveryAddressCol"
    private TableColumn<OrderTableEntry, String> deliveryAddressCol; // Value injected by FXMLLoader

    @FXML // fx:id="deliveryTimeCol"
    private TableColumn<OrderTableEntry, LocalDateTime> deliveryTimeCol; // Value injected by FXMLLoader

    @FXML // fx:id="orderIDcol"
    private TableColumn<OrderTableEntry, Long> orderIDcol; // Value injected by FXMLLoader

    @FXML // fx:id="orderTable"
    private TableView<OrderTableEntry> orderTable; // Value injected by FXMLLoader

    @FXML // fx:id="refundCol"
    private TableColumn<OrderTableEntry, Double> refundCol; // Value injected by FXMLLoader

    @FXML // fx:id="statusChoiceBox"
    private ChoiceBox<?> statusChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="statusCol"
    private TableColumn<OrderTableEntry, OrderStatus> statusCol; // Value injected by FXMLLoader

    @FXML // fx:id="totalPriceCol"
    private TableColumn<OrderTableEntry, Double> totalPriceCol; // Value injected by FXMLLoader

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert DeliveryMethodCol != null : "fx:id=\"DelevieryMethodCol\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert StoreChoiceBox != null : "fx:id=\"StoreChoiceBox\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert creationDateCol != null : "fx:id=\"creationDateCol\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert customerNameCol != null : "fx:id=\"customerNameCol\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert deliveryAddressCol != null : "fx:id=\"deliveryAddressCol\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert deliveryTimeCol != null : "fx:id=\"deliveryTimeCol\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert orderIDcol != null : "fx:id=\"orderIDcol\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert orderTable != null : "fx:id=\"orderTable\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert refundCol != null : "fx:id=\"refundCol\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert statusChoiceBox != null : "fx:id=\"statusChoiceBox\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert statusCol != null : "fx:id=\"statusCol\" was not injected: check your FXML file 'OrderTable.fxml'.";
        assert totalPriceCol != null : "fx:id=\"totalPriceCol\" was not injected: check your FXML file 'OrderTable.fxml'.";
        DeliveryMethodCol.setCellValueFactory(new PropertyValueFactory<OrderTableEntry, OrderType>("method"));
        deliveryTimeCol.setCellValueFactory(new PropertyValueFactory<OrderTableEntry, LocalDateTime>("deliveryTime"));
        creationDateCol.setCellValueFactory(new PropertyValueFactory<OrderTableEntry, LocalDateTime>("createDate"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<OrderTableEntry, String>("ownerName"));
        deliveryAddressCol.setCellValueFactory(new PropertyValueFactory<OrderTableEntry, String>("address"));
        orderIDcol.setCellValueFactory(new PropertyValueFactory<OrderTableEntry, Long>("id"));
        refundCol.setCellValueFactory(new PropertyValueFactory<OrderTableEntry, Double>("refund"));
        statusCol.setCellValueFactory(new PropertyValueFactory<OrderTableEntry, OrderStatus>("status"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<OrderTableEntry, Double>("totalPrice"));
        if (panel != null) {
            panel.closeConnection();
            panel = null;
        }
        panel = OperationsPanelFactory.createPanel(DashBoardController.panelEnum, EmployeeApp.getSocket(), this);
        ((SignedInEmployeePanel) panel).getAllStoresForReports();

        //Setting up listener to CatalogChoiceBox
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if ((newValue != null)) {
                    Store store = (Store) newValue;
                    ((SignedInEmployeePanel) panel).sendGetOrdersFromServer(store.getId());
                    currentStoreID = store.getId();
                }
            }
        };
        StoreChoiceBox.getSelectionModel().selectedItemProperty().addListener(changeListener);

        orderTable.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                OrderTableEntry selectedOrder = orderTable.getSelectionModel().getSelectedItem();
                openOrder(selectedOrder);
            }
        });
    }

    public void openOrder(OrderTableEntry orderEntry) {
        setUpOrderPopUp();
        OrderTablePopUpController controller = fxmlLoader.getController();
        scene = scene == null ? new Scene(root) : scene;
        stage.setScene(scene);
        controller.setData(orderEntry);
        controller.choiceStatus.getSelectionModel().select(orderEntry.getOrder().getOrderStatus());
        stage.show();

    }

    private void setUpOrderPopUp() {
        stage = stage == null ? new Stage() : stage;
        fxmlLoader = fxmlLoader == null ? new FXMLLoader(EmployeeApp.class.getResource("OrderTablePopUp.fxml")) : fxmlLoader;
        if (root == null) {
            try {
                root = fxmlLoader.load();
            } catch (Exception e) {
            }
        }
    }

    @Subscribe
    public void onGetAllStores(List<Store> storeList) {
        Platform.runLater(()->{
            if (DashBoardController.panelEnum.equals(PanelEnum.GENERAL_EMPLOYEE) || DashBoardController.panelEnum.equals(PanelEnum.STORE_MANAGER))
            {
                for (Store store: storeList) {
                    if(store.getId().equals(DashBoardController.employee.getStore().getId()))
                        StoreChoiceBox.setItems(FXCollections.observableArrayList(store));
                }
            }
            else
            {
                StoreChoiceBox.setItems(FXCollections.observableArrayList(storeList));
                StoreChoiceBox.getItems().remove(0);
            }
            StoreChoiceBox.getSelectionModel().select(0);
        });
    }
    @Subscribe
    public void handleOrdersByIDStore(OrderEvent orders) {
        Platform.runLater(() -> {
            List<OrderTableEntry> orderTableEntries;
            orderTableEntries = convertOrderToOrderTableEntry(orders);
            ObservableList<OrderTableEntry> observableOrderList = FXCollections.observableArrayList(orderTableEntries);
            orderTable.getItems().clear();
            orderTable.setItems(observableOrderList);
            orderTable.refresh();
        });
    }

    private List<OrderTableEntry> convertOrderToOrderTableEntry(OrderEvent orders) {
        List<OrderTableEntry> orderTableEntries = new LinkedList<>();
        for (Order orderIter : orders.getOrders()) {
            OrderTableEntry orderTableEntry = new OrderTableEntry();
            orderTableEntry.setOrder(orderIter);
            orderTableEntries.add(orderTableEntry);
        }
        return orderTableEntries;
    }
    @Subscribe
    public void onHandleMessageFromServer(String reply) {
        Platform.runLater(() -> {
            if (reply.equals("order update success")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("עדכון הזמנה");
                alert.setContentText("ההזמנה עודכנה בהצלחה!");
                alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                ButtonType button = new ButtonType("אישור");
                alert.getButtonTypes().setAll(button);
                alert.show();
                ((SignedInEmployeePanel) panel).sendGetOrdersFromServer(currentStoreID);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("שגיאה");
                alert.setContentText("עדכון ההזמנה נכשל");
                alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                ButtonType button = new ButtonType("אישור");
                alert.getButtonTypes().setAll(button);
                alert.show();
            }
        });

    }

}
