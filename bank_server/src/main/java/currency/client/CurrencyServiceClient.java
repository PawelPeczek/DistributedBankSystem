package currency.client;

import currency.exchange.gen.Currency;
import currency.exchange.gen.CurrencyExchangeGrpc;
import currency.exchange.gen.CurrencyUpdate;
import currency.exchange.gen.CurrencyUpdatesSubscriptionRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class CurrencyServiceClient {

    private static final Logger logger = Logger.getLogger(CurrencyServiceClient.class.getName());
    private final String host;
    private final int port;
    private ManagedChannel channel;
    private CurrencyExchangeGrpc.CurrencyExchangeStub asyncStub;
    private HashMap<Currency, Float> currencyValues = new HashMap<>();
    private boolean running;
    private Thread runningThread;
    private final Lock lock = new ReentrantLock();
    private final Condition shouldTerminate  = lock.newCondition();
    private final List<Currency> currencyToSubscribe;
    private CurrencyClientObserver clientObserver;

    public CurrencyServiceClient(String host, int port, List<Currency> currencyToSubscribe) {
        this.currencyToSubscribe = new LinkedList<>(currencyToSubscribe);
        this.host = host;
        this.port = port;
    }

    public synchronized boolean isRunning(){
        return this.running;
    }

    public synchronized float getCurrencyValue(Currency currency) throws CurrencyNotAvailableException, CurrencyServiceNotAvalableException {
        if(!this.isRunning()){
            throw new CurrencyServiceNotAvalableException("Currency service temporally unavailable.");
        }
        if(currencyValues.containsKey(currency)){
            return currencyValues.get(currency);
        } else {
            throw new CurrencyNotAvailableException("Currency not available in bank.");
        }
    }

    public synchronized void run() {
        if(this.isRunning()){
            return;
        }
        this.runningThread = new UpdateReceiver();
        this.runningThread.start();
    }

    public void stop() {
        lock.lock();
        logger.info("CurrencyClient received stop signal");
        this.shouldTerminate.signalAll();
        this.runningThread.interrupt();
        this.runningThread = null;
        logger.info("CurrencyClient has stopped");
        lock.unlock();
    }

    public void forceShutdown(){
        if(this.runningThread != null){
            this.runningThread.interrupt();
        }
    }

    public synchronized void setClientObserver(CurrencyClientObserver observer){
        this.clientObserver = observer;
    }

    private synchronized void markClientAsRunning(){
        this.running = true;
    }

    private synchronized void markClientAsStopped(){
        this.running = false;
    }

    private synchronized void notifyUnavailableService(){
        if(clientObserver != null){
            clientObserver.notifyCurrencyServiceClientShutdown();
        }
    }

    private synchronized void connectToChannel(){
        logger.info("Connecting to channel [IN PROGRESS]");
        this.channel = ManagedChannelBuilder.forAddress(this.host, this.port)
                .usePlaintext()
                .build();
        this.asyncStub = CurrencyExchangeGrpc.newStub(channel);
        logger.info("Connecting to channel [DONE]");
    }

    private synchronized void updateCurrencyValue(CurrencyUpdate update){
        logger.info("Currency update received. Currency: " + update.getCurrency() + " value: " + update.getValue());
        Currency currency = update.getCurrency();
        float value = update.getValue();
        this.currencyValues.put(currency, value);
    }

    private class CurrencyResponseStreamObserver implements StreamObserver<CurrencyUpdate> {

        @Override
        public void onNext(CurrencyUpdate currencyUpdate) {
            updateCurrencyValue(currencyUpdate);
        }

        @Override
        public void onError(Throwable t) {
            this.handleStreamStopEvent(t.getMessage());
        }

        @Override
        public void onCompleted() {
            this.handleStreamStopEvent("Stream has ended");
        }

        private void handleStreamStopEvent(String message){
            logger.warning("Currency stream has stopped: " + message);
            lock.lock();
            shouldTerminate.signal();
            lock.unlock();
        }
    }

    private class UpdateReceiver extends Thread {
        @Override
        public void run() {
            logger.info("Currency update receiving thread started");
            lock.lock();
            if(clientObserver != null) {
                clientObserver.waitForObserverReadiness();
            }
            connectToChannel();
            CurrencyResponseStreamObserver responseObserver = new CurrencyResponseStreamObserver();
            CurrencyUpdatesSubscriptionRequest.Builder requestBuilder = CurrencyUpdatesSubscriptionRequest.newBuilder();
            for(Currency currency : currencyToSubscribe){
                requestBuilder.addCrrencyToObserve(currency);
            }
            CurrencyUpdatesSubscriptionRequest request = requestBuilder.build();
            asyncStub.subscribeCurrencyUpdates(request, responseObserver);
            markClientAsRunning();
            try {
                shouldTerminate.await();
                logger.info("Currency update receiving thread started termination procedure");
            } catch (InterruptedException ignored) {

            } finally {
                markClientAsStopped();
                channel.shutdown();
                currencyValues = new HashMap<>();
                notifyUnavailableService();
                logger.info("Currency update receiving thread finished termination procedure");
                lock.unlock();
            }
        }
    }
}
