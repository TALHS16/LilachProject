package org.lilachshop.customerclient; /**
 * Sample Skeleton for 'historyItem.fxml' Controller Class
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.lilachshop.entities.*;
import org.lilachshop.commonUtils.Utilities;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

public class HistoryItemController {
    private Order order;
    @FXML
    private Button CancelOrder;
    @FXML
    private Button fillComplaint;
    @FXML
    private HBox layout;

    @FXML
    private Label orderNum;

    @FXML
    private Label price;

    @FXML
    private Label status;

    @FXML
    private Circle statusColor;

    @FXML
    private Button ComplaintReply;
    @FXML
    private Label orderStatusLabel;

    public void setData(Order order) {
        this.order = order;
        if(order.getOrderStatus().equals(OrderStatus.CANCELED)){
            CancelOrder.setDisable(true);
            statusColor.setFill(Color.RED);
            orderStatusLabel.setText("בוטלה");
        }
        else if(order.getOrderStatus().equals(OrderStatus.PENDING)){
            statusColor.setFill(Color.GREEN);
            orderStatusLabel.setText("בדרך");
        }
        else{
            statusColor.setFill(Color.BLUE);
            orderStatusLabel.setText("התקבלה בהצלחה");
            CancelOrder.setDisable(true);
        }

        for (myOrderItem itemsType : order.getItems()) {
            for (int i = 0; i < itemsType.getCount(); i++) {
                ImageView img = new ImageView();
                img.setImage(Utilities.bytesToImageConverter(itemsType.getItem().getImageBlob()));
                img.setFitWidth(70);
                img.setFitHeight(70);
                layout.getChildren().add(img);
            }
            //status.setText(order.);
            orderNum.setText("# " + order.getId());
            price.setText(Double.toString(order.getTotalPrice()));
        }
    }

    public void onComplaintReply(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MessageBoxComplaint.fxml"));
            Parent root = fxmlLoader.load();
            MessageBoxComplaintController controller = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            Complaint complaint = order.getComplaint();
            controller.setData(complaint);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onCancelOrder(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CancelOrderPopUp.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        CancelOrderPopUpController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        Double refundSum =  calculateRefundValue(order);
        controller.setData(LocalDateTime.now().toString().replace("T", " "),
                order.getCreationDate().toString().replace("T", " "),
                String.valueOf(refundSum), refundSum, order, this);
        stage.setScene(new Scene(root1));
        stage.show();
    }

    private Double calculateRefundValue(Order order) {
        LocalDateTime creationDate = order.getCreationDate();
        LocalDateTime now = LocalDateTime.now();
        long numberOfMinutesDiff = MINUTES.between(creationDate, now);
        if(numberOfMinutesDiff > 180){
            return 0.0;
        }
        else if(numberOfMinutesDiff >= 60){
            return (double)order.getTotalPrice() * 0.5;
        }
        return (double)order.getTotalPrice();
        //todo: implemnt calculation!
    }

    public void onFilingComplaint(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("complaintForm.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        ComplaintController controller = fxmlLoader.getController();
        controller.setData(order, this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    public void disableCancelOrderBtn(){ CancelOrder.setDisable(true);}
    public void disablePostComplaintBtn() {
        fillComplaint.setDisable(true);
    }

    public void disableShowComplaintBtn() {
        ComplaintReply.setDisable(true);
    }

    public void enableShowComplaintBtn() {
        ComplaintReply.setDisable(false);
    }
    public void updateOrderStatusToCanceled(){
        statusColor.setFill(Color.RED);
        orderStatusLabel.setText("בוטלה");
    }

}
