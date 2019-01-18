package persistence.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.PersistenceUtils;

public abstract class AbstractCRUDRepo<T> implements SimpleCRUD<T> {

    public AbstractCRUDRepo(){
        persistenceUtils = PersistenceUtils.getInstance();
    }

    @Override
    public void delete(T element) throws  Exception{
        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            Transaction transaction = session.beginTransaction();
            try{
                session.delete(element);
                transaction.commit();
            }catch (Exception e){
                transaction.rollback();
                throw new Exception(e);
            }
        }
    }

    @Override
    public void update(T element) throws  Exception {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(element);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new Exception(e);
            }
        }
    }

    protected PersistenceUtils persistenceUtils;
}
