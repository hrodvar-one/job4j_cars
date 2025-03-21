package ru.job4j.cars.repository.pricehistory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PriceHistoryRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить список всех историй цены автомобилей.
     * @return Список всех историй цены автомобилей.
     */
    public List<PriceHistory> getAllPriceHistory() {
        return crudRepository.query(
                "FROM PriceHistory",
                PriceHistory.class
        );
    }

    /**
     * Сохранить историю цены автомобиля.
     */
    public void savePriceHistory(PriceHistory priceHistory) {
        crudRepository.run(session -> session.persist(priceHistory));
    }

    /**
     * Получить историю цены автомобиля по ID.
     * @param id ID истории цены автомобиля.
     * @return История цены автомобиля по ID. Если не найдена, то Optional.empty().
     */
    public Optional<PriceHistory> getPriceHistoryById(Long id) {
        return crudRepository.optional(
                "select p from PriceHistory p where p.id = :id", PriceHistory.class,
                Map.of("id", id)
        );
    }
}
