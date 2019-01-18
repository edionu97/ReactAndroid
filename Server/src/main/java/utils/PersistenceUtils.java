package utils;

import models.Contact;
import models.FetchDetails;
import models.User;
import models.UserContact;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class PersistenceUtils {

    /**
     * Gets the an instance of the persistence utils calss
     * @return an instance
     */
    public static PersistenceUtils getInstance(){

        if(persistenceUtils == null){
            synchronized (PersistenceUtils.class){
                if(persistenceUtils == null){
                    persistenceUtils = new PersistenceUtils();
                }
            }
        }
        return persistenceUtils;
    }

    /**
     * Get an object though it we can make sessions with hibernate
     * @return an instance of that object
     */

    public SessionFactory getSessionFactory(){
        return  sessionFactory;
    }

    private PersistenceUtils(){
        sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Contact.class)
                .addAnnotatedClass(UserContact.class)
                .addAnnotatedClass(FetchDetails.class)
                .buildSessionFactory();
    }


    private static volatile PersistenceUtils persistenceUtils = null;
    private  SessionFactory sessionFactory;
}
