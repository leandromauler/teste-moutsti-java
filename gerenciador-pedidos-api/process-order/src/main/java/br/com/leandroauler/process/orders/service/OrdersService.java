package br.com.leandroauler.process.orders.service;

import br.com.leandroauler.process.orders.entity.ItemOrder;
import br.com.leandroauler.process.orders.entity.Orders;
import br.com.leandroauler.process.orders.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    private final Logger logger = LoggerFactory.getLogger(OrdersService.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    private final ProductService productService;
    private final ItemOrderService itemOrderService;
    private final OrdersRepository ordersRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrdersService(ProductService productService, ItemOrderService itemOrderService, OrdersRepository ordersRepository, RabbitTemplate rabbitTemplate) {
        this.productService = productService;
        this.itemOrderService = itemOrderService;
        this.ordersRepository = ordersRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Orders queueRequest(Orders order) {
        logger.info("Enfileirando pedido: {}", order.toString());
        rabbitTemplate.convertAndSend(exchangeName, "", order);
        return order;
    }

    public void save(Orders order) {
        logger.info("Processando pedido...");
        productService.save(order.getItens());
        List<ItemOrder> itemPedidos = itemOrderService.save(order.getItens());
        ordersRepository.save(order);
        itemOrderService.updatedItemPedido(itemPedidos, order);
        logger.info("Pedido salvo: {}" + order.toString());
    }
}
