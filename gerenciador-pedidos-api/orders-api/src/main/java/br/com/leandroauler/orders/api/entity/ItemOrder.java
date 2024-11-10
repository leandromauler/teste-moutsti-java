package br.com.leandroauler.orders.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrder {

    private UUID id = UUID.randomUUID();

    private Product product;

    private Integer quantity;
}
