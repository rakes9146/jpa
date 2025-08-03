package rk.jpa.jdbc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

    private static Connection databaseConnectionInstance;
    private static Properties properties;
    private DatabaseManager(){

    }

    public static synchronized Connection getDatabaseConnection() {
        if(databaseConnectionInstance == null){
            try {
                properties = new Properties();
                ClassLoader classLoader = DatabaseManager.class.getClass().getClassLoader();
                if(classLoader == null){
                    classLoader = ClassLoader.getSystemClassLoader();
                }
                InputStream inputStream = classLoader.getResourceAsStream("db.properties");
                properties.load(inputStream);
                String url = properties.getProperty("db.url");
                String userName = properties.getProperty("db.user");
                String password = properties.getProperty("db.password");
                databaseConnectionInstance = DriverManager.getConnection(url, userName, password );

            } catch (SQLException e) {
                System.err.println("Database Connection Error");
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                System.err.println("File not found");
                 throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return databaseConnectionInstance;
    }

}
