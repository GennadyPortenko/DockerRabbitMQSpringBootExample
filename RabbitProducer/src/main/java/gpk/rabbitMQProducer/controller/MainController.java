package gpk.rabbitMQProducer.controller;

import gpk.rabbitMQProducer.scheduling.IDynamicScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAsync
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MainController {

    private final AmqpTemplate template;
    private @Value("${rabbitmq.fanout.exchange.name}") String FANOUT_EXCHANGE_NAME;
    private @Value("${rabbitmq.topic.exchange.name}") String TOPIC_EXCHANGE_NAME;
    private @Value("${rabbitmq.queue.name}") String routingKey;
    private String exchange = "";

    private final IDynamicScheduler dynamicScheduler;
    private int messageNum;

    @RequestMapping("/start/direct/{routingKey}/{interval}")
    String startDirect(@PathVariable String routingKey, @PathVariable long interval) {
        this.routingKey = routingKey;
        this.exchange = "";
        return start(interval);
    }

    @RequestMapping("/start/fanout/{interval}")
    String startFanout(@PathVariable long interval) {
        this.routingKey = "";
        this.exchange = FANOUT_EXCHANGE_NAME;
        return start(interval);
    }

    @RequestMapping("/start/topic/{routingKey}/{interval}")
    String startTopic(@PathVariable String routingKey, @PathVariable long interval) {
        this.routingKey = routingKey;
        this.exchange = TOPIC_EXCHANGE_NAME;
        return start(interval);
    }

    private String start(long interval) {
        messageNum = 1;
        dynamicScheduler.setInterval(interval);
        dynamicScheduler.start(this::sendMessage);
        String status = "Started with an interval of " + interval + " ms. Exchange : \'" + exchange + "\', routing key : \'" + routingKey + "\'";
        System.out.println(status);
        return status;
    }

    @RequestMapping("/stop")
    String queue1() {
        dynamicScheduler.stop();
        return "Stopped.";
    }

    void sendMessage() {
        String message = "message " + messageNum++;
        System.out.println("Sending message with routing key \'" + routingKey + "\' : " + message);
        template.convertAndSend(exchange, routingKey, message);
    }
}