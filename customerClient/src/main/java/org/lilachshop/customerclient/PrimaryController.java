package org.lilachshop.customerclient;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.ExamplePanel;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.PanelEnum;

public class PrimaryController implements Initializable {

    private static Panel panel;

    @FXML
    private Label message_label;

    @FXML
    private Button msgButton;

    @FXML
    void onSendMessageClick(ActionEvent event) {
        try {
            ((ExamplePanel) panel).sendMessageToServer("example message");
        } catch (Exception e) {
            System.out.println("Conversion operation is not supported.");
            e.printStackTrace();
        }
    }

    @Subscribe  // Do not forget this annotation or else eventbus won't know what method should be triggered.
    public void handleMessageReceivedFromClient(String msg) {
        Platform.runLater(() -> {
            message_label.setText(msg);
        });
    }

    @FXML
    void initialize() {
        assert message_label != null : "fx:id=\"message_label\" was not injected: check your FXML file 'primary.fxml'.";
        assert msgButton != null : "fx:id=\"msgButton\" was not injected: check your FXML file 'primary.fxml'.";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        panel = OperationsPanelFactory.createPanel(PanelEnum., this); // this should be the default panel according to customer/employee
//        if (panel == null) {
//            throw new RuntimeException("Panel creation failed!");
//        }
    }
}
