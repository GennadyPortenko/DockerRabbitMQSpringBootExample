package gpk.rabbitMQProducer.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
public class DynamicScheduler implements IDynamicScheduler, Trigger {

    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledFuture;

    private long delayInterval = 1000;

    @Override
    public void setInterval(long millis) {
        this.delayInterval = millis;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        Date lastTime = triggerContext.lastActualExecutionTime();
        return (lastTime == null) ? new Date() : new Date(lastTime.getTime() + delayInterval);
    }

    @Override
    public void start(Runnable runnable) {
        if (scheduledFuture != null) {
           scheduledFuture.cancel(true);
        }
        scheduledFuture = taskScheduler.schedule(runnable, this);
    }

    @Override
    public void stop() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

}
