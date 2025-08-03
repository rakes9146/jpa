package rk.jpa.context;

public interface EntityHelper<K, E> {
    K getPrimaryKey(E entity);
}