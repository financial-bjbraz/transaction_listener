package br.com.bjbraz.tx;

import br.com.bjbraz.tx.rootstock.RootstockListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class Receiver {
    private Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Autowired
    private RootstockListener listener;

    @RabbitListener(queues = "${rabbit.queue.name}")
    public void receiveMessage(String message) throws IOException {
        logger.info("Received message: " + message);
        if(validateMessage(message)){
            this.listener.listenToAddress(message);
        }

    }

    private boolean validateMessage(String message){
        return message.startsWith("0x");
    }

}