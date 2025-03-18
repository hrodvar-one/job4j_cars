package ru.job4j.cars.repository.owner;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OwnerRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private OwnerRepository ownerRepository;

    @BeforeEach
    void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        ownerRepository = new OwnerRepository(crudRepository);
    }

    @AfterEach
    void cleanUp() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Owner").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Позитивный тест создания объекта типа Owner
     */
    @Test
    public void whenSaveAOwnerIsSuccess() {
        User user = new User();
        user.setLogin("John Doe");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();

        Owner owner = new Owner();
        owner.setName("John Doe");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        session = sf.openSession();
        session.beginTransaction();

        Owner savedOwner = session.get(Owner.class, owner.getId());

        Hibernate.initialize(savedOwner.getUser());

        session.getTransaction().commit();
        session.close();

        assertNotNull(savedOwner, "Owner should not be null");
        assertEquals(owner.getName(), savedOwner.getName(), "Owner name should match");
        assertEquals(owner.getUser().getLogin(), savedOwner.getUser().getLogin(), "User login should match");
    }

    /**
     * Негативный тест создания объекта типа Owner
     */
    @Test
    public void whenSaveAOwnerIsFail() {
        User user = new User();
        user.setLogin("John Doe");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();

        Owner owner = new Owner();
        owner.setName("John Doe");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        session = sf.openSession();
        session.beginTransaction();

        Owner savedOwner = session.get(Owner.class, owner.getId());

        Hibernate.initialize(savedOwner.getUser());

        session.getTransaction().commit();
        session.close();

        assertThat(savedOwner.getName()).isNotEqualTo("Harry Potter");
    }

    /**
     * Позитивный тест получения объекта типа Owner по id
     */
    @Test
    public void whenGetOwnerByIdIsSuccess() {
        User user = new User();
        user.setLogin("user login");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();

        Owner owner = new Owner();
        owner.setName("John Doe");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        Optional<Owner> savedOwner = ownerRepository.getOwnerById(owner.getId());

        assertTrue(savedOwner.isPresent());

        assertThat(savedOwner.get().getName()).isEqualTo("John Doe");
    }

    /**
     * Негативный тест получения объекта типа Owner по id
     */
    @Test
    public void whenGetOwnerByIdIsFail() {
        User user = new User();
        user.setLogin("user login");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();

        Owner owner = new Owner();
        owner.setName("John Doe");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        int nonExistentId = 999;
        Optional<Owner> foundOwner = ownerRepository.getOwnerById(nonExistentId);

        assertThat(foundOwner).isEmpty();
    }

    /**
     * Позитивный тест вывода всех объектов типа Owner
     */
    @Test
    public void whenGetAllOwnersIsSuccess() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        user1.setLogin("user login 1");
        user2.setLogin("user login 2");
        user3.setLogin("user login 3");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user1);
        session.save(user2);
        session.save(user3);
        session.getTransaction().commit();
        session.close();

        Owner owner1 = new Owner();
        Owner owner2 = new Owner();
        Owner owner3 = new Owner();

        owner1.setName("owner name 1");
        owner2.setName("owner name 2");
        owner3.setName("owner name 3");

        owner1.setUser(user1);
        owner2.setUser(user2);
        owner3.setUser(user3);

        ownerRepository.saveOwner(owner1);
        ownerRepository.saveOwner(owner2);
        ownerRepository.saveOwner(owner3);

        List<Owner> owners = ownerRepository.getAllOwners();
        
        assertThat(owners).hasSize(3);
        assertThat(owners).containsExactlyInAnyOrder(owner1, owner2, owner3);
    }

    /**
     * Негативный тест вывода всех объектов типа Owner
     */
    @Test
    public void whenGetAllOwnersIsFail() {
        List<Owner> owners = ownerRepository.getAllOwners();

        assertThat(owners).isEmpty();
    }

    /**
     * Позитивный тест обновления объекта типа Owner
     */
    @Test
    public void whenUpdateOwnerIsSuccess() {
        User user = new User();

        user.setLogin("user login");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();

        Owner owner = new Owner();

        owner.setName("owner name");

        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        Optional<Owner> savedOwnerOptional = ownerRepository.getOwnerById(owner.getId());

        assertThat(savedOwnerOptional).isPresent();

        Owner savedOwner = savedOwnerOptional.get();
        savedOwner.setName("Updated Test Owner");

        ownerRepository.updateOwner(savedOwner);

        Optional<Owner> updatedOwnerOptional = ownerRepository.getOwnerById(owner.getId());
        assertThat(updatedOwnerOptional).isPresent();

        Owner updatedOwner = updatedOwnerOptional.get();
        assertEquals("Updated Test Owner", updatedOwner.getName(), "Owner name should be updated");
    }

    /**
     * Негативный тест обновления объекта типа Owner
     */
    @Test
    public void whenUpdateOwnerIsFail() {
        User user = new User();

        user.setLogin("user login");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();

        Owner owner = new Owner();

        owner.setName("owner name");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        Optional<Owner> savedOwnerOptional = ownerRepository.getOwnerById(owner.getId());
        assertThat(savedOwnerOptional).isPresent();

        Owner savedOwner = savedOwnerOptional.get();

        Owner nonExistentOwner = new Owner();

        nonExistentOwner.setId(999);
        nonExistentOwner.setName("Updated Test Owner");

        assertThrows(Exception.class, () -> {
            ownerRepository.updateOwner(nonExistentOwner);
        }, "Updating a non-existent Owner should throw an exception");

        Optional<Owner> updatedOwnerOptional = ownerRepository.getOwnerById(savedOwner.getId());
        assertThat(updatedOwnerOptional).isPresent();
        Owner updatedOwner = updatedOwnerOptional.get();
        assertThat(updatedOwner.getName()).isEqualTo("owner name");
    }

    /**
     * Позитивный тест удаления объекта типа Owner по id
     */
    @Test
    public void whenDeleteOwnerIsSuccess() {
        User user = new User();

        user.setLogin("user login");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();

        Owner owner = new Owner();

        owner.setName("owner name");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        Optional<Owner> savedOwnerOptional = ownerRepository.getOwnerById(owner.getId());
        assertThat(savedOwnerOptional).isPresent();

        Owner savedOwner = savedOwnerOptional.get();

        ownerRepository.deleteOwnerById(savedOwner.getId());

        Optional<Owner> deletedOwnerOptional = ownerRepository.getOwnerById(savedOwner.getId());

        assertThat(deletedOwnerOptional).isEmpty();
    }

    /**
     * Негативный тест удаления объекта типа Owner по id
     */
    @Test
    public void whenDeleteOwnerIsFail() {
        User user = new User();

        user.setLogin("user login");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();

        Owner owner = new Owner();

        owner.setName("owner name");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        Optional<Owner> savedOwnerOptional = ownerRepository.getOwnerById(owner.getId());
        assertThat(savedOwnerOptional).isPresent();

        Owner savedOwner = savedOwnerOptional.get();

        int nonExistentId = 999;
        ownerRepository.deleteOwnerById(nonExistentId);

        Optional<Owner> deletedOwnerOptional = ownerRepository.getOwnerById(savedOwner.getId());
        assertThat(deletedOwnerOptional).isPresent();
    }
}