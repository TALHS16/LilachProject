/**
 * Sample Skeleton for 'filter.fxml' Controller Class
 */

package org.lilachshop.customerclient;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import org.lilachshop.entities.Color;
import org.lilachshop.entities.ItemType;
import org.lilachshop.panels.CustomerAnonymousPanel;
import org.lilachshop.panels.StoreCustomerPanel;

public class FilterController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="close"
    private Button close; // Value injected by FXMLLoader

    @FXML // fx:id="filter"
    private Button filter; // Value injected by FXMLLoader


    @FXML
    private Label maxPrice;

    @FXML
    private Label minPrice;


    @FXML // fx:id="comboColor"
    private ChoiceBox<Color> comboColor; // Value injected by FXMLLoader

    @FXML // fx:id="comboType"
    private ChoiceBox<ItemType> comboType; // Value injected by FXMLLoader

    @FXML // fx:id="sliderPrice"
    private RangeSlider sliderPrice; // Value injected by FXMLLoader

    @FXML
    void onFilter(ActionEvent event) {
        if (CustomerApp.getMyCustomer() == null)
            ((CustomerAnonymousPanel) CustomerApp.getPanel()).sendGetFilteredCatalog(CustomerApp.getMyStore().getCatalog().getId(), (int) (sliderPrice.getLowValue()), (int) (sliderPrice.getHighValue()), comboColor.getSelectionModel().getSelectedItem(), comboType.getSelectionModel().getSelectedItem());
        else {
            ((StoreCustomerPanel) CustomerApp.getPanel()).sendGetFilteredCatalog(CustomerApp.getMyStore().getCatalog().getId(), (int) (sliderPrice.getLowValue()), (int) (sliderPrice.getHighValue()), comboColor.getSelectionModel().getSelectedItem(), comboType.getSelectionModel().getSelectedItem());
        }
        ((Stage) filter.getScene().getWindow()).close();
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'filter.fxml'.";
        assert comboColor != null : "fx:id=\"comboColor\" was not injected: check your FXML file 'filter.fxml'.";
        assert comboType != null : "fx:id=\"comboType\" was not injected: check your FXML file 'filter.fxml'.";
        assert sliderPrice != null : "fx:id=\"sliderPrice\" was not injected: check your FXML file 'filter.fxml'.";
        sliderPrice.setMin(0);
        sliderPrice.setMax(300);
        sliderPrice.setLowValue(25);
        sliderPrice.setHighValue(150);
        sliderPrice.setSnapToTicks(true);
        filter.disableProperty().bind(sliderPrice.highValueProperty().isEqualTo(0));
        sliderPrice.lowValueProperty().addListener((observableValue, number, t1) -> {
            sliderPrice.setLowValue(t1.intValue());
        });

        sliderPrice.highValueProperty().addListener((observableValue, number, t1) -> {
            sliderPrice.setHighValue(t1.intValue());
        });
        maxPrice.textProperty().bind(sliderPrice.lowValueProperty().asString());
        minPrice.textProperty().bind(sliderPrice.highValueProperty().asString());

        ObservableList<ItemType> oTypeList = FXCollections.observableArrayList();
        comboType.setItems(oTypeList);
        comboType.getItems().add(null);
        comboType.getItems().addAll(Arrays.asList(ItemType.values()));

        ObservableList<Color> oColorList = FXCollections.observableArrayList();
        comboColor.setItems(oColorList);
        comboColor.getItems().add(null);
        comboColor.getItems().addAll(Arrays.asList(Color.values()));
    }

    @FXML
    void onClose(ActionEvent event) {
        ((Stage) close.getScene().getWindow()).close();
    }

}
