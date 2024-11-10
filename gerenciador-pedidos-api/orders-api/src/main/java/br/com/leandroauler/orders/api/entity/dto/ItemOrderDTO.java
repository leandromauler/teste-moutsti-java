package br.com.leandroauler.orders.api.entity.dto;

import br.com.leandroauler.orders.api.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrderDTO {

    private UUID id;

    private Product product;

    private Integer quantity;

    private Orders orders;
}
