package gpk.rabbitMQProducer.configuration;

import gpk.rabbitMQProducer.scheduling.DynamicScheduler;
import gpk.rabbitMQProducer.scheduling.IDynamicScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class MainConfiguration {

    @Bean
    IDynamicScheduler dynamicScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();
        return new DynamicScheduler(threadPoolTaskScheduler);
    }

}
