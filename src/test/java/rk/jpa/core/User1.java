package rk.jpa.core;

import rk.jpa.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity(name = "user_table")
public class User1 {
    @Id
    @GenerationType(strategy = "Identity.Auto")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name ="password")
    private String password;

    @Column(name ="email")
    private String email;

    @Column(name ="mobno")
    private Long mobno;

    @Column(name = "user_height")
    private Double userHeight;

    @Column(name = "user_weight")
    private Float userWeight;


    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    public User1() {
    }

    public User1(Integer id, String username, String password, String email, Long mobno) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobno = mobno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public Double getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(Double userHeight) {
        this.userHeight = userHeight;
    }

    public Float getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(Float userWeight) {
        this.userWeight = userWeight;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getMobno() {
        return mobno;
    }

    public void setMobno(Long mobno) {
        this.mobno = mobno;
    }
}
