package services;

import currency.client.CurrencyServiceClient;
import currency.exchange.gen.Currency;
import guardian.SystemGuardian;
import persistance.HibernateHelper;
import services.sessions.SessionManager;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class BankSystem {

    private final String bankName;
    private SystemGuardian guardian = null;
    private Lock lock = new ReentrantLock();
    private Condition terminateCondition = lock.newCondition();

    public BankSystem(String bankName){
        this.bankName = bankName;
    }

    public void start(){
        String hibernateConfigPath = SystemConfig.getHibernateConfigPath(this.bankName);
        HibernateHelper.initializeHibernate(hibernateConfigPath);
        LinkedList<Currency> currencyToSubscribe = SystemConfig.getCurrencyToSubscribe(this.bankName);
        String currencyExchangeHost = SystemConfig.getCurrencyExchangeHost();
        int currencyExchangePort = SystemConfig.getCurrencyExchangePort();
        System.out.println(currencyExchangeHost + ":" + currencyExchangePort);
        CurrencyServiceClient client = new CurrencyServiceClient(currencyExchangeHost, currencyExchangePort, currencyToSubscribe);
        SessionManager sessionManager = new SessionManager(client);
        SystemGuardian guardian = new SystemGuardian(client, sessionManager);
        guardian.start();
    }

    public void awaitTermination(){
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
        try {
            this.lock.lock();
            this.terminateCondition.await();
            this.lock.unlock();
        } catch (InterruptedException e) {
            this.stop();
        }
    }

    public void stop(){
        if(guardian != null){
            guardian.terminate();
        }
    }

    private class ShutdownThread extends Thread {

        @Override
        public void run() {
            lock.lock();
            terminateCondition.signal();
            lock.unlock();
        }
    }

}
