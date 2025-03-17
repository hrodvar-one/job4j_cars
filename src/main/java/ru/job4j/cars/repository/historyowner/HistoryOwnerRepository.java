package ru.job4j.cars.repository.historyowner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.HistoryOwner;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HistoryOwnerRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить список всех историй владения автомобилей.
     * @return Список всех историй владения автомобилей.
     */
    public List<HistoryOwner> getAllHistoryOwner() {
        return crudRepository.query(
                "FROM HistoryOwner",
                HistoryOwner.class
        );
    }

    /**
     * Сохранить историю владения автомобилем.
     */
    public void saveHistoryOwner(HistoryOwner historyOwner) {
        crudRepository.run(session -> session.persist(historyOwner));
    }

    /**
     * Получить историю владения автомобилем по ID.
     * @param id ID историй владения.
     * @return История владения автомобилем по ID. Если не найдена, то Optional.empty().
     */
    public Optional<HistoryOwner> getHistoryOwnerById(Integer id) {
        return crudRepository.optional(
                "select ho from HistoryOwner ho where ho.id = :fId", HistoryOwner.class,
                Map.of("fId", id)
        );
    }
}
