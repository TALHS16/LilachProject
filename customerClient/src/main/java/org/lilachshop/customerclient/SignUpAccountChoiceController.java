package org.lilachshop.customerclient;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.entities.AccountType;
import org.lilachshop.entities.Store;
import org.lilachshop.events.AccountChoiceEvent;
import org.lilachshop.panels.CustomerAnonymousPanel;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import javafx.beans.value.ChangeListener;
import org.lilachshop.panels.PanelEnum;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SignUpAccountChoiceController implements Initializable {

    static private Panel panel = null;
    static Store defaultStore = null;
    static List<Store> allStoresList = null;
    AccountType chosenAccount;

    @FXML
    private Pane ChainAccountBtn;

    @FXML
    private Rectangle ChainAccountHighlight;

    @FXML
    private ImageView ChainAccountPaneHighlight;

    @FXML
    private Pane StoreAccountBtn;

    @FXML
    private Pane YearlyAccountBtn;

    @FXML
    private Rectangle YearlyAccountHighlight;

    @FXML
    private Text chooseStoreTXT;

    @FXML
    private Button nextBtn;

    @FXML
    private Rectangle storeAccountHighlight;

    @FXML
    private ChoiceBox<Store> storeChoiceBox;

    @FXML
    void onClickNextbtn(ActionEvent event) { //moves next to Final stage

        AccountChoiceEvent event3 = new AccountChoiceEvent();

        switch (chosenAccount) {
            case STORE_ACCOUNT: {
                event3 = new AccountChoiceEvent(AccountType.STORE_ACCOUNT, storeChoiceBox.getSelectionModel().getSelectedItem());
                EventBus.getDefault().post(event3);
                moveToFinalStage();
                break;
            }
            case CHAIN_ACCOUNT: {
                event3 = new AccountChoiceEvent(AccountType.CHAIN_ACCOUNT, defaultStore);
                EventBus.getDefault().post(event3);
                moveToFinalStage();
                break;
            }
            case ANNUAL_SUBSCRIPTION: {
                // alert on charging 100shekels if Annual account
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("חיוב עלות מנוי שנתי");
            alert.setHeaderText("נבחר סוג חשבון - מנוי שנתי, אנא אשר חיוב עלות מנוי");
            alert.setResizable(false);
            alert.setContentText(" האם הנך מאשר חיוב בעלות 100 שקלים?");
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);

            if (button == ButtonType.OK) {
               alert2.setContentText("עסקה בסך 100 שקלים אושרה");
               alert2.show();
               event3 = new AccountChoiceEvent(AccountType.ANNUAL_SUBSCRIPTION, defaultStore);
               EventBus.getDefault().post(event3);
               moveToFinalStage();
               break;
            } else {
                alert2.setContentText("אנא, בחר סוג חשבון שוב");
                alert2.show();
                YearlyAccountHighlight.setVisible(false);
                chosenAccount = null;
                nextBtn.setDisable(true);
            }
            } default:{
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("שגיאה בבחירת סוג חשבון");
                a.setHeaderText("לא נבחר סוג חשבון");
                a.setResizable(false);
                a.setContentText("אנא בחר סוג חשבון");
                a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            }
        }

    }

    @FXML
    void onClickBtnBack(ActionEvent event) {  // moves back to CreditCard stage
        Stage stage = CustomerApp.getStage();
        FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("SignUp4.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    void onClickStoreAccountBtn(MouseEvent event) {
        nextBtn.setDisable(true);
        storeAccountHighlight.setVisible(true);
        ChainAccountHighlight.setVisible(false);
        YearlyAccountHighlight.setVisible(false);

        storeChoiceBox.setDisable(false);
        chosenAccount = AccountType.STORE_ACCOUNT;

    }

    @FXML
    void onClickChainAccount(MouseEvent event) {
        nextBtn.setDisable(false);

        storeAccountHighlight.setVisible(false);
        ChainAccountHighlight.setVisible(true);
        YearlyAccountHighlight.setVisible(false);

        storeChoiceBox.setDisable(true);
        storeChoiceBox.getSelectionModel().clearSelection();
        chosenAccount = AccountType.CHAIN_ACCOUNT;
    }

    @FXML
    void onClickYearlyAccount(MouseEvent event) {
        nextBtn.setDisable(false);

        storeAccountHighlight.setVisible(false);
        ChainAccountHighlight.setVisible(false);
        YearlyAccountHighlight.setVisible(true);

        storeChoiceBox.setDisable(true);
        storeChoiceBox.getSelectionModel().clearSelection();
        chosenAccount = AccountType.ANNUAL_SUBSCRIPTION;
    }

    @Subscribe
    public void onGetAllStores(List<Store> allStores) {
        defaultStore = defaultStore == null ? allStores.get(0) : defaultStore;
        allStoresList = allStoresList == null ? FXCollections.observableArrayList(allStores): allStoresList;
        Platform.runLater(() -> {
            storeChoiceBox.setItems(FXCollections.observableArrayList(allStoresList));
            storeChoiceBox.getItems().remove(0);
            System.out.println("num of items in choice box:" + storeChoiceBox.getItems().size());

        });

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (panel == null)
            panel = OperationsPanelFactory.createPanel(PanelEnum.CUSTOMER_ANONYMOUS, CustomerApp.getSocket(), this);

        CustomerAnonymousPanel customerAnonymousPanel = (CustomerAnonymousPanel) panel;
        if (customerAnonymousPanel == null) {
            throw new RuntimeException("Bad panel output in Signup Stage 3 controller");
        }

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    nextBtn.setDisable(false);
                }
            }
        };

        if (allStoresList == null) {
            customerAnonymousPanel.getAllStores();
        } else {
            Platform.runLater(()->{
                storeChoiceBox.setItems(FXCollections.observableArrayList(allStoresList));
                System.out.println("num of items in choice box:" + storeChoiceBox.getItems().size());
            });
        }
        storeChoiceBox.getSelectionModel().selectedItemProperty().addListener(changeListener);

    }
    private void moveToFinalStage(){
        Stage stage = CustomerApp.getStage();
        FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("SignUp5.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
        stage.show();
    }
}
