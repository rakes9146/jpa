package rk.jpa.jdbc;


import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public interface JpaJDBCExecutor {

    void createTable(String query);

    void dropTable(String Query);

    int insert(String query, Map<Integer, Object> parameterMap) throws SQLException;

    int update(String query, Map<Integer, Object> parameterMap) throws SQLException;

    int delete(String query, Map<Integer, Object> parameterMap) throws SQLException;

    ResultSet selectSingleResult(String query, Map<Integer, Object> parameterMap) throws SQLException;

    ResultSet selectMultipleResult(String query, Map<Integer, Object> parameterMap);

    public Connection getConnection();

    default void statementExecutor(Statement statement, String query) {
        try {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    default int preparedStatementExcecutor(PreparedStatement preparedStatement,
                                           Map<Integer, Object> parameterMap) {
        try {
            updatePreparedStatementParameter(preparedStatement, parameterMap);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw
                    new RuntimeException(e);
        }
    }

    default ResultSet preparedStatementQueryExcecutor(PreparedStatement preparedStatement,
                                                      Map<Integer, Object> parameterMap) {
        try {
            updatePreparedStatementParameter(preparedStatement, parameterMap);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePreparedStatementParameter(PreparedStatement preparedStatement, Map<Integer, Object> parameterMap) {

        parameterMap.forEach((k, v) -> {

            try {
                switch (v) {

                    case Integer intVal -> preparedStatement.setInt(k, intVal);
                    case Long longVal -> preparedStatement.setLong(k, longVal);
                    case String strVal -> preparedStatement.setString(k, strVal);
                    case Double doubleVal -> preparedStatement.setDouble(k, doubleVal);
                    case Float floatVal -> preparedStatement.setFloat(k, floatVal);
                    case LocalDate dateVal -> preparedStatement.setDate(k, Date.valueOf(dateVal));
                    case LocalDateTime timestampVal -> preparedStatement.setTimestamp(k, Timestamp.valueOf(timestampVal));
                    case null, default -> System.out.println("");  //preparedStatement.setObject(k, v);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static <T> T convertDataType(Object obj, Field field) {
    T val;
    return switch (field.getType().getSimpleName()) {
            case "Integer" -> (T) Integer.valueOf(obj.toString());
            case "Long" -> (T) Long.valueOf(obj.toString());
            case "String" -> (T) String.valueOf(obj);
            case "Double" -> (T) Double.valueOf(obj.toString());
            case "Float" -> (T) Float.valueOf(obj.toString());
            case "LocalDate" -> (T) LocalDate.parse(obj.toString());
            case "LocalDateTime" ->(T) LocalDateTime.parse(obj.toString());
            case null, default -> throw new IllegalArgumentException("Unknown type");
        };
    }


}


