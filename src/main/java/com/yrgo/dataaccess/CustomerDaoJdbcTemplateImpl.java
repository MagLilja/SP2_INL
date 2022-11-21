package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class CustomerDaoJdbcTemplateImpl implements CustomerDao, DaoInterface {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Customer customer) {
        String INSERT_SQL = "INSERT INTO CUSTOMER (CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(INSERT_SQL,
                customer.getCustomerId(),
                customer.getCompanyName(),
                customer.getEmail(),
                customer.getTelephone(),
                customer.getNotes());
    }


    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        String SQL = "SELECT * FROM customer WHERE CUSTOMER_ID = ?";
        return this.jdbcTemplate.queryForObject(SQL, Customer.class, customerId);
    }

    @Override
    public List<Customer> getByName(String customerName) {
        String SQL = "SELECT * FROM customer WHERE COMPANY_NAME = ?";
        return this.jdbcTemplate.query(SQL, new CustomerRowMapper(), customerName);
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        String SQL_UPDATE = """
                UPDATE CUSTOMER 
                SET COMPANY_NAME=?, EMAIL=?, TELEPHONE=?, NOTES=? 
                WHERE CUSTOMER_ID = ?
                """;
        this.jdbcTemplate.update(SQL_UPDATE,
                customerToUpdate.getCompanyName(),
                customerToUpdate.getEmail(),
                customerToUpdate.getTelephone(),
                customerToUpdate.getNotes(),
                customerToUpdate.getCustomerId());

    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        this.jdbcTemplate.update("DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?",
                oldCustomer.getCustomerId());

    }

    @Override
    public List<Customer> getAllCustomers() {
        return this.jdbcTemplate.query("SELECT * FROM CUSTOMER", new CustomerRowMapper());
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        Customer customer = this.jdbcTemplate.queryForObject("SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = ?",
                new CustomerRowMapper(), customerId);
        List<Call> calls = this.jdbcTemplate.query("SELECT * FROM CALL WHERE CUSTOMER_ID = ?",
                new CallRowMapper(), customerId);
        if (customer != null) {
            customer.setCalls(calls);
        }
        return customer;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws DataAccessException {
        String INSERT_CALL_SQL = """
                INSERT INTO TBL_CALL(NOTES, TIME_AND_DATE, CUSTOMER_ID) VALUES (?, ?, ?)
                """;
        this.jdbcTemplate.update(INSERT_CALL_SQL, newCall.getNotes(), newCall.getTimeAndDate(), customerId);
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


        this.jdbcTemplate.execute(CREATE_TABLE_CUSTOMER);
        this.jdbcTemplate.execute(CREATE_TABLE_CALL);

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
