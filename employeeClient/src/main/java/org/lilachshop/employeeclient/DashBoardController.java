package org.lilachshop.employeeclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.entities.Employee;
import org.lilachshop.entities.Role;
import org.lilachshop.entities.User;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.PanelEnum;
import org.lilachshop.panels.SignedInEmployeePanel;

public class DashBoardController {

    static Employee employee = null;
    private static Panel panel;
    private Stage stage;
    static PanelEnum panelEnum = null;
    @FXML
    private HBox orderBtn;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private HBox catalogBtn;

    @FXML
    private HBox complaintsBtn;

    @FXML
    private HBox costumerBtn;

    @FXML
    private Pane displayer;

    @FXML
    private HBox employeeBtn;

    @FXML
    private HBox reportBtn;

    @FXML
    private BorderPane mainPane;


    @FXML
    private Text helloNameLabel;


    @FXML
    private Text signOutbtn;


    @FXML
    void onClickReportBtn(MouseEvent event) { // WE DON'T USE IT

    }

    @FXML
    void InClickOrderReportBtn(ActionEvent event) {
        try {
            this.displayer.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrderReport.fxml"));
            AnchorPane anchorPaneOrderReport = fxmlLoader.load();
            anchorPaneOrderReport.prefWidthProperty().bind(displayer.prefWidthProperty());
            anchorPaneOrderReport.prefHeightProperty().bind(displayer.prefHeightProperty());
            displayer.getChildren().add(anchorPaneOrderReport);
            OrderReportController controller = fxmlLoader.getController();
            controller.setData(employee.getStore().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onClickComplaintReportBtn(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                this.displayer.getChildren().clear();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ComplaintReport.fxml"));
                AnchorPane anchorPaneComplaintReport = fxmlLoader.load();
                anchorPaneComplaintReport.prefWidthProperty().bind(displayer.prefWidthProperty());
                anchorPaneComplaintReport.prefHeightProperty().bind(displayer.prefHeightProperty());
                displayer.getChildren().add(anchorPaneComplaintReport);
                ComplaintReportController controller = fxmlLoader.getController();
                controller.setData(employee.getStore().getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    void onClickIncomeReportBtn(Event event) {
        Platform.runLater(() -> {
            try {
                this.displayer.getChildren().clear();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("IncomeReport.fxml"));
                AnchorPane anchorPaneIncomeReport = fxmlLoader.load();
                anchorPaneIncomeReport.prefWidthProperty().bind(displayer.prefWidthProperty());
                anchorPaneIncomeReport.prefHeightProperty().bind(displayer.prefHeightProperty());
                displayer.getChildren().add(anchorPaneIncomeReport);
                IncomeReportController controller = fxmlLoader.getController();
                controller.setData(employee.getStore().getId());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


    @FXML
    void onClickEmployeeBtn(MouseEvent event) throws IOException { // waiting for the GUI & Controller to be done
        Platform.runLater(() -> {
            try {
                this.displayer.getChildren().clear();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EmployeeTable.fxml"));
                BorderPane anchorPaneEmployeeTable = fxmlLoader.load();
                displayer.getChildren().add(anchorPaneEmployeeTable);
//                EmployeeTableController controller = fxmlLoader.getController();
//                controller.employee= this.employee;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void onClickCostumerBtn(MouseEvent event) {     // waiting for the GUI & Controller to be done
        Platform.runLater(() -> {
            try {
                this.displayer.getChildren().clear();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerTable.fxml"));
                BorderPane anchorPaneCustomerTable = fxmlLoader.load();
                displayer.getChildren().add(anchorPaneCustomerTable);
//                CustomerTableController controller = fxmlLoader.getController();
//                controller.employee= this.employee;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    void onClickCatalogBtn(MouseEvent event) {
        Platform.runLater(() -> {
            try {
                this.displayer.getChildren().clear();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CatalogEditTable.fxml"));
                BorderPane anchorPaneCatalog = fxmlLoader.load();
                displayer.getChildren().add(anchorPaneCatalog);
//                CatalogEditTableController  controller = fxmlLoader.getController();
//                controller.employee= this.employee;

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    void onClickOrdersBtn(MouseEvent event) {
        try {
            this.displayer.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrderTable.fxml"));
            BorderPane anchorPaneComplaints = fxmlLoader.load();
            displayer.getChildren().add(anchorPaneComplaints);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickComplaintsBtn(MouseEvent event) {
        Platform.runLater(() -> {
            try {
                this.displayer.getChildren().clear();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customerServiceView.fxml"));
                BorderPane anchorPaneComplaints = fxmlLoader.load();
                displayer.getChildren().add(anchorPaneComplaints);
                CustomerServiceViewController controller = fxmlLoader.getController();
                controller.employee = this.employee;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void onExitButton(WindowEvent windowEvent) {
        if (panel instanceof SignedInEmployeePanel) {
            ((SignedInEmployeePanel) panel).sendSignOutRequestToServer((User) this.employee);
        }
        System.out.println("Graceful termination, goodbye ;)");
        System.exit(0);
    }

    @Subscribe
    public void dummySubscribeMethod(Object object) {
    }



    @FXML
    void onClickSignout(MouseEvent event) {
        ((SignedInEmployeePanel) panel).sendSignOutRequestToServer((User) this.employee);
        FXMLLoader fxmlLoader = new FXMLLoader(DashBoardController.class.getResource("EmployeeLogin.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {

        assert catalogBtn != null : "fx:id=\"catalogBtn\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert complaintsBtn != null : "fx:id=\"complaintsBtn\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert costumerBtn != null : "fx:id=\"costumerBtn\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert displayer != null : "fx:id=\"displayer\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert reportBtn != null : "fx:id=\"reportBtn\" was not injected: check your FXML file 'dashboard.fxml'.";
    }

    public void setData(Employee employee) {
        DashBoardController.employee = employee;
        helloNameLabel.setText("שלום, " + employee.getUserName());
        stage = EmployeeApp.getStage();
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::onExitButton);
        Role role = employee.getRole();
        switch (role) {

            case STORE_EMPLOYEE: {
                panelEnum = PanelEnum.GENERAL_EMPLOYEE;
                catalogBtn.setVisible(true);
                complaintsBtn.setDisable(true);
                costumerBtn.setDisable(true);
                reportBtn.setDisable(true);
                employeeBtn.setDisable(true);
                break;
            }

            case STORE_MANAGER: {
                panelEnum = PanelEnum.STORE_MANAGER;
                catalogBtn.setVisible(true);
                reportBtn.setVisible(true);
                complaintsBtn.setDisable(true);
                costumerBtn.setDisable(true);
                employeeBtn.setDisable(true);
                break;
            }

            case CHAIN_MANAGER: {
                panelEnum = PanelEnum.CHAIN_MANAGER;
                catalogBtn.setVisible(true);
                reportBtn.setVisible(true);
                complaintsBtn.setDisable(true);
                costumerBtn.setDisable(true);
                employeeBtn.setDisable(true);
                break;
            }

            case CUSTOMER_SERVICE: {
                panelEnum = PanelEnum.CUSTOMER_SERVICE;
                complaintsBtn.setVisible(true);
                catalogBtn.setDisable(true);
                costumerBtn.setDisable(true);
                reportBtn.setDisable(true);
                employeeBtn.setDisable(true);
                break;
            }

            case SYSTEM_MANAGER: {
                panelEnum = PanelEnum.SYSTEM_MANAGER;
                employeeBtn.setVisible(true);
                costumerBtn.setVisible(true);
                catalogBtn.setDisable(true);
                complaintsBtn.setDisable(true);
                reportBtn.setDisable(true);
                break;
            }
        }
        panel = OperationsPanelFactory.createPanel(panelEnum, EmployeeApp.getSocket(), this);
    }


}
