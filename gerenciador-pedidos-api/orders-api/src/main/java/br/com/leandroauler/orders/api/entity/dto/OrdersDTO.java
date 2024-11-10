package br.com.leandroauler.orders.api.entity.dto;

import br.com.leandroauler.orders.api.entity.ItemsOrders;
import br.com.leandroauler.orders.api.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrdersDTO implements Serializable {

    private static final long serialVersionUID = 1L;

//    private UUID id;
//    private String client;
//    private List<ItemsOrders> itens = new ArrayList<>();
//    private Double totalValue;
//    private String emailNotification;
//    private Status status;
//    private LocalDateTime createdAt;

    private UUID id;
    private String client;
    private Double totalValue;
    private String emailNotification;
    private Status status;
    private List<ItemOrderDTO> itens; // Ou List<ItemsOrders> se for direto
    private LocalDateTime createdAt;

    // Construtor que aceita todos os parâmetros
    public OrdersDTO(UUID id, String client, Double totalValue, String emailNotification, Status status, List<ItemOrderDTO> itens, LocalDateTime createdAt) {
        this.id = id;
        this.client = client;
        this.totalValue = totalValue;
        this.emailNotification = emailNotification;
        this.status = status;
        this.itens = itens;
        this.createdAt = createdAt;
    }

}
