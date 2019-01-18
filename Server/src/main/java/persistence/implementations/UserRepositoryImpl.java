package persistence.implementations;

import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.interfaces.UserRepository;
import utils.PersistenceUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    public UserRepositoryImpl() {
        persistenceUtils = PersistenceUtils.getInstance();
    }

    @Override
    public boolean login(String username, String password) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(
                    builder.and(
                            builder.equal(
                                    root.get("userName"),
                                    username
                            ),
                            builder.equal(
                                    root.get("userPass"),
                                    password
                            )
                    )
            );

            return !(session.createQuery(query).getResultList().isEmpty());
        }
    }

    @Override
    public User findByUsername(String username) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<User> query = builder.createQuery(User.class);

            Root<User> root = query.from(User.class);

            query.select(root).where(
                    builder.equal(root.get("userName"), username)
            );

            return session.createQuery(query).getResultList().stream().findFirst().orElse(null);
        }

    }

    @Override
    public synchronized void createAccount(String username, String password) throws Exception {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();
            try {
                session.save(new User(username, password));
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new Exception(e);
            }
        }
    }

    @Override
    public List<User> getAllUsers() {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            CriteriaQuery<User> query = session.getCriteriaBuilder().createQuery(User.class);
            Root<User> criteriaRoot = query.from(User.class);
            query.select(criteriaRoot);

            return session.createQuery(query).getResultList();
        }
    }

    private PersistenceUtils persistenceUtils;
}
