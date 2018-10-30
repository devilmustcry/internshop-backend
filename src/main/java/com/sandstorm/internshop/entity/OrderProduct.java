package com.sandstorm.internshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "order_products")
@Data
@Accessors(chain = true)
public class OrderProduct extends BaseEntity<String> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    @NotNull
    private Integer amount;

    @Column
    @NotNull
    private Double netPrice;


}
