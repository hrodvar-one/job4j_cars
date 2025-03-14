package ru.job4j.cars.repository.historyowner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.HistoryOwner;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;

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
}
