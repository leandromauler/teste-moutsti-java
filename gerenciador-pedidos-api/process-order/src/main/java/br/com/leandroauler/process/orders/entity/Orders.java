package br.com.leandroauler.process.orders.entity;



import br.com.leandroauler.process.orders.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "orders")
public class Orders {

    @Id
    private UUID id = UUID.randomUUID();
    private String client;
    @OneToMany(mappedBy = "orders")
    private List<ItemOrder> itens = new ArrayList<>();
    private Double totalValue;
    private String emailNotification;
    @Enumerated(EnumType.STRING)
    private Status status = Status.EM_PROCESSAMENTO;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

}
