package rk.jpa.entity;

import rk.jpa.exception.EntityNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface EntityManager {

    //Saves a new (transient) entity. No return.
    public void save(Object object);

    //Returns a new managed copy of the given detached entity.
    public <T> T mergedEntity(T object);

    //Finds an entity by primary key, returns managed instance or null.
    public <T> T find(Object primaryKey) throws EntityNotFoundException, SQLException;


    <T> List<T> findAll() throws SQLException;

    //Deletes the entity from the database. Entity must be managed.
    public void remove(Object entity);

    //Clears the entire persistence context (detaches all managed entities).
    public void clear();

    //	Forces immediate synchronization with the database.
    public void flush();

    //Typed JPQL query.
    public <T> List<T> createQuery(String jpql, Class<T> resultClass);


    //Typed JPQL query with single result.
    public <T> T createNativeQuery(String jpql, Class<T> resultClass);


}
