package rk.jpa.core;

import rk.jpa.annotations.Column;
import rk.jpa.annotations.Entity;
import rk.jpa.annotations.Id;

@Entity(name = "department")
public class Department {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    public Department(long l, String engineering) {
    }

    // Getters and setters...
}
