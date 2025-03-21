package ru.job4j.cars.repository.participates;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Participates;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ParticipatesRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить список всех Participates.
     */
    public List<Participates> getAllParticipates() {
        return crudRepository.query(
                "FROM Participates",
                Participates.class
        );
    }

    /**
     * Сохранить Participates.
     */
    public void saveParticipates(Participates participates) {
        crudRepository.run(session -> session.persist(participates));
    }

    /**
     * Получить Participates по ID.
     */
    public Optional<Participates> getParticipatesById(int id) {
        return crudRepository.optional(
                "FROM Participates WHERE id = :fId", Participates.class,
                Map.of("fId", id)
        );
    }
}
