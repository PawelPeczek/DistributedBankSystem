package persistance.DAO;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistance.HibernateHelper;
import persistance.model.BankClient;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public class BankClientDAO {

    public Optional<BankClient> getByPeselAndPassword(String PESEL, String password){
        String passwordHash = this.getPasswordHash(password);
        try (Session session = HibernateHelper.getSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<BankClient> criteria = builder.createQuery(BankClient.class);
            Root<BankClient> myObjectRoot = criteria.from(BankClient.class);
            criteria.select(myObjectRoot).where(builder.equal(myObjectRoot.get("PESEL"), PESEL));
            TypedQuery<BankClient> query = session.createQuery(criteria);
            List<BankClient> results = query.getResultList();
            if(results.isEmpty() || results.size() > 1){
                return Optional.empty();
            }
            BankClient singleResult = results.get(0);
            if(passwordHash.equals(singleResult.getPassword())){
                return Optional.of(singleResult);
            } else {
                return Optional.empty();
            }
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public Optional<BankClient> create(String PESEL, String password, boolean isPremium){
        Optional<BankClient> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            BankClient bankClient = new BankClient();
            bankClient.setPESEL(PESEL);
            String passwordHash = this.getPasswordHash(password);
            bankClient.setPassword(passwordHash);
            bankClient.setPremiumClient(isPremium);
            session.persist(bankClient);
            result = Optional.of(bankClient);
            tx.commit();
        } catch (Exception ex) {
            result = Optional.empty();
        }
        return result;
    }


    private String getPasswordHash(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException ignored) { }
        return "";
    }

}
