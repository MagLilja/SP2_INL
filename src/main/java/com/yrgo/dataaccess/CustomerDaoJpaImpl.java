package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class CustomerDaoJpaImpl implements CustomerDao, DaoInterface {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void create(Customer customer) {
        entityManager.persist(customer);
    }


    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        String SQL = "SELEC * FROM customer WHERE CUSTOMER_ID = ?";
        try {
            return (Customer) entityManager.createQuery("SELECT customer FROM Customer as customer WHERE customer.customerId = :customerId")
                    .setParameter("customerId", customerId)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getByName(String customerName) {
        String SQL = "SELECT * FROM customer WHERE COMPANY_NAME = ?";
        return this.entityManager.createQuery("SELECT customer FROM Customer customer")
                .getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        this.entityManager.createQuery("""
                        UPDATE Customer AS c 
                        SET c.companyName = :companyName,
                            c.email = :email,
                            c.telephone = :telephone,
                            c.notes = :notes
                        WHERE c.customerId = :customerId                    
                        """)
                .setParameter("companyName", customerToUpdate.getCompanyName())
                .setParameter("email", customerToUpdate.getEmail())
                .setParameter("telephone", customerToUpdate.getTelephone())
                .setParameter("notes", customerToUpdate.getNotes())
                .setParameter("customerId", customerToUpdate.getCustomerId())
                .executeUpdate();

    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        this.entityManager.remove(oldCustomer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return this.entityManager.createQuery("SELECT customer FROM Customer customer")
                .getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
//        Customer customer = this.entityManager.queryForObject("SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = ?",
//                new CustomerRowMapper(), customerId);
//        List<Call> calls = this.entityManager.query("SELECT * FROM CALL WHERE CUSTOMER_ID = ?",
//                new CallRowMapper(), customerId);
//        if (customer != null) {
//            customer.setCalls(calls);
//        }
//        return customer;
        return null;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws DataAccessException {
//        newCall
//        this.entityManager.persist(newCall);
//        String INSERT_CALL_SQL = """
//                INSERT INTO TBL_CALL(NOTES, TIME_AND_DATE, CUSTOMER_ID) VALUES (?, ?, ?)
//                """;
//        this.entityManager.update(INSERT_CALL_SQL, newCall.getNotes(), newCall.getTimeAndDate(), customerId);
    }

    @Override
    @PostConstruct
    public void createTable() {

        String CREATE_TABLE_CUSTOMER = """
                CREATE TABLE CUSTOMER(
                CUSTOMER_ID VARCHAR(20) PRIMARY KEY ,
                COMPANY_NAME VARCHAR(50), 
                EMAIL VARCHAR(50), 
                TELEPHONE VARCHAR(20), 
                NOTES VARCHAR(255))
                """;

        String CREATE_TABLE_CALL = """
                CREATE TABLE TBL_CALL(
                NOTES VARCHAR(255),
                TIME_AND_DATE DATE, 
                CUSTOMER_ID VARCHAR(20) REFERENCES CUSTOMER(CUSTOMER_ID))
                """;
//
//
//        this.entityManager.execute(CREATE_TABLE_CUSTOMER);
//        this.entityManager.execute(CREATE_TABLE_CALL);

    }

    private class CustomerRowMapper implements RowMapper<Customer> {

        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString(1);
            String companyName = rs.getString(2);
            String email = rs.getString(3);
            String telephone = rs.getString(4);
            String notes = rs.getString(5);
            return new Customer(id, companyName, email, telephone, notes);
        }
    }

    private class CallRowMapper implements RowMapper<Call> {

        @Override
        public Call mapRow(ResultSet rs, int rowNum) throws SQLException {
            String notes = rs.getString(1);
            Date timeAndDate = rs.getDate(2);
            return new Call(notes, timeAndDate);
        }
    }
}
