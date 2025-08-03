package rk.jpa.core;


import rk.jpa.annotations.Column;
import rk.jpa.annotations.Entity;
import rk.jpa.annotations.GenerationType;
import rk.jpa.annotations.Id;

@Entity
public class Address {

    @Id
    @GenerationType(strategy = "GenerationType.identy")
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    // Getters and setters
}
