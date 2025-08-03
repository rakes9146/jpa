package rk.jpa.core;


import rk.jpa.annotations.*;

import java.util.List;

@Entity
public class Department1 {

    @Id
    @GenerationType(strategy = "dsf")
    private Long id;

    @OneToMany(mappedBy = "department") // "department" is the field in Employee class
    private List<Employee> employees;

    @Column(name = "dept_name")
    private String name;

    // Getters and setters
}
