package com.yrgo.client;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.services.calls.CallHandlingService;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class SimpleClient {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");

        DiaryManagementService diaryService = container.getBean(DiaryManagementService.class);
        CallHandlingService callService = container.getBean(CallHandlingService.class);

        Call newCall = new Call("Dom called from Twin Peaks Company");

        List<Action> actions = new ArrayList<Action>();
        GregorianCalendar gregorianCalendar = new GregorianCalendar(2019, Calendar.DECEMBER, 10);
        actions.add(new Action("Call back Dom as soon as possible for feedback",
                LocalDate.of(2019,Month.DECEMBER,10), "user"));
        actions.add(new Action("Check if Dom called again",
                LocalDate.of(2019,Month.DECEMBER,11)
, "user"));

        try {
            callService.recordCall("NV10", newCall, actions);
        } catch (CustomerNotFoundException e) {
            System.err.println("This customer does not exist.");
        }

        System.out.println("Here are the actions:");
        diaryService.getAllIncompleteActions("user").forEach(System.out::println);

        container.close();
    }

}
