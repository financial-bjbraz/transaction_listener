package br.com.bjbraz.tx;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    static final String topicExchangeName = "tx-creation-exchange";

    @Bean public Queue queue(){
        return new Queue("tx-creation", false);
    }

    @Bean public Exchange exchange(){
        return new DirectExchange(topicExchangeName);
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange){
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("rootstock")
                .noargs();
    }
}

