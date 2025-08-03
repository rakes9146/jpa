package rk.jpa.context;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PersistenceContext<E> {

    private final Map<Object, EntityWrapper<E>> context = new HashMap<>();
    private final Class<E> entityClass;
    private final String idFieldName;

    public PersistenceContext(Class<E> entityClass) {
        this.entityClass = entityClass;
        this.idFieldName = findIdFieldName(entityClass);
    }

    public void persist(E entity) {
        Object key = getPrimaryKey(entity);
        context.put(key, new EntityWrapper<>(entity, EntityState.NEW));
    }

    public void merge(E entity) {
        Object key = getPrimaryKey(entity);
        context.put(key, new EntityWrapper<>(entity, EntityState.MANAGED));
    }

    public void detach(E entity) {
        Object key = getPrimaryKey(entity);
        EntityWrapper<E> wrapper = context.get(key);
        if (wrapper != null) {
            wrapper.setState(EntityState.DETACHED);
        }
    }

    public void remove(E entity) {
        Object key = getPrimaryKey(entity);
        EntityWrapper<E> wrapper = context.get(key);
        if (wrapper != null) {
            wrapper.setState(EntityState.REMOVED);
        }
    }

    public EntityState getState(E entity) {
        Object key = getPrimaryKey(entity);
        EntityWrapper<E> wrapper = context.get(key);
        return wrapper != null ? wrapper.getState() : null;
    }

    public void clear() {
        context.clear();
    }

    public void printContext() {
        context.forEach((k, v) ->
                System.out.println("[" + k + "] " + v.getEntity() + " - " + v.getState()));
    }

    // --------- Internal helper methods ---------

    private Object getPrimaryKey(E entity) {
        try {
            Field field = entityClass.getDeclaredField(idFieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to extract primary key", e);
        }
    }

    private String findIdFieldName(Class<E> clazz) {
        // Look for field named "id"
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase("id")) {
                return field.getName();
            }
        }
        throw new RuntimeException("No 'id' field found in " + clazz.getSimpleName());
    }
}

