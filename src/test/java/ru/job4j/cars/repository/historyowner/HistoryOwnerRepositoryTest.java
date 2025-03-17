package ru.job4j.cars.repository.historyowner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.hibernate.cfg.Configuration;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.cars.repository.CrudRepository;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HistoryOwnerRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private HistoryOwnerRepository historyOwnerRepository;

    @BeforeEach
    void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        historyOwnerRepository = new HistoryOwnerRepository(crudRepository);
    }

    @AfterEach
    void cleanUp() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Car").executeUpdate();
        session.createQuery("delete from Owner").executeUpdate();
        session.createQuery("delete from HistoryOwner").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Позитивный тест вывода всех историй владельцев
     */
    @Test
    void whenGetAllHistoryOwnersThenSuccess() {

    }
}