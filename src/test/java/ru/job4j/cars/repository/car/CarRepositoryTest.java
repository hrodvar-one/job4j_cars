package ru.job4j.cars.repository.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.cars.model.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager.createQuery("DELETE FROM HistoryOwner").executeUpdate();
        entityManager.createQuery("DELETE FROM Car").executeUpdate();
        entityManager.createQuery("DELETE FROM Engine").executeUpdate();
        entityManager.createQuery("DELETE FROM Brand").executeUpdate();
    }

    @Test
    public void testGetCarById_WhenCarExists() {

        Brand brand = new Brand();
        brand.setName("Toyota");
        entityManager.persist(brand);

        Engine engine = new Engine();
        engine.setName("V6");
        entityManager.persist(engine);

        User user = new User();
        user.setLogin("John Doe");

        Owner owner = new Owner();
        owner.setName("John Doe");
        owner.setUser(new User());

        Car car = new Car();
        car.setName("Corolla");
        car.setBrand(brand);
        car.setEngine(engine);
        entityManager.persist(car);

        HistoryOwner historyOwner = new HistoryOwner();
        historyOwner.setCar(car);
//        historyOwner.setOwnerName("John Doe");
        entityManager.persist(historyOwner);

        entityManager.flush();

        Optional<Car> result = carRepository.getCarById(car.getId());

        assertTrue(result.isPresent(), "Car должен быть найден");
        Car foundCar = result.get();
        assertEquals("Corolla", foundCar.getName(), "Название автомобиля должно быть Corolla");
        assertEquals("Toyota", foundCar.getBrand().getName(), "Бренд должен быть Toyota");
        assertEquals("V6", foundCar.getEngine().getName(), "Двигатель должен быть V6");
        assertEquals(1, foundCar.getHistoryOwners().size(), "Должен быть один владелец");
//        assertEquals("John Doe", foundCar.getHistoryOwners().iterator().next().getOwnerName(), "Имя владельца должно быть John Doe");
    }
}