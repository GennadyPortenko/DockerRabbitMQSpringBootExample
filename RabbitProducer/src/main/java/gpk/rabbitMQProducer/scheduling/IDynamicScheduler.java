package gpk.rabbitMQProducer.scheduling;

public interface IDynamicScheduler {
    void setInterval(long millis);
    void start(Runnable runnable);
    void stop();
}
