package org.lilachshop.employeeclient;

import java.net.URL;
import java.util.*;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.entities.Employee;
import org.lilachshop.entities.Role;
import org.lilachshop.entities.Store;
import org.lilachshop.events.AddEmployeeEvent;
import org.lilachshop.events.StoreEvent;

public class AddEmployeePopUpController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField employeeUsernameTF;

    @FXML
    private TextField employeePasswordTF;

    @FXML
    private ChoiceBox<Role> roleChoiceBox;

    @FXML
    private ChoiceBox<Store> storeChoiceBox;

    @FXML
    private Button resetBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Label takenLabel;

    private Stage stage;

    private static EventBus bus = null;

    private Boolean canEmployeeBeAdded;

    public EventBus getBus() {
        return bus;
    }

    @Subscribe
    public void setEmployeeCanBeAdded(Boolean result) {
        canEmployeeBeAdded = result;
    }

    @FXML
    void onClickAdd(ActionEvent event) {
        Store store = storeChoiceBox.getSelectionModel().getSelectedItem();
        Role role = roleChoiceBox.getSelectionModel().getSelectedItem();
        String username = employeeUsernameTF.getText();
        String password = employeePasswordTF.getText();

        // Try to add an employee
        if (canEmployeeBeAdded) {
            Employee employee = new Employee(store, role, username, password);
            bus.post(new AddEmployeeEvent(employee));
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (canEmployeeBeAdded) {
            onClickReset(null);
            takenLabel.setVisible(false);
            stage = (Stage) addBtn.getScene().getWindow();
            stage.close();
        } else {
            takenLabel.setVisible(true);
            canEmployeeBeAdded = true;
        }
    }

    @FXML
    void onClickReset(ActionEvent event) {
        employeeUsernameTF.setText("");
        employeePasswordTF.setText("");
        roleChoiceBox.getSelectionModel().clearSelection();
        storeChoiceBox.getSelectionModel().clearSelection();
        storeChoiceBox.setDisable(false);
    }

    @Subscribe
    public void onReceiveStores(StoreEvent event) {
        List<Store> stores = event.getStores();
        Platform.runLater(() -> {
            storeChoiceBox.setItems(FXCollections.observableArrayList(stores));
            storeChoiceBox.getItems().remove(0);
        });
    }

    @Subscribe
    public void onClearScreen(String msg) {
        switch (msg) {
            case "Clear widgets": {
                Platform.runLater(() -> onClickReset(null));
                break;
            }
//            case "Print taken": {
//                Platform.runLater(() -> {
//                    takenLabel.setVisible(true);
//                    employeeUsernameTF.setOnKeyPressed(new EventHandler<KeyEvent>() {
//                        @Override
//                        public void handle(KeyEvent event) {
//                            takenLabel.setVisible(false);
//                            employeeUsernameTF.setOnKeyPressed(null);
//                        }
//                    });
//                });
//            }
        }
    }

    @FXML
    void initialize() {
        assert addBtn != null : "fx:id=\"addBtn\" was not injected: check your FXML file 'AddEmployeePopUp.fxml'.";
        assert employeeUsernameTF != null : "fx:id=\"employeeFirstNameTF\" was not injected: check your FXML file 'AddEmployeePopUp.fxml'.";
        assert employeePasswordTF != null : "fx:id=\"employeeLastNameTF\" was not injected: check your FXML file 'AddEmployeePopUp.fxml'.";
        assert resetBtn != null : "fx:id=\"resetBtn\" was not injected: check your FXML file 'AddEmployeePopUp.fxml'.";
        assert roleChoiceBox != null : "fx:id=\"roleChoiceBox\" was not injected: check your FXML file 'AddEmployeePopUp.fxml'.";
        assert storeChoiceBox != null : "fx:id=\"storeChoiceBox\" was not injected: check your FXML file 'AddEmployeePopUp.fxml'.";
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canEmployeeBeAdded = true;
        takenLabel.setVisible(false);
        bus = bus == null ? EventBus.builder().build() : bus;
        if (!bus.isRegistered(this))
            bus.register(this);

        onClickReset(null);


        addBtn.disableProperty().
                bind(Bindings.isEmpty(employeeUsernameTF.textProperty()).
                        or(Bindings.isEmpty(employeePasswordTF.textProperty())).
                        or(roleChoiceBox.valueProperty().isNull()));
//                        or(storeChoiceBox.valueProperty().isNull()));


        roleChoiceBox.setItems(FXCollections.observableArrayList(Arrays.asList(Role.values())));
        roleChoiceBox.valueProperty().addListener(new ChangeListener<Role>() {
            @Override
            public void changed(ObservableValue<? extends Role> observable, Role oldValue, Role newValue) {
                if (newValue == Role.SYSTEM_MANAGER || newValue == Role.CHAIN_MANAGER ||
                        newValue == Role.CUSTOMER_SERVICE) {
                    storeChoiceBox.getSelectionModel().select(-1);
                    storeChoiceBox.setDisable(true);
                } else {
                    storeChoiceBox.setDisable(false);
                }
            }
        });
    }
}

