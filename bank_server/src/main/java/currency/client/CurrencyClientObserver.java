package currency.client;

public interface CurrencyClientObserver {
    void notifyCurrencyServiceClientShutdown();
    void waitForObserverReadiness();
}
