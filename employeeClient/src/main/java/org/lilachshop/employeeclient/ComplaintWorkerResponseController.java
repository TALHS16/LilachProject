package org.lilachshop.employeeclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.lilachshop.entities.ComplaintStatus;
import org.lilachshop.entities.Order;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ComplaintWorkerResponseController {
    CustomerServiceViewController controllerInstance;
    Order order;
    double refundAmountToUpdate = 0.0;
    @FXML
    private Button updateRefundBtn;

    @FXML
    private Label refundAmount;

    @FXML
    private TextField enteredRefund;

    @FXML
    private Label refundLabel;

    @FXML
    private Label complaintNumber;

    @FXML
    private Label complaintStatus;

    @FXML
    private Label lastDateToHandle;

    @FXML
    private TextArea response;

    @FXML
    private TextArea content;

    @FXML
    private Button SendBtn;

    @FXML
    private Button showItems;

    @FXML
    private Label orderNum;

    @FXML
    private Label orderPrice;

    @FXML
    private Label customerName;

    @FXML
    void onShowItemsClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowOrder.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        ShowOrderController controller = fxmlLoader.getController();
        controller.setData(order);
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    public void setComplaintData(String _complaintNumber, String _complaintStatus, LocalDateTime _lastDateToHandle,
                                 String _content, CustomerServiceViewController _controllerInstance, Order order) {
        complaintNumber.setText(_complaintNumber);
        complaintStatus.setText(_complaintStatus);
        String tempDate = _lastDateToHandle.truncatedTo(ChronoUnit.DAYS).toString();
        lastDateToHandle.setText(tempDate.substring(0, tempDate.length() - 6));
        content.setText(_content);
        this.order = order;
        controllerInstance = _controllerInstance;
        this.orderNum.setText(String.valueOf(order.getId()));
        this.orderPrice.setText(String.valueOf(order.getTotalPrice() + "ש''ח"));
        this.customerName.setText(String.valueOf(order.getCustomer().getName()));


        if (order.getComplaint().getStatus().equals(ComplaintStatus.CLOSED)) {
            SendBtn.setDisable(true);
            updateRefundBtn.setDisable(true);
            response.setText(order.getComplaint().getReply());
            response.setEditable(false);
            refundAmount.setText(String.valueOf(order.getRefund()));
            refundAmount.setDisable(true);
            enteredRefund.setText(String.valueOf(order.getRefund()));
            enteredRefund.setDisable(true);
        }

    }

    @FXML
    void onSendBtn(ActionEvent event) throws IOException {
        order.setRefund(refundAmountToUpdate);

        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        a.setHeaderText("תגובתך לתלונה נשלחה ללקוח, התלונה נסגרה");
        a.setTitle("תגובה לתלונת לקוח");
        a.setContentText("");
        a.show();
        controllerInstance.closeComplaint(complaintNumber.getText(), response.getText(), order);
        updateRefundBtn.setDisable(true);
        response.setText(response.getText());
        SendBtn.setDisable(true);
        Stage stage = (Stage) SendBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void OnTextEntered(KeyEvent event) {

    }

    @FXML
    void onUpdateRefundBtn(ActionEvent event) {
        if (!enteredRefund.getText().matches("[0-9]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("שגיאה");
            alert.setContentText("יש להכניס מספרים בלבד!");
            enteredRefund.setText("");
            alert.show();

        } else {
            refundAmount.setText(enteredRefund.getText() + "ש''ח");
            refundAmountToUpdate = Double.parseDouble(enteredRefund.getText());
        }
    }
}
