package currency.client;

public class CurrencyNotAvailableException extends Exception {
    public CurrencyNotAvailableException(String message) {
        super(message);
    }
}
