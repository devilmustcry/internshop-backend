package com.sandstorm.internshop.entity;

import lombok.Data;
import org.hibernate.annotations.LazyToOne;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "order_products")
@Data
public class OrderProduct extends BaseEntity<String> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    @NotNull
    private Integer amount;

    @Column
    @NotNull
    private Double netPrice;

}
