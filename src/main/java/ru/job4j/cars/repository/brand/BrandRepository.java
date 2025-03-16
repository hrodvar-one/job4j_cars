package ru.job4j.cars.repository.brand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BrandRepository {

    private final CrudRepository crudRepository;

    public List<Brand> getAllBrands() {
        return crudRepository.query("from Brand", Brand.class);
    }

    public void saveBrand(Brand brand) {
        crudRepository.run(session -> session.persist(brand));
    }

    public Optional<Brand> getBrandById(Integer id) {
        return crudRepository.optional(
                "select b from Brand b where b.id = :fId", Brand.class,
                Map.of("fId", id)
        );
    }

    public void updateBrand(Brand brand) {
        crudRepository.run(session -> session.merge(brand));
    }

    public void deleteBrandById(Integer id) {
        crudRepository.run(
                "delete from Brand where id = :fId",
                Map.of("fId", id)
        );
    }
}
