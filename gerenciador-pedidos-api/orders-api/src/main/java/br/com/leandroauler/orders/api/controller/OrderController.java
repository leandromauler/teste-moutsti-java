package br.com.leandroauler.orders.api.controller;

import br.com.leandroauler.orders.api.entity.Order;
import br.com.leandroauler.orders.api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pedidos", description = "Recurso para manipulação de pedidos")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Operation(summary = "Criar um pedido",responses = @ApiResponse(responseCode = "201", description = "Pedido criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))))
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        logger.info("Criando pedido: {}", order.toString());
        order = orderService.queueRequest(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
