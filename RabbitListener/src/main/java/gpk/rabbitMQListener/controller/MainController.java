package gpk.rabbitMQListener.controller;

import gpk.rabbitMQListener.component.RabbitMQListener;
import gpk.rabbitMQListener.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MainController {

    private final RabbitMQListener rabbitListener;

    @PostConstruct
    public void init() {
        System.out.println(rabbitListener);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/send")
    @ResponseBody
    public String send() {
        return "sent";
    }

    @SendTo("/client/monitor")
    public MessageDto sendTo() {
        return new MessageDto("response message");
    }

    @MessageMapping("/hello")
    @SendTo("/client/monitor")
    public MessageDto greeting(@RequestBody MessageDto message) {
        System.out.println(message.getText());
        return new MessageDto("Hello from server.");
    }

}
