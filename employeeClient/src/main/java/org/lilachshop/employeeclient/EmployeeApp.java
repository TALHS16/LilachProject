package org.lilachshop.employeeclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.lilachshop.commonUtils.Socket;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * JavaFX App
 */
public class EmployeeApp extends Application {
    static Socket socket;
    private static final double WIDTH = 1280.0;
    private static final double HEIGHT = 800.0;

    private static Scene scene;
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("EmployeeLogin"), WIDTH, HEIGHT);
        stage.setScene(scene);
        EmployeeApp.stage = stage;
        stage.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, EmployeeApp::onCloseWindowEvent);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EmployeeApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void onCloseWindowEvent(WindowEvent event) {
        System.out.println("Graceful termination, goodbye ;)");
        System.exit(0);
    }

    public static void main(String[] args) {
        setSocket(args);
        launch(args);
    }

    public static Socket getSocket() {
        return socket;
    }

    private static void setSocket(String[] args) {
        try {
            int port = Socket.DEFAULT_PORT;
            try {
                port = Integer.parseInt(args[1]);
                socket = new Socket(args[0], port);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                socket = new Socket(args[0]);
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host.");
        } catch (IndexOutOfBoundsException e) {
            socket = new Socket();
        }
    }
}
