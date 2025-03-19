package ru.job4j.cars.repository.user;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
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

    /**
     * Негативный тест удаления объекта типа User по id
     */
    @Test
    void whenDeleteUserIsFail() {
        User user = new User();
        user.setLogin("test login");

        userRepository.create(user);

        Optional<User> savedUserOptional = userRepository.findById(user.getId());
        assertThat(savedUserOptional).isPresent();

        User savedUser = savedUserOptional.get();

        int nonExistingId = 999;

        userRepository.delete(nonExistingId);

        Optional<User> deletedUserOptional = userRepository.findById(savedUser.getId());
        assertThat(deletedUserOptional).isPresent();
    }

    /**
     * Позитивный тест получения всех объектов типа User отсортированных по id
     */
    @Test
    void whenGetAllUsersSortedByIdIsSuccess() {

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        user1.setLogin("user1");
        user2.setLogin("user2");
        user3.setLogin("user3");

        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);

        List<User> users = userRepository.findAllOrderById();

        assertEquals(3, users.size(), "Должно быть возвращено 3 пользователя");

        assertEquals("user1", users.get(0).getLogin(), "Первый пользователь должен быть user1");
        assertEquals("user2", users.get(1).getLogin(), "Второй пользователь должен быть user2");
        assertEquals("user3", users.get(2).getLogin(), "Третий пользователь должен быть user3");

        assertTrue(users.get(0).getId() < users.get(1).getId(), "ID первого пользователя должен быть меньше ID второго");
        assertTrue(users.get(1).getId() < users.get(2).getId(), "ID второго пользователя должен быть меньше ID третьего");
    }

    /**
     * Негативный тест получения всех объектов типа User отсортированных по id
     */
    @Test
    void whenGetAllUsersSortedByIdIsFail() {
        List<User> users = userRepository.findAllOrderById();

        assertTrue(users.isEmpty(), "Список пользователей должен быть пустым");
    }

    /**
     * Позитивный тест получения пользователей содержащих key в строке
     */
    @Test
    void whenGetUsersByLikeLoginIsSuccess() {

        User user1 = new User();
        user1.setLogin("john_doe");

        User user2 = new User();
        user2.setLogin("jane_doe");

        User user3 = new User();
        user3.setLogin("alice_smith");

        User user4 = new User();
        user4.setLogin("bob_johnson");

        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);
        userRepository.create(user4);

        List<User> users = userRepository.findByLikeLogin("doe");

        assertEquals(2, users.size(), "Должно быть возвращено 2 пользователя с login, содержащим 'doe'");

        assertTrue(users.stream().anyMatch(user -> user.getLogin().equals("john_doe")),
                "Должен быть возвращен пользователь john_doe");
        assertTrue(users.stream().anyMatch(user -> user.getLogin().equals("jane_doe")),
                "Должен быть возвращен пользователь jane_doe");
        assertFalse(users.stream().anyMatch(user -> user.getLogin().equals("alice_smith")),
                "Пользователь alice_smith не должен быть возвращен");
        assertFalse(users.stream().anyMatch(user -> user.getLogin().equals("bob_johnson")),
                "Пользователь bob_johnson не должен быть возвращен");
    }

    /**
     * Негативный тест получения пользователей содержащих key в строке
     */
    @Test
    void whenGetUsersByLikeLoginIsFail() {

        User user1 = new User();
        user1.setLogin("john_doe");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setLogin("jane_doe");
        user2.setPassword("password2");

        userRepository.create(user1);
        userRepository.create(user2);

        List<User> users = userRepository.findByLikeLogin("smith");

        assertTrue(users.isEmpty(), "Список пользователей должен быть пустым");
    }

    /**
     * Позитивный тест получения пользователя по login
     */
    @Test
    void whenGetUserByLoginIsSuccess() {
        User user = new User();

        user.setLogin("john_doe");

        userRepository.create(user);

        Optional<User> savedUser = userRepository.findByLogin("john_doe");

        assertTrue(savedUser.isPresent(), "Saved user should not be empty.");
        assertEquals(user.getLogin(), savedUser.get().getLogin(), "Logins should be the same.");
    }

    /**
     * Негативный тест получения пользователя по login
     */
    @Test
    void whenGetUserByLoginIsFail() {

        User user = new User();
        user.setLogin("john_doe");

        userRepository.create(user);

        Optional<User> result = userRepository.findByLogin("non_existent_login");

        assertTrue(result.isEmpty(), "Optional должен быть пустым, так как пользователь с таким login не существует");
    }
}