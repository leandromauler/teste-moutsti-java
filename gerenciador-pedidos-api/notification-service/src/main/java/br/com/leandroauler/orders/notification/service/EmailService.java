package br.com.leandroauler.orders.notification.service;

import br.com.leandroauler.orders.notification.entity.Order;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMailNotification(Order order) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("order-apo@moutsti.com.br");
        simpleMailMessage.setTo(order.getEmailNotification());
        simpleMailMessage.setSubject("Pedido de Compra: " + order.getId() + " - Notificação");
        simpleMailMessage.setText("Prezado(a),\n\nO pedido de compra " + order.getId() + " foi gerado com sucesso.\n\nAtenciosamente,\nEquipe de Pedidos");
        mailSender.send(simpleMailMessage);
    }

    private String generateMessage(Order order) {
        return "Prezado(a),\n\nO pedido de compra " + order.getId() + " foi gerado com sucesso.\n\nAtenciosamente,\nEquipe de Pedidos";
    }


}
