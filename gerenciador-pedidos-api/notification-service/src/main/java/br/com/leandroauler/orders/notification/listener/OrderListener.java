package br.com.leandroauler.orders.notification.listener;

import br.com.leandroauler.orders.notification.entity.Order;
import br.com.leandroauler.orders.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    private final Logger logger = LoggerFactory.getLogger(OrderListener.class);

    private final EmailService emailService;

    public OrderListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "orders.v1.order-created.generate-notification")
    public void sendNotification(Order order) {
        emailService.sendMailNotification(order);
        logger.info("Notificação Gerada: {}", order);
    }
}
