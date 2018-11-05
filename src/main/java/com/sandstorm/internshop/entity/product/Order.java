package com.sandstorm.internshop.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sandstorm.internshop.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity(name = "orders")
@Data
@Accessors(chain = true)
public class Order extends BaseEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private Double price;

    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private Double discount;

    @Column(columnDefinition="Decimal(10,2) default '0.00'", name = "net_price")
    private Double netPrice;

    @Column(columnDefinition = "Boolean default 0", name = "is_used_coupon")
    private Boolean isUsedCoupon;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProductList;

}
