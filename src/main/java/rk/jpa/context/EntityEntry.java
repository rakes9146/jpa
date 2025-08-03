package rk.jpa.context;

public class EntityEntry<E> {
    E entity;
    EntityState entityState;
    E originalSnapshot;

    public EntityEntry(E entity, EntityState entityState, E originalSnapshot) {
        this.entity = entity;
        this.entityState = entityState;
        this.originalSnapshot = originalSnapshot;
    }
}
