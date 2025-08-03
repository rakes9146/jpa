package rk.jpa.jdbc;

import java.sql.*;
import java.util.Map;
import java.util.logging.Logger;

public class JpaJDBCExecutorMySQLImpl implements JpaJDBCExecutor {

    private static final Logger logger = Logger.getLogger(JpaJDBCExecutorMySQLImpl.class.getName());

    Connection jdbcConnection;
    Statement statement;

    public JpaJDBCExecutorMySQLImpl() {
        this.jdbcConnection = DatabaseManager.getDatabaseConnection();
        try {
            this.statement = this.jdbcConnection.createStatement();
            logger.info("connection initialized");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTable(String query) {
        logger.info("Table creation executing");
        this.statementExecutor(statement, query);
        logger.info("table created successfully");
    }

    @Override
    public void dropTable(String query) {
        logger.info("drop table executing");
        this.statementExecutor(statement, query);
    }

    @Override
    public int insert(String query, Map<Integer, Object> parameterMap) throws SQLException {
        logger.info("insertion statement execution");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = jdbcConnection.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.preparedStatementExcecutor(preparedStatement, parameterMap);
    }

    @Override
    public int update(String query, Map<Integer, Object> parameterMap) throws SQLException {
        logger.info("update statement executing");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = jdbcConnection.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.preparedStatementExcecutor(preparedStatement, parameterMap);
    }


    @Override
    public int delete(String query, Map<Integer, Object> parameterMap) throws SQLException {
        logger.info("delete statement executing");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = jdbcConnection.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.preparedStatementExcecutor(preparedStatement, parameterMap);
    }


    //update the query to get single result rather then modifying multiple data
    @Override
    public ResultSet selectSingleResult(String query, Map<Integer, Object> parameterMap) throws SQLException {
        logger.info("Result set executing");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = jdbcConnection.prepareStatement(query);
            ResultSet singleResultSet = this.preparedStatementQueryExcecutor(preparedStatement, parameterMap);
            return singleResultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet selectMultipleResult(String query, Map<Integer, Object> parameterMap) {
        logger.info("Multiple result response executing");
        try {
            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(query);
            ResultSet singleResultSet = this.preparedStatementQueryExcecutor(preparedStatement, parameterMap);
            return singleResultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() {
        return this.jdbcConnection;
    }
}

