package gpk.rabbitMQListener.configuration;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfiguration {

    final String QUEUE_NAME;

    public RabbitConfiguration(@Value("${rabbitmq.queue.name}") String QUEUE_NAME) {
        this.QUEUE_NAME = QUEUE_NAME;
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }


    @Bean
    public ConnectionFactory connectionFactory(@Value("${rabbitmq.host}") final String RABBIT_HOST,
                                               @Value("${rabbitmq.port}") final String RABBIT_PORT,
                                               @Value("${rabbitmq.username}") final String RABBIT_USERNAME,
                                               @Value("${rabbitmq.password}") final String RABBIT_PASSWORD) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(RABBIT_HOST + ":" + RABBIT_PORT);
        connectionFactory.setUsername(RABBIT_USERNAME);
        connectionFactory.setPassword(RABBIT_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory)
    {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

}
