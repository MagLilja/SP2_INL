package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


@Repository
public class CustomerDaoJpaImpl implements CustomerDao {
    @PersistenceContext
    private EntityManager em;


    @Override
    public void create(Customer customer) {

            em.persist(customer);

    }


    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        try {
            return (Customer) em.createQuery("SELECT customer FROM Customer as customer WHERE customer.customerId = :customerId")
                    .setParameter("customerId", customerId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getByName(String customerName) {
        //noinspection unchecked
        return (List<Customer>) this.em.createQuery("SELECT customer FROM Customer customer WHERE customer.companyName = :customerName")
                .setParameter("customerName", customerName)
                .getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        this.em.merge(customerToUpdate);
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        this.em.remove(oldCustomer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return this.em.createQuery("SELECT customer FROM Customer customer", Customer.class)
                .getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        return (Customer) em.createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.calls as calls WHERE c.customerId = :customerID")
                .setParameter("customerID", customerId)
                .getSingleResult();
    }

    @Override
    public void addCall(Call newCall, String customerId) throws DataAccessException {
        Customer customer = em.find(Customer.class, customerId);
        customer.addCall(newCall);
    }

}
