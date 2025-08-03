package rk.jpa.entity;

import java.util.logging.Logger;

public abstract class EntityManagerFactory {

    static Logger logger = Logger.getLogger(EntityManagerFactory.class.getName());

    public static EntityManager getEntityManager(Class clazz) {
        EntityManager entityManager = null;
        try {
            entityManager = new EntityManagerImpl(clazz);
            return entityManager;
        } catch (Exception e) {
            logger.severe("Error occure while generating factory");
            e.printStackTrace();
        }
        return entityManager;
    }

}
