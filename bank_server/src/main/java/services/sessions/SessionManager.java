package services.sessions;

import Bank.BankAccount;
import Bank.BankAccountPrx;
import Bank.PremiumBankAccountPrx;
import com.zeroc.Ice.*;
import currency.client.CurrencyServiceClient;
import persistance.initialization.BankAccountFactory;
import persistance.model.BankClient;
import services.access_controll.AccountManager;
import services.access_controll.TokenManager;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class SessionManager {

    private static String PREMIUM_CATEGORY_NAME = "premium";
    private static String STANDARD_CATEGORY_NAME = "standard";
    private static String ADAPTER_NAME = "BankAdapter";
    private static String ACCOUNT_MANAGER_NAME = "manager";
    private static String ACCOUNT_MANAGER_CATEGORY = "login";
    private static String CONFIG_PATH = "./config.server";

    private final CurrencyServiceClient currencyServiceClient;
    private final Lock lock = new ReentrantLock();
    private final LinkedList<String> registeredTokens = new LinkedList<>();
    private final HashMap<String, Identity> activeServants = new HashMap<>();
    private final HashMap<Identity, String> activeIdentities = new HashMap<>();
    private ObjectAdapter adapter;
    private Communicator communicator;
    private InvalidSessionInspector housekeepingThread;

    public SessionManager(CurrencyServiceClient currencyServiceClient){
        this.currencyServiceClient = currencyServiceClient;
    }

    public void start() {
        this.communicator = Util.initialize(null, SessionManager.CONFIG_PATH);
        this.adapter = this.communicator.createObjectAdapter("BankAdapter");
        AccountManager accountManager = new AccountManager(this);
        Identity accountManagerIdentity = new Identity(SessionManager.ACCOUNT_MANAGER_NAME, SessionManager.ACCOUNT_MANAGER_CATEGORY);
        System.out.println(accountManagerIdentity.category);
        System.out.println(accountManagerIdentity.name);
        this.adapter.add(accountManager, accountManagerIdentity);
        this.adapter.activate();
        this.housekeepingThread = new InvalidSessionInspector();
        this.housekeepingThread.start();
    }

    public void terminate(){
        this.communicator.shutdown();
        if(this.housekeepingThread != null){
            this.housekeepingThread.interrupt();
        }
    }

    public BankAccountPrx registerSession(BankClient client, String token) throws SessionAlreadyExistsException {
        lock.lock();
        String category = this.generateIdentityCategoryString(client.isPremiumClient());
        Identity newSessionIdentity = new Identity(client.getPESEL(), category);
        if(this.alreadyLoggedIn(newSessionIdentity)){
            if(this.sessionExpired(newSessionIdentity)){
                this.deactivateIdentity(newSessionIdentity);
            } else {
                lock.unlock();
                throw new SessionAlreadyExistsException();
            }
        }
        BankAccount account = this.getBankAccount(client);
        BankAccountPrx accountProxy = this.registerBankAccount(account, client.getPESEL(), client.isPremiumClient(), token);
        lock.unlock();
        return accountProxy;
    }

    public void unregisterToken(String token){
        lock.lock();
        Identity identity = this.activeServants.get(token);
        this.removeSession(token, identity);
        lock.unlock();
    }

    private String generateIdentityCategoryString(boolean isPremium){
        if(isPremium){
            return SessionManager.PREMIUM_CATEGORY_NAME;
        } else {
            return SessionManager.STANDARD_CATEGORY_NAME;
        }
    }

    private boolean alreadyLoggedIn(Identity newSessionIdentity) {
        return this.activeIdentities.containsKey(newSessionIdentity);
    }

    private boolean sessionExpired(Identity newSessionIdentity) {
        String token = this.activeIdentities.get(newSessionIdentity);
        return this.sessionExpired(token);
    }

    private void deactivateIdentity(Identity identity) {
        String token = this.activeIdentities.get(identity);
        this.removeSession(token, identity);
    }

    private void removeSession(String token, Identity identity){
        this.activeIdentities.remove(identity);
        this.activeServants.remove(token);
        this.registeredTokens.remove(token);
        adapter.remove(identity);
    }

    private BankAccount getBankAccount(BankClient client){
        BankAccount account;
        if(client.isPremiumClient()){
            account = BankAccountFactory.initializePremiumBankAccount(client, this.currencyServiceClient);
        } else {
            account = BankAccountFactory.initalizeBankAccount(client);
        }
        return account;
    }

    private BankAccountPrx registerBankAccount(BankAccount account, String PESEL, boolean premiumAccount, String token) {
        String category = this.generateIdentityCategoryString(premiumAccount);
        Identity identity = new Identity(PESEL, category);
        this.activeIdentities.put(identity, token);
        this.activeServants.put(token, identity);
        this.registeredTokens.add(token);
        ObjectPrx proxy = this.adapter.add(account, identity);
        if(premiumAccount){
            return PremiumBankAccountPrx.uncheckedCast(proxy);
        } else {
            return BankAccountPrx.uncheckedCast(proxy);
        }
    }

    private void proceedSessionInspection(){
        lock.lock();
        for(String token : this.registeredTokens){
            if(this.sessionExpired(token)){
                Identity identity = this.activeServants.get(token);
                this.removeSession(token, identity);
            } else {
                break;
            }
        }
        lock.unlock();
    }

    private boolean sessionExpired(String token){
        TokenManager tokenManager = new TokenManager();
        return !tokenManager.isTokenValid(token);
    }

    private class InvalidSessionInspector extends Thread {

        private int SLEEP_TIME = 10 * 60 * 1000;

        @Override
        public void run(){
            try {
                Thread.sleep(this.SLEEP_TIME);
            } catch (InterruptedException ignored) { }
            proceedSessionInspection();
        }

    }

}
