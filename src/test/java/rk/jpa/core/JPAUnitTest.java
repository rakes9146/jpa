package rk.jpa.core;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rk.jpa.entity.EntityManager;
import rk.jpa.entity.EntityManagerFactory;
import rk.jpa.exception.EntityNotFoundException;
import rk.jpa.jdbc.JpaJDBCExecutor;
import rk.jpa.jdbc.JpaJDBCExecutorMySQLImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class JPAUnitTest {


    @Test
    public void testCreateTable() throws SQLException {
        User1 user = new User1(); // Populate if needed
        AnnotationsProcessor processor = new AnnotationsProcessor(User1.class);
        JpaQueryAndEntityConvertorService jpa1 = new JpaQueryAndEntityConvertorService(User1.class);
        jpa1.createTableQuery();

        String creqtQuery = jpa1.createInsertQuery();
        System.out.println(creqtQuery);


        for (Map.Entry<String, String> mapEntry : processor.getEntityFieldAndDBColumnKeyMap().entrySet()) {
            System.out.println("Key : " + mapEntry.getKey() + " Value " + mapEntry.getValue());
        }
        JpaJDBCExecutor jpa = new JpaJDBCExecutorMySQLImpl();
        jpa.createTable(creqtQuery);
        processor.getEntityFieldAndDBColumnKeyMap();
        System.out.println("Insert Queries \n" + jpa1.convertEntityToInsertQuery());

    }

    @Test
    public void saveDataTest() throws EntityNotFoundException, SQLException {
        EntityManager entityManager = EntityManagerFactory.getEntityManager(User1.class);

        User1 user1 = new User1();
        user1.setUsername("Mohan");
        user1.setMobno(91462683L);
        user1.setEmail("kavit@gmail.com.com");
        user1.setPassword("@@@#$$23232####");
        user1.setUserWeight(110.332f);
        user1.setUserHeight(83.2d);
        user1.setCreatedDate(LocalDate.now());
        user1.setUpdatedDate(LocalDate.now());

        entityManager.save(user1);
        /**
         List<User1> userList = entityManager.findAll(User1.class);
         userList.forEach(user11-> {
         System.out.println("Id "+user11.getId());
         System.out.println("Username "+user11.getUsername());
         System.out.println("Email "+user11.getEmail());
         System.out.println("MobNo "+user11.getMobno());
         System.out.println("Password "+user11.getPassword());
         System.out.println("-----------------XXXXX------------------");
         }); **/

        // User1 user2 = entityManager.find( 5);
        // user2.setPassword("#233!@@");
        //  user2.setEmail("rakeshsl546@gmail.com@@");
        // user2.setUsername("Dinesh");
        // user2.setId(12);
        // entityManager.save(user2);
        //       entityManager.remove(5);
/**
 List<User1> userList = entityManager.findAll();
 userList.forEach(user11->{
 System.out.println("Id "+user11.getId());
 System.out.println("Username "+user11.getUsername());
 System.out.println("Email "+user11.getEmail());
 System.out.println("MobNo "+user11.getMobno());
 System.out.println("Password "+user11.getPassword());
 System.out.println("Created Date "+user11.getCreatedDate().toString());
 System.out.println("Updated Date "+user11.getUpdatedDate().toString());
 System.out.println("Weight "+user11.getUserHeight().toString());
 System.out.println("Height "+user11.getUserWeight().toString());
 });
 **/
    }

    @Test
    public void updateDataTest() throws EntityNotFoundException, SQLException {
        EntityManager entityManager = EntityManagerFactory.getEntityManager(User1.class);

        User1 user1 = entityManager.find(3);
        user1.setUsername("Dinesh");
        user1.setMobno(7878383L);
        user1.setEmail("dinesh@gmail.com");
        entityManager.save(user1);
    }



    @Test
    public void findAll() throws EntityNotFoundException, SQLException {
        EntityManager entityManager = EntityManagerFactory.getEntityManager(User1.class);

        List<User1> userList = entityManager.findAll();
        System.out.println("User Details");

        userList.forEach(user11 -> {
            System.out.println("Id " + user11.getId());
            System.out.println("Username " + user11.getUsername());
            System.out.println("Email " + user11.getEmail());
            System.out.println("MobNo " + user11.getMobno());
            System.out.println("Password " + user11.getPassword());
            ;
            System.out.println("Created Date " + user11.getCreatedDate());
            ;
            System.out.println("Updated Date  " + user11.getUpdatedDate());
            ;
            System.out.println("Height " + user11.getUserHeight());
            System.out.println("Weight " + user11.getUserWeight());

        });
    }


    @Test
    public void findOne() throws EntityNotFoundException, SQLException {
        EntityManager entityManager = EntityManagerFactory.getEntityManager(User1.class);

        User1 user11 = entityManager.find(3);
        System.out.println("User Details");

        System.out.println("Id " + user11.getId());
        System.out.println("Username " + user11.getUsername());
        System.out.println("Email " + user11.getEmail());
        System.out.println("MobNo " + user11.getMobno());
        System.out.println("Password " + user11.getPassword());
        ;
        System.out.println("Created Date " + user11.getCreatedDate());
        ;
        System.out.println("Updated Date  " + user11.getUpdatedDate());
        ;
        System.out.println("Height " + user11.getUserHeight());
        System.out.println("Weight " + user11.getUserWeight());
    }

    @Test
    public void remove() {
        EntityManager entityManager = EntityManagerFactory.getEntityManager(User1.class);
        entityManager.remove(2);
        System.out.println("Entity Removed");

    }

}