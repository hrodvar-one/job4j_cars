package ru.job4j.cars.repository.owner;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OwnerRepository {

    private final CrudRepository crudRepository;

    public Optional<Owner> getOwnerById(int id) {
        return crudRepository.optional(
                "FROM Owner WHERE id = :id",
                Owner.class,
                Map.of("id", id)
        );
    }

    public List<Owner> getAllOwners() {
        return crudRepository.query(
                "FROM Owner",
                Owner.class
        );
    }

    public void saveOwner(Owner owner) {
        crudRepository.run(session -> session.save(owner));
    }

    public void updateOwner(Owner owner) {
        crudRepository.run(session -> session.update(owner));
    }

    public void deleteOwnerById(int id) {
        crudRepository.run(
                "DELETE FROM Owner WHERE id = :id",
                Map.of("id", id)
        );
    }
}
