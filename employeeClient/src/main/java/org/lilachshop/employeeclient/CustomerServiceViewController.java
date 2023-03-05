package org.lilachshop.employeeclient;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.entities.*;
import org.lilachshop.panels.CustomerServicePanel;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.PanelEnum;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class CustomerServiceViewController implements Initializable {
    public Employee employee;
    List<Order> orders;
    private static Panel panel;
    @FXML
    private TableView<Complaint> tableView;

    @FXML
    private TableColumn<Complaint, String> complaintNumber;

    @FXML
    private TableColumn<Complaint, String> content;

    @FXML
    private TableColumn<Complaint, String> creationDate;

    @FXML
    private TableColumn<Complaint, String> status;

    ObservableList<Complaint> listOfComplaints;

    @Override
    public void initialize(URL url, ResourceBundle rb){

        complaintNumber.setCellValueFactory(new PropertyValueFactory<Complaint, String>("id"));
        content.setCellValueFactory(new PropertyValueFactory<Complaint, String>("content"));
        creationDate.setCellValueFactory(new PropertyValueFactory<Complaint, String>("creationDate"));
        status.setCellValueFactory(new PropertyValueFactory<Complaint, String>("status"));
        panel = OperationsPanelFactory.createPanel(PanelEnum.CUSTOMER_SERVICE,EmployeeApp.getSocket(), this); // this should be the default panel according to customer/employee
        if (panel == null) {
            throw new RuntimeException("Panel creation failed!");
        }
        // send request to server to get all existing complaints
        ((CustomerServicePanel) panel).GetAllClientsComplaintsRequestToServer();

    }

    private void setListOfComplaints(List<Complaint> complaintsFromServer) {
        listOfComplaints = FXCollections.observableArrayList();
        listOfComplaints.addAll(complaintsFromServer);
        tableView.setEditable(true);
        tableView.setItems(listOfComplaints);
        tableView.setOnMouseClicked(e ->{
            try {
                presentRowSelected();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void closeComplaint(String updatedComplaintNumber, String reply, Order order){
        for (Complaint complaint : listOfComplaints) {
            if(String.valueOf(complaint.getId()).equals(updatedComplaintNumber)){
                complaint.setStatus(ComplaintStatus.CLOSED);
                complaint.setReply(reply);
                // close complaint on server
                ((CustomerServicePanel) panel).ReplyToComplaintRequestToServer(complaint, reply, order);
                break;
            }
        }
        tableView.setItems(listOfComplaints);
        tableView.refresh();
    }

    private Order getComplaintOrder(Complaint complaint) {
        for(Order order: orders){
            if (order.getComplaint().getId() == (complaint.getId())){
                return order;
            }
        }
        throw new RuntimeException("complaint has no order");
    }

    private void presentRowSelected() throws IOException {
        if (listOfComplaints.isEmpty())
            return;
        Complaint selectedComplaint = tableView.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("complaintWorkerResponse.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Order order = selectedComplaint.getOrder();
        ComplaintWorkerResponseController controller = fxmlLoader.getController();
        ComplaintStatus complaintStatus = selectedComplaint.getStatus();
        String status;
        switch (complaintStatus){
            case LATE:{
                status= "באיחור";
                break;
            }
            case OPEN:{
                status= "פתוח";
                break;
            }
            case CLOSED:{
                status= "סגור";
                break;
            }
            default: status = "";
        }
        order.setComplaint(selectedComplaint);
        try{
            controller.setComplaintData(
                    String.valueOf(selectedComplaint.getId()),
                    status,
                    selectedComplaint.getCreationDate(),
                    selectedComplaint.getContent(), this, order);
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @Subscribe
    public void handleComplaintsReceivedFromClient(Object msg) {
        System.out.println("Complaints recieved from server");

        Platform.runLater(()->{
            setListOfComplaints((List<Complaint>)msg);
        });
    }

}