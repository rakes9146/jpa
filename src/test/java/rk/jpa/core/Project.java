package rk.jpa.core;


import rk.jpa.annotations.*;

import java.util.List;

@Entity
public class Project {

    @Id
    @GenerationType(strategy = "Genear")
    private Long id;

    @ManyToMany(mappedBy = "projects")
    private List<Employee> employees;

    @Column(name = "proj_name")
    private String projectName;

    // Getters and setters
}