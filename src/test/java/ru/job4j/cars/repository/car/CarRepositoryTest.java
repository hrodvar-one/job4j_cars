package ru.job4j.cars.repository.car;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.hibernate.cfg.Configuration;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CrudRepository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CarRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private CarRepository carRepository;

    @BeforeEach
    public void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        carRepository = new CarRepository(crudRepository);
    }

    @AfterEach
    void cleanUp() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Car").executeUpdate();
        session.createQuery("delete from HistoryOwner").executeUpdate();
        session.createQuery("delete from Engine").executeUpdate();
        session.createQuery("delete from Brand").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Позитивный тест создания объекта типа Car
     */
    @Test
    public void whenSaveACarThenSuccess() {

        Brand brand = new Brand();
        brand.setName("Test Brand");

        Engine engine = new Engine();
        engine.setName("Test Engine");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(brand);
        session.save(engine);
        session.getTransaction().commit();
        session.close();

        Car car = new Car();
        car.setName("Test Car");
        car.setBrand(brand);
        car.setEngine(engine);

        carRepository.saveCar(car);

        session = sf.openSession();
        session.beginTransaction();

        Car savedCar = session.get(Car.class, car.getId());

        Hibernate.initialize(savedCar.getEngine());
        Hibernate.initialize(savedCar.getBrand());

        session.getTransaction().commit();
        session.close();

        assertNotNull(savedCar, "Car should not be null");
        assertEquals(car.getName(), savedCar.getName(), "Car name should match");
        assertEquals(car.getBrand().getName(), savedCar.getBrand().getName(), "Brand name should match");
        assertEquals(car.getEngine().getName(), savedCar.getEngine().getName(), "Engine name should match");
    }

    /**
     * Негативный тест создания объекта типа Car
     */
    @Test
    public void whenSaveACarThenReturnException() {

        Car car = new Car();
        car.setName("Test Car");

        Exception exception = assertThrows(Exception.class, () -> {
            carRepository.saveCar(car);
        });

        assertNotNull(exception, "Exception should be thrown when saving Car without Brand and Engine");
    }

    /**
     * Позитивный тест получения объекта типа Car по id
     */
    @Test
    public void whenGetCarByIdThenReturnTheSameCar() {

        Brand brand = new Brand();
        brand.setName("Test Brand");

        Engine engine = new Engine();
        engine.setName("Test Engine");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand);
            session.save(engine);
            session.getTransaction().commit();
        }

        Car car = new Car();
        car.setName("Test Car");
        car.setBrand(brand);
        car.setEngine(engine);

        carRepository.saveCar(car);

        Optional<Car> savedCar = carRepository.getCarById(car.getId());

        assertTrue(savedCar.isPresent(), "Car should be present in the database");
        assertEquals(car.getId(), savedCar.get().getId(), "Car ID should match");
    }

    /**
     * Негативный тест получения объекта типа Car по id
     */
    @Test
    public void whenGetCarByIdThenReturnNull() {
        Optional<Car> savedCar = carRepository.getCarById(-1);

        assertFalse(savedCar.isPresent(), "Car should not be present in the database.");
    }

    /**
     *  Позитивный тест получения всех объектов типа Car
     */
    @Test
    public void whenGetAllCarsThenReturnAllCars() {

        Brand brand1 = new Brand();
        Brand brand2 = new Brand();
        Brand brand3 = new Brand();

        brand1.setName("Test Brand 1");
        brand2.setName("Test Brand 2");
        brand3.setName("Test Brand 3");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand1);
            session.save(brand2);
            session.save(brand3);
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

        carRepository.saveCar(car1);
        carRepository.saveCar(car2);
        carRepository.saveCar(car3);

        List<Car> cars = carRepository.getAllCars();

        assertThat(cars).hasSize(3);
        assertThat(cars).containsExactlyInAnyOrder(car1, car2, car3);
    }

    /**
     * Негативный тест получения всех объектов типа Car
     */
    @Test
    public void whenGetAllCarsThenReturnEmptyList() {
        List<Car> cars = carRepository.getAllCars();

        assertThat(cars).isEmpty();
    }

    /**
     * Позитивный тест обновления объекта типа Car
     */
    @Test
    public void whenUpdateCarThenReturnTheSameCar() {

        Brand brand1 = new Brand();
        Brand brand2 = new Brand();

        brand1.setName("Test Brand 1");
        brand2.setName("Test Brand 2");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand1);
            session.save(brand2);
            session.getTransaction().commit();
        }

        Car car = new Car();

        car.setName("Test Car");
        car.setBrand(brand1);

        carRepository.saveCar(car);

        Optional<Car> savedCarOptional = carRepository.getCarById(car.getId());

        assertThat(savedCarOptional).isPresent();

        Car savedCar = savedCarOptional.get();
        savedCar.setName("Updated Test Car");
        savedCar.setBrand(brand2);

        carRepository.updateCar(savedCar);

        Optional<Car> updatedCarOptional = carRepository.getCarById(car.getId());
        assertThat(updatedCarOptional).isPresent();

        Car updatedCar = updatedCarOptional.get();
        assertEquals("Updated Test Car", updatedCar.getName(), "Car name should be updated");
        assertEquals(brand2.getName(), updatedCar.getBrand().getName(), "Brand should be updated");
    }

    /**
     * Негативный тест обновления объекта типа Car
     */
    @Test
    public void whenUpdateCarIsFail() {

        Brand brand = new Brand();

        brand.setName("Test Brand");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand);
            session.getTransaction().commit();
        }

        Car car = new Car();

        car.setName("Test Car");
        car.setBrand(brand);

        carRepository.saveCar(car);

        Optional<Car> savedCarOptional = carRepository.getCarById(car.getId());
        assertThat(savedCarOptional).isPresent();

        Car savedCar = savedCarOptional.get();

        Car nonExistentCar = new Car();

        nonExistentCar.setId(999);
        nonExistentCar.setName("Updated Test Car");
        nonExistentCar.setBrand(brand);

        assertThrows(Exception.class, () -> {
            carRepository.updateCar(nonExistentCar);
        }, "Updating a non-existent Car should throw an exception");

        Optional<Car> updatedCarOptional = carRepository.getCarById(savedCar.getId());
        assertThat(updatedCarOptional).isPresent();
        Car updatedCar = updatedCarOptional.get();
        assertThat(updatedCar.getName()).isEqualTo("Test Car");
    }

    /**
     * Позитивный тест удаления объекта типа Car
     */
    @Test
    public void whenDeleteCarIsSuccess() {

        Brand brand = new Brand();

        brand.setName("Test Brand");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand);
            session.getTransaction().commit();
        }

        Car car = new Car();

        car.setName("Test Car");
        car.setBrand(brand);

        carRepository.saveCar(car);

        Optional<Car> savedCarOptional = carRepository.getCarById(car.getId());
        assertThat(savedCarOptional).isPresent();

        Car savedCar = savedCarOptional.get();

        carRepository.deleteCarById(savedCar.getId());

        Optional<Car> deletedCarOptional = carRepository.getCarById(savedCar.getId());

        assertThat(deletedCarOptional).isEmpty();
    }

    /**
     * Негативный тест удаления объекта типа Car
     */
    @Test
    public void whenDeleteCarIsFail() {

        Brand brand = new Brand();

        brand.setName("Test Brand");

        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(brand);
            session.getTransaction().commit();
        }

        Car car = new Car();

        car.setName("Test Car");
        car.setBrand(brand);

        carRepository.saveCar(car);

        Optional<Car> savedCarOptional = carRepository.getCarById(car.getId());
        assertThat(savedCarOptional).isPresent();

        Car savedCar = savedCarOptional.get();

        Integer nonExistentId = 999;
        carRepository.deleteCarById(nonExistentId);

        Optional<Car> deletedCarOptional = carRepository.getCarById(savedCar.getId());
        assertThat(deletedCarOptional).isPresent();
    }
}