package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ActionDaoJpaImpl implements ActionDao {
    private static final String DELETE_SQL = "DELETE FROM ACTION WHERE ACTION_ID=?";
    private static final String UPDATE_SQL = "UPDATE ACTION SET DETAILS=?, COMPLETE=?, OWNING_USER=?, REQUIRED_BY=? WHERE ACTION_ID=?";
    private static final String INSERT_SQL = "INSERT INTO ACTION (DETAILS, COMPLETE, OWNING_USER, REQUIRED_BY) VALUES (?,?,?,?)";
    private static final String GET_INCOMPLETE_SQL = "SELECT ACTION_ID, DETAILS, COMPLETE, OWNING_USER, REQUIRED_BY FROM ACTION WHERE OWNING_USER=? AND COMPLETE=?";

    @PersistenceContext
    private EntityManager entityManager;


    public void create(Action newAction) {
        entityManager.persist(newAction);
    }

    public List<Action> getIncompleteActions(String userId) {
        entityManager.createQuery("SELECT action FROM Action AS action WHERE action.owningUser = :owningUser AND action.complete = FALSE ");
        return null;
    }

    public void update(Action actionToUpdate) throws RecordNotFoundException {
        entityManager.merge(actionToUpdate);
    }

    public void delete(Action oldAction) throws RecordNotFoundException {
        entityManager.remove(oldAction);
    }
}




