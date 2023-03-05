package org.lilachshop.employeeclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

public class IncomeReportController implements Initializable {
    public Employee employee;
    List<Order> ordersFromAllStores;
    private Panel panel;
    long storeManagerId;
    @FXML
    private Label totalChainIncomeLabel;

    List<Order> orders;
    @FXML
    private Button newScreenBtn;
    @FXML
    private Label chooseStoreLabel;

    @FXML
    private BarChart<String, Integer> complaintBarChart;

    @FXML
    private NumberAxis complaintNumber;

    @FXML
    private Label TotalChainIncomeLabel;

    @FXML
    private CategoryAxis days;

    @FXML
    private DatePicker endDate;

    @FXML
    private Button okBtn;

    @FXML
    private DatePicker startDate;

    @FXML
    private ComboBox<String> storeList;

    @FXML
    private Label totalIncome;
    @FXML
    private Label totalincomeINS;

    @FXML
    void onChangeStore(ActionEvent event) {
        String selectedStore = storeList.getSelectionModel().getSelectedItem();
        complaintBarChart.getData().clear();
//        startDate.setValue(null);
//        endDate.setValue(null);
        totalIncome.setText("");
        totalChainIncomeLabel.setText("");
        totalincomeINS.setVisible(false);
//        if(selectedStore.equals("לילך הרצליה")){
//            ((ChainManagerPanel) panel).getStoreOrders(3);
//        }
//        else if(selectedStore.equals("לילך חיפה")){
//            ((ChainManagerPanel) panel).getStoreOrders(2);
//        }
//        else if(selectedStore.equals("לילך תל אביב")){
//            ((ChainManagerPanel) panel).getStoreOrders(4);
//        }
//        else if(selectedStore.equals("כל החנויות")){
//            ((ChainManagerPanel) panel).getAllOrders();
//        }
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
            long storeId;
            if (storeList.getSelectionModel().getSelectedItem().equals("לילך חיפה")) {
                storeId = 2;
            } else if (storeList.getSelectionModel().getSelectedItem().equals("לילך הרצליה")) {
                storeId = 3;
            } else {
                storeId = 4;
            }
            if (storeList.getSelectionModel().getSelectedItem().equals("כל החנויות")) {
                ((ChainManagerPanel) panel).getAllOrders();
            } else {
                ((ChainManagerPanel) panel).getStoreOrders(storeId);
            }
        } else {
            ((StoreManagerPanel) panel).getStoreOrders(storeManagerId);
        }


    }

    private void presentStoresIncomeData(LocalDateTime start, LocalDateTime end) {
        complaintBarChart.getXAxis().animatedProperty().set(false);
        complaintBarChart.getData().clear();

        String selectedStore = storeList.getSelectionModel().getSelectedItem();
        List<Order> ordersToCalculateOn;

        if (DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)) {
            if (selectedStore == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("שגיאה");
                alert.setContentText("יש לבחור חנות");
                alert.show();
                return;
            }
            if (selectedStore.equals("כל החנויות")) {
                ordersToCalculateOn = ordersFromAllStores;
            } else {
                ordersToCalculateOn = orders;
            }
        } else {
            ordersToCalculateOn = orders;
        }

        int todayIncomeCounter = 0;
        long curTotalIncome = 0;
        XYChart.Series<String, Integer> set = new XYChart.Series<>();
        for (LocalDateTime date = start; date.isBefore(end); date = date.plusDays(1)) {
            if (ordersToCalculateOn == null) {
                break;
            }
            for (Order order : ordersToCalculateOn) {
                if (Utilities.hasTheSameDate(date, order.getCreationDate()) &&
                        !order.getOrderStatus().equals(OrderStatus.CANCELED)) {
                    todayIncomeCounter += order.getTotalPrice();
                }
            }
            set.getData().add(new XYChart.Data<String, Integer>(date.toString(), todayIncomeCounter));
            curTotalIncome += todayIncomeCounter;
            todayIncomeCounter = 0;
        }
        complaintBarChart.getData().addAll(set);
        this.totalIncome.setText(String.valueOf(curTotalIncome));
        totalincomeINS.setVisible(true);
        if (DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)) {
            if (selectedStore.equals("כל החנויות")) {
                totalChainIncomeLabel.setText("סך הכנסות הרשת לפרק זמן זה:");
                calcTotalIncomeForAllStores(start, end);
            } else {
                totalChainIncomeLabel.setText("סך הכנסות החנות לפרק זמן זה:");
            }
        }
    }

    private void calculateForCurrentStore() {
    }

    private void calcTotalIncomeForAllStores(LocalDateTime start, LocalDateTime end) {
        long todayIncomeCounter = 0;
        long curTotalIncomeForAllStores = 0;
        List<Order> relevantOrders = new ArrayList<>();
        for (Order order : ordersFromAllStores) {
            if (!order.getOrderStatus().equals(OrderStatus.CANCELED)) {
                relevantOrders.add(order);
            }
        }
        for (LocalDateTime date = start; date.isBefore(end); date = date.plusDays(1)) {
            for (Order order : relevantOrders) {
                if (Utilities.hasTheSameDate(date, order.getCreationDate())) {
                    todayIncomeCounter += (order.getTotalPrice() - order.getRefund());
                }
            }
            curTotalIncomeForAllStores += todayIncomeCounter;
            todayIncomeCounter = 0;
        }
        totalIncome.setText(String.valueOf(curTotalIncomeForAllStores));
        totalincomeINS.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        panel = OperationsPanelFactory.createPanel(PanelEnum.STORE_MANAGER, this);

        storeList.getItems().addAll("לילך חיפה", "לילך תל אביב", "לילך הרצליה", "כל החנויות");
        //todo: if chain manger is logged in, do haifa, if store manger logged in, do store managers store
        //storeList.promptTextProperty().set("לילך חיפה");
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

    @FXML
    void onNewScreenBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("IncomeReport.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        IncomeReportController controller = fxmlLoader.getController();
        controller.setData(storeManagerId);
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @Subscribe
    public void handleMessageFromClient(List<Order> orders) {
        this.orders = orders;
        Platform.runLater(() -> {
            LocalDateTime start = startDate.getValue().atStartOfDay();
            LocalDateTime end = endDate.getValue().atStartOfDay();
            presentStoresIncomeData(start, end);
        });
    }

    @Subscribe
    public void handleMessageAllOrdersFromClient(OrderEvent orderEvent) {
        this.ordersFromAllStores = orderEvent.getOrders();
        Platform.runLater(() -> {
            LocalDateTime start = startDate.getValue().atStartOfDay();
            LocalDateTime end = endDate.getValue().atStartOfDay();
            presentStoresIncomeData(start, end);
        });
    }

    public void setData(long storeId) { // WILL NEED to "copy" this function to the others 2 reports orders
        panel = OperationsPanelFactory.createPanel(DashBoardController.panelEnum, EmployeeApp.getSocket(), this);
        storeManagerId = storeId;
        if (DashBoardController.panelEnum.equals(PanelEnum.STORE_MANAGER)) {
            newScreenBtn.setVisible(false);
            storeList.setVisible(false);
            storeList.setVisible(false);
            chooseStoreLabel.setVisible(false);
            newScreenBtn.setVisible(false);
            totalChainIncomeLabel.setText("סך הכנסות החנות לפרק זמן זה:");
            chooseStoreLabel.setVisible(false);
//            ((StoreManagerPanel) panel).getStoreOrders(storeId);
        } else {
            if (DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)) {
//                ((ChainManagerPanel) panel).getStoreOrders(2);
//                ((ChainManagerPanel) panel).getAllOrders();
                totalChainIncomeLabel.setText("");
            }
        }
    }
}
