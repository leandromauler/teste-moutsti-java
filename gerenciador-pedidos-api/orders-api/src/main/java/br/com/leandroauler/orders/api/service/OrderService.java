package br.com.leandroauler.orders.api.service;

import br.com.leandroauler.orders.api.entity.Orders;
import br.com.leandroauler.orders.api.entity.dto.Order;
import br.com.leandroauler.orders.api.exception.OrderNotFoundException;
import br.com.leandroauler.orders.api.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    private final OrdersRepository orderRepository;

    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrdersRepository orderRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Order queueRequest(Order order) {
        logger.info("Enfileirando pedido: {}", order.toString());
        rabbitTemplate.convertAndSend(exchangeName, "", order);
        return order;
    }

    public Orders createOrder(Orders order) {
        if (existsOrder(order)) {
            throw new IllegalArgumentException("Pedido com os mesmos atributos já existe.");
        }
        logger.info("Enfileirando pedido: {}", order.toString());
        rabbitTemplate.convertAndSend(exchangeName, "", order);
        return orderRepository.save(order);
    }

    // Método para verificar se um pedido com os mesmos atributos já existe
    public boolean existsOrder(Orders order) {
        return orderRepository.findAll().stream()
                .anyMatch(existingOrder ->
                        existingOrder.getClient().equals(order.getClient()) &&
                                existingOrder.getTotalValue().equals(order.getTotalValue()) &&
                                existingOrder.getStatus().equals(order.getStatus()) &&
                                existingOrder.getCreatedAt().equals(order.getCreatedAt()) &&
                                existingOrder.getItens().equals(order.getItens()));
    }


    @Cacheable(value = "ordersCache", key = "#orderId")
    public Orders findById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Pedido com ID " + orderId + " não encontrado."));
    }

    public boolean isDuplicateOrder(Orders orders) {
        List<Orders> existingOrders = orderRepository.findAll();

        for (Orders existingOrder : existingOrders) {
            if (ordersAreEqual(existingOrder, orders)) {
                logger.info("Pedido duplicado encontrado: {}", existingOrder.getId());
                return true;
            }
        }
        return false;
    }

    private boolean ordersAreEqual(Orders order1, Orders order2) {
        return order1.getClient().equals(order2.getClient()) &&
                order1.getTotalValue().equals(order2.getTotalValue()) &&
                order1.getStatus().equals(order2.getStatus()) &&
                order1.getCreatedAt().equals(order2.getCreatedAt()) &&
                order1.getItens().equals(order2.getItens()) &&
                order1.getEmailNotification().equals(order2.getEmailNotification());
    }

}

