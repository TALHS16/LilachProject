
/*
package org.lilachshop.customerclient;

import javafx.application.Platform;
import org.lilachshop.entities.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.CustomerAnonymousPanel;
import org.lilachshop.panels.Panel;
import org.greenrobot.eventbus.Subscribe;

public class PrimaryController_proto implements Initializable {
    private static Panel panel;
    final String base_path = "/images/";
    private Flower flowerShown;
    @FXML
    private ImageView FlowerImg;

    @FXML
    private Label FlowerNameLabel;

    @FXML
    private Label FlowerPrice;

    @FXML
    private TextField textFieldPriceChange;

    public Label getFlowerPrice() {
        return FlowerPrice;
    }

    public void setFlowerPrice(Label flowerPrice) {
        FlowerPrice = flowerPrice;
    }

    @FXML
    private VBox chosenFlower;

    @FXML
    private Button priceUpdate;

    @FXML
    private Button showPriceField;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Label priceUpdateWarningLabel;

    List<Item> items;
    private MyListener myListener;
    private List<Flower> flowerList = new ArrayList<>();

//    private List<Flower> getFlowerList() {
//        List<Flower> flowerList = new ArrayList<>();
//        Flower flower;
//        for (int i = 0; i < 5; i++) {
//            flower = new Flower();
//            flower.setName("סחלב קורל");
//            flower.setColor("סגול");
//            flower.setImgSrc(base_path + "sahlav_coral.jpg");
//            flower.setPrice("100 ש''ח");
//            flowerList.add(flower);
//
//            flower = new Flower();
//            flower.setName("ורד ענבר");
//            flower.setColor("ירוק");
//            flower.setImgSrc(base_path + "vered_inbar.jpg");
//            flower.setPrice("80 ש''ח");
//            flowerList.add(flower);
//
//            flower = new Flower();
//            flower.setName("סחלב לבן");
//            flower.setColor("ירוק");
//            flower.setImgSrc(base_path + "sahlav_lavan.jpg");
//            flower.setPrice("120 ש''ח");
//            flowerList.add(flower);
//
//            flower = new Flower();
//            flower.setName("סחלב סגול");
//            flower.setColor("ירוק");
//            flower.setImgSrc(base_path + "purple.jpg");
//            flower.setPrice("60 ש''ח");
//            flowerList.add(flower);
//
//            flower = new Flower();
//            flower.setName("נרקיס חצוצרה");
//            flower.setColor("ירוק");
//            flower.setImgSrc(base_path + "narkis_hatsostra.jpg");
//            flower.setPrice("40 ש''ח");
//            flowerList.add(flower);
//
//            flower = new Flower();
//            flower.setName("ורד ורוד");
//            flower.setColor("ירוק");
//            flower.setImgSrc(base_path + "vered_varod.jpg");
//            flower.setPrice("140 ש''ח");
//            flowerList.add(flower);
//        }
//        return flowerList;
//    }

    List<ItemController> itemControllers = new ArrayList<>();

    @FXML
    void initialize() {
    }


    public void setChosenItem(Flower flower) {
        FlowerNameLabel.setText(flower.getName());

        FlowerPrice.setText(Integer.toString(flower.getPrice()));
        try {
            FlowerImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(flower.getImgSrc()))));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        flowerShown = flower;
    }

    //private SimpleClient client;

    void sendPriceUpdatedItem(Flower flower, List<Item> items) {
        try {
            //Item item = convertFlowerToItem(flower);
            Item item = getItemById(flower, items);

            //   client = SimpleClient.getClient();
            //  client.openConnection();
            //  client.sendToServer(item);
            // todo: get server ok response that price was updated in DB, else do rollback/nothing?
//            while (!client.isDataReady()) {
//                Thread.sleep(300);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Item getItemById(Flower flower, List<Item> items) {
        for (Item item : items) {
            if (item.getId() == flower.getId()) {
                item.setPrice(Integer.parseInt(flower.getPrice()));

                return item;
            }
        }
        // needs to be fixed!!!!
        return items.get(0);
    }

    private Item convertFlowerToItem(Flower flower) {
        return new Item(flower.getName(), Integer.parseInt(flower.getPrice()), flower.getImgSrc());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        panel = OperationsPanelFactory.createPanel(2, this); // this should be the default panel according to customer/employee
        if (panel == null) {
            throw new RuntimeException("Panel creation failed!");
        }
        try {
            textFieldPriceChange.setVisible(false);
            priceUpdateWarningLabel.setVisible(false);
            priceUpdate.setVisible(false);
            ((CustomerAnonymousPanel) panel).sendCatalogRequestToServer();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addItemsToFlowerList(List<Item> items) {
        List<Flower> retFlowerList = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            Item curItem = items.get(i);
            retFlowerList.add(new Flower(curItem.getName(), String.valueOf(curItem.getPrice()), curItem.getImage(), curItem.getId()));

        }
        flowerList.addAll(retFlowerList);
    }

    @FXML
    void onChangePrice(ActionEvent event) {
        textFieldPriceChange.setVisible(true);
        priceUpdate.setVisible(true);
    }

    @FXML
    void onPriceUpdate(ActionEvent event) {
        try {
            String newPrice;
            newPrice = textFieldPriceChange.getText();
            try {
                int new_converted_price = Integer.parseInt(newPrice, 10); // convert to base 10
                flowerShown.setPrice(newPrice);

                FlowerPrice.setText(newPrice);
                ItemController itemController = itemControllers.get(flowerShown.getId() - 1);
                itemController.setPriceInCatalog(flowerShown);
                textFieldPriceChange.clear();
                textFieldPriceChange.setVisible(false);
                priceUpdate.setVisible(false);
                priceUpdateWarningLabel.setVisible(false);
                sendPriceUpdatedItem(flowerShown, items);
            } catch (NumberFormatException e) {
                textFieldPriceChange.clear();
                priceUpdateWarningLabel.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void handleCatalogReceivedFromClient(Object msg) {
        System.out.println("primary controller prototype recieved message from server");
        addItemsToFlowerList((List<Item>) msg);
        Platform.runLater(()->{
            renderCatalogOnScreen();
        });
    }

    private void renderCatalogOnScreen() {
        if (flowerList.size() > 0) {
            setChosenItem(flowerList.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(Flower flower) {
                    textFieldPriceChange.setVisible(false);
                    priceUpdateWarningLabel.setVisible(false);
                    priceUpdate.setVisible(false);
                    textFieldPriceChange.clear();
                    setChosenItem(flower);
                }
            };
        }
        int column = 0;
        int row = 1;
        try {
            for (Flower flower : flowerList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ItemController itemController = fxmlLoader.getController();
                itemControllers.add(itemController);
                itemController.setData(flower, myListener);
                if (column == 3) {
                    column = 0;
                    row++;
                }
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_COMPUTED_SIZE);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_COMPUTED_SIZE);
                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(5));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


*/
