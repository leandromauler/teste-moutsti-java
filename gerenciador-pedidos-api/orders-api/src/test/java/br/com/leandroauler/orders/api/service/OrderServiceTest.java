package br.com.leandroauler.orders.api.service;

import br.com.leandroauler.orders.api.entity.Orders;
import br.com.leandroauler.orders.api.entity.dto.Order;
import br.com.leandroauler.orders.api.entity.enums.Status;
import br.com.leandroauler.orders.api.exception.OrderNotFoundException;
import br.com.leandroauler.orders.api.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrdersRepository orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderService orderService;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    private Orders orders;
    private Order order;
    private UUID orderId;
    private UUID ordersId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ordersId = UUID.randomUUID();
        orders = new Orders();
        orders.setId(ordersId);
        orders.setClient("Test Client");
        orders.setTotalValue(100.0);
        orders.setStatus(Status.EM_PROCESSAMENTO);
        orders.setCreatedAt(LocalDateTime.now());

        orderId = UUID.randomUUID();
        order = new Order();
        orders.setClient("Test Client");
        orders.setTotalValue(100.0);

    }

    @Test
    void testQueueRequest() {
        // Act
        Order result = orderService.queueRequest(order);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);

        verify(rabbitTemplate, times(1)).convertAndSend(exchangeCaptor.capture(), routingKeyCaptor.capture(), orderCaptor.capture());

        // Assert
        assertEquals(exchangeName, exchangeCaptor.getValue());
        assertEquals("", routingKeyCaptor.getValue());
        assertEquals(order, orderCaptor.getValue());
        assertEquals(order, result);
    }

    @Test
    void testFindById_OrderExists() {
        // Arrange
        when(orderRepository.findById(ordersId)).thenReturn(Optional.of(orders));

        // Act
        Orders result = orderService.findById(ordersId);

        // Assert
        assertEquals(orders, result);
        verify(orderRepository, times(1)).findById(ordersId);
    }

    @Test
    void testFindById_OrderNotFound() {
        // Arrange
        when(orderRepository.findById(ordersId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> orderService.findById(ordersId));
        verify(orderRepository, times(1)).findById(ordersId);
    }

    @Test
    void testCreateOrder_OrderAlreadyExists() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(orders));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(orders);
        });

        assertEquals("Pedido com os mesmos atributos já existe.", exception.getMessage());
        verify(orderRepository, never()).save(any(Orders.class)); // Não deve salvar o pedido
    }

}

