package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerNotFoundException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class CustomerDaoHibernateImpl implements CustomerDao, DaoInterface {
    private HibernateTemplate template;

    @Autowired
    public CustomerDaoHibernateImpl(HibernateTemplate hibernateTemplate) {
        this.template = hibernateTemplate;
    }

    @Override

    public void create(Customer customer) {
        String INSERT_SQL = "INSERT INTO CUSTOMER (CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES) VALUES (?,?,?,?,?)";
        template.save(customer);
    }


    @Override
    public Customer getById(String customerId) throws RecordNotFoundException, CustomerNotFoundException {
        List<Customer> results = template.execute(session -> {
            Query<Customer> q = session.createQuery("FROM Customer where CUSTOMER_ID=:id");
            q.setParameter("id", customerId);
            return q.list();
        });
        if (results.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        return results.get(0);
    }

    @Override
    public List<Customer> getByName(String customerName) {

        List<Customer> results = template.execute(session -> {
            Query<Customer> q = session.createQuery("FROM Customer WHERE COMPANY_NAME=:name");
            q.setParameter("name", customerName);
            return q.list();
        });

        return results;

    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        template.execute(session -> {
            Query query = session.createQuery("UPDATE Customer set COMPANY_NAME=:name, EMAIL=:email, TELEPHONE=:telephone, NOTES=:notes WHERE CUSTOMER_ID=:id");
            query.setParameter("name", customerToUpdate.getCompanyName());
                query.setParameter("email", customerToUpdate.getEmail());
                query.setParameter("telephone", customerToUpdate.getTelephone());
                query.setParameter("notes", customerToUpdate.getNotes());
                query.setParameter("id", customerToUpdate.getCustomerId());
                return query.list();
        });




    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        Customer customer = template.get(Customer.class, oldCustomer.getCustomerId());
        template.delete(customer);

    }

    @Override
    public List<Customer> getAllCustomers() {

        return this.template.execute(session -> session.createQuery("FROM Customer").list());

    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
//        Customer customer = this.template.queryForObject("SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = ?",
//                new CustomerRowMapper(), customerId);
//        List<Call> calls = this.template.query("SELECT * FROM CALL WHERE CUSTOMER_ID = ?",
//                new CallRowMapper(), customerId);
//        if (customer != null) {
//            customer.setCalls(calls);
//        }
//        return customer;
        return null;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws DataAccessException {
        newCall.

        String INSERT_CALL_SQL = """
                INSERT INTO TBL_CALL(NOTES, TIME_AND_DATE, CUSTOMER_ID) VALUES (?, ?, ?)
                """;
        this.template.update(INSERT_CALL_SQL, newCall.getNotes(), newCall.getTimeAndDate(), customerId);
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


        this.template.execute(CREATE_TABLE_CUSTOMER);
        this.template.execute(CREATE_TABLE_CALL);

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
