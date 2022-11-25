package com.yrgo.services.customers;

import com.yrgo.dataaccess.CustomerDao;
import com.yrgo.dataaccess.RecordNotFoundException;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerManagementServiceProductionImpl implements CustomerManagementService {

    @Autowired
    private CustomerDao customerDao;
    String customer_not_found = "Customer not found";


//    public CustomerManagementServiceProductionImpl(CustomerDao customerDao) {
//        this.customerDao = customerDao;
//    }

    @Override
    @Transactional
    public void newCustomer(Customer newCustomer) {
        customerDao.create(newCustomer);

    }

    @Override
    @Transactional
    public void updateCustomer(Customer changedCustomer) throws CustomerNotFoundException {
        try {
            customerDao.update(changedCustomer);
        } catch (RecordNotFoundException e) {

            System.err.println(customer_not_found);
            throw new CustomerNotFoundException();
        }

    }

    @Override
    @Transactional
    public void deleteCustomer(Customer oldCustomer) throws CustomerNotFoundException {
        try {
            customerDao.delete(oldCustomer);
        } catch (RecordNotFoundException e) {
            System.err.println(customer_not_found);
            throw new CustomerNotFoundException();
        }

    }

    @Override
    @Transactional
    public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
        try {
            return customerDao.getById(customerId);
        } catch (RecordNotFoundException e) {
            System.err.println(customer_not_found);
            throw new CustomerNotFoundException();
        }
    }

    @Override
    @Transactional
    public List<Customer> findCustomersByName(String name) {
        return customerDao.getByName(name);
    }

    @Override
    @Transactional
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    @Override
    @Transactional
    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        try {
            return customerDao.getFullCustomerDetail(customerId);
        } catch (RecordNotFoundException e) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    @Transactional
    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        try {
            customerDao.addCall(callDetails, customerId);
        } catch (DataAccessException e) {
            throw new CustomerNotFoundException();
        }

    }

}
