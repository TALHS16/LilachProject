package org.lilachshop.employeeclient;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.events.AddItemEvent;
import org.lilachshop.entities.*;
import org.lilachshop.events.RefreshCatalogEvent;
import org.lilachshop.panels.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;


public class CatalogEditTableController implements Initializable {

    static public Set<Catalog> allCatalog = null;
    static public Panel panel = null;

    private static Stage stage = null;
    private static Parent root = null;
    private static FXMLLoader fxmlLoader = null;
    private static Scene scene = null;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableView<Item> itemTable;

    @FXML
    private TableColumn<Item, Integer> discount;

    @FXML
    private TableColumn<Item, Long> id;

    @FXML
    private TableColumn<Item, String> name;

    @FXML
    private TableColumn<Item, Integer> price;


    @FXML
    private TableColumn<Item, Color> itemColorCol;

    @FXML
    private TableColumn<Item, String> descriptionCol;

    @FXML
    private TableColumn<Item, ItemType> itemTypeCol;

    @FXML
    private ChoiceBox<Catalog> storeCatalogChoice;

    @FXML
    private Button uploadImageBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //setting connection to receive msg from Pop-up
        EventBus.getDefault().register(this);

        //Setting up the table values
        Platform.runLater(() -> {
            this.name.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
            this.price.setCellValueFactory(new PropertyValueFactory<Item, Integer>("Price"));
            this.id.setCellValueFactory(new PropertyValueFactory<Item, Long>("id"));
            this.discount.setCellValueFactory(new PropertyValueFactory<Item, Integer>("percent"));
            this.descriptionCol.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
            this.itemColorCol.setCellValueFactory((new PropertyValueFactory<Item, Color>("color")));
            this.itemTypeCol.setCellValueFactory((new PropertyValueFactory<Item, ItemType>("itemType")));
        });

        itemTable.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                System.out.println("double-Click -open Edit pop up");
                Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
                openEditItem(selectedItem);
            }
        });
        //Setting up panel
        if (panel != null) {
            panel.closeConnection();
            panel = null;
        }
        panel = OperationsPanelFactory.createPanel(DashBoardController.panelEnum, EmployeeApp.getSocket(), this);
        StoreEmployeePanel storeEmployeePanel = (StoreEmployeePanel) panel;

        //Setting up listener to CatalogChoiceBox
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if ((newValue != null)) {
                    Catalog catalogToTable = (Catalog) newValue;
                    System.out.println(catalogToTable.getId());
                    System.out.println("send request to server to get the catalog" + catalogToTable.getId());
                    storeEmployeePanel.getCatalogByID(catalogToTable.getId());
                }
            }
        };
        storeCatalogChoice.getSelectionModel().selectedItemProperty().addListener(changeListener);

        //Setting up the choices in ChoiceBox
        if (allCatalog == null) {
            storeEmployeePanel.getAllCatalog();
        } else {
            storeCatalogChoice.setItems(FXCollections.observableArrayList(allCatalog));
        }
    }


    @Subscribe
    public void onRefreshTable(RefreshCatalogEvent event) {
        System.out.println("Refresh table with catalog " + event.getId());
        Platform.runLater(() -> {
            storeCatalogChoice.getSelectionModel().clearSelection();
            storeCatalogChoice.getSelectionModel().select(event.getId() - 1);
        });


    }

    //When server sends list of items Display in Table
    @Subscribe
    public void onGetCatalog(Catalog catalog) {
        System.out.println("This is Catalog from server" + catalog.getId());
        Platform.runLater(() -> {
            ObservableList<Item> storeCatalog = FXCollections.observableArrayList(catalog.getItems());
            itemTable.getItems().clear();
            itemTable.setItems(storeCatalog);
            itemTable.refresh();
        });
    }

    @Subscribe
    public void onGetAllCatalogs(List<Catalog> allCatalogs) {
        Platform.runLater(() -> {
            System.out.println("Setting up Catalog Choice");
            System.out.println(allCatalogs);
            storeCatalogChoice.setItems(FXCollections.observableArrayList(allCatalogs));
            if (!DashBoardController.panelEnum.equals(PanelEnum.CHAIN_MANAGER)) {
                storeCatalogChoice.getSelectionModel().select(DashBoardController.employee.getStore().getCatalog());
                storeCatalogChoice.setDisable(true);
            } else
                storeCatalogChoice.getSelectionModel().select(0);
        });
    }


    @FXML
    void onClickAddbtn(ActionEvent event) throws IOException {
        setUpEditItemPopUp();
        //sending relevant info to pop up
        EventBus.getDefault().post(new AddItemEvent(((StoreEmployeePanel) panel).getEmployee()));

        scene = scene == null ? new Scene(root) : scene;
        stage.setScene(scene);
        stage.show();

    }

    public void openEditItem(Item item) {
        setUpEditItemPopUp();
        CatalogEditPopUpController controller = fxmlLoader.getController();
        scene = scene == null ? new Scene(root) : scene;
        stage.setScene(scene);
        controller.setItemDetailinTF(item, storeCatalogChoice.getSelectionModel().getSelectedItem());
        stage.show();

    }

    private void setUpEditItemPopUp() {
        stage = stage == null ? new Stage() : stage;
        fxmlLoader = fxmlLoader == null ? new FXMLLoader(EmployeeApp.class.getResource("CatalogEditPopUp.fxml")) : fxmlLoader;
        if (root == null) {
            try {
                root = fxmlLoader.load();
            } catch (Exception e) {
            }
        }
    }


    @FXML
    void onClickDeleteBtn(ActionEvent event) {
        Item item = itemTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("מחיקת מוצר");
        alert.setHeaderText("מחיקת פריט - " + item.getName());
        alert.setResizable(false);
        alert.setContentText("האם ברצונך למחוק את הפריט?");
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);

        if (button == ButtonType.OK) {
            StoreEmployeePanel generalEmployeePanel = (StoreEmployeePanel) panel;
            generalEmployeePanel.deleteItem(item, storeCatalogChoice.getSelectionModel().getSelectedItem().getId());
        } else {
            System.out.println("canceled");
        }


    }
}
