package br.com.leandroauler.process.orders.listener;

import br.com.leandroauler.process.orders.entity.Orders;
import br.com.leandroauler.process.orders.entity.enums.Status;
import br.com.leandroauler.process.orders.service.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    private final Logger logger = LoggerFactory.getLogger(OrderListener.class);

    private final OrdersService ordersService;

    public OrderListener(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @RabbitListener(queues = "orders.v1.order-created.generate-processing")
    public void createOrder(Orders order) {
        order.setStatus(Status.PROCESSADO);

        ordersService.save(order);

        logger.info("Pedido processado: {}", order.toString());
    }
}
