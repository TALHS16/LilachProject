package org.lilachshop.employeeclient;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.commonUtils.Utilities;
import org.lilachshop.events.AddItemEvent;
import org.lilachshop.entities.*;
import org.lilachshop.events.RefreshCatalogEvent;
import org.lilachshop.panels.StoreEmployeePanel;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.PanelEnum;

import javax.imageio.ImageIO;

public class CatalogEditPopUpController implements Initializable {

    static public Panel panel = null;
    private boolean saveMode;
    private boolean editMode;
    static public Set<Catalog> allCatalog = null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea DescriptionTF;

    @FXML
    private TextField itemIDTF;

    @FXML
    private ImageView itemImgUpload;

    @FXML
    private TextField itemNameTF;

    @FXML
    private TextField priceTF;

    @FXML
    private Button saveBtn;

    @FXML
    private TextField discountTF;

    @FXML
    private ChoiceBox<Catalog> catalogChoiceBox;

    @FXML
    private ChoiceBox<ItemType> itemTypeChoiceBox;

    @FXML
    private ChoiceBox<Color> itemColorChoiceBox;

    @FXML
    private Button fileChooserBtn;

    @FXML
    private TextField filePathTF;

    Alert a = new Alert(Alert.AlertType.INFORMATION);

    byte[] imageblob = null;

    @FXML
    void onClickSaveBtn(ActionEvent event) {
        //setting up item Info for Database Change
        Item item = validateItemInfoBeforeSave();
        if (item != null) {
            if (panel != null) {
                panel.closeConnection();
                panel = null;
            }
            panel = OperationsPanelFactory.createPanel(DashBoardController.panelEnum, EmployeeApp.getSocket(), this);
            StoreEmployeePanel generalEmployeePanel = (StoreEmployeePanel) panel;
            if (saveMode) {
                //setting up item Info for Database Change
                generalEmployeePanel.saveNewItem(item, catalogChoiceBox.getSelectionModel().getSelectedItem(), saveMode);
            } else {
                item.setId(Integer.parseInt(itemIDTF.getText()));
                generalEmployeePanel.saveNewItem(item, catalogChoiceBox.getSelectionModel().getSelectedItem(), saveMode);
            }
        }
    }

    @Subscribe
    public void onChangesToDB(String msg) {
        System.out.println(msg);
        Catalog catalog = catalogChoiceBox.getSelectionModel().getSelectedItem();
        EventBus.getDefault().post(new RefreshCatalogEvent(catalog.getId()));
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        Platform.runLater(() -> {
            clearAllTextField();
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText(msg);
            a.show();
            stage.close();
        });
    }


    @FXML
    void uploadImg(MouseEvent event) {

    }

    @FXML
    void onFileChooser(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        String path = selectedFile.getPath();
        String fileName = selectedFile.getName();
        String extension = fileName.split("\\.")[1].toUpperCase(Locale.ROOT);
        try {
            imageblob = Utilities.imgFileToBytesConverter(selectedFile, extension);
            itemImgUpload.setImage(Utilities.bytesToImageConverter(imageblob));
            filePathTF.setText(path);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("שגיאה בהעלאת תמונה");
            alert.setContentText("לא ניתן לעלאות תמונה זו, אנא נסה תמונה אחרת");
            filePathTF.clear();
        }

    }


    @Subscribe
    public void onAddItemEvent(AddItemEvent event) {
        saveMode = true;
        System.out.println("POP UP - ADD ITEM");
        if (panel != null) {
            panel.closeConnection();
            panel = null;
        }
        panel = OperationsPanelFactory.createPanel(DashBoardController.panelEnum, EmployeeApp.getSocket(), this);
        StoreEmployeePanel generalEmployeePanel = (StoreEmployeePanel) panel;
        generalEmployeePanel.getAllCatalog();
    }


    @Subscribe
    public void onGetAllCatalog(List<Catalog> catalogs) {
        allCatalog = allCatalog == null ? new HashSet<>(catalogs) : allCatalog;
        Platform.runLater(() -> {
            if (DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)) {
                catalogChoiceBox.setItems(FXCollections.observableArrayList(catalogs));
                DescriptionTF.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::onCloseWindowEvent);
            } else {
                catalogChoiceBox.setItems(FXCollections.observableArrayList(List.of(DashBoardController.employee.getStore().getCatalog())));
            }
        });
    }


    @FXML
    void initialize() {
        assert DescriptionTF != null : "fx:id=\"DescriptionTF\" was not injected: check your FXML file 'CatalogEditPopUp.fxml'.";
        assert itemIDTF != null : "fx:id=\"itemIDTF\" was not injected: check your FXML file 'CatalogEditPopUp.fxml'.";
        assert itemImgUpload != null : "fx:id=\"itemImgUpload\" was not injected: check your FXML file 'CatalogEditPopUp.fxml'.";
        assert itemNameTF != null : "fx:id=\"itemNameTF\" was not injected: check your FXML file 'CatalogEditPopUp.fxml'.";
        assert priceTF != null : "fx:id=\"priceTF\" was not injected: check your FXML file 'CatalogEditPopUp.fxml'.";
        assert saveBtn != null : "fx:id=\"saveBtn\" was not injected: check your FXML file 'CatalogEditPopUp.fxml'.";
        assert catalogChoiceBox != null : "fx:id=\"storeChoiceBox\" was not injected: check your FXML file 'CatalogEditPopUp.fxml'.";

    }

    public void onCloseWindowEvent(WindowEvent event) {
        clearAllTextField();
    }

    public void setStoreChoiceBox(ChoiceBox<Catalog> storeChoiceBox) {
        this.catalogChoiceBox = storeChoiceBox;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setting up EventBus
        if (!(EventBus.getDefault().isRegistered(this))) {
            EventBus.getDefault().register(this);
        }
        Platform.runLater(() -> {
            //Setting up Enum ChoiceBox
            itemTypeChoiceBox.setItems(FXCollections.observableArrayList(ItemType.values()));
            itemColorChoiceBox.setItems((FXCollections.observableArrayList(Color.values())));
        });
    }

    //initialize the pop-up with the item info
    public void setItemDetailinTF(Item itemToEdit, Catalog catalog) {
        DescriptionTF.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::onCloseWindowEvent);
        //Setting mode
        saveMode = false;
        //setting up text Fields
        itemNameTF.setText(itemToEdit.getName());
        priceTF.setText(String.valueOf(itemToEdit.getPrice()));
        discountTF.setText(String.valueOf(itemToEdit.getPercent()));
        DescriptionTF.setText(itemToEdit.getDescription());
        itemTypeChoiceBox.getSelectionModel().select(itemToEdit.getItemType());
        itemColorChoiceBox.getSelectionModel().select(itemToEdit.getColor());
        itemIDTF.setText(String.valueOf(itemToEdit.getId()));
        this.catalogChoiceBox.setItems(FXCollections.observableArrayList(catalog));
        catalogChoiceBox.getSelectionModel().select(catalog);
        if (itemToEdit.getImageBlob() != null) {
            itemImgUpload.setImage(Utilities.bytesToImageConverter(itemToEdit.getImageBlob()));
            imageblob = itemToEdit.getImageBlob();
        }
    }

    private Item validateItemInfoBeforeSave() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("שגיאה במילוי פרטי מוצר");

        if ((itemNameTF.getText() == null) || (!Utilities.containHebrewOrNumber(itemNameTF.getText()))) {
            itemIDTF.clear();
            alert.setHeaderText("שגיאה במילוי שם פריט");
            alert.setContentText("שם פריט ריק או אינו בעברית, אנא מלא שנית");
            alert.show();
            return null;
        }
        if ((DescriptionTF.getText() == null) || (!Utilities.containHebrewOrNumber(DescriptionTF.getText().replaceAll("[$&+,:;=?@#|'<>.-^*()%!]", "")))) {
            DescriptionTF.clear();
            alert.setHeaderText("שגיאה במילוי תיאור פריט");
            alert.setContentText("תיאור ריק או אינו מכיל תווים לא חוקיים, אנא מלא שנית בעברית");
            alert.show();
            return null;
        }
        if (itemTypeChoiceBox.getSelectionModel().isEmpty()) {
            alert.setHeaderText("שגיאה במילוי סוג פריט");
            alert.setContentText("סוג פריט לא נבחר, אנא בחר סוג פריט");
            alert.show();
            return null;
        }
        if (itemColorChoiceBox.getSelectionModel().isEmpty()) {
            alert.setHeaderText("שגיאה במילוי צבע פריט");
            alert.setContentText("סוג פריט לא נבחר, אנא בחר צבע פריט");
            alert.show();
            return null;
        }
        if (catalogChoiceBox.getSelectionModel().isEmpty()) {
            alert.setHeaderText("שגיאה בבחירת חנות");
            alert.setContentText("לא נבחרה חנות, אנא בחר חנות");
            alert.show();
            return null;
        }
        try {
            int price = Integer.parseInt(priceTF.getText());
            if (price < 0)
                throw new Exception();
        } catch (Exception e) {
            alert.setHeaderText("שגיאה במילוי מחיר פריט");
            alert.setContentText("מחיר הפריט שהוקלד אינו תקין, אנא לא שנית");
            alert.show();
            priceTF.clear();
            return null;
        }
        try {
            int discount = Integer.parseInt(discountTF.getText());
            if ((discount < 0) || (discount > 100))
                throw new Exception();
        } catch (Exception e) {
            alert.setHeaderText("שגיאה במילוי מחיר פריט");
            alert.setContentText("אחוז ההנחה שהוקלד אינו תקין, אנא לא שנית");
            alert.show();
            discountTF.clear();
            return null;
        }
        if (imageblob == null) {
            alert.setHeaderText("שגיאה בבחירת תמונה");
            alert.setContentText("לא נבחרה תמונה, אנא בחר תמונה לפריט");
            alert.show();
            return null;
        }

        Item item = new Item(itemNameTF.getText(), DescriptionTF.getText(),
                Integer.parseInt(discountTF.getText()),
                Integer.parseInt(priceTF.getText())
                , itemTypeChoiceBox.getSelectionModel().getSelectedItem(),
                itemColorChoiceBox.getSelectionModel().getSelectedItem(),
                imageblob);
        return item;
    }

    public void clearAllTextField() {
        itemIDTF.clear();
        itemNameTF.clear();
        priceTF.clear();
        discountTF.clear();
        DescriptionTF.clear();
        itemImgUpload.setImage(null);
        itemColorChoiceBox.setValue(null);
        itemTypeChoiceBox.setValue(null);
        catalogChoiceBox.getSelectionModel().clearSelection();
        filePathTF.clear();
        imageblob = null;
    }
}
