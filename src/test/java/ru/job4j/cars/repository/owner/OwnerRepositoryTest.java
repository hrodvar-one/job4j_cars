package ru.job4j.cars.repository.owner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

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
        Owner owner = new Owner();
        owner.setName("John Doe");
        owner.setUser(new User());

        ownerRepository.saveOwner(owner);

    }
}