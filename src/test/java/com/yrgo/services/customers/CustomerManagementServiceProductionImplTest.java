package com.yrgo.services.customers;

import com.yrgo.domain.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"/other_tiers.xml", "/application_test.xml"})
@Transactional
class CustomerManagementServiceProductionImplTest {

    @Autowired
    private CustomerManagementService customerManagementService;

    @Test
    public void testCreatingNewCustomer() {

        // arrange
        Customer customer = new Customer("CS03939", "Acme", "Good Customer");
        int initialSize = customerManagementService.getAllCustomers().size();

        // act
        customerManagementService.newCustomer(customer);
        int finalSize = customerManagementService.getAllCustomers().size();
        assertThat(finalSize).isEqualTo(initialSize + 1);
    }

    @Test
    public void testFindingCustomerById()  {

        // arrange
        Customer customer = new Customer("CS03939", "Acme", "Good Customer");
        customerManagementService.newCustomer(customer);

        // act
        try {
            Customer foundCustomer = customerManagementService.findCustomerById("CS03939");
            assertThat(foundCustomer).isEqualTo(customer);
        } catch (CustomerNotFoundException e) {
            fail("CustomerNotFoundException should not be thrown");
        }

    }

}