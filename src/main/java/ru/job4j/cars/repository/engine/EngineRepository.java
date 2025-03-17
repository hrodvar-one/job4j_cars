package ru.job4j.cars.repository.engine;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EngineRepository {

    private final CrudRepository crudRepository;

    public Optional<Engine> getEngineById(int id) {
        return crudRepository.optional(
                "FROM Engine WHERE id = :id",
                Engine.class,
                Map.of("id", id)
        );
    }

    public List<Engine> getAllEngines() {
        return crudRepository.query(
                "FROM Engine",
                Engine.class
        );
    }

    public void saveEngine(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
    }

    public void update(Engine engine) {
        crudRepository.run(session -> session.merge(engine));
    }

    public void deleteEngineById(int id) {
        crudRepository.run(
                "DELETE FROM Engine WHERE id = :id",
                Map.of("id", id)
        );
    }
}
