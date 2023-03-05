package org.lilachshop.employeeclient;

import java.net.ConnectException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.commonUtils.Utilities;
import org.lilachshop.entities.*;
import org.lilachshop.events.CustomerEvent;
import org.lilachshop.events.StoreEvent;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.PanelEnum;
import org.lilachshop.panels.SystemManagerPanel;

public class CustomerTableController implements Initializable {

    static private Panel panel = null;

    static private SystemManagerPanel sPanel;

    @FXML
    public TableColumn<Customer, String> emailCol;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private ObservableList<Customer> oCustomerList;

    @FXML
    private TableColumn<Customer, AccountType> accountTypeCol;

    @FXML
    private TableColumn<Customer, String> addressCol;

    @FXML
    private TableColumn<Customer, String> cardOwnerCol;

    @FXML
    private TableColumn<Customer, String> cardOwnerIDCol;

    @FXML
    private TableColumn<Customer, LocalDate> creationDateCol;


    @FXML
    private TableColumn<Customer, String> creditCardNumberCol;

    @FXML
    private TableColumn<Customer, Long> customerIDCol;

    @FXML
    private TableColumn<Customer, String> customerNameCol;

    @FXML
    private TableColumn<Customer, String> cvcCol;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<Customer, ActiveDisabledState> disabledCol;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> expDateCol;

    @FXML
    private TableColumn<Customer, String> passwordCol;

    @FXML
    private TableColumn<Customer, String> phoneCol;

    @FXML
    private TableColumn<Customer, Store> storeCol;

    @FXML
    private TableColumn<Customer, String> usernameCol;

    private List<Store> stores;

    @FXML
    void onDeleteClicked(ActionEvent event) {
        sPanel.deleteCustomerByID(customerTable.getSelectionModel().getSelectedItem().getId());
        customerTable.getItems().remove(customerTable.getSelectionModel().getSelectedItem());
        customerTable.refresh();
    }

    @Subscribe
    public void onReceiveCustomers(CustomerEvent event) {
        List<Customer> customers = event.getCustomers();
        Platform.runLater(() -> {
            oCustomerList = FXCollections.observableArrayList(customers);
            customerTable.setItems(oCustomerList);
            customerTable.refresh();
        });
    }

    @Subscribe
    public void onReceiveStores(StoreEvent storeEvent) {
        stores = storeEvent.getStores().size() > 0 ? storeEvent.getStores() : null;
    }

    private Set<Store> getStoreSet() {
        return new HashSet<>(stores);
    }

    @FXML
    void initialize() {
        assert accountTypeCol != null : "fx:id=\"accountTypeCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert addressCol != null : "fx:id=\"addressCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert cardOwnerCol != null : "fx:id=\"cardOwnerCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert cardOwnerIDCol != null : "fx:id=\"cardOwnerCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert creationDateCol != null : "fx:id=\"creationDateCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert creditCardNumberCol != null : "fx:id=\"creditCardNumberCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert customerIDCol != null : "fx:id=\"customerIDCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert customerNameCol != null : "fx:id=\"customerNameCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert cvcCol != null : "fx:id=\"cvcCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert deleteBtn != null : "fx:id=\"deleteBtn\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert disabledCol != null : "fx:id=\"disabledCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert customerTable != null : "fx:id=\"customerTable\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert expDateCol != null : "fx:id=\"expDateCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert passwordCol != null : "fx:id=\"passwordCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert phoneCol != null : "fx:id=\"phoneCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert storeCol != null : "fx:id=\"storeCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
        assert usernameCol != null : "fx:id=\"usernameCol\" was not injected: check your FXML file 'CustomerTable.fxml'.";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (panel != null) {
            panel.closeConnection();
            panel = null;
        }
        panel = OperationsPanelFactory.createPanel(PanelEnum.SYSTEM_MANAGER, EmployeeApp.getSocket(), this);
        if (panel instanceof SystemManagerPanel) {
            sPanel = (SystemManagerPanel) panel;
        } else {
            sPanel = null;
            System.out.println("Unauthorized user access.");
            ((Stage) deleteBtn.getScene().getWindow()).close();
        }
        sPanel.getAllCustomers();
        sPanel.getAllStores();

        deleteBtn.setVisible(false);
        setCells();
    }

    private void setCells() {
        customerTable.setEditable(true);    // THIS COST ME A DAY OF MY LIFE!!
        Consumer<Customer> dbUpdater = new Consumer<Customer>() {
            @Override
            public void accept(Customer customer) {
                sPanel.updateCustomer(customer);
            }
        };

        Alert alert = new Alert(Alert.AlertType.ERROR);

        customerIDCol.setCellValueFactory(new PropertyValueFactory<Customer, Long>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        Utilities.setTextFieldFactory(customerNameCol, (c, s) -> {
            c.setName(s);
            return null;
        }, Customer::getName, dbUpdater);


        usernameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("userName"));
        Utilities.setTextFieldFactory(usernameCol, (c, s) -> {
            c.setUserName(s);
            return null;
        }, User::getUserName, dbUpdater, Boolean.TRUE);

        passwordCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("userPassword"));
        Utilities.setTextFieldFactory(passwordCol, (c, s) -> {
            c.setUserPassword(s);
            return null;
        }, Customer::getName, dbUpdater);

        emailCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
        Utilities.setTextFieldFactory(emailCol, (c, e) -> {
            try {
                c.setEmail(e);
            } catch (Exception ex) {
                alert.setHeaderText(ex.getMessage());
                alert.show();
                return null;
            }
            return null;
        }, Customer::getEmail, dbUpdater);

        addressCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        Utilities.setTextFieldFactory(addressCol, (c, s) -> {
            c.setAddress(s);
            return null;
        }, Customer::getName, dbUpdater);
        storeCol.setCellValueFactory(new PropertyValueFactory<Customer, Store>("store"));/*combobox*/
        accountTypeCol.setCellValueFactory(new PropertyValueFactory<Customer, AccountType>("accountType"));/*combobox*/
        creationDateCol.setCellValueFactory(new PropertyValueFactory<Customer, LocalDate>("creationDate"));
        creditCardNumberCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("cardNumber"));
        Utilities.setTextFieldFactory(creditCardNumberCol, (c, s) -> {
            try {
                c.getCard().setNumber(s);
            } catch (Exception e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
            return null;
        }, Customer::getName, dbUpdater);

        expDateCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("cardExpDate"));
        Utilities.setTextFieldFactory(expDateCol, (c, s) -> {
            try {
                c.getCard().setExpDate(s);
            } catch (Exception e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
            return null;
        }, c -> c.getCard().getExpDateStringFormat(), dbUpdater);

        cvcCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("cardThreeDigits"));
        Utilities.setTextFieldFactory(cvcCol, (c, s) -> {
            try {
                c.getCard().setThreeDigits(s);
            } catch (Exception e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
            return null;
        }, Customer::getCardThreeDigits, dbUpdater);

        cardOwnerCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("cardOwnerName"));/*validation*/
        Utilities.setTextFieldFactory(cardOwnerCol, (c, s) -> {
            try {
                c.getCard().setOwnerName(s);
            } catch (Exception e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
            return null;
        }, Customer::getCardOwnerName, dbUpdater);

        cardOwnerIDCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("cardOwnerId"));
        Utilities.setTextFieldFactory(cardOwnerIDCol, (c, s) -> {
            try {
                c.getCard().setCardOwnerId(s);
            } catch (Exception e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
            return null;
        }, Customer::getCardOwnerId, dbUpdater);

        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));
        Utilities.setTextFieldFactory(phoneCol, (c, s) -> {
            try {
                c.setPhoneNumber(s);
            } catch (Exception e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
            return null;
        }, Customer::getPhoneNumber, dbUpdater);

        disabledCol.setCellValueFactory(new PropertyValueFactory<Customer, ActiveDisabledState>("accountState"));
        var disabledBoxCallback = new Callback<TableColumn<Customer, ActiveDisabledState>, TableCell<Customer, ActiveDisabledState>>() {
            @Override
            public TableCell<Customer, ActiveDisabledState> call(TableColumn<Customer, ActiveDisabledState> param) {
                ComboBox<ActiveDisabledState> box = new ComboBox<>();
                TableCell<Customer, ActiveDisabledState> cell = new TableCell<Customer, ActiveDisabledState>() {
                    @Override
                    protected void updateItem(ActiveDisabledState item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null)
                            setGraphic(null);
                        else {
                            setEditable(false);
                            setText(String.valueOf(item));
                        }
                    }
                };

                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        var selModel = customerTable.getSelectionModel();
                        if (!selModel.isSelected(cell.getTableRow().getIndex(), cell.getTableColumn()))
                            return;
                        if (event.getButton().equals(MouseButton.PRIMARY)) {
                            Customer customer = customerTable.getSelectionModel().getSelectedItem();
                            if (customer != null)
                                box.setItems(FXCollections.observableArrayList(ActiveDisabledState.values()));
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
                            Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
                            if (customer != null) {
                                ButtonType hebrewYes = new ButtonType("כן", ButtonBar.ButtonData.OK_DONE);
                                ButtonType hebrewNo = new ButtonType("לא", ButtonBar.ButtonData.CANCEL_CLOSE);

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "נא אשר להמשך פעולה זו.", hebrewYes, hebrewNo);
                                alert.setTitle("חסימת משתמש");
                                alert.setHeaderText("חסום/הפשר משתמש.");
                                alert.setResizable(false);
                                alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                                Optional<ButtonType> result = alert.showAndWait();
                                ButtonType buttonType = result.orElse(hebrewNo);

                                if (buttonType == hebrewYes) {
                                    customer.setAccountState(box.getSelectionModel().getSelectedItem());
                                    sPanel.updateCustomer(customer);
                                }
                                cell.setEditable(false);
                                customerTable.refresh();
                            }
                        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                            cell.setEditable(false);
                            customerTable.refresh();
                            customerTable.getSelectionModel().clearSelection();
                        }
                    }
                });
                return cell;
            }
        };
        disabledCol.setCellFactory(disabledBoxCallback);

        var storeBoxCallback = new Callback<TableColumn<Customer, Store>, TableCell<Customer, Store>>() {
            @Override
            public TableCell<Customer, Store> call(TableColumn<Customer, Store> param) {
                ComboBox<Store> box = new ComboBox<>();
                TableCell<Customer, Store> cell = new TableCell<Customer, Store>() {
                    @Override
                    protected void updateItem(Store item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null)
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
                        var selModel = customerTable.getSelectionModel();
                        if (!selModel.isSelected(cell.getTableRow().getIndex(), cell.getTableColumn()))
                            return;
                        if (event.getButton().equals(MouseButton.PRIMARY)) {
                            Customer customer = customerTable.getSelectionModel().getSelectedItem();
                            if (customerTable != null) {
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
                            Customer customer = customerTable.getSelectionModel().getSelectedItem();
                            if (customer != null) {
                                if (!customer.getAccountType().equals(AccountType.STORE_ACCOUNT)) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setHeaderText("לא ניתן לשנות חנות עבור לקוח שאינו 'לקוח חנות'.");
                                    alert.show();
                                    cell.setEditable(false);
                                    customerTable.refresh();
                                    return;
                                }
                                customer.setStore(box.getSelectionModel().getSelectedItem());
                                cell.setEditable(false);
                                sPanel.updateCustomer(customer);
                                customerTable.refresh();
                            }
                        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                            cell.setEditable(false);
                            customerTable.refresh();
                            customerTable.getSelectionModel().clearSelection();
                        }
                    }
                });
                return cell;
            }
        };
        storeCol.setCellFactory(storeBoxCallback);

        var accountTypeBoxCallback = new Callback<TableColumn<Customer, AccountType>, TableCell<Customer, AccountType>>() {
            @Override
            public TableCell<Customer, AccountType> call(TableColumn<Customer, AccountType> param) {
                ComboBox<AccountType> box = new ComboBox<>();
                TableCell<Customer, AccountType> cell = new TableCell<Customer, AccountType>() {
                    @Override
                    protected void updateItem(AccountType item, boolean empty) {
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
                        var selModel = customerTable.getSelectionModel();
                        if (!selModel.isSelected(cell.getTableRow().getIndex(), cell.getTableColumn()))
                            return;
                        if (event.getButton().equals(MouseButton.PRIMARY)) {
                            Customer customer = customerTable.getSelectionModel().getSelectedItem();
                            if (customer != null)
                                box.setItems(FXCollections.observableArrayList(AccountType.values()));
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
                            Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
                            if (customer != null) {
                                AccountType selectedType = box.getSelectionModel().getSelectedItem();

                                if (selectedType != AccountType.STORE_ACCOUNT) {
                                    ButtonType hebrewYes = new ButtonType("כן", ButtonBar.ButtonData.OK_DONE);
                                    ButtonType hebrewNo = new ButtonType("לא", ButtonBar.ButtonData.CANCEL_CLOSE);

                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "שינוי סוג משתמש ממשתמש חנות יקשר משתמש זה לחנות ברירת מחדל.", hebrewYes, hebrewNo);
                                    alert.setTitle("עדכון שדה");
                                    alert.setHeaderText("האם ברצונך לעדכן שדה זה?");
                                    alert.setResizable(false);
                                    alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                                    Optional<ButtonType> result = alert.showAndWait();
                                    ButtonType buttonType = result.orElse(hebrewNo);

                                    if (buttonType != hebrewYes) {
                                        cell.setEditable(false);
                                        customerTable.refresh();
                                        return;
                                    }
                                    customer.setStore(stores.get(0));
                                }
                                customer.getAccount().setAccountType(box.getSelectionModel().getSelectedItem());
                                cell.setEditable(false);
                                sPanel.updateCustomer(customer);
                                customerTable.refresh();
                            }
                        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                            cell.setEditable(false);
                            customerTable.refresh();
                            customerTable.getSelectionModel().clearSelection();
                        }
                    }
                });
                return cell;
            }
        };
        accountTypeCol.setCellFactory(accountTypeBoxCallback);
    }
}
