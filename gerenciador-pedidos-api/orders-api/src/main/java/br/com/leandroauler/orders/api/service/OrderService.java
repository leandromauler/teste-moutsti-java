package br.com.leandroauler.orders.api.service;

import br.com.leandroauler.orders.api.entity.ItemsOrders;
import br.com.leandroauler.orders.api.entity.dto.ItemOrderDTO;
import br.com.leandroauler.orders.api.entity.dto.Order;
import br.com.leandroauler.orders.api.entity.Orders;
import br.com.leandroauler.orders.api.entity.dto.OrdersDTO;
import br.com.leandroauler.orders.api.exception.OrderNotFoundException;
import br.com.leandroauler.orders.api.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Cacheable(value = "ordersCache", key = "#orderId")
    public Orders findById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Pedido com ID " + orderId + " não encontrado."));
    }

}

