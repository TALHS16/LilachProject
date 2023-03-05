package org.lilachshop.customerclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import org.lilachshop.entities.Complaint;
import org.lilachshop.entities.ComplaintStatus;
import org.lilachshop.entities.Order;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.panels.PanelEnum;
import org.lilachshop.panels.StoreCustomerPanel;

public class ComplaintController implements Initializable {
    Order order;
    private static Panel panel;
    HistoryItemController historyItemController;
    @FXML
    private Button onSendComplaintClick;

    @FXML
    private TextArea complaintText;

    @FXML
    private Label orderNumber;
    public void setData(Order order, HistoryItemController historyItemController){
        this.orderNumber.setText(String.valueOf(order.getId()));
        this.order = order;
        this.historyItemController = historyItemController;
    }
    @FXML
    void SendComplaintClicked(ActionEvent event) throws IOException {
        if(complaintText.getText().equals("")){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("שגיאה");
            a.setHeaderText("התלונה לא נשלחה - אנא מלא את תוכן התלונה");
            a.setContentText("");
            a.show();
            return;
        }

        Alert a = new Alert(Alert.AlertType.NONE);
        a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setHeaderText("תלונתך נשלחה למערכת");
        a.setTitle("הגשת תלונה");
        a.setContentText("");
        a.show();

        Complaint complaint = new Complaint(LocalDateTime.now().plusDays(1), ComplaintStatus.OPEN, complaintText.getText(), LocalDateTime.now(), "");
        order.setComplaint(complaint);
        complaint.setOrder(order);
        complaint.setStore(order.getStore());
        ((StoreCustomerPanel) panel).sendComplaintToServer(complaint, order);
        System.out.println(complaint.getContent());
        historyItemController.disablePostComplaintBtn();
        historyItemController.enableShowComplaintBtn();
        Stage stage = (Stage) onSendComplaintClick.getScene().getWindow();
        stage.close();
    }

    @Subscribe
    public void handleMessageReceivedFromClient(String msg) {
        System.out.println("complaintController recieved message from server");
    }

    @FXML
    void initialize() {
        assert complaintText != null : "fx:id=\"complaintText\" was not injected: check your FXML file 'complaintForm.fxml'.";
        assert onSendComplaintClick != null : "fx:id=\"onSendComplaintClick\" was not injected: check your FXML file 'complaintForm.fxml'.";
        assert orderNumber != null : "fx:id=\"orderNumber\" was not injected: check your FXML file 'complaintForm.fxml'.";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panel = OperationsPanelFactory.createPanel(PanelEnum.STORE_CUSTOMER, CustomerApp.getSocket(), this); // this should be the default panel according to customer/employee
        if (panel == null) {
            throw new RuntimeException("Panel creation failed!");
        }
    }
}
