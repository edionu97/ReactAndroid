package persistence.implementations;

import models.FetchDetails;
import models.enums.OperationType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.interfaces.FetchDetailsRepository;
import persistence.utils.AbstractCRUDRepo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class FetchDetailsRepositoryImpl extends AbstractCRUDRepo<FetchDetails> implements FetchDetailsRepository {

    @Override
    public void add(String tableName, OperationType type, String data, String username) throws Exception {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(new FetchDetails(tableName, type, data, username));
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new Exception(e);
            }
        }
    }

    @Override
    public FetchDetails findById(int fdid) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<FetchDetails> query = builder.createQuery(FetchDetails.class);

            Root<FetchDetails> root = query.from(FetchDetails.class);

            query.select(root).where(
                    builder.equal(root.get("fdid"), fdid)
            );

            return session.createQuery(query).getResultList().stream().findFirst().orElse(null);
        }
    }

    @Override
    public FetchDetails findByAllFields(String tableName, OperationType type, String data, String username) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<FetchDetails> query = builder.createQuery(FetchDetails.class);

            Root<FetchDetails> root = query.from(FetchDetails.class);

            query.select(root).where(
                    builder.and(
                        builder.equal(root.get("tableName"), tableName),
                        builder.equal(root.get("type"), type),
                        builder.equal(root.get("data"), data),
                        builder.equal(root.get("username"), username)
                    )
            );

            return session.createQuery(query).getResultList().stream().findFirst().orElse(null);
        }
    }

    @Override
    public List<FetchDetails> getAll() {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            CriteriaQuery<FetchDetails> query = session.getCriteriaBuilder().createQuery(FetchDetails.class);
            Root<FetchDetails> criteriaRoot = query.from(FetchDetails.class);
            query.select(criteriaRoot);

            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<FetchDetails> getUnfetched(String  username) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<FetchDetails> query = builder.createQuery(FetchDetails.class);

            Root<FetchDetails> root = query.from(FetchDetails.class);

            query.select(root).where(
                    builder.and(
                        builder.equal(root.get("fetched"), false),
                        builder.equal(root.get("username"), username)
                    )
            );

            return session.createQuery(query).getResultList();
        }
    }
}
