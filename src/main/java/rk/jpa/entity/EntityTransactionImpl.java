package rk.jpa.entity;

import rk.jpa.jdbc.JpaJDBCExecutor;
import rk.jpa.jdbc.JpaJDBCExecutorMySQLImpl;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.logging.Logger;

public class EntityTransactionImpl implements EntityTransaction {


    private static final Logger logger = Logger.getLogger(EntityTransactionImpl.class.getName());
    Savepoint savepoint;
    JpaJDBCExecutor jpaJDBCExecutor;


    public EntityTransactionImpl(JpaJDBCExecutor jpaJDBCExecutor) {
        this.jpaJDBCExecutor = jpaJDBCExecutor;
    }

    @Override
    public void begin() {
        try {
            jpaJDBCExecutor.getConnection().setAutoCommit(false);
            savepoint = jpaJDBCExecutor.getConnection().setSavepoint();
        } catch (SQLException e) {
            logger.severe("Error occured, while updating auto commit false");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        try {
            jpaJDBCExecutor.getConnection().commit();
        } catch (SQLException e) {
            logger.severe("Error occured, while commiting transaction");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        try {
            jpaJDBCExecutor.getConnection().rollback(savepoint);
        } catch (SQLException e) {
            logger.severe("Error occured, while rollback transaction");
            throw new RuntimeException(e);
        }
        ;
    }
}
