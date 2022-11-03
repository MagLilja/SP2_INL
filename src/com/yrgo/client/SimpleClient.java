package com.yrgo.client;

import com.yrgo.services.customers.CustomerManagementService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimpleClient {

    public static void main(String[] args) {
        var container = new ClassPathXmlApplicationContext("application.xml");
        CustomerManagementService service = container.getBean("customerManagementService", CustomerManagementService.class);
//        service.getAllCustomers().forEach(System.out::println);

        service.findCustomersByName("River Ltd").forEach(System.out::println);
    }

}
