package br.com.fiap.postech.service_notificacoes.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.HashMap;

@Configuration
public class RabbitMQFanoutConfig {

	public static final String EXCHANGE_NAME = "notificationExchange";
	public static final String ROUTING_KEY = "notificationKey";
    public static final String EMAIL_QUEUE = "email_queue";
    public static final String HISTORICO_QUEUE = "historico_queue";
    public static final String FINAL_SEND_QUEUE = "final_send_queue";
    public static final String DELAY_EXCHANGE = "delay.exchange";
    public static final String DELAY_QUEUE = "delay.queue";
    public static final String INITIAL_RECEIVE_QUEUE = "initial_receive_queue";


    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue finalSendQueue() {
        return new Queue(FINAL_SEND_QUEUE, true);
    }

    @Bean
    public Binding finalSendBinding(@Qualifier("finalSendQueue") Queue finalSendQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(finalSendQueue).to(fanoutExchange);
    }

    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }

    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", EXCHANGE_NAME);
        args.put("x-dead-letter-routing-key", "");

        return new Queue(DELAY_QUEUE, true, false, false, args);
    }

    @Bean
    public Binding delayBinding(@Qualifier("delayQueue") Queue delayQueue, DirectExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_QUEUE);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }
    
    @Bean
	public Queue historicoQueue() {
		return new Queue(HISTORICO_QUEUE, true);
	}

    @Bean
    public Binding emailBinding(@Qualifier("emailQueue") Queue emailQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(emailQueue).to(fanoutExchange);
    }

    @Bean
    public Binding historicoBinding(@Qualifier("historicoQueue") Queue historicoQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(historicoQueue).to(fanoutExchange);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue initialReceiveQueue() {
        return new Queue(INITIAL_RECEIVE_QUEUE, true);
    }

    @Bean
    public Binding initialReceiveBinding(@Qualifier("initialReceiveQueue") Queue initialReceiveQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(initialReceiveQueue).to(fanoutExchange);
    }
}
