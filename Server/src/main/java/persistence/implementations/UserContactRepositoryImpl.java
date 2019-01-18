package persistence.implementations;

import models.Contact;
import models.User;
import models.UserContact;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.interfaces.UserContactRepository;
import persistence.utils.AbstractCRUDRepo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserContactRepositoryImpl extends AbstractCRUDRepo<UserContact> implements UserContactRepository {

    @Override
    public void add(User user, Contact contact) throws Exception {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();
            try {
                session.save(new UserContact(user, contact));
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new Exception(e);
            }
        }
    }

    @Override
    public UserContact findContactById(int uid, int cid) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<UserContact> query = builder.createQuery(UserContact.class);

            Root<UserContact> root = query.from(UserContact.class);

            query.select(root).where(
                    builder.and(
                            builder.equal(root.get("uid"), uid),
                            builder.equal(root.get("cid"), cid)
                    )
            );

            return session.createQuery(query).getResultList().stream().findFirst().orElse(null);
        }
    }

    @Override
    public List<UserContact> getAll() {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            CriteriaQuery<UserContact> query = session.getCriteriaBuilder().createQuery(UserContact.class);
            Root<UserContact> criteriaRoot = query.from(UserContact.class);
            query.select(criteriaRoot);

            return session.createQuery(query).getResultList();
        }
    }
}
