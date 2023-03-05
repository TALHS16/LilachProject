package org.lilachshop.employeeclient;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.commonUtils.Utilities;
import org.lilachshop.entities.*;
import org.lilachshop.events.OrderEvent;
import org.lilachshop.panels.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderReportController implements Initializable {
    public Employee employee;
    private Panel panel;
    private long storeManagerId;
    Catalog catalog;
    List<Order> orders = new ArrayList<>();
    List<Order> ordersFromAllStores = new ArrayList<>();
    List<Catalog> allCatalogs = new ArrayList<>();
    List<Item> itemsFromAllStores = new ArrayList<>();
    ObservableList<ItemSalesObservable> listOfObservableItems;
    @FXML
    private Label totalNumOfOrdersLabel;
    @FXML
    private Label totalNumOfOrders;
    @FXML
    private Button newScreenBtn;
    @FXML
    private Label totalChainNumberOfOrders;

    @FXML
    private Label chooseStoreLabel;

    @FXML
    private TableColumn<ItemSalesObservable, Integer> ItemName;

    @FXML
    private TableColumn<ItemSalesObservable, Integer> OrderNumber;

    @FXML
    private TableColumn<ItemSalesObservable, Integer> Price;

    @FXML
    private DatePicker endDate;

    @FXML
    private Button okBtn;

    @FXML
    private DatePicker startDate;

    @FXML
    private ComboBox<String> storeList;

    @FXML
    private TableView<ItemSalesObservable> tableView;

    @FXML
    void onChangeStore(ActionEvent event) {
        String selectedStore = storeList.getSelectionModel().getSelectedItem();
        tableView.getItems().clear();
        totalNumOfOrders.setText("");
    }

    @FXML
    void updateBarChart(ActionEvent event) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        try {
            start = startDate.getValue().atStartOfDay();
            end = endDate.getValue().atStartOfDay();
            if (start == null || end == null) {
                displayNullAlert();
                return;
            }
        } catch (Exception e) {
            displayNullAlert();
            return;
        }
        if (end.isBefore(start)) {
            displayChronologyAlert();
            return;
        }
        if (DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)) {
            if (storeList.getSelectionModel().getSelectedItem() == null) {
                displayNullStoreAlert();
                return;
            }
        }
        if(DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)){
            String selectedStore = storeList.getSelectionModel().getSelectedItem();
            if (selectedStore.equals("לילך הרצליה")) {
                ((StoreManagerPanel) panel).getStoreOrders(3);
                ((StoreManagerPanel) panel).getStoreCatalog(3);
                totalNumOfOrdersLabel.setText("כמות ההזמנות שבוצעו בחנות בזמן זה:");
            } else if (selectedStore.equals("לילך חיפה")) {
                ((StoreManagerPanel) panel).getStoreOrders(2);
                ((StoreManagerPanel) panel).getStoreCatalog(2);
                totalNumOfOrdersLabel.setText("כמות ההזמנות שבוצעו בחנות בזמן זה:");
            } else if (selectedStore.equals("לילך תל אביב")) {
                ((StoreManagerPanel) panel).getStoreOrders(4);
                ((StoreManagerPanel) panel).getStoreCatalog(4);
                totalNumOfOrdersLabel.setText("כמות ההזמנות שבוצעו בחנות בזמן זה:");
            } else if (selectedStore.equals("כל החנויות")) {
                ((StoreManagerPanel) panel).getStoreCatalog(2); //todo: change to general catalog!!!
                ((ChainManagerPanel) panel).getAllOrders();
                totalNumOfOrdersLabel.setText("כמות ההזמנות שבוצעו ברשת בזמן זה:");
            }
        }
        else {
            ((StoreManagerPanel) panel).getStoreOrders(storeManagerId);
            ((StoreManagerPanel) panel).getStoreCatalog(storeManagerId);
        }
        //calculating number of sales for every product
//        presentStoresIncomeData(start, end);

    }

    private void presentStoresIncomeData(LocalDateTime start, LocalDateTime end) {
        List<ItemSalesObservable> observableItems = getObservalbeItems();
        listOfObservableItems = FXCollections.observableArrayList();
        listOfObservableItems.addAll(observableItems);
        tableView.setEditable(true);
        tableView.setItems(listOfObservableItems);
        calcTotalNumOfOrders(start, end);
    }

    private void calcTotalNumOfOrders(LocalDateTime start, LocalDateTime end) {
        long todayOrderCounter = 0;
        long curTotalOrderForAllStores = 0;
        List<Order> relevantOrders = new ArrayList<>();
        List<Order> ordersToCalculateOn;

        if (DashBoardController.panelEnum.equals(PanelEnum.STORE_MANAGER)) {
            ordersToCalculateOn = orders;
        } else {
            if (storeList.getSelectionModel().getSelectedItem().equals("כל החנויות")) {
                totalNumOfOrdersLabel.setText("כמות ההזמנות שבוצעו ברשת בזמן זה:");
                ordersToCalculateOn = ordersFromAllStores;
            } else {
                totalNumOfOrdersLabel.setText("כמות ההזמנות שבוצעו בחנות בזמן זה:");
                ordersToCalculateOn = orders;
            }
        }
        for (Order order : ordersToCalculateOn) {
            if (!order.getOrderStatus().equals(OrderStatus.CANCELED)) {
                relevantOrders.add(order);
            }
        }
        try {
            for (LocalDateTime date = start; date.isBefore(end); date = date.plusDays(1)) {
                for (Order order : relevantOrders) {
                    if (Utilities.hasTheSameDate(date, order.getCreationDate())) {
                        //if (order.getCreationDate().equals(date)) {
                        todayOrderCounter += 1; // order.getAmountOfProducts();//1;
                    }
                }
                curTotalOrderForAllStores += todayOrderCounter;
                todayOrderCounter = 0;
            }
        } catch (Exception e) {
            System.out.println("cant calculate number of orders");
        }
        totalNumOfOrders.setText(String.valueOf(curTotalOrderForAllStores));
    }

    private List<ItemSalesObservable> getObservalbeItems() {
        List<ItemSalesObservable> itemSalesObservables = new ArrayList<>();
        List<Item> relevantItems = catalog.getItems();
        List<Catalog> relevantCatalogs;

        if (DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER) &&
                (storeList.getSelectionModel().getSelectedItem().equals("כל החנויות"))) {
            relevantCatalogs = allCatalogs;
        } else {
            relevantCatalogs = new ArrayList<>();
            relevantCatalogs.add(catalog);
        }

        for (Catalog catalog_iter : relevantCatalogs) {
            if (catalog_iter.getId() == 1) {
                continue;
            }
            for (Item item : catalog_iter.getItems()) {
                int numOfSales = getNumOfSalesForItem(item, catalog_iter.getId());
                ItemSalesObservable itemSalesObservable = new ItemSalesObservable(numOfSales, item.getPrice(), item.getName() + " - " + catalog_iter.toString());
                itemSalesObservables.add(itemSalesObservable);
            }
        }

        return itemSalesObservables;
    }

    private int getNumOfSalesForItem(Item item, long catalogId) { // todo: think of a more efficient way to calculate this
        int counter = 0;
        List<Order> ordersInDateRange = new ArrayList<>();
        LocalDateTime start = startDate.getValue().atStartOfDay();
        LocalDateTime end = endDate.getValue().atStartOfDay();
        // saving all orders in range in a list
        List<Order> relevantOrders;
        if (DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER) &&
                (storeList.getSelectionModel().getSelectedItem().equals("כל החנויות"))) {
            relevantOrders = ordersFromAllStores;
        } else {
            relevantOrders = orders;
        }

        for (LocalDateTime date = start; date.isBefore(end); date = date.plusDays(1)) {
            for (Order order : relevantOrders) {
                if (Utilities.hasTheSameDate(date, order.getCreationDate())
                        &&
                        !(order.getOrderStatus().equals(OrderStatus.CANCELED))) {
                    ordersInDateRange.add(order);
                }
            }
        }
        //finding number of sales
        for (Order order : ordersInDateRange) {
            List<myOrderItem> orderItems = order.getItems();
            for (myOrderItem itemFromOrder : orderItems) {
                if (itemFromOrder.getName().equals(item.getName())
                        && (order.getStore().getCatalog().getId() == ((int) catalogId))) {
                    counter += itemFromOrder.getCount();

                }
            }
        }
        return counter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panel = OperationsPanelFactory.createPanel(DashBoardController.panelEnum, EmployeeApp.getSocket(), this);
//        panel = OperationsPanelFactory.createPanel(PanelEnum.STORE_MANAGER, this);

        storeList.getItems().addAll("לילך חיפה", "לילך תל אביב", "לילך הרצליה", "כל החנויות");
//        storeList.promptTextProperty().set("לילך חיפה");
        ItemName.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
        OrderNumber.setCellValueFactory(new PropertyValueFactory<>("numOfSales"));
        Price.setCellValueFactory(new PropertyValueFactory<>("Price"));
    }

    private void displayChronologyAlert() {
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setHeaderText("יש לבחור תאריך התחלה מוקדם יותר מתאריך הסיום");
        a.setTitle("בחירת טווח תאריכים");
        a.setContentText("");
        a.show();
    }

    private void displayNullAlert() {
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setHeaderText("יש לבחור תאריך התחלה וסיום!");
        a.setTitle("בחירת טווח תאריכים");
        a.setContentText("");
        a.show();
    }

    private void displayNullStoreAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setHeaderText("שגיאה");
        a.setContentText("יש לבחור חנות");
        a.show();
    }

    @FXML
    void onNewScreenBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrderReport.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @Subscribe
    public void handleMessageFromClient(Catalog catalog) {
        this.catalog = catalog;
        Platform.runLater(() -> {
            LocalDateTime start = startDate.getValue().atStartOfDay();
            LocalDateTime end = endDate.getValue().atStartOfDay();
            try {
                presentStoresIncomeData(start, end);

            }catch (Exception ignored){

            }
        });
    }

    @Subscribe
    public void handleMessageFromClient(List<Order> orders) {
        this.orders = orders;
        Platform.runLater(() -> {
            LocalDateTime start = startDate.getValue().atStartOfDay();
            LocalDateTime end = endDate.getValue().atStartOfDay();
            try {
                presentStoresIncomeData(start, end);

            }catch (Exception ignored){

            }
        });
    }

    @Subscribe
    public void handleMessageAllOrdersFromClient(OrderEvent orderEvent) {
        this.ordersFromAllStores = orderEvent.getOrders();
        this.orders = orderEvent.getOrders();
        this.itemsFromAllStores = orderEvent.getItems();
        this.allCatalogs = orderEvent.getCatalogs();
        Platform.runLater(() -> {
            LocalDateTime start = startDate.getValue().atStartOfDay();
            LocalDateTime end = endDate.getValue().atStartOfDay();
            presentStoresIncomeData(start, end);
        });
    }

    public void setData(long storeId) {
        storeManagerId = storeId;
        if (DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)) {
//            ((ChainManagerPanel) panel).getStoreOrders(2);
//            ((ChainManagerPanel) panel).getStoreCatalog(2);
//            ((ChainManagerPanel) panel).getAllOrders();
            totalNumOfOrdersLabel.setText("");
        } else if (DashBoardController.panelEnum.equals(PanelEnum.STORE_MANAGER)) {
//            ((StoreManagerPanel) panel).getStoreOrders(storeId);
//            ((StoreManagerPanel) panel).getStoreCatalog(storeId);
            storeList.setVisible(false);
            chooseStoreLabel.setVisible(false);
            newScreenBtn.setVisible(false);
//            totalChainNumberOfOrders.setVisible(false);
            totalNumOfOrdersLabel.setText("כמות ההזמנות שבוצעו בחנות בזמן זה:");
        }
    }
}
