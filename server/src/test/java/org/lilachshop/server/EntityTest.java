package org.lilachshop.server;


import org.junit.jupiter.api.*;
import org.lilachshop.entities.CreditCard;
import org.lilachshop.entities.Customer;

import java.io.IOException;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EntityTest {

    private static EntityFactory entityFactory;

    @BeforeAll
    static void setupHibernate() throws IOException {
        entityFactory = EntityFactory.getEntityFactory();
        entityFactory.fillDataBase();
    }

    @Test
    @Order(1)
    public void shouldReturnCustomers() {
        List<Customer> customers = entityFactory.getCustomers();
        Assertions.assertTrue(customers.size() > 0);
        System.out.println("Credit Cards:");
        for (Customer c : customers) {
            System.out.println(c.getCard().getExpDate());
        }
    }
}
