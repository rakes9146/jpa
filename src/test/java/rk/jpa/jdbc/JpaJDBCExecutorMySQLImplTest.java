package rk.jpa.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JpaJDBCExecutorMySQLImplTest {


    private static JpaJDBCExecutorMySQLImpl executor;

    @BeforeAll
    static void setup() throws SQLException {
        executor = new JpaJDBCExecutorMySQLImpl();
        executor.createTable("""
            CREATE TABLE IF NOT EXISTS data_types_test (
                id INT PRIMARY KEY,
                name VARCHAR(100),
                score DOUBLE,
                rating FLOAT,
                birth_date DATE,
                created_at TIMESTAMP
            )
        """);
    }

    @Test
    @Order(1)
    void shouldInsertAllDataTypes() throws SQLException {
        String query = "INSERT INTO data_types_test (id, name, score, rating, birth_date, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 101);
        params.put(2, "Alice");
        params.put(3, 95.75); // double
        params.put(4, 4.5f);  // float
        params.put(5, Date.valueOf("1990-01-01"));
        params.put(6, Timestamp.valueOf("2025-07-15 10:30:00"));

        int rows = executor.insert(query, params);
        assertEquals(1, rows, "Insert should affect 1 row");
    }

    @Test
    @Order(2)
    void shouldUpdateFloatAndDouble() throws SQLException {
        String query = "UPDATE data_types_test SET score = ?, rating = ? WHERE id = ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 88.88); // new double
        params.put(2, 3.3f);  // new float
        params.put(3, 101);

        int rows = executor.update(query, params);
        assertEquals(1, rows, "Update should affect 1 row");
    }

    @Test
    @Order(3)
    void shouldSelectAndVerifyUpdatedValues() throws SQLException {
        String query = "SELECT * FROM data_types_test WHERE id = ?";
        Map<Integer, Object> params = Map.of(1, 101);

        ResultSet rs = executor.selectSingleResult(query, params);
        try {
            assertTrue(rs.next(), "Record should exist");
            assertEquals("Alice", rs.getString("name"));
            assertEquals(88.88, rs.getDouble("score"));
            assertEquals(3.3f, rs.getFloat("rating"));
            assertEquals(Date.valueOf("1990-01-01"), rs.getDate("birth_date"));
            assertEquals(Timestamp.valueOf("2025-07-15 10:30:00"), rs.getTimestamp("created_at"));
        } catch (SQLException e) {
            fail("Error reading result set: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void shouldDeleteRecordSuccessfully() throws SQLException {
        String query = "DELETE FROM data_types_test WHERE id = ?";
        Map<Integer, Object> params = Map.of(1, 101);

        int rows = executor.delete(query, params);
        assertEquals(1, rows, "Delete should affect 1 row");
    }

    @Test
    @Order(5)
    void shouldDropTestTable() throws SQLException {
        executor.dropTable("DROP TABLE IF EXISTS data_types_test");
    }

}
