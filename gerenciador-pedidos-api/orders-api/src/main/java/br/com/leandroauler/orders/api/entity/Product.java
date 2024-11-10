package br.com.leandroauler.orders.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {


    private UUID productId = UUID.randomUUID();

    private String name;

    private String description;

    private Double price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Product(String name, String description, Double price, Integer quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
