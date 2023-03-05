package org.lilachshop.customerclient;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.lilachshop.entities.Complaint;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.PanelEnum;
import org.lilachshop.panels.StoreCustomerPanel;


public class MessageBoxComplaintController implements Initializable {
    Complaint complaint;
    static private Panel panel; // i added that

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField complainAnswerTF;

    @FXML
    private TextField complainDescriptionTF;

    @FXML
    private Text complaintIDTF;

    @FXML
    private Text noReplyYet;


    @FXML
    void initialize() {
        assert complainAnswerTF != null : "fx:id=\"complainAnswerTF\" was not injected: check your FXML file 'messageBoxComplaint.fxml'.";
        assert complainDescriptionTF != null : "fx:id=\"complainDescriptionTF\" was not injected: check your FXML file 'messageBoxComplaint.fxml'.";
        assert complaintIDTF != null : "fx:id=\"complaintIDTF\" was not injected: check your FXML file 'messageBoxComplaint.fxml'.";

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//         panel = OperationsPanelFactory.createPanel(PanelEnum.STORE_CUSTOMER, CustomerApp.getSocket(), this);


    }

    public void setData(Complaint complaint) {
        this.complaint = complaint;
        complaintIDTF.setText(String.valueOf(complaint.getId()));
        complainDescriptionTF.setText(complaint.getContent());
        if (complaint.getReply().equals(""))
            noReplyYet.setVisible(true);
        else {
            noReplyYet.setVisible(false);
            complainAnswerTF.setText(complaint.getReply());
        }

    }
}
