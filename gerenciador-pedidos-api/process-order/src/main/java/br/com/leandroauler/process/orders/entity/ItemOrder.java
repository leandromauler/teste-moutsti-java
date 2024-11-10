package br.com.leandroauler.process.orders.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="item_orders")
public class ItemOrder {

    @Id
    private UUID id = UUID.randomUUID();
    @ManyToOne
    private Product product;
    private Integer quantity;
    @ManyToOne
    private Orders orders;
}
