package rk.jpa.jdbc;


import org.junit.Test;

import java.sql.Connection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {


    @Test
    public void shouldCreateValidDatabaseConnection() {
        // Get the connection from your singleton class
        Connection connection = DatabaseManager.getDatabaseConnection();

        // Ensure it's not null
        assertNotNull(connection, "Database connection should not be null");

        // Check if it's open and usable
        try {
            assertFalse(connection.isClosed(), "Database connection should be open");
        } catch (Exception e) {
            fail("Error while checking connection state: " + e.getMessage());
        }
    }


    @Test
    public void shouldReturnSameConnectionInstanceOnMultipleCalls() {
        // First call to get the connection
        Connection firstInstance = DatabaseManager.getDatabaseConnection();
        assertNotNull(firstInstance, "First connection should not be null");

        // Second call to get the connection
        Connection secondInstance = DatabaseManager.getDatabaseConnection();
        assertNotNull(secondInstance, "Second connection should not be null");

        // Check if both instances are the same object (singleton)
        assertSame(firstInstance, secondInstance,
                "Multiple calls should return the same connection instance");
    }

    @Test
    public void shouldReturnSameConnectionAcrossMultipleThreads() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        ConcurrentLinkedQueue<Connection> connections = new ConcurrentLinkedQueue<>();

        IntStream.range(0, threadCount).forEach(i -> executor.submit(() -> {
            try {
                Connection conn = DatabaseManager.getDatabaseConnection();
                connections.add(conn);
            } finally {
                latch.countDown();
            }
        }));

        latch.await();
        executor.shutdown();

        // Validate all connections are the same instance
        Connection first = connections.peek();
        assertNotNull(first, "First connection should not be null");

        for (Connection conn : connections) {
            assertSame(first, conn, "All threads should receive the same connection instance");
        }
    }
}