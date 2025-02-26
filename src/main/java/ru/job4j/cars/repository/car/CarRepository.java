package ru.job4j.cars.repository.car;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarRepository {

    private final CrudRepository crudRepository;

    public Optional<Car> getCarById(int id) {
        return crudRepository.optional(
                "FROM Car WHERE id = :id",
                Car.class,
                Map.of("id", id)
        );
    }

    public List<Car> getAllCars() {
        return crudRepository.query(
                "FROM Car",
                Car.class
        );
    }

    public void save(Car car) {
        crudRepository.run(session -> session.save(car));
    }

    public void update(Car car) {
        crudRepository.run(session -> session.update(car));
    }

    public void deleteCarById(int id) {
        crudRepository.run(
                "DELETE FROM Car WHERE id = :id",
                Map.of("id", id)
        );
    }
}
