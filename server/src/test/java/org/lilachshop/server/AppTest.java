package org.lilachshop.server;

import org.junit.jupiter.api.*;
import org.lilachshop.entities.ExampleEntity;
import org.lilachshop.entities.ExampleEnum;
import org.lilachshop.server.ocsf_client_test.AbstractClient;
import org.lilachshop.requests.DebugRequest;
import org.lilachshop.commonUtils.Utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {

    static private class TempClient extends AbstractClient {
        private String result = null;
        private List<ExampleEntity> result_entities;

        public TempClient() {
            super("localhost", 3000);
            System.out.println("Client: Client setup complete.");
        }

        @Override
        protected void handleMessageFromServer(Object msg) {
            if (Objects.equals(Utilities.getClassType(msg), "List<ExampleEntity>"))
                result_entities = (List<ExampleEntity>) msg;    // todo: should be sent through eventbus to PANEL. not like this!
            result = "ok";
        }

        public String getResult() {
            if (result == null)
                throw new RuntimeException("Server failed to respond");
            return result;
        }
    }

    static private class ServerBooter implements Runnable {

        public ServerBooter() {
            super();
        }

        @Override
        public void run() {
            System.out.println("Server: Setting up server...");
            LilachServer lilachServer = null;
            try {
                lilachServer = new LilachServer(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Server: Done.");
            try {
                System.out.println("Server: Listening!");
                lilachServer.listen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @BeforeAll
    static void runServer() {
        Assertions.assertDoesNotThrow(() -> {
            Thread thread = new Thread(new ServerBooter(), "Server");
            thread.start(); // run server
            Thread.sleep(5000); // Hibernate setup is long...
        });
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    @Order(1)
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }


    @Test
    @Order(2)
    public void shouldSendRequestAndBeAnswered() throws IOException, InterruptedException {
        TempClient tempClient = new TempClient();
        System.out.println("Client: Connecting to server...");
        tempClient.openConnection();
        System.out.println("Client: Connected to server.");
        System.out.println("Client: Sending request to server...");

        tempClient.sendToServer(new DebugRequest("example message"));

        System.out.println("Client: Done.");
        Thread.sleep(1000);
        tempClient.closeConnection();
        assertEquals("ok", tempClient.getResult());
    }

    @Test
    @Order(3)
    public void shouldCreateExampleEntity() throws IOException, InterruptedException {
        TempClient tempClient = new TempClient();
        System.out.println("Client: Connecting to server...");
        tempClient.openConnection();
        System.out.println("Client: Connected to server.");
        System.out.println("Client: Sending request to server...");

        tempClient.sendToServer(new DebugRequest("write entity"));

        System.out.println("Client: Done.");
        Thread.sleep(1000);
        tempClient.closeConnection();
        assertEquals("ok", tempClient.getResult());
        System.out.println("Note: Check debugdb for changes manually!");
    }


    @Test
    @Order(4)
    public void shouldGetAllEntities() throws IOException, InterruptedException {
        TempClient tempClient = new TempClient();
        System.out.println("Client: Connecting to server...");
        tempClient.openConnection();
        System.out.println("Client: Connected to server.");
        System.out.println("Client: Sending request to server...");

        tempClient.sendToServer(new DebugRequest("get all entities"));

        System.out.println("Client: Done.");
        Thread.sleep(1000); // wait for result - in real case scenario should be sent by EventBus to destination.
        tempClient.closeConnection();
        System.out.println("Printing table contents:");
        for (ExampleEntity ee :
                tempClient.result_entities) {
            System.out.println(ee);
        }
    }

    @Test
    @Order(5)
    public void shouldUpdateEntityWithID1ToEnumType3() throws IOException, InterruptedException {
        TempClient tempClient = new TempClient();
        System.out.println("Client: Connecting to server...");
        tempClient.openConnection();
        System.out.println("Client: Connected to server.");
        System.out.println("Client: Sending request to server...");

        DebugRequest updateRequest = new DebugRequest("update entity1", 1, ExampleEnum.TYPE3);

        tempClient.sendToServer(updateRequest);

        System.out.println("Client: Done.");
        Thread.sleep(3000);
        tempClient.closeConnection();
        assertEquals("ok", tempClient.getResult());
        System.out.println("Note: Check debugdb for changes manually!");
    }

    @Test
    @Order(6)
    public void test6_shouldShowTYPE3() throws IOException, InterruptedException {
        shouldGetAllEntities();
    }
}
