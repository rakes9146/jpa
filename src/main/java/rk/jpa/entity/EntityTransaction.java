package rk.jpa.entity;

public interface EntityTransaction {

    public void begin();

    public void commit();

    public void rollback();
}
