package br.com.leandroauler.orders.api.entity;

import br.com.leandroauler.orders.api.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private UUID id = UUID.randomUUID();
    private String client;
    private List<ItemOrder> itens = new ArrayList<>();
    private Double totalValue;
    private String emailNotification;
    private Status status = Status.EM_PROCESSAMENTO;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

}
