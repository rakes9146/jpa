package rk.jpa.core;

import rk.jpa.annotations.Column;
import rk.jpa.annotations.Entity;
import rk.jpa.annotations.Id;

@Entity(name = "order")
public class Order {
    @Id
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "total_amount")
    private Double totalAmount;

    // Getters and setters...
}
