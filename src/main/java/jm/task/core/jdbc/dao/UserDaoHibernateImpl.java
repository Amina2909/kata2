package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl extends Util implements UserDao {
    private final String sqlCreate = "CREATE TABLE IF NOT EXISTS test.MyTable" +
            " (id mediumint not null auto_increment, name VARCHAR(50), " +
            "lastname VARCHAR(50), " +
            "age tinyint, " +
            "PRIMARY KEY (id))";
    private final String sqlDrop = "Drop table if exists test.MyTable";
    private Session session;
    private Transaction transaction;

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try {
            session = getConnection().openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery(sqlCreate).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.getSessionFactory().close();
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            session = getConnection().openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery(sqlDrop).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.getSessionFactory().close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            session = getConnection().openSession();
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.getSessionFactory().close();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            session = getConnection().openSession();
            transaction = session.beginTransaction();
            session.get(User.class, id);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.getSessionFactory().close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            session = getConnection().openSession();
            transaction = session.beginTransaction();
            list = session.createCriteria(User.class).list();
            System.out.println(list.toString());
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.getSessionFactory().close();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try {
            session = getConnection().openSession();
            transaction = session.beginTransaction();
            final List<User> instances = session.createCriteria(User.class).list();
            for (Object o : instances) {
                session.delete(o);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.getSessionFactory().close();
        }
    }
}
