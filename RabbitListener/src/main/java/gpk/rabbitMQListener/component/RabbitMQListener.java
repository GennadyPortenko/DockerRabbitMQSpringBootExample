package gpk.rabbitMQListener.component;

import com.rabbitmq.client.Channel;
import gpk.rabbitMQListener.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class RabbitMQListener {

    private final SimpMessagingTemplate simpMessagingTemplate;
    @Setter
    private long sleepDelayMillis = 500;

    @Override
    public String toString() {
        return "sleepDleayMillis : " + sleepDelayMillis;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void receive(final String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException, InterruptedException {
        Thread.sleep(sleepDelayMillis);
        simpMessagingTemplate.convertAndSend("/client/monitor", new MessageDto(message));
        channel.basicAck(tag, false);
    }
}
