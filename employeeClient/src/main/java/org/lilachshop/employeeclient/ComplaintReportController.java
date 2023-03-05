package org.lilachshop.employeeclient;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.commonUtils.Utilities;
import org.lilachshop.entities.Complaint;
import org.lilachshop.events.ComplaintsEvent;
import org.lilachshop.panels.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.Chronology;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

// todo: solve all the errors when changing stores and dates
public class ComplaintReportController implements Initializable {
    private Panel panel;
    List<Complaint> complaints;
    List<Complaint> allStoresComplaints;
    long storeManagerId;
    @FXML
    private Label TotalSumOfComplaints;
    @FXML
    private Button newScreenBtn;
    @FXML
    private Label totalComplaintsLabel;
    @FXML
    private BarChart<?, ?> complaintBarChart;

    @FXML
    private NumberAxis complantNumber;

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
    private Label chooseStoreLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panel = OperationsPanelFactory.createPanel(DashBoardController.panelEnum, EmployeeApp.getSocket(), this);
//        panel = OperationsPanelFactory.createPanel(PanelEnum.STORE_MANAGER, this);

        storeList.getItems().addAll("לילך חיפה", "לילך תל אביב", "לילך הרצליה", "כל החנויות");
        //todo: if chain manger is logged in, do haifa, if store manger logged in, do store managers store
        storeList.promptTextProperty().set("בחר חנות");
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
        }
        if (end.isBefore(start)) {
            displayChronologyAlert();
            return;
        }
        if (storeList.getSelectionModel().getSelectedItem() == null &&
                DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)) {
            displayNullStoreAlert();
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
                ((ChainManagerPanel) panel).getAllComplaints();// gets haifa by default
            } else {
                ((ChainManagerPanel) panel).getStoreComplaint(storeId);
            }
        } else {
            ((StoreManagerPanel) panel).getStoreComplaint(storeManagerId);
        }
//        presentStoresComplaintData(start, end);

    }

    private void presentStoresComplaintData(LocalDateTime start, LocalDateTime end) {
        List<Complaint> relevantComplaintList = complaints;
        if (DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)) {
            if (storeList.getSelectionModel().getSelectedItem().equals("כל החנויות")) {
                relevantComplaintList = allStoresComplaints;
            }
        }

        complaintBarChart.getData().clear();
        complaintBarChart.getXAxis().animatedProperty().set(false);

        int todayComplaintCounter = 0;
        int totalComplaintCounter = 0;
        XYChart.Series set = new XYChart.Series<>();
        for (LocalDateTime date = start; date.isBefore(end); date = date.plusDays(1)) {
            for (Complaint complaint : relevantComplaintList) {
                if (Utilities.hasTheSameDate(date, complaint.getCreationDate())) {
                    todayComplaintCounter++;
                }
            }
            set.getData().add(new XYChart.Data(date.toString(), todayComplaintCounter));
            totalComplaintCounter += todayComplaintCounter;
            todayComplaintCounter = 0;
        }
        complaintBarChart.getData().addAll(set);
        TotalSumOfComplaints.setText(String.valueOf(totalComplaintCounter));
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
    void onChangeStore(ActionEvent event) {
        String selectedStore = storeList.getSelectionModel().getSelectedItem();
        complaintBarChart.getData().clear();
//        startDate.setValue(null);
//        endDate.setValue(null);
        TotalSumOfComplaints.setText("");
        //todo: get complaints from all stores
//        if (selectedStore.equals("לילך הרצליה")) {
//            ((ChainManagerPanel) panel).getStoreComplaint(3);
//        } else if (selectedStore.equals("לילך חיפה")) {
//            ((ChainManagerPanel) panel).getStoreComplaint(2);
//        }
//        else if (selectedStore.equals("לילך תל אביב")) {
//            ((ChainManagerPanel) panel).getStoreComplaint(4);
//        }
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ComplaintReport.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        ComplaintReportController complaintReportController = fxmlLoader.getController();
        complaintReportController.setData(storeManagerId);
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @Subscribe
    public void handleMessageFromClient(List<Complaint> complaints) {
        this.complaints = complaints;
        Platform.runLater(()->{
            LocalDateTime start = startDate.getValue().atStartOfDay();
            LocalDateTime end = endDate.getValue().atStartOfDay();
            presentStoresComplaintData(start, end);
        });
    }

    @Subscribe
    public void handleAllComplaintsEvent(ComplaintsEvent complaintsEvent) {
        allStoresComplaints = complaintsEvent.getAllComplaints();
        Platform.runLater(()->{
            LocalDateTime start = startDate.getValue().atStartOfDay();
            LocalDateTime end = endDate.getValue().atStartOfDay();
            presentStoresComplaintData(start, end);
        });
    }

    public void setData(long storeId) {
        storeManagerId = storeId;
        if (DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)) {
//            ((ChainManagerPanel) panel).getStoreComplaint(2);
//            ((ChainManagerPanel) panel).getAllComplaints();// gets haifa by default
        } else if (DashBoardController.panelEnum.equals(PanelEnum.STORE_MANAGER)) {
//            ((StoreManagerPanel) panel).getStoreComplaint(storeId);
            storeList.setVisible(false);
            chooseStoreLabel.setVisible(false);
            newScreenBtn.setVisible(false);
        }
    }
}

