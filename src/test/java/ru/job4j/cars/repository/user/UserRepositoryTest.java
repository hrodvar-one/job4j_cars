package ru.job4j.cars.repository.user;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import javax.xml.bind.ValidationException;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        userRepository = new UserRepository(crudRepository);
    }

    @AfterEach
    void cleanUp() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Позитивный тест создания объекта типа User
     */
    @Test
    void whenCreateUserIsSuccess() {

        User user = new User();

        user.setLogin("test login");
        user.setPassword("test password");

        userRepository.create(user);

        Session session = sf.openSession();
        session.beginTransaction();

        User savedUser = session.get(User.class, user.getId());

        session.getTransaction().commit();
        session.close();

        assertNotNull(savedUser, "User should not be null.");
        assertEquals(user.getLogin(), savedUser.getLogin(), "Login should be the same.");
    }

    /**
     * Позитивный тест получения объекта типа User по id
     */
    @Test
    void whenGetUserByIdIsSuccess() {
        User user = new User();
        user.setLogin("test login");

        userRepository.create(user);

        Optional<User> savedUser = userRepository.findById(user.getId());
        assertTrue(savedUser.isPresent(), "Saved user should not be empty.");
        assertEquals(user.getLogin(), savedUser.get().getLogin(), "Logins should be the same.");
    }

    /**
     * Негативный тест получения объекта типа User по id
     */
    @Test
    void whenGetUserByIdIsFail() {
        Optional<User> user = userRepository.findById(-1);

        assertFalse(user.isPresent(), "User should not be present in the database.");
    }

    /**
     * Позитивный тест обновления объекта типа User по id
     */
    @Test
    void whenUpdateUserIsSuccess() {
        User user = new User();
        user.setLogin("test login");

        userRepository.create(user);

        Optional<User> savedUser = userRepository.findById(user.getId());

        assertTrue(savedUser.isPresent(), "Saved user should not be empty.");
        assertEquals(user.getLogin(), savedUser.get().getLogin(), "Logins should be the same.");
    }

    /**
     * Негативный тест обновления объекта типа User по id
     */
    @Test
    void whenUpdateUserIsFail() {
        Optional<User> user = userRepository.findById(-1);

        assertFalse(user.isPresent(), "User should not be present in the database.");
    }

    /**
     * Позитивный тест удаления объекта типа User по id
     */
    @Test
    void whenDeleteUserIsSuccess() {
        User user = new User();
        user.setLogin("test login");

        userRepository.create(user);

        Optional<User> savedUserOptional = userRepository.findById(user.getId());
        assertThat(savedUserOptional).isPresent();

        User savedUser = savedUserOptional.get();

        userRepository.delete(savedUser.getId());

        Optional<User> deletedUserOptional = userRepository.findById(savedUser.getId());

        assertThat(deletedUserOptional).isEmpty();
    }
}