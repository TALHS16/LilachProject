package org.lilachshop.panels;

import org.jetbrains.annotations.Nullable;
import org.lilachshop.commonUtils.Socket;

public class OperationsPanelFactory {
    private OperationsPanelFactory() {
    }

    @Nullable
    public static Panel createPanel(PanelEnum panel_num, Socket socket, Object controller) {
        switch (panel_num) {
//            case 1 -> {
//                return new ExamplePanel("localhost", 3000, controller);
//            }
            case CUSTOMER_ANONYMOUS: {
                return new CustomerAnonymousPanel(socket, controller);
            }
            case STORE_CUSTOMER: {
                return new StoreCustomerPanel(socket, controller);
            }
            case CHAIN_CUSTOMER: {
                return new ChainCustomerPanel(socket, controller);
            }
            case ANNUAL_CUSTOMER: {
                return new AnnualCustomerPanel(socket, controller);
            }
            case EMPLOYEE_ANONYMOUS: {
                return new EmployeeAnonymousPanel(socket, controller);
            }
            case GENERAL_EMPLOYEE: {
                return new StoreEmployeePanel(socket, controller);
            }
            case STORE_MANAGER: {
                return new StoreManagerPanel(socket, controller);
            }
            case CHAIN_MANAGER: {
                return new ChainManagerPanel(socket, controller);
            }
            case CUSTOMER_SERVICE: {
                return new CustomerServicePanel(socket, controller);
            }
            case SYSTEM_MANAGER: {
                return new SystemManagerPanel(socket, controller);
            }
            default: {
                System.out.println("No panel found.");
                return null;
            }

        }
    }
}
