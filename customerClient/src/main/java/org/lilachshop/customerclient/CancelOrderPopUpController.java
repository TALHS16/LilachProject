package org.lilachshop.customerclient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.entities.Order;
import org.lilachshop.entities.OrderStatus;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.PanelEnum;
import org.lilachshop.panels.RegisteredCustomerPanel;

public class CancelOrderPopUpController {
    HistoryItemController historyItemController;
    static Panel panel = null;
    Order order;
    double refundValue;
    @FXML
    private Button OkBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label cancelTime;

    @FXML
    private Label orderTime;

    @FXML
    private Label refundSum;

    @FXML
    void onOkBtnClick(ActionEvent event) {
        order.setRefund(refundValue);
        order.setOrderStatus(OrderStatus.CANCELED);
        if (panel instanceof RegisteredCustomerPanel) {
            ((RegisteredCustomerPanel) panel).cancelOrderRequest(order);
        } else {
            throw new RuntimeException("something is wrong with the panels");
        }
    }


    @FXML
    void oncancelBtnClick(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void setData(String _cancelTime, String _orderTime, String _refundSum, double _refundValue, Order _order, HistoryItemController _historyItemController){
        panel = OperationsPanelFactory.createPanel(CustomerApp.panelEnum, CustomerApp.getSocket(), this);
        cancelTime.setText(_cancelTime);
        orderTime.setText(_orderTime);
        refundSum.setText(_refundSum);
        refundValue = _refundValue;
        order = _order;
        historyItemController = _historyItemController;
    }
    @Subscribe
    public void onCancelRequestResponse(String msg){
        Platform.runLater(()->{
            if(msg.equals("Cancellation was successful")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("ביטול ההזמנה");
                alert.setContentText("ביטול ההזמנה עבר בהצלחה, חשבונך זוכה בסך: "+ String.valueOf(refundValue) + " שקלים ");
                alert.show();
                historyItemController.disableCancelOrderBtn();
                historyItemController.updateOrderStatusToCanceled();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("ביטול ההזמנה");
                alert.setContentText("ביטול ההזמנה נכשל");
                alert.show();
            }
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });

    }
}
