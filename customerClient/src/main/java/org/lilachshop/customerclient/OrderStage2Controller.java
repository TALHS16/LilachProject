/**
 * Sample Skeleton for 'OrderStage2.fxml' Controller Class
 */

package org.lilachshop.customerclient;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import com.browniebytes.javafx.control.DateTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.type.LocalDateTimeType;
import org.lilachshop.commonUtils.Utilities;
import org.lilachshop.entities.DeliveryDetails;
import org.lilachshop.entities.Order;
import org.lilachshop.entities.PickUpDetails;
import org.lilachshop.entities.User;

import static org.lilachshop.commonUtils.Utilities.containHebrew;

public class OrderStage2Controller {
    Order myOrder;
    boolean deliveryBool = false;
    boolean pickupBool = false;
    boolean chooseBoxPickUp = false;
    boolean chooseBoxDelivery = false;
    Alert alert;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="address"
    private TextField address; // Value injected by FXMLLoader

    @FXML // fx:id="costumerRec"
    private CheckBox costumerRec; // Value injected by FXMLLoader

    @FXML // fx:id="date"
    private DateTimePicker date; // Value injected by FXMLLoader

    @FXML // fx:id="delivery"
    private VBox delivery; // Value injected by FXMLLoader

    @FXML // fx:id="deliveryCheck"
    private CheckBox deliveryCheck; // Value injected by FXMLLoader

    @FXML // fx:id="name"
    private Label name; // Value injected by FXMLLoader

    @FXML // fx:id="next"
    private Button next; // Value injected by FXMLLoader

    @FXML // fx:id="phoneNum"
    private TextField phoneNum; // Value injected by FXMLLoader

    @FXML // fx:id="pickup"
    private VBox pickup; // Value injected by FXMLLoader

    @FXML // fx:id="pickupCheck"
    private CheckBox pickupCheck; // Value injected by FXMLLoader

    @FXML // fx:id="prev"
    private Button prev; // Value injected by FXMLLoader

    @FXML // fx:id="receipient"
    private TextField receipient; // Value injected by FXMLLoader

    @FXML // fx:id="receiveImm"
    private CheckBox receiveImm; // Value injected by FXMLLoader

    @FXML // fx:id="selfDate"
    private DateTimePicker selfDate; // Value injected by FXMLLoader

    @FXML // fx:id="selfRecieveImm"
    private CheckBox selfRecieveImm; // Value injected by FXMLLoader


    @FXML // fx:id="shopNum"
    private Text shopNum; // Value injected by FXMLLoader


    @FXML
    private HBox selfHboxDate;

    @FXML
    private HBox selfHboxTime;


    @FXML
    private HBox deliveryHboxDate;

    @FXML
    private HBox deliveryHboxTime;

    @FXML
    void gotoNext(ActionEvent event) {
        boolean flagNext = false;
        if (ValidateInput())
            flagNext = true;

        if(flagNext)
        {
            LocalDateTime dateTime;
            if (deliveryBool)
            {
                if (!receiveImm.isSelected()  ) {
                    dateTime = convertToLocalDateTimeViaInstant(date.getTime());
                }
                else
                {
                    dateTime = LocalDateTime.now().plusHours(3);
                }
                myOrder.setDeliveryDetails(new DeliveryDetails(dateTime,phoneNum.getText(),receipient.getText(),address.getText()));
            }
            else
            {
                if (!selfRecieveImm.isSelected()  ) {
                    dateTime = convertToLocalDateTimeViaInstant(selfDate.getTime());
                }
                else
                {
                    dateTime = LocalDateTime.now().plusHours(3).plusMinutes(5);
                }
                myOrder.setPickUpDetails(new PickUpDetails(dateTime));
            }
            Stage stage = CustomerApp.getStage();
            try {
                FXMLLoader fxmlLoader1 = new FXMLLoader(CartController.class.getResource("OrderStage3.fxml"));
                Parent root = fxmlLoader1.load();
                OrderStage3Controller orderStage3Controller = fxmlLoader1.getController();
                orderStage3Controller.showInfo(myOrder);
                stage.setScene(new Scene(root));
                stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, CustomerApp::onCloseWindowEvent);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean ValidateInput() {
        if (deliveryBool) {
            alert.setHeaderText(":פרטי המשלוח אינם תקינים");
            if (phoneNum.getText().equals("") || phoneNum.getText().replace("_", "").length() != 10) {
                alert.setContentText("טלפון אינו תקין - אנא מלא שוב");
                alert.show();
                phoneNum.clear();
                return false;
            }
            if (!FieldInHebrewOrDisplayError(receipient, "שם המקבל אינו תקין - אנא מלא שוב בעברית")) {
                return false;
            }
            if (receipient.getText().equals(""))
            {
                alert.setContentText("שם המקבל/ת אינו יכול להיות ריק, אני הכנס שם מקבל/ת");
                alert.show();
                receipient.clear();
                return false;
            }
            if (address.getText().equals("") || !Utilities.containHebrewOrNumber(address.getText())) {
                alert.setContentText("הכתובת אינה תקינה - אנא מלא שוב בעברית");
                alert.show();
                address.clear();
                return false;
            }
            if (!receiveImm.isSelected()  ) {
                if (date.getTime() == null) {
                    alert.setContentText("אנא בחר תאריך ושעה תקינים");
                    alert.show();
                    date.setTime((LocalDateTime) null);
                    return false;
                }
                else if (convertToLocalDateTimeViaInstant(date.getTime()).isBefore(LocalDateTime.now().plusHours(3)))
                {
                    alert.setContentText("אנא בחר תאריך ושעה שהינם לפחות 3 שעות ממועד ההזמנה");
                    alert.show();
                    date.setTime((LocalDateTime) null);
                    return false;

                }
            }
        }
        else if (pickupBool)
        {
            alert.setHeaderText("פרטי האיסוף אינם תקינים");
            if (!selfRecieveImm.isSelected()  ) {
                if (selfDate.getTime() == null) {
                    alert.setContentText("אנא בחר תאריך ושעה תקינים");
                    alert.show();
                    return false;
                }
                else if (convertToLocalDateTimeViaInstant(selfDate.getTime()).isBefore(LocalDateTime.now().plusHours(3)))
                {
                    alert.setContentText("אנא בחר תאריך ושעה שהינם לפחות 3 שעות ממועד ההזמנה");
                    alert.show();
                    return false;

                }
            }

        }
        else if ((!pickupBool) && (!deliveryBool))
        {
            alert.setHeaderText("");
            alert.setContentText("בחר משלוח או איסוף עצמי");
            alert.show();
            return false;
        }
        return true;
    }

    boolean FieldInHebrewOrDisplayError(TextField F, String msg) {
        if (!containHebrew(F.getText())) {
            alert.setContentText(msg);
            alert.show();
            F.clear();
            return false;
        }
        return true;
    }

    @FXML
    void gotoPrev(ActionEvent event) {
        Stage stage = CustomerApp.getStage();
        try {
            FXMLLoader fxmlLoader1 = new FXMLLoader(CartController.class.getResource("OrderStage1.fxml"));
            Parent root = fxmlLoader1.load();
            OrderStage1Controller orderStage1Controller = fxmlLoader1.getController();
            orderStage1Controller.showInfo(myOrder);
            stage.setScene(new Scene(root));
            stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, CustomerApp::onCloseWindowEvent);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void returnToCatalog(MouseEvent event) {
        CustomerApp.getCustomerCatalog();
    }

    @FXML
    void showDelivery(ActionEvent event) {
        if(!deliveryBool)
        {
            deliveryBool = true;
            delivery.setVisible(true);
            if (pickupBool) {
                pickupCheck.setSelected(false);
                pickup.setVisible(false);
                pickupBool = false;
            }
        }
        else
        {
            delivery.setVisible(false);
            deliveryBool = false;
        }
    }

    @FXML
    void showPickUp(ActionEvent event) {
        if(!pickupBool)
        {
            pickupBool = true;
            pickup.setVisible(true);
            if (deliveryBool) {
                deliveryCheck.setSelected(false);
                delivery.setVisible(false);
                deliveryBool = false;
            }
        }
        else
        {
            pickup.setVisible(false);
            pickupBool = false;
        }
    }

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        receipient.setText(CustomerApp.getMyCustomer().getName());
        phoneNum.setText(CustomerApp.getMyCustomer().getPhoneNumber());
        address.setText(CustomerApp.getMyCustomer().getAddress());
        name.setText("שלום, " + CustomerApp.getMyCustomer().getName());
        shopNum.setText(CustomerApp.getMyStore().getStoreName());
        selfDate.setTime(LocalDateTime.now().plusHours(3).plusMinutes(5));
        date.setTime(LocalDateTime.now().plusHours(3).plusMinutes(5));
    }

    @FXML
    void onSelfRecieveImm(ActionEvent event) {
            selfHboxDate.setVisible(chooseBoxPickUp);
            selfDate.setTime(LocalDateTime.now().plusHours(3).plusMinutes(5));
            chooseBoxPickUp = !chooseBoxPickUp;
    }

    @FXML
    void onRecieveImm(ActionEvent event) {
        deliveryHboxDate.setVisible(chooseBoxDelivery);
        date.setTime(LocalDateTime.now().plusHours(3).plusMinutes(5));
        chooseBoxDelivery = !chooseBoxDelivery;
    }

    public void showInfo(Order order) {
        myOrder = order;
    }
}
