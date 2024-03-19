package br.com.bjbraz.tx;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
        import org.springframework.beans.factory.annotation.Autowired;

@Component
public class Sender {

    @Autowired private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message, String routingKey){
        rabbitTemplate.convertAndSend(
                "tx-created-exchange", routingKey, message);
    }
}
