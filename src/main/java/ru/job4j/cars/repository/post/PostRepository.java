package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить пост
     * @param post передаем новый пост
     */
    public void savePost(Post post) {
        crudRepository.run(session -> session.persist(post));
    }

    /**
     * Получить Post по id
     */
    public Optional<Post> getPostById(Long id) {
        return crudRepository.optional(
                "select p from Post p where p.id = :id", Post.class,
                Map.of("id", id));
    }

    /**
     * Получить все объявления.
     */
    public List<Post> getAllPosts() {
        return crudRepository.query(
                "FROM Post",
                Post.class
        );
    }

    /**
     * Получить все объявления за последний день.
     * @return список объявлений.
     */
    public List<Post> getAllPostsForLastDay() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        return crudRepository.query(
                "FROM Post p WHERE p.created >= :yesterday ORDER BY p.id DESC",
                Post.class,
                Map.of("yesterday", yesterday)
        );
    }

    /**
     * Получить все объявления с фото.
     * @return список объявлений.
     */
    public List<Post> getAllPostsWithPhoto() {

        return crudRepository.query(
                "SELECT p FROM Post p WHERE SIZE(p.photos) > 0 ORDER BY p.id DESC",
                Post.class
        );
    }

    /**
     * Получить все объявления с определенной маркой авто.
     * @param brandName имя брэнда авто.
     * @return список объявлений.
     */
    public List<Post> getAllPostsWithSpecificCarBrand(String brandName) {

        return crudRepository.query(
                "SELECT p FROM Post p WHERE p.car.brand.name = :brandName ORDER BY p.id DESC",
                Post.class,
                Map.of("brandName", brandName)
        );
    }

    /**
     * Обновить Post.
     */
    public void updatePost(Post post) {
        crudRepository.run(session -> session.merge(post));
    }

    /**
     * Удалить Post.
     */
    public void deletePostById(Long id) {
        crudRepository.run(
                "DELETE FROM Post p WHERE p.id = :id",
                Map.of("id", id)
        );
    }
}
