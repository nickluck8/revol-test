package com.test.main.repository;

import com.test.main.model.Account;
import com.test.main.util.HibernateUtils;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class AccountRepository implements Repository<Account, Long> {
    private static SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

    @Override
    public List<Account> getAll() {
        List<Account> accounts;
        try (Session session = sessionFactory.openSession()) {
            accounts = session.createQuery("FROM Account").list();
        }
        return accounts;
    }

    @Override
    public Account add(Account account) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(account);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public Account update(Long id, Account account) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(account);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public Account delete(Long id) {
        Transaction transaction = null;
        Account accountToDelete = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            accountToDelete = session.get(Account.class, id);
            session.delete(accountToDelete);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return accountToDelete;
    }

    @Override
    public Account getById(Long id) {
        Account account;
        try (Session session = sessionFactory.openSession()) {
            account = session.get(Account.class, id);
        }
        return account;
    }

    @Override
    public void transferMoney(Account fromAccount, Account toAccount) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(fromAccount);
            session.saveOrUpdate(toAccount);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Account getByIdForUpdate(Long id) {
        Account account;
        try (Session session = sessionFactory.openSession()) {
            account = session.get(Account.class, id, LockMode.PESSIMISTIC_WRITE);
        }
        return account;
    }
}
