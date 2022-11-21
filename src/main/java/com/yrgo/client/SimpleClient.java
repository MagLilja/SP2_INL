package com.yrgo.client;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import com.yrgo.services.calls.CallHandlingService;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class SimpleClient {

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application_annotation.xml")) {

            CustomerManagementService customerService = container.getBean(CustomerManagementService.class);
            CallHandlingService callService = container.getBean(CallHandlingService.class);
            DiaryManagementService diaryService = container.getBean(DiaryManagementService.class);

            customerService.newCustomer(new Customer("CS03939", "Acme", "Good Customer"));
            Call call = new Call("Call from Acme");
            Action action = new Action("Call back tomorrow", LocalDate.of(2016, 12, 1), "Customer Harry");
            Action action1 = new Action("Talk to Harry", LocalDate.of(2016, 12, 10), "Customer Harry");
            //Testing the call service with invalid customer id
            List<Action> actionList = List.of(action, action1);
            try {
                callService.recordCall("BAD ID", call, actionList);
            } catch (CustomerNotFoundException e) {
                System.out.println("Customer not found");
            }

            //Testing the call service with valid customer id
            try {
                callService.recordCall("CS03939", call, actionList);
            } catch (CustomerNotFoundException e) {
                System.out.println("Customer not found");
            }

            //Testing the diary service
            diaryService.getAllIncompleteActions("Customer Harry").forEach(System.out::println);
        }


    }

}
