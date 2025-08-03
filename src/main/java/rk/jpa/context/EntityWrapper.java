package rk.jpa.context;

public class EntityWrapper<E> {
    private E entity;
    private EntityState state;

    public EntityWrapper(E entity, EntityState state) {
        this.entity = entity;
        this.state = state;
    }

    public E getEntity() {
        return entity;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }
}

