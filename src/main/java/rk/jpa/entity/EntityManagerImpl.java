package rk.jpa.entity;

import rk.jpa.context.EntityState;
import rk.jpa.context.PersistenceContext;
import rk.jpa.core.JpaQueryAndEntityConvertorService;
import rk.jpa.exception.EntityNotFoundException;
import rk.jpa.jdbc.JpaJDBCExecutor;
import rk.jpa.jdbc.JpaJDBCExecutorMySQLImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class EntityManagerImpl implements EntityManager {

    //NoteMove to interface later
    PersistenceContext persistenceContext;

    Class entityClass;

    //Note move to interface
    JpaQueryAndEntityConvertorService jpaQueryService;

    JpaJDBCExecutor jdbcExecutor;

    //Note move to interface
    EntityTransaction entityTransaction;


    public EntityManagerImpl(Class clazz) throws SQLException {
        this.entityClass = clazz;
        this.persistenceContext = new PersistenceContext(clazz);
        this.jpaQueryService = new JpaQueryAndEntityConvertorService(clazz);
        this.jdbcExecutor = new JpaJDBCExecutorMySQLImpl();
        this.entityTransaction = new EntityTransactionImpl(jdbcExecutor);
        jdbcExecutor.createTable(jpaQueryService.createTableQuery());
    }

    @Override
    public void save(Object object) {

        try {
            entityTransaction.begin();
            if (persistenceContext.getState(object) == null) {
                String insertQuery = jpaQueryService.createInsertQuery();
                jdbcExecutor.insert(insertQuery, jpaQueryService.entityFieldValueParaMapForInsert(object));
                persistenceContext.persist(object);
            } else {
                String updateQuery = jpaQueryService.createUpdateQuery();
                jdbcExecutor.update(updateQuery, jpaQueryService.entityFieldValueParaMapForUpdate(object));
                persistenceContext.merge(object);

            }

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public <T> T mergedEntity(T object) {
        return null;
    }

    @Override
    public <T> T find(Object primaryKey) throws EntityNotFoundException, SQLException {
        String selectQuery = jpaQueryService.getSelectQuery(primaryKey);
        ResultSet rs = jdbcExecutor.selectSingleResult(selectQuery, jpaQueryService.entityFieldValueParaMapForSelect(entityClass, primaryKey));
        T entity = jpaQueryService.convertEntityToSingleResult(entityClass, rs);
        if (!Objects.isNull(entity)) {
            persistenceContext.persist(entity);
        } else {
            throw new EntityNotFoundException("Entity not found for %s with primary key %s".formatted(entityClass, primaryKey));
        }

        return entity;
    }

    @Override
    public <T> List<T> findAll() throws SQLException {
        String selectQuery = jpaQueryService.getSelectQuery(null);
        ResultSet rs = jdbcExecutor.selectSingleResult(selectQuery, jpaQueryService.entityFieldValueParaMapForSelect(entityClass, null));
        return jpaQueryService.convertResultSetToEntityList(entityClass, rs);
    }

    @Override
    public void remove(Object entity) {


        try {
            entityTransaction.begin();
            if (entity instanceof Integer primaryKey) {
                   entity = find(primaryKey);
            }
            String deleteQuery = jpaQueryService.createDeleteQuery();
            jdbcExecutor.delete(deleteQuery, jpaQueryService.entityFieldValueParaMapForDelete(entity));
            entityTransaction.commit();
            persistenceContext.remove(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Clear the entity manager from the persistence context
     **/
    @Override
    public void clear() {
        persistenceContext.clear();
    }

    @Override
    public void flush() {

    }

    @Override
    public <T> List<T> createQuery(String jpql, Class<T> resultClass) {
        return List.of();
    }

    @Override
    public <T> T createNativeQuery(String jpql, Class<T> resultClass) {
        return null;
    }
}
