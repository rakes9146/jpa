package rk.jpa.core;

import rk.jpa.annotations.Column;
import rk.jpa.annotations.Entity;
import rk.jpa.annotations.Id;

import java.util.List;

@Entity(name = "role")
public class Role {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "permissions")
    private String permissions; // Could be JSON or CSV format

    public <E> Role(long l, String admin, List<E> of) {
    }

    // Getters and setters...
}
