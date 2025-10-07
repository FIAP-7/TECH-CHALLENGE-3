package br.com.fiap.postech.service_notificacoes.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQFanoutConfig {

	public static final String EXCHANGE_NAME = "notificationExchange";
	public static final String ROUTING_KEY = "notificationKey";
    public static final String EMAIL_QUEUE = "email_queue";
    public static final String HISTORICO_QUEUE = "historico_queue";

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
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
}
