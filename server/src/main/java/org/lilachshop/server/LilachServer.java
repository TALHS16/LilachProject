package org.lilachshop.server;

import org.lilachshop.entities.*;
import org.lilachshop.events.RefreshCatalogEvent;
import org.lilachshop.events.*;
import org.lilachshop.server.ocsf.AbstractServer;
import org.lilachshop.server.ocsf.ConnectionToClient;
import org.lilachshop.requests.*;

import java.io.IOException;
import java.util.*;

public class LilachServer extends AbstractServer {
    private static EntityFactory entityFactory;
    private static Set<User> connectedUsers = null;

    public LilachServer(Integer... port) throws IOException {
        // default is 3000, otherwise needs to be specified.
        super(port.length > 0 ? port[0] : 3000);
        assert port.length < 2 : "Server should receive only a port.";
        if (connectedUsers == null) {
            connectedUsers = new HashSet<>();
        }
        try {
            entityFactory = EntityFactory.getEntityFactory();
        } catch (Exception e) {
            System.out.println("Unable to setup EntityFactory.");
            throw e;
        }
//        entityFactory.fillDataBase();
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (msg == null) {
            try {
                client.sendToClient("Exception: Message was null!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        //************************** SignOut Request *****************************************

        if ((msg.getClass().equals(SignOutRequest.class))) {
            SignOutRequest request = (SignOutRequest) msg;
            String message = request.getRequest();
            switch (message) {
                case "SIGN_OUT" -> {
                    connectedUsers.removeIf(u -> u.getId().equals(request.getUser().getId()));
                }
                case "CHECK_SIGNED_OUT" -> {
                    if (connectedUsers.contains(request.getUser())) {
                        try {
                            client.sendToClient(Boolean.FALSE);
                        } catch (IOException e) {
                            System.out.println("Unable to send SignOut answer to client.");
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            client.sendToClient(Boolean.TRUE);
                        } catch (IOException e) {
                            System.out.println("Unable to send SignOut answer to client.");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        //************************** Cart Request ********************************************
        else if((msg.getClass().equals(CartRequest.class))){
            CartRequest request = (CartRequest) msg;
            String message_from_client = request.getRequest();
            if (message_from_client.equals("refresh cart items")) {
                List<myOrderItem> myOrderItemList = request.getOrderItemList();
                List<Item> allItems = entityFactory.getAllItems();
                Set<Item> itemSet = new HashSet<>(allItems);
                for (Item item: itemSet){
                    for (myOrderItem orderItem: myOrderItemList){
                        if(item.getId() == orderItem.getItem().getId()){
                            orderItem.setItem(item);
                        }
                    }
                }
//                for (myOrderItem myOrderItem : myOrderItemList) {
//                    Item itemIter = myOrderItem.getItem();
//                    Item updatedItem = entityFactory.getItemByID(itemIter.getId());
//                    myOrderItem.setItem(updatedItem);
//                }
                try {
                    client.sendToClient(new UpdateCartEvent("updated item list success", myOrderItemList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //************************** Order Request *******************************************

        else if ((msg.getClass().equals(OrderRequest.class))) {
            OrderRequest request = (OrderRequest) msg;
            String message_from_client = request.getRequest();
            if (message_from_client.equals("create new order")) {
                entityFactory.addOrder(request.getOrder());
            } else if (message_from_client.equals("get store orders")) {
                long storeID = request.getStoreID();
                List<Order> orders = entityFactory.getOrdersByStoreId(storeID);
                OrderEvent orderEvent = new OrderEvent(orders);
                try {
                    client.sendToClient(orderEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (message_from_client.equals("update order")) {
                Order order = request.getOrder();
                entityFactory.createOrUpdateSingleRecord(order);
                try {
                    client.sendToClient("order update success");
                } catch (Exception e) {
                }
            }
            if (message_from_client.equals("get all clients orders")) {
                long customerID = request.getCustomerID();
                List<Order> customerOrders = entityFactory.getOrderCustomerID(customerID);
                try {
                    OrderEvent ordersEvent = new OrderEvent(customerOrders);
                    client.sendToClient(ordersEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (message_from_client.equals("cancel order")) {
                try {
                    Order order = request.getOrder();
                    entityFactory.createOrUpdateSingleRecord(order);
                    client.sendToClient("Cancellation was successful");
                } catch (Exception e) {
                    try {
                        client.sendToClient("Cancellation failed");
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                }

            }
        }

        //************************** Customer edit Request ***********************************

        else if (msg.getClass().equals(CustomerEditRequest.class)) {
            CustomerEditRequest request = (CustomerEditRequest) msg;
            String message_from_client = request.getRequest();

            switch (message_from_client) {
                case "GET_ALL_CUSTOMERS" -> {
                    List<Customer> customers = entityFactory.getCustomers();
                    try {
                        client.sendToClient(new CustomerEvent(customers));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "CREATE_UPDATE_CUSTOMER" -> {
                    Customer customer = request.getCustomer();
                    entityFactory.addCustomer(customer); // this also updates customers
                }
                default -> {
                    System.out.println("got to default in customer edit request");
                }
            }
        }

        //************************ Report Request*********************************************

        else if (msg.getClass().equals(ReportsRequest.class)) {
            ReportsRequest request = (ReportsRequest) msg;
            String message_from_client = request.getRequest();

            switch (message_from_client) {
                case "get all stores orders" -> {
                    List<Order> orders = entityFactory.getOrders();
                    List<Item> items = entityFactory.getAllItems();
                    List<Catalog> allCatalogs = entityFactory.getAllCatalogs();
                    OrderEvent orderEvent = new OrderEvent(orders);
                    orderEvent.setItems(items);
                    orderEvent.setCatalogs(allCatalogs);
                    try {
                        client.sendToClient(orderEvent);
                    } catch (Exception e) {

                    }
                }
                case "get all complaints" -> {
                    List<Complaint> allCompaints = entityFactory.getAllComplaints();
                    ComplaintsEvent complaintsEvent = new ComplaintsEvent(allCompaints);
                    try {
                        client.sendToClient(complaintsEvent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "get store complaints" -> {
                    long storeID = request.getStoreID();
                    List<Complaint> complaints = entityFactory.getComplaintsByStoreId(storeID);
                    try {
                        client.sendToClient(complaints);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "get store orders" -> {
                    long storeID = request.getStoreID();
                    List<Order> orders = entityFactory.getOrdersByStoreId(storeID);
                    try {
                        client.sendToClient(orders);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "get store catalog" -> {
                    long storeID = request.getStoreID();
                    Catalog catalog = entityFactory.getSingleCatalogEntityRecord(storeID);
                    try {
                        client.sendToClient(catalog);
                    } catch (Exception e) {
                    }
                }
            }
        }


        //************************** Employee edit Request ***********************************

        else if (msg.getClass().equals(EmployeeEditRequest.class)) {
            System.out.println("Got EmployeeEditRequest...");
            EmployeeEditRequest request = (EmployeeEditRequest) msg;
            String message_from_client = request.getRequest();
            System.out.println("Message content: <" + message_from_client + ">");
            switch (message_from_client) {
                case "GET_ALL_EMPLOYEES" -> {
                    List<Employee> employees = entityFactory.getEmployees();
                    try {
                        client.sendToClient(employees);
                    } catch (IOException e) {
                        System.out.println("Could not send list of employees to client.");
                        e.printStackTrace();
                    }
                }
                case "SET_ALL_EMPLOYEES" -> {
                    List<Employee> employees = request.getAllEmployeesToEdit();
                    entityFactory.addAllEmployees(employees);
                }
                case "DELETE_EMPLOYEES_BY_ID" -> {
                    Set<Long> ids = request.getIdsToDelete();
                    entityFactory.removeEmployeesByID(ids);
                }
                case "CREATE_UPDATE_EMPLOYEE" -> {
                    entityFactory.updateEmployee(request.getEmployeeToEdit());
                }
            }
        }

        //************************** Store Request ***********************************

        else if (msg.getClass().equals(StoreRequest.class)) {
            StoreRequest request = (StoreRequest) msg;
            String messageFromClient = request.getRequest();

            switch (messageFromClient) {
                //CASE: want to get list of store, example for common usage - choiceBox
                case "get all stores" -> {
                    List<Store> allStores = entityFactory.getAllStores();
                    try {
                        client.sendToClient(allStores);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "get all stores event" -> {
                    {
                        List<Store> allStores = entityFactory.getAllStores();
                        try {
                            client.sendToClient(new StoreEvent(allStores));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        //************************Employee Login Request*****************************

        else if (msg.getClass().equals(EmployeeLoginRequest.class)) {
            EmployeeLoginRequest request = (EmployeeLoginRequest) msg;
            String userName = request.getUserName();
            String password = request.getPassword();
            List<Employee> employees = entityFactory.getEmployees();
            for (Employee employee : employees) {
                if (employee.getUserName().equals(userName) && employee.getUserPassword().equals(password)) {
                    for (User user : connectedUsers) {
                        if (user.getUserName().equals(userName) && user.getUserPassword().equals(password)) {
                            try {
                                client.sendToClient("Employee is connected already");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                    try {   // Employee isn't logged yet, so log him in.
                        connectedUsers.add(employee);
                        client.sendToClient(employee);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                client.sendToClient("Incorrect details");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //************************ SignUp Request********************************

        else if (msg.getClass().equals(SignUpRequest.class)) {
            SignUpRequest request = (SignUpRequest) msg;
            String message_from_client = request.getRequest();

            try {
                switch (message_from_client) {
                    case "create new customer" -> {
                        System.out.println("Server: got new signup request with username: " + request.getCustomer().getUserName());
                        entityFactory.addCustomer(request.getCustomer());
                    }
                    case "check if username taken" -> {
                        Customer customer = entityFactory.getCustomerByUserName(request.getUserName());
                        Boolean taken = (customer != null);
                        client.sendToClient(taken); //return true of taken already,otherwise false

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //************************ Customer Login Request************************
        else if (msg.getClass().equals(CustomerLoginRequest.class)) {
            CustomerLoginRequest request = (CustomerLoginRequest) msg;
            String message_from_client = request.getRequest();
            try {
                switch (message_from_client) {
                    case "customer login request" -> {
                        String requestPassword = request.getUserPassword();
                        String requestUserName = request.getUserName();
                        //check if customer exist + if he is already connected
                        List<Customer> customers = entityFactory.getCustomers();
                        for (Customer customer : customers) {
                            if ((customer.getUserPassword().equals(requestPassword)) &&
                                    customer.getUserName().equals(requestUserName)) {

                                //account exists - check if already logged in
                                if (connectedUsers.contains((User) customer)) {
                                    client.sendToClient("User is connected already");

                                } else {
                                    try {
                                        if (customer.getAccountState().equals(ActiveDisabledState.ACTIVE)) {
                                            connectedUsers.add(customer);
                                            client.sendToClient(customer);
                                        } else
                                            client.sendToClient("client account disabled");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                return;
                            }
                        }
                        try {
                            client.sendToClient("client not exist");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //************************ User Complaint Request*****************************
        else if (msg.getClass().equals(UserComplaintRequest.class)) {
            UserComplaintRequest request = (UserComplaintRequest) msg;
            String message_from_client = request.getRequest();
            try {
                switch (message_from_client) {
                    case "post new complaint" -> {
                        System.out.println("posting new complaint:");
                        Complaint complaint = request.getComplaint();
                        Order order = request.getOrder();
                        entityFactory.createOrUpdateSingleRecord(order);
                        entityFactory.createOrUpdateSingleRecord(complaint);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        //************************ Support Complaint Request*****************************

        else if (msg.getClass().equals(SupportComplaintRequest.class)) {
            SupportComplaintRequest request = (SupportComplaintRequest) msg;
            String message_from_client = request.getRequest();
            try {
                switch (message_from_client) {
                    case "get all complaints" -> {
                        List<Complaint> complaints = entityFactory.getAllComplaints();
                        client.sendToClient(complaints);
                    }
                    case "reply to customer complaint" -> {
                        Complaint complaint = request.getComplaint();
                        complaint.setReply(request.getReply());
                        Order order = request.getOrder();
                        entityFactory.createOrUpdateSingleRecord(order);
                        entityFactory.createOrUpdateSingleRecord(complaint);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        if(msg.getClass().equals(ComplaintRequest.class)){
//            ComplaintRequest request = (ComplaintRequest) msg;
//            String message_from_client = request.getRequest();
//            try{
//                switch (message_from_client){
//                    case "post new complaint" ->{
//                        System.out.println("posting new complaint:");
//                        Complaint complaint = request.getComplaint();
//                        entityFactory.createOrUpdateSingleRecord(complaint);
//                        System.out.println(complaint.getContent());
//                    }
//                    case "get all complaints" ->{
//                        List<Complaint> complaints = entityFactory.getAllComplaints();
//                        client.sendToClient(complaints);
//                    }
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }


        //************************ Catalog Request*****************************

        else if (msg.getClass().equals(CatalogRequest.class)) {
            CatalogRequest request = (CatalogRequest) msg;
            String message_from_client = request.getRequest();
            try {
                switch (message_from_client) {
                    case "get catalog" -> {
                        Catalog catalog = entityFactory.getSingleCatalogEntityRecord(request.getId());
                        List<Item> items = catalog.getItems();
                        ItemsEvent itemEvent = new ItemsEvent(items);
                        client.sendToClient(itemEvent);
                    }
                    case "Get Catalog By StoreID" -> {
                        Catalog catalog = entityFactory.getSingleCatalogEntityRecordByStoreID(request.getId());
                        client.sendToClient(catalog);
                    }
                    case "add new Item to Catalog" -> {
                        System.out.println("Server: got request to add item to catalog num." + request.getId());
                        entityFactory.addItemToCatalog(request.getId(), request.getItem());
                        client.sendToClient("item added to catalog successfully!");
                    }
                    case "get all catalogs" -> {
                        client.sendToClient(entityFactory.getAllCatalogs());
                    }
                    case "get catalog by id" -> {
                        System.out.println("Server: got request to send Catalog by id" + request.getId());
                        client.sendToClient(entityFactory.getSingleCatalogEntityRecord(request.getId()));
                    }
                    case "get catalog by filter" -> {
                        List<Item> items = entityFactory.filterCatalog(request.getId(), request.getMinPrice(), request.getMaxPrice(), request.getColor(), request.getType());
                        ItemsEvent itemEvent = new ItemsEvent(items);
                        client.sendToClient(itemEvent);
                    }
                    case "delete item" -> {
                        System.out.println("Server:got request to delete item num " + request.getItem().getId());
                        entityFactory.removeItem(request.getItem(), request.getId());
                        client.sendToClient(new RefreshCatalogEvent((int) request.getId()));
                    }
                    case "edit Item to Catalog" -> {
                        System.out.println("Server: got request to Edit item num" + request.getItem().getId() + "in catalog" + request.getId());
                        Item itemBeforeUpdate = entityFactory.getItemByID(request.getItem().getId());
                        itemBeforeUpdate.setAsCopyOf(request.getItem());
                        entityFactory.createOrUpdateSingleRecord(itemBeforeUpdate);
                        client.sendToClient("item edited in catalog successfully!");
                        System.out.println("item edited in catalog successfully!");
                    }
                    default -> {
                        client.sendToClient("request does not exist");
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed sending reply to client.");
                e.printStackTrace();
            }
        }
        // debug request
        else if (msg.getClass().equals(DebugRequest.class)) {
            DebugRequest request = (DebugRequest) msg;
            String message_from_client = request.getRequest();
            try {
                switch (message_from_client) {
                    case "example message" -> {
                        System.out.println("Server: Received a 'example message' from client.");
                        System.out.println("Server: Sending a reply!");
                        client.sendToClient("This is a reply from LilachServer!");
                        System.out.println("Server: Message sent to client.");
                    }
                    case "write entity" -> {
                        System.out.println("Server: Writing new example entity!");
                        ExampleEntity exampleEntity = new ExampleEntity(ExampleEnum.TYPE1);
                        entityFactory.createExampleEntity(exampleEntity);
                        client.sendToClient("This is a reply from LilachServer!");
                    }
                    case "get all entities" -> client.sendToClient(entityFactory.getAllExampleEntities());

                    case "update entity1" -> {
                        int id_key = request.getIDToUpdate();
                        ExampleEnum exampleEnumToUpdate = request.getUpdateToEnum();
                        entityFactory.updateExampleEntityEnumByID(id_key, exampleEnumToUpdate);
                        client.sendToClient("This is a reply from LilachServer!");
                    }

                    case "write catalog" -> {
//                        entityFactory.createCatalog();
//                        entityFactory.createCatalogFromExistingOne();
                        List<Store> stores = entityFactory.getStores();
                        List<Customer> customers = entityFactory.getCustomers();
                        List<Employee> employees = entityFactory.getEmployees();
                        List<Complaint> complaints = entityFactory.getComplaints();
                        List<Order> orders = entityFactory.getOrders();
                        //todo: make successful queries from database!
//                        List<Store> stores = entityFactory.getStores();
//                        Store store1 = stores.get(0);
//                        List<Order> orders = store1.getOrders();
//                        List<Complaint> store1Complaints = store1.getComplaints();
//                        System.out.println(store1Complaints.get(0).getContent());

                        client.sendToClient("Catalog is created!");
                    }

                    case "get all items" -> {
                        entityFactory.getAllItems();
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed sending reply to client.");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void clientConnected(ConnectionToClient client) {
        System.out.println(client.toString() + "connected.");
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected.");
    }
}
