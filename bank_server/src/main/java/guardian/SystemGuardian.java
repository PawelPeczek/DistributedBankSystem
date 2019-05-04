package guardian;

import currency.client.CurrencyClientObserver;
import currency.client.CurrencyServiceClient;
import services.sessions.SessionManager;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class SystemGuardian extends Thread implements CurrencyClientObserver {

    private final Lock lock = new ReentrantLock();
    private final Condition stateHasChanged  = lock.newCondition();
    private static final Logger logger = Logger.getLogger(CurrencyServiceClient.class.getName());
    private boolean unhandledCurrencyClientShutdown = false;
    private final Lock terminationCheckFromOutside = new ReentrantLock();
    private boolean terminationFlag = false;
    private final CurrencyServiceClient currencyServiceClient;
    private final SessionManager sessionManager;
    private boolean systemStarted = false;
    private boolean terminationStarted = false;

    public SystemGuardian(CurrencyServiceClient currencyServiceClient, SessionManager sessionManager){
        this.currencyServiceClient = currencyServiceClient;
        this.currencyServiceClient.setClientObserver(this);
        this.sessionManager = sessionManager;
    }

    public void notifyCurrencyServiceClientShutdown(){
        this.terminationCheckFromOutside.lock();
        if(this.terminationStarted){
            this.terminationCheckFromOutside.unlock();
            return;
        }
        this.terminationCheckFromOutside.unlock();
        this.lock.lock();
        this.unhandledCurrencyClientShutdown = true;
        this.stateHasChanged.signal();
        this.lock.unlock();
    }

    @Override
    public void waitForObserverReadiness() {
        this.lock.lock();
        this.lock.unlock();
    }

    public void terminate(){
        logger.info("BankSystem guardian received termination signal");
        this.lock.lock();
        this.terminationFlag = true;
        this.stateHasChanged.signal();
        this.lock.unlock();
        System.out.println("BankSystem guardian completed termination");
    }


    public void run(){
        while(true){
            this.lock.lock();
            if(terminationFlag){
                this.stopSystemThreads();
                return;
            }
            if(!this.systemStarted){
                logger.info("BankSystem guardian is starting a system");
                startSystem();
                logger.info("BankSystem initialized");
            }
            try {
                logger.info("BankSystem guardian is handling pending events");
                this.handlePendingEvent();
                logger.info("BankSystem guardian - all pending events handled.");
                this.stateHasChanged.await();
            } catch (InterruptedException e) {
                this.stopSystemThreads();
                return;
            } finally {
                this.lock.unlock();
            }
        }
    }

    private void startSystem() {
        try {
            this.currencyServiceClient.run();
            this.sessionManager.start();
            this.systemStarted = true;
        } catch (Exception ex){
            ex.printStackTrace();
            logger.warning(ex.getMessage());
            this.currencyServiceClient.forceShutdown();
            this.interrupt();
        }
    }

    private void handlePendingEvent() throws InterruptedException {
        if(unhandledCurrencyClientShutdown){
            this.handleCurrencyClientShutdown();
        }
    }

    private void handleCurrencyClientShutdown() throws InterruptedException {
        if(!this.currencyServiceClient.isRunning()){
            this.currencyServiceClient.stop();
            this.currencyServiceClient.run();
            Thread.sleep(10000);
        }
        this.unhandledCurrencyClientShutdown = false;
    }

    private void stopSystemThreads(){
        this.terminationCheckFromOutside.lock();
        logger.info("BankSystem guardian is stopping all system threads");
        this.terminationStarted = true;
        this.currencyServiceClient.stop();
        this.sessionManager.terminate();
        System.out.println("BankSystem guardian - all threads stopped.");
        this.terminationCheckFromOutside.unlock();
    }

}
