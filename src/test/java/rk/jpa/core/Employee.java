package rk.jpa.core;


import rk.jpa.annotations.*;

import javax.annotation.processing.Generated;
import java.util.List;

@Entity
public class Employee {

    @Id
    @GenerationType(strategy = "Identity")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id") // Foreign key in employee table
    private Department department;

    @Column(name = "emp_name")
    private String name;

    @OneToOne
    @JoinColumn(name = "address_id") // Foreign key in employee table
    private Address address;


    @ManyToMany
    @JoinColumn(
            name = "employee_project",
            joinColumnName =  "employee_id",
            inverseJoinColumns = "project_id"
    )
    private List<Project> projects;
}
