package services.access_controll;

import Bank.*;
import com.zeroc.Ice.Current;
import persistance.DAO.BankClientDAO;
import persistance.model.BankClient;
import services.sessions.SessionAlreadyExistsException;
import services.sessions.SessionManager;

import java.util.Optional;

public class AccountManager implements Bank.AccountManager {

    private final SessionManager sessionManager;
    private static final float PREMIUM_INCOME = 80000;

    public AccountManager(SessionManager manager){
        this.sessionManager = manager;
    }

    @Override
    public void createAccount(AccountCreationData creationData, Current current) throws AccountCreationException {
        String PESEL = creationData.PESEL;
        String password = creationData.password;
        boolean isPremium = creationData.incomeLevel >= AccountManager.PREMIUM_INCOME;
        BankClientDAO clientDAO = new BankClientDAO();
        Optional<BankClient> createdClient = clientDAO.create(PESEL, password, isPremium);
        if(!createdClient.isPresent()){
            throw new AccountCreationException("Client already exists.");
        } else {
            System.out.println(createdClient.get().getId());
        }
    }

    @Override
    public LoginResponse login(String PESEL, String password, Current current) throws AuthenticationException {
        System.out.println("LOGIN");
        LoginResponse response = new LoginResponse();
        BankClientDAO clientDAO = new BankClientDAO();
        Optional<BankClient> client = clientDAO.getByPeselAndPassword(PESEL, password);
        if(!client.isPresent()){
            throw new AuthenticationException("Credentials invalid");
        }
        boolean clientIsPremium = client.get().isPremiumClient();
        if(clientIsPremium){
            response.accountType = AccountType.PREMIUM;
        } else {
            response.accountType = AccountType.STANDARD;
        }
        try {
            TokenManager tokenManager = new TokenManager();
            String token = tokenManager.generateToken(PESEL, clientIsPremium);
            response.secretToken = token;
            BankClient foundClient = client.get();
            System.out.println("STATE: " + foundClient.getState());
            response.account = this.sessionManager.registerSession(foundClient, token);
        } catch (SessionAlreadyExistsException e) {
            throw new AuthenticationException("Client already logged-in.");
        }

        return response;
    }

    @Override
    public void logout(Current current) {
        System.out.println("LOGOUT");
        try {
            TokenManager tokenManager = new TokenManager();
            String token = tokenManager.getTokenFromContext(current);
            System.out.println("Token: " + token);
            if(tokenManager.isTokenValid(token)){
                System.out.println("Token valid");
                this.sessionManager.unregisterToken(token);
                System.out.println("Unregistered token");
            } else {
                System.out.println("Token invalid");
            }
        } catch (TokenNotPresentInContextException ignored) { }
    }


}
