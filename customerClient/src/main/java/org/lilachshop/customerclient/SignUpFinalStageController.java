package org.lilachshop.customerclient;

import java.net.URL;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.entities.*;
import org.lilachshop.events.Signup1Event;
import org.lilachshop.events.Signup2Event;
import org.lilachshop.events.AccountChoiceEvent;
import org.lilachshop.events.CreditCardEvent;
import org.lilachshop.panels.*;

public class SignUpFinalStageController {

    static private Panel panel = null;
    EventBus signupBus = null;
    Customer registeringCustomer;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backCatalogBtn;

    @FXML
    void onClickBackCatalogBtn(ActionEvent event) {
        CustomerApp.setMyFlowers(new LinkedList<myOrderItem>());
        CustomerApp.setMyCustomer(null);
        CustomerApp.createPanel();
    }


    @Subscribe
    public void onSignUp1Event(Signup1Event signupEvent) {
        //set username and password to user
        registeringCustomer.setUserName(signupEvent.getUsername());
        registeringCustomer.setUserPassword(signupEvent.getPassword());
        System.out.println("gotEvent1");
    }

    @Subscribe
    public void onSignUp2Event(Signup2Event signupEvent) {
        //set Personal Info to user
        registeringCustomer.setName(signupEvent.getFirstName() + " " + signupEvent.getLastName());
        try {
            registeringCustomer.setPhoneNumber(signupEvent.getPhoneNumber());
        } catch (Exception ignored) {
        }
        registeringCustomer.setAddress(signupEvent.getCity() + ", " + signupEvent.getAddress());
        registeringCustomer.setEmail(signupEvent.getEmail());
        System.out.println("gotEvent2");
    }


    @Subscribe
    public void onSignUp4Event(CreditCardEvent signupEvent) {
        //set creditCard info for user
        registeringCustomer.setCard(signupEvent.getCard());
        }

    @Subscribe
    public void onSignUp3Event(AccountChoiceEvent signupEvent) {
        //set Account Choice Info to user
        registeringCustomer.setAccount(new Account(signupEvent.getChosenAccount()));
        registeringCustomer.setStore(signupEvent.getStore());

        //send request to server to signup user
        CustomerAnonymousPanel customerAnonymousPanel = (CustomerAnonymousPanel) panel;
        registeringCustomer.setAccountState(ActiveDisabledState.ACTIVE);
        customerAnonymousPanel.sendSignUpRequest(registeringCustomer);

        //reset Sign up
        registeringCustomer = null;
        EventBus.getDefault().unregister(this);
    }



    @FXML
    void initialize() {

        assert backCatalogBtn != null : "fx:id=\"backCatalogBtn\" was not injected: check your FXML file 'SignUp5.fxml'.";
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);


        registeringCustomer = registeringCustomer == null ? new Customer() : registeringCustomer;
        if (panel == null)
            panel = OperationsPanelFactory.createPanel(CustomerApp.panelEnum,CustomerApp.getSocket(), this);
    }

}
