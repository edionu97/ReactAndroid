package persistence.implementations;

import models.Contact;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.interfaces.ContactRepository;
import persistence.utils.AbstractCRUDRepo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ContactRepositoryImpl extends AbstractCRUDRepo<Contact> implements ContactRepository {

    @Override
    public void add(String firstName, String lastName, String phoneNumber) throws Exception {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(getOrCreate(firstName, lastName, phoneNumber));
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new Exception(e);
            }
        }

    }

    @Override
    public Contact findContactByFirstName(String firstName) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Contact> query = builder.createQuery(Contact.class);

            Root<Contact> root = query.from(Contact.class);

            query.select(root).where(
                    builder.equal(root.get("firstName"), firstName)
            );

            return session.createQuery(query).getResultList().stream().findFirst().orElse(null);
        }
    }

    @Override
    public Contact findContactById(int cid) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Contact> query = builder.createQuery(Contact.class);

            Root<Contact> root = query.from(Contact.class);

            query.select(root).where(
                    builder.equal(root.get("cid"), cid)
            );

            return session.createQuery(query).getResultList().stream().findFirst().orElse(null);
        }
    }

    @Override
    public List<Contact> getAll() {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaQuery<Contact> query = session.getCriteriaBuilder().createQuery(Contact.class);
            Root<Contact> criteriaRoot = query.from(Contact.class);
            query.select(criteriaRoot);

            return session.createQuery(query).getResultList();
        }
    }

    private Contact getOrCreate(String firstName, String lastName, String phoneNumber) {

//        Contact contact = findContactByFirstName(firstName);
//
//        return contact == null ? new Contact(firstName, lastName, phoneNumber) : contact;

        return new Contact(firstName, lastName, phoneNumber);
    }
}
