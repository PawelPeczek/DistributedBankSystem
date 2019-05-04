package persistance;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateHelper {
    private static SessionFactory sessionFactory;

    static {

    }

    public static Session getSession() throws HibernateException {
        return sessionFactory.openSession();
    }

    public static void initializeHibernate(String configResourcePath){
        try {
            Configuration configuration = new Configuration();
            configuration.configure(configResourcePath);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

}