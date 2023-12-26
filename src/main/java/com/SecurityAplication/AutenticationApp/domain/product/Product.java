package com.SecurityAplication.AutenticationApp.domain.product;

import com.SecurityAplication.AutenticationApp.DTO.ProductDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Long cod;
    private String productName;
    private BigDecimal productValue;
    private Long quantity;

    public Product(ProductDTO product) {
        this.id = product.id();
        this.cod = product.cod();
        this.productName = product.productName();
        this.quantity = product.quantity();
        this.productValue = product.productValue();
    }
}
