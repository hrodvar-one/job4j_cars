package ru.job4j.cars.repository.historyowner;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.hibernate.cfg.Configuration;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.car.CarRepository;
import ru.job4j.cars.repository.owner.OwnerRepository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HistoryOwnerRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private HistoryOwnerRepository historyOwnerRepository;
    private CarRepository carRepository;
    private OwnerRepository ownerRepository;

    @BeforeEach
    void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        historyOwnerRepository = new HistoryOwnerRepository(crudRepository);
        carRepository = new CarRepository(crudRepository);
        ownerRepository = new OwnerRepository(crudRepository);
    }

    @AfterEach
    void cleanUp() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from HistoryOwner").executeUpdate();
        session.createQuery("delete from Car").executeUpdate();
        session.createQuery("delete from Brand").executeUpdate();
        session.createQuery("delete from Engine").executeUpdate();
        session.createQuery("delete from Owner").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Позитивный тест создания объекта типа HistoryOwner
     */
    @Test
    public void whenSaveAHistoryOwnerIsSuccess() {
        Brand brand = new Brand();
        brand.setName("Test Brand");

        Engine engine = new Engine();
        engine.setName("Test Engine");

        User user = new User();
        user.setLogin("John Doe");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand);
            session.save(engine);
            session.save(user);
            session.getTransaction().commit();
        }

        Car car = new Car();
        car.setName("Test Car");
        car.setBrand(brand);
        car.setEngine(engine);

        carRepository.saveCar(car);

        Owner owner = new Owner();
        owner.setName("Test Owner");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        HistoryOwner historyOwner = new HistoryOwner();
        historyOwner.setCar(car);
        historyOwner.setOwner(owner);

        historyOwnerRepository.saveHistoryOwner(historyOwner);

        Session session = sf.openSession();
        session.beginTransaction();

        HistoryOwner savedHistoryOwner = session.get(HistoryOwner.class, historyOwner.getId());

        Hibernate.initialize(savedHistoryOwner.getCar());
        Hibernate.initialize(savedHistoryOwner.getOwner());

        session.getTransaction().commit();
        session.close();

        assertNotNull(savedHistoryOwner, "HistoryOwner should not be null");
        assertEquals(historyOwner.getId(), savedHistoryOwner.getId(), "HistoryOwner id should match");
        assertEquals(historyOwner.getCar().getName(),
                savedHistoryOwner.getCar().getName(), "Car name should match");
        assertEquals(historyOwner.getOwner().getName(),
                savedHistoryOwner.getOwner().getName(), "Owner name should match");
    }

    /**
     * Негативный тест создания объекта типа HistoryOwner
     */
    @Test
    public void whenSaveAHistoryOwnerIsFail() {
        Brand brand = new Brand();
        brand.setName("Test Brand");

        Engine engine = new Engine();
        engine.setName("Test Engine");

        User user = new User();
        user.setLogin("John Doe");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand);
            session.save(engine);
            session.save(user);
            session.getTransaction().commit();
        }

        Car car = new Car();
        car.setName("Test Car");
        car.setBrand(brand);
        car.setEngine(engine);

        carRepository.saveCar(car);

        Owner owner = new Owner();
        owner.setName("Test Owner");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        HistoryOwner historyOwner = new HistoryOwner();
        historyOwner.setCar(car);
        historyOwner.setOwner(owner);

        historyOwnerRepository.saveHistoryOwner(historyOwner);

        Long nonExistentId = 999L;

        Optional<HistoryOwner> optionalHistoryOwner = historyOwnerRepository.getHistoryOwnerById(nonExistentId);

        assertThat(optionalHistoryOwner).isEmpty();
    }

    /**
     * Позитивный тест получения объекта типа HistoryOwner по id
     */
    @Test
    public void whenGetHistoryOwnerByIdIsSuccess() {
        Brand brand = new Brand();
        brand.setName("Test Brand");

        Engine engine = new Engine();
        engine.setName("Test Engine");

        User user = new User();
        user.setLogin("John Doe");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand);
            session.save(engine);
            session.save(user);
            session.getTransaction().commit();
        }

        Car car = new Car();
        car.setName("Test Car");
        car.setBrand(brand);
        car.setEngine(engine);

        carRepository.saveCar(car);

        Owner owner = new Owner();
        owner.setName("Test Owner");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        HistoryOwner historyOwner = new HistoryOwner();
        historyOwner.setCar(car);
        historyOwner.setOwner(owner);

        historyOwnerRepository.saveHistoryOwner(historyOwner);

        Optional<HistoryOwner> savedOptionalHistoryOwner =
                historyOwnerRepository.getHistoryOwnerById(historyOwner.getId());

        assertThat(savedOptionalHistoryOwner).isPresent();

        Optional<HistoryOwner> optionalHistoryOwner =
                historyOwnerRepository.getHistoryOwnerById(savedOptionalHistoryOwner.get().getId());

        assertThat(optionalHistoryOwner).isPresent();
        assertThat(optionalHistoryOwner.get().getCar().getName()).isEqualTo("Test Car");
    }

    /**
     * Негативный тест получения объекта типа HistoryOwner по id
     */
    @Test
    public void whenGetHistoryOwnerByIdIsFail() {
        Brand brand = new Brand();
        brand.setName("Test Brand");

        Engine engine = new Engine();
        engine.setName("Test Engine");

        User user = new User();
        user.setLogin("John Doe");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand);
            session.save(engine);
            session.save(user);
            session.getTransaction().commit();
        }

        Car car = new Car();
        car.setName("Test Car");
        car.setBrand(brand);
        car.setEngine(engine);

        carRepository.saveCar(car);

        Owner owner = new Owner();
        owner.setName("Test Owner");
        owner.setUser(user);

        ownerRepository.saveOwner(owner);

        HistoryOwner historyOwner = new HistoryOwner();
        historyOwner.setCar(car);
        historyOwner.setOwner(owner);

        historyOwnerRepository.saveHistoryOwner(historyOwner);

        Long nonExistentId = 999L;
        Optional<HistoryOwner> foundOptionalHistoryOwner =
                historyOwnerRepository.getHistoryOwnerById(nonExistentId);

        assertThat(foundOptionalHistoryOwner).isEmpty();
    }

    /**
     * Позитивный тест вывода всех объектов типа HistoryOwner
     */
    @Test
    public void whenGetAllHistoryOwnersIsSuccess() {
        Brand brand1 = new Brand();
        Brand brand2 = new Brand();
        Brand brand3 = new Brand();

        brand1.setName("Test Brand 1");
        brand2.setName("Test Brand 2");
        brand3.setName("Test Brand 3");

        Engine engine1 = new Engine();
        Engine engine2 = new Engine();
        Engine engine3 = new Engine();

        engine1.setName("Test Engine 1");
        engine2.setName("Test Engine 2");
        engine3.setName("Test Engine 3");

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        user1.setLogin("John Doe 1");
        user2.setLogin("John Doe 2");
        user3.setLogin("John Doe 3");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand1);
            session.save(brand2);
            session.save(brand3);
            session.save(engine1);
            session.save(engine2);
            session.save(engine3);
            session.save(user1);
            session.save(user2);
            session.save(user3);
            session.getTransaction().commit();
        }

        Car car1 = new Car();
        Car car2 = new Car();
        Car car3 = new Car();

        car1.setName("Test Car 1");
        car2.setName("Test Car 2");
        car3.setName("Test Car 3");

        car1.setBrand(brand1);
        car2.setBrand(brand2);
        car3.setBrand(brand3);

        car1.setEngine(engine1);
        car2.setEngine(engine2);
        car3.setEngine(engine3);

        carRepository.saveCar(car1);
        carRepository.saveCar(car2);
        carRepository.saveCar(car3);

        Owner owner1 = new Owner();
        Owner owner2 = new Owner();
        Owner owner3 = new Owner();

        owner1.setName("Test Owner 1");
        owner2.setName("Test Owner 2");
        owner3.setName("Test Owner 3");

        owner1.setUser(user1);
        owner2.setUser(user2);
        owner3.setUser(user3);

        ownerRepository.saveOwner(owner1);
        ownerRepository.saveOwner(owner2);
        ownerRepository.saveOwner(owner3);

        HistoryOwner historyOwner1 = new HistoryOwner();
        HistoryOwner historyOwner2 = new HistoryOwner();
        HistoryOwner historyOwner3 = new HistoryOwner();

        historyOwner1.setCar(car1);
        historyOwner2.setCar(car2);
        historyOwner3.setCar(car3);

        historyOwner1.setOwner(owner1);
        historyOwner2.setOwner(owner2);
        historyOwner3.setOwner(owner3);

        historyOwnerRepository.saveHistoryOwner(historyOwner1);
        historyOwnerRepository.saveHistoryOwner(historyOwner2);
        historyOwnerRepository.saveHistoryOwner(historyOwner3);

        List<HistoryOwner> savedHistoryOwners = historyOwnerRepository.getAllHistoryOwner();
        
        assertThat(savedHistoryOwners).hasSize(3);
        assertThat(savedHistoryOwners).containsExactlyInAnyOrder(historyOwner1, historyOwner2, historyOwner3);
    }

    /**
     * Негативный тест вывода всех объектов типа HistoryOwner
     */
    @Test
    public void whenGetAllHistoryOwnersIsFail() {
        Brand brand1 = new Brand();
        Brand brand2 = new Brand();
        Brand brand3 = new Brand();

        brand1.setName("Test Brand 1");
        brand2.setName("Test Brand 2");
        brand3.setName("Test Brand 3");

        Engine engine1 = new Engine();
        Engine engine2 = new Engine();
        Engine engine3 = new Engine();

        engine1.setName("Test Engine 1");
        engine2.setName("Test Engine 2");
        engine3.setName("Test Engine 3");

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        user1.setLogin("John Doe 1");
        user2.setLogin("John Doe 2");
        user3.setLogin("John Doe 3");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand1);
            session.save(brand2);
            session.save(brand3);
            session.save(engine1);
            session.save(engine2);
            session.save(engine3);
            session.save(user1);
            session.save(user2);
            session.save(user3);
            session.getTransaction().commit();
        }

        Car car1 = new Car();
        Car car2 = new Car();
        Car car3 = new Car();

        car1.setName("Test Car 1");
        car2.setName("Test Car 2");
        car3.setName("Test Car 3");

        car1.setBrand(brand1);
        car2.setBrand(brand2);
        car3.setBrand(brand3);

        car1.setEngine(engine1);
        car2.setEngine(engine2);
        car3.setEngine(engine3);

        carRepository.saveCar(car1);
        carRepository.saveCar(car2);
        carRepository.saveCar(car3);

        Owner owner1 = new Owner();
        Owner owner2 = new Owner();
        Owner owner3 = new Owner();

        owner1.setName("Test Owner 1");
        owner2.setName("Test Owner 2");
        owner3.setName("Test Owner 3");

        owner1.setUser(user1);
        owner2.setUser(user2);
        owner3.setUser(user3);

        ownerRepository.saveOwner(owner1);
        ownerRepository.saveOwner(owner2);
        ownerRepository.saveOwner(owner3);

        HistoryOwner historyOwner1 = new HistoryOwner();
        HistoryOwner historyOwner2 = new HistoryOwner();
        HistoryOwner historyOwner3 = new HistoryOwner();

        historyOwner1.setCar(car1);
        historyOwner2.setCar(car2);
        historyOwner3.setCar(car3);

        historyOwner1.setOwner(owner1);
        historyOwner2.setOwner(owner2);
        historyOwner3.setOwner(owner3);

        historyOwnerRepository.saveHistoryOwner(historyOwner1);
        historyOwnerRepository.saveHistoryOwner(historyOwner2);
        historyOwnerRepository.saveHistoryOwner(historyOwner3);

        List<HistoryOwner> savedHistoryOwners = historyOwnerRepository.getAllHistoryOwner();

        AssertionError error = assertThrows(AssertionError.class, () -> {
            assertThat(savedHistoryOwners).hasSize(2);
        });

        assertThat(error.getMessage()).contains("Expected size: 2 but was: 3");
    }
}