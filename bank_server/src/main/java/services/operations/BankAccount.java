package services.operations;

import Bank.AuthorizationException;
import Bank.OperationException;
import com.zeroc.Ice.Current;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistance.HibernateHelper;
import persistance.model.BankClient;
import persistance.model.Operation;
import services.access_controll.TokenManager;
import services.access_controll.TokenNotPresentInContextException;

public class BankAccount implements Bank.BankAccount {

    protected final BankClient client;

    public BankAccount(BankClient client){
        this.client = client;
    }

    @Override
    public float getState(Current current) throws AuthorizationException {
        this.authorizeOperation(current);
        return client.getState();
    }

    @Override
    public float putMoney(float value, Current current) throws AuthorizationException, OperationException {
        this.authorizeOperation(current);
        try(Session session = HibernateHelper.getSession()){
            Transaction tx = session.beginTransaction();
            Operation operation = new Operation(value, this.client);
            this.client.putMoney(operation);
            session.save(operation);
            tx.commit();
            return this.client.getState();
        } catch (OperationException ex){
          throw ex;
        } catch (Exception ex){
            throw new OperationException("Internal error");
        }
    }

    @Override
    public float withdrawMoney(float value, Current current) throws AuthorizationException, OperationException {
        this.authorizeOperation(current);
        try(Session session = HibernateHelper.getSession()){
            Transaction tx = session.beginTransaction();
            Operation operation = new Operation(-value, this.client);
            this.client.withdrawMoney(operation);
            session.save(operation);
            tx.commit();
            return this.client.getState();
        }  catch (OperationException ex){
            throw ex;
        } catch (Exception ex){
            throw new OperationException("Internal error");
        }
    }

    private void authorizeOperation(Current current) throws AuthorizationException {
        TokenManager tokenManager = new TokenManager();
        try {
            String token = tokenManager.getTokenFromContext(current);
            if(!tokenManager.isTokenValid(token)){
                throw new AuthorizationException();
            }
        } catch (TokenNotPresentInContextException e) {
            throw new AuthorizationException();
        }
    }
}
