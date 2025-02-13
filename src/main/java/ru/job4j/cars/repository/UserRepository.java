package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {

    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query<?> query = session.createQuery("update User u set u.login = :newLogin where u.id = :id");

            query.setParameter("newLogin", user.getLogin());
            query.setParameter("id", user.getId());

            query.executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query<?> query = session.createQuery("delete from User u where u.id = :id");
            query.setParameter("id", userId);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<User> query = session.createQuery("from User ORDER BY id ASC", User.class);
            List<User> users = query.getResultList();
            tx.commit();
            return users;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<User> query = session.createQuery(
                    "from User where id = :id", User.class);

            query.setParameter("id", userId);

            User user = query.getSingleResult();

            return Optional.ofNullable(user);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<User> query = session.createQuery(
                    "from User u where lower(u.login) like lower(:login)", User.class);
            query.setParameter("login", "%" + key.toLowerCase() + "%");
            List<User> users = query.getResultList();
            tx.commit();
            return users;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<User> query = session.createQuery(
                    "from User u where u.login = :login", User.class);

            query.setParameter("login", login);

            Optional<User> user = query.uniqueResultOptional();
            tx.commit();

            return user;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}
