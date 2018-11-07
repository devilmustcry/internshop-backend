package com.sandstorm.internshop.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sandstorm.internshop.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "products")
public class Product extends BaseEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonProperty("product_id")
    private Long id;

    @NotNull
    @NotBlank
    @Column
    private String name;

    @NotNull
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private Double price;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderProduct> orderProductList;

}
