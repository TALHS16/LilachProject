package org.lilachshop.employeeclient;

import javafx.fxml.FXML;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        EmployeeApp.setRoot("secondary");

    }
}
