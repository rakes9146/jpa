package rk.jpa.core;

import rk.jpa.annotations.*;
import rk.jpa.core.Profile;

import java.util.List;
import java.util.Set;

@Entity(name = "user_table")
public class User {
    @Id
    @GenerationType(strategy = "Identity.Auto")
    private Long id;

    @Column(name = "username")
    private String username;


    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany
    @JoinColumn(name = "role_id")
    private Set<Role> roles;

    public  User(long id, String username, Profile profile,  Department dept, Set<Role> roles) {
    this.id = id;
    this.username = username;
    this.profile = profile;
    this.department = dept;
    this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User(long id, String name, Profile profile, Set<Role> roles) {
            this.id = id;
            this.username = name;
            this.profile = profile;
            this.roles = roles;

    }

    public User() {

    }

    // Getters/setters omitted for brevity
}
