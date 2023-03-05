package org.lilachshop.employeeclient;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.entities.*;
import org.lilachshop.events.AddEmployeeEvent;
import org.lilachshop.events.StoreEvent;
import org.lilachshop.panels.*;

import static org.lilachshop.commonUtils.Utilities.setTextFieldFactory;

public class EmployeeTableController implements Initializable {

    public Employee employee; // Omer added that
    static private Panel panel;

    static private SystemManagerPanel sPanel;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<Employee, String> userNameCol;

    @FXML
    private TableColumn<Employee, Long> employeeID;

    @FXML
    private TableColumn<Employee, String> passwordCol;

    @FXML
    private TableColumn<Employee, Role> roleCol;

    @FXML
    private TableColumn<Employee, Store> storeCol;

    @FXML
    private TableView<Employee> employeeTable;

    AddEmployeePopUpController popUpController;

    private static EventBus popUpBus;

    private Parent popUpRoot;

    private static Stage popUpStage = null;

    private static boolean wasSentToPopUp = false;

    private ObservableList<Employee> listOfEmployees;

    private List<Store> stores;

    @Subscribe
    public void onRecieveEmployees(List<Employee> employees) {
        Platform.runLater(() -> {
            setEmployeeRows(employees);
        });
    }

    @Subscribe
    public void onReceiveStores(StoreEvent storeEvent) {
        this.stores = storeEvent.getStores().size() > 0 ? storeEvent.getStores() : null;
        if (!wasSentToPopUp) {
            popUpBus.post(storeEvent);
            wasSentToPopUp = true;
        }
    }

    private void setEmployeeRows(List<Employee> employees) {
        ObservableList<Employee> oEmployees = FXCollections.observableArrayList(employees);
        employeeTable.setItems(oEmployees);
        employeeTable.refresh();
        listOfEmployees = oEmployees;
        employeeTable.setEditable(true);
        employeeTable.setItems(listOfEmployees);
    }

    private void setPlaceHolder() {
        if (employeeTable.getItems().isEmpty()) {
            employeeTable.setPlaceholder(new Label("No rows to display"));
        }
    }

    private Set<String> getStoreNames() {
        List<String> storeNames = new LinkedList<>();
        stores.forEach((s) -> {
            storeNames.add(s.getStoreName());
        });
        return new HashSet<>(storeNames);
    }

    private Set<Store> getStoreSet() {
        return new HashSet<>(stores);
    }

    public boolean isUniqueUsername(List<Employee> employees, String value) {
        for (Employee e : employees) {
            if (Objects.equals(e.getUserName(), value)) {
                return false;
            }
        }
        return true;
    }

    @Subscribe
    public void onGetNewEmployee(AddEmployeeEvent event) {
        Employee employee = event.getEmployee();
        List<Employee> employees = new LinkedList<>(employeeTable.getItems());
        if (!isUniqueUsername(employees, employee.getUserName())) {
            popUpBus.post(Boolean.FALSE);
            return;
        }
        Platform.runLater(() -> {
            employees.add(employee);
            ObservableList<Employee> oEmployees = FXCollections.observableArrayList(employees);
            employeeTable.setItems(oEmployees);
            employeeTable.refresh();
            listOfEmployees = oEmployees;
            sPanel.createEmployee(employee);
            sPanel.getAllEmployees();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("נוסף עובד!");
            a.setTitle("הוספת עובד");
            a.show();
        });
    }

    @FXML
    void onAddClicked(ActionEvent event) {
        popUpStage = popUpStage == null ? new Stage() : popUpStage;
        popUpStage.setTitle("הוספת עובד");
        popUpStage.setAlwaysOnTop(true);
        if (popUpRoot.getScene() == null)
            popUpStage.setScene(new Scene(popUpRoot));
        popUpStage.show();
        popUpStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::onClosePopUp);
    }

    private void onClosePopUp(WindowEvent event) {
        popUpBus.post("Clear widgets");
    }


    @FXML
    void onDeleteClicked(ActionEvent event) {
        sPanel.deleteEmployeeByID(employeeTable.getSelectionModel().getSelectedItem().getId());
        employeeTable.getItems().remove(employeeTable.getSelectionModel().getSelectedItem());
        employeeTable.refresh();
    }

    @FXML
    void initialize() {
        assert addBtn != null : "fx:id=\"addBtn\" was not injected: check your FXML file 'EmployeeTable.fxml'.";
        assert deleteBtn != null : "fx:id=\"deleteBtn\" was not injected: check your FXML file 'EmployeeTable.fxml'.";
        assert userNameCol != null : "fx:id=\"employeeFirstName\" was not injected: check your FXML file 'EmployeeTable.fxml'.";
        assert employeeID != null : "fx:id=\"employeeID\" was not injected: check your FXML file 'EmployeeTable.fxml'.";
        assert passwordCol != null : "fx:id=\"employeeLastName\" was not injected: check your FXML file 'EmployeeTable.fxml'.";
        assert roleCol != null : "fx:id=\"employeeRole\" was not injected: check your FXML file 'EmployeeTable.fxml'.";
        assert storeCol != null : "fx:id=\"employeeStore\" was not injected: check your FXML file 'EmployeeTable.fxml'.";
        assert employeeTable != null : "fx:id=\"employeeTable\" was not injected: check your FXML file 'EmployeeTable.fxml'.";
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellsValueFactories();
        setPlaceHolder();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEmployeePopUp.fxml"));
            popUpRoot = loader.load();
            popUpController = loader.getController();
            popUpBus = popUpController.getBus();
            popUpBus.register(this);
        } catch (IOException e) {
            System.out.println("Unable to load pop up display.");
            e.printStackTrace();
        }
        if (panel != null) {
            panel.closeConnection();
            panel = null;
        }
        panel = OperationsPanelFactory.createPanel(PanelEnum.SYSTEM_MANAGER, EmployeeApp.getSocket(), this);
        try {
            sPanel = (SystemManagerPanel) panel;
        } catch (Exception e) {
            System.out.println("Insufficient permissions!");
            e.printStackTrace();
            System.exit(-1);
        }
        sPanel.getAllEmployees();
        sPanel.getAllStores();


        var roleBoxCallback = new Callback<TableColumn<Employee, Role>, TableCell<Employee, Role>>() {
            @Override
            public TableCell<Employee, Role> call(TableColumn<Employee, Role> param) {
                ComboBox<Role> box = new ComboBox<>();
                TableCell<Employee, Role> cell = new TableCell<Employee, Role>() {
                    @Override
                    protected void updateItem(Role item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setGraphic(null);
                        else {
                            setEditable(false);
                            setText(item.name());
                        }
                    }
                };

                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        var selModel = employeeTable.getSelectionModel();
                        if (!selModel.isSelected(cell.getTableRow().getIndex(), cell.getTableColumn()))
                            return;
                        if (event.getButton().equals(MouseButton.PRIMARY)) {
                            Employee employee = employeeTable.getSelectionModel().getSelectedItem();
                            if (employee != null)
                                box.setItems(FXCollections.observableArrayList(Role.values()));
                            cell.setEditable(true);

                        }
                        if (event.getClickCount() == 2 && cell.isEditable()) {
                            box.getSelectionModel().select(0);
                            cell.setText(null);
                            cell.setGraphic(box);
                        }

                    }
                });

                cell.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode().equals(KeyCode.ENTER)) {
                            Employee employee = (Employee) employeeTable.getSelectionModel().getSelectedItem();
                            if (employee != null) {
                                employee.setRole(box.getSelectionModel().getSelectedItem());
                                cell.setEditable(false);
                                sPanel.updateEmployee(employee);
                                employeeTable.refresh();
                            }
                        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                            cell.setEditable(false);
                            employeeTable.refresh();
                            employeeTable.getSelectionModel().clearSelection();
                        }
                    }
                });
                return cell;
            }
        };
        roleCol.setCellFactory(roleBoxCallback);

        var storeBoxCallback = new Callback<TableColumn<Employee, Store>, TableCell<Employee, Store>>() {
            @Override
            public TableCell<Employee, Store> call(TableColumn<Employee, Store> param) {
                ComboBox<Store> box = new ComboBox<>();
                TableCell<Employee, Store> cell = new TableCell<Employee, Store>() {
                    @Override
                    protected void updateItem(Store item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setGraphic(null);
                        else {
                            setEditable(false);
                            setText(item.getStoreName());
                        }
                    }
                };

                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        var selModel = employeeTable.getSelectionModel();
                        if (!selModel.isSelected(cell.getTableRow().getIndex(), cell.getTableColumn()))
                            return;
                        if (event.getButton().equals(MouseButton.PRIMARY)) {
                            Employee employee = employeeTable.getSelectionModel().getSelectedItem();
                            if (employee != null) {
                                box.setItems(FXCollections.observableArrayList(getStoreSet()));
                            }
                            cell.setEditable(true);
                        }
                        if (event.getClickCount() == 2 && cell.isEditable()) {
                            box.getSelectionModel().select(0);
                            cell.setText(null);
                            cell.setGraphic(box);
                        }

                    }
                });

                cell.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode().equals(KeyCode.ENTER)) {
                            Employee employee = (Employee) employeeTable.getSelectionModel().getSelectedItem();
                            if (employee != null) {
                                employee.setStore(box.getSelectionModel().getSelectedItem());
                                cell.setEditable(false);
                                sPanel.updateEmployee(employee);
                                employeeTable.refresh();
                            }
                        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                            cell.setEditable(false);
                            employeeTable.refresh();
                            employeeTable.getSelectionModel().clearSelection();
                        }
                    }
                });
                return cell;
            }
        };
        storeCol.setCellFactory(storeBoxCallback);

        setTextFieldFactory(userNameCol, ((employee, username) -> {
            employee.setUserName(username);
            return null;
        }), User::getUserName, employee -> sPanel.updateEmployee(employee), Boolean.TRUE);
        setTextFieldFactory(passwordCol, ((employee, password) -> {
            employee.setUserPassword(password);
            return null;
        }), User::getUserPassword, employee -> sPanel.updateEmployee(employee));
    }

    private void setCellsValueFactories() {
        employeeID.setCellValueFactory(new PropertyValueFactory<Employee, Long>("id"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("userName"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("userPassword"));
        roleCol.setCellValueFactory(new PropertyValueFactory<Employee, Role>("role"));
        storeCol.setCellValueFactory(new PropertyValueFactory<Employee, Store>("store"));
    }
}
