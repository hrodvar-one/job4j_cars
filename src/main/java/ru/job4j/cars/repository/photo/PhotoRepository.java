package ru.job4j.cars.repository.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Photo;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PhotoRepository {

    private final CrudRepository crudRepository;

    public List<Photo> getAllPhotos() {
        return crudRepository.query("from Photo", Photo.class);
    }

    public void savePhoto(Photo photo) {
        crudRepository.run(session -> session.persist(photo));
    }

    public Optional<Photo> getPhotoById(Integer id) {
        return crudRepository.optional(
                "select p from Photo p where p.id = :id",
                Photo.class,
                Map.of("id", id)
        );
    }

    public void updatePhoto(Photo photo) {
        crudRepository.run(session -> session.merge(photo));
    }

    public void deletePhotoById(Integer id) {
        crudRepository.run(
                "delete from Photo p where p.id = :id",
                Map.of("id", id)
        );
    }
}
