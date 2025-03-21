package ru.job4j.cars.repository.post;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.brand.BrandRepository;
import ru.job4j.cars.repository.car.CarRepository;
import ru.job4j.cars.repository.photo.PhotoRepository;
import ru.job4j.cars.repository.user.UserRepository;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private PostRepository postRepository;
    private CarRepository carRepository;
    private BrandRepository brandRepository;
    private UserRepository userRepository;
    private PhotoRepository photoRepository;

    @BeforeEach
    public void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        postRepository = new PostRepository(crudRepository);
        carRepository = new CarRepository(crudRepository);
        brandRepository = new BrandRepository(crudRepository);
        userRepository = new UserRepository(crudRepository);
        photoRepository = new PhotoRepository(crudRepository);
    }

    @AfterEach
    void cleanUp() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Photo").executeUpdate();
        session.createQuery("delete from Post").executeUpdate();
        session.createQuery("delete from Car").executeUpdate();
        session.createQuery("delete from Brand").executeUpdate();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Позитивный тест создания объекта типа Post
     */
    @Test
    void whenCreatePostIsSuccess() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post = new Post();
        post.setDescription("description");
        post.setCar(optionalSavedCar.get());
        post.setUser(optionalSavedUser.get());

        postRepository.savePost(post);

        Session session = sf.openSession();
        session.beginTransaction();

        Post savedPost = session.get(Post.class, post.getId());

        session.getTransaction().commit();

        session.close();

        assertNotNull(savedPost, "Post should not be null");
        assertEquals(post.getDescription(), savedPost.getDescription(), "Post description should match");
    }

    /**
     * Негативный тест создания объекта типа Post
     */
    @Test
    public void whenCreatePostIsFail() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post = new Post();
        post.setDescription("description");
        post.setCar(optionalSavedCar.get());
        post.setUser(optionalSavedUser.get());

        postRepository.savePost(post);

        Session session = sf.openSession();
        session.beginTransaction();

        Post savedPost = session.get(Post.class, post.getId());

        session.getTransaction().commit();

        session.close();

        assertNotNull(savedPost, "Post should not be null");
        assertNotEquals("description2", savedPost.getDescription(), "Post description should match");
    }

    /**
     * Позитивный тест получения всех объектов типа Post
     */
    @Test
    public void whenGetAllPostsIsSuccess() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post1 = new Post();
        Post post2 = new Post();

        post1.setDescription("description1");
        post2.setDescription("description2");

        post1.setCar(optionalSavedCar.get());
        post2.setCar(optionalSavedCar.get());

        post1.setUser(optionalSavedUser.get());
        post2.setUser(optionalSavedUser.get());

        postRepository.savePost(post1);
        postRepository.savePost(post2);

        Session session = sf.openSession();
        session.beginTransaction();

        Post savedPost1 = session.get(Post.class, post1.getId());
        Post savedPost2 = session.get(Post.class, post2.getId());

        session.getTransaction().commit();

        session.close();

        List<Post> posts = postRepository.getAllPosts();

        assertThat(posts).hasSize(2);
        assertThat(posts).containsExactlyInAnyOrder(savedPost1, savedPost2);
    }

    /**
     * Негативный тест получения всех объектов типа Post
     */
    @Test
    public void whenGetAllPostsIsFail() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post1 = new Post();
        Post post2 = new Post();

        post1.setDescription("description1");
        post2.setDescription("description2");

        post1.setCar(optionalSavedCar.get());
        post2.setCar(optionalSavedCar.get());

        post1.setUser(optionalSavedUser.get());
        post2.setUser(optionalSavedUser.get());

        postRepository.savePost(post1);
        postRepository.savePost(post2);

        List<Post> posts = postRepository.getAllPosts();

        AssertionError error = assertThrows(AssertionError.class, () -> {
            assertThat(posts).hasSize(3);
        });

        assertThat(error.getMessage()).contains("Expected size: 3 but was: 2");
    }

    /**
     * Позитивный тест получения объекта типа Post по id
     */
    @Test
    public void whenGetPostByIdIsSuccess() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post = new Post();
        post.setDescription("description");
        post.setCar(optionalSavedCar.get());
        post.setUser(optionalSavedUser.get());

        postRepository.savePost(post);

        Session session = sf.openSession();
        session.beginTransaction();
        Post savedPost = session.get(Post.class, post.getId());
        session.getTransaction().commit();
        session.close();

        Optional<Post> optionalFoundPost = postRepository.getPostById(savedPost.getId());

        assertThat(optionalFoundPost).isPresent();
        assertThat(optionalFoundPost.get().getDescription()).isEqualTo("description");
    }

    /**
     * Негативный тест получения объекта типа Post по id
     */
    @Test
    public void whenGetPostByIdIsFail() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post = new Post();
        post.setDescription("description");
        post.setCar(optionalSavedCar.get());
        post.setUser(optionalSavedUser.get());

        postRepository.savePost(post);

        Session session = sf.openSession();
        session.beginTransaction();
        Post savedPost = session.get(Post.class, post.getId());
        session.getTransaction().commit();
        session.close();

        Optional<Post> optionalFoundPost = postRepository.getPostById(savedPost.getId());

        assertThat(optionalFoundPost).isPresent();
        assertThat(optionalFoundPost.get().getDescription()).isNotEqualTo("description2");
    }

    /**
     * Позитивный тест обновления объекта типа Post
     */
    @Test
    public void whenUpdatePostIsSuccess() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post = new Post();
        post.setDescription("description");
        post.setCar(optionalSavedCar.get());
        post.setUser(optionalSavedUser.get());

        postRepository.savePost(post);

        Optional<Post> optionalSavedPost = postRepository.getPostById(post.getId());
        assertThat(optionalSavedPost).isPresent();

        Post savedPost = optionalSavedPost.get();

        savedPost.setDescription("updated description");

        postRepository.updatePost(savedPost);

        Optional<Post> optionalUpdatedPost = postRepository.getPostById(savedPost.getId());

        assertThat(optionalUpdatedPost).isPresent();
        Post updatedPost = optionalUpdatedPost.get();
        assertThat(updatedPost.getDescription()).isEqualTo("updated description");
    }

    /**
     * Негативный тест обновления объекта типа Post
     */
    @Test
    public void whenUpdatePostIsFail() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post = new Post();
        post.setDescription("description");
        post.setCar(optionalSavedCar.get());
        post.setUser(optionalSavedUser.get());

        postRepository.savePost(post);

        Optional<Post> optionalSavedPost = postRepository.getPostById(post.getId());
        assertThat(optionalSavedPost).isPresent();

        Post savedPost = optionalSavedPost.get();

        Post nonExistentPost = new Post();
        nonExistentPost.setId(999L);
        nonExistentPost.setDescription("description2");
        nonExistentPost.setCar(optionalSavedCar.get());
        nonExistentPost.setUser(optionalSavedUser.get());

        postRepository.updatePost(nonExistentPost);

        Optional<Post> optionalUpdatedPost = postRepository.getPostById(savedPost.getId());

        assertThat(optionalUpdatedPost).isPresent();
        Post updatedPost = optionalUpdatedPost.get();
        assertThat(updatedPost.getDescription()).isEqualTo("description");
    }

    /**
     * Позитивный тест удаления объекта типа Post
     */
    @Test
    public void whenDeletePostByIdIsSuccess() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post = new Post();
        post.setDescription("description");
        post.setCar(optionalSavedCar.get());
        post.setUser(optionalSavedUser.get());

        postRepository.savePost(post);

        Optional<Post> optionalSavedPost = postRepository.getPostById(post.getId());
        assertThat(optionalSavedPost).isPresent();

        Post savedPost = optionalSavedPost.get();

        postRepository.deletePostById(savedPost.getId());

        Optional<Post> optionalDeletedPost = postRepository.getPostById(savedPost.getId());

        assertThat(optionalDeletedPost).isEmpty();
    }

    /**
     * Негативный тест удаления объекта типа Post
     */
    @Test
    public void whenDeletePostByIdIsFail() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post = new Post();
        post.setDescription("description");
        post.setCar(optionalSavedCar.get());
        post.setUser(optionalSavedUser.get());

        postRepository.savePost(post);

        Optional<Post> optionalSavedPost = postRepository.getPostById(post.getId());
        assertThat(optionalSavedPost).isPresent();

        Post savedPost = optionalSavedPost.get();

        Long nonExistentId = 999L;
        postRepository.deletePostById(nonExistentId);

        Optional<Post> remainingOptionalPost = postRepository.getPostById(savedPost.getId());
        assertThat(remainingOptionalPost).isPresent();
    }

    /**
     * Позитивный тест получения всех объектов типа Post за последний день
     */
    @Test
    public void whenGetAllPostsForLastDayIsSuccess() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post1 = new Post();
        Post post2 = new Post();

        post1.setDescription("description1");
        post2.setDescription("description2");

        post1.setCar(optionalSavedCar.get());
        post2.setCar(optionalSavedCar.get());

        post1.setUser(optionalSavedUser.get());
        post2.setUser(optionalSavedUser.get());

        post1.setCreated(LocalDateTime.now().minusDays(2));

        postRepository.savePost(post1);
        postRepository.savePost(post2);

        Session session = sf.openSession();
        session.beginTransaction();

        Post savedPost2 = session.get(Post.class, post2.getId());

        session.getTransaction().commit();

        session.close();

        List<Post> posts = postRepository.getAllPostsForLastDay();

        assertThat(posts).hasSize(1);
        assertThat(posts).containsExactlyInAnyOrder(savedPost2);
    }

    /**
     * Негативный тест получения всех объектов типа Post за последний день
     */
    @Test
    public void whenGetAllPostsForLastDayIsFail() {
        User user = new User();

        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post1 = new Post();
        Post post2 = new Post();

        post1.setDescription("description1");
        post2.setDescription("description2");

        post1.setCar(optionalSavedCar.get());
        post2.setCar(optionalSavedCar.get());

        post1.setUser(optionalSavedUser.get());
        post2.setUser(optionalSavedUser.get());

        post1.setCreated(LocalDateTime.now().minusHours(12));
        post1.setCreated(LocalDateTime.now().minusHours(5));

        postRepository.savePost(post1);
        postRepository.savePost(post2);

        Session session = sf.openSession();
        session.beginTransaction();

        Post savedPost1 = session.get(Post.class, post1.getId());
        Post savedPost2 = session.get(Post.class, post2.getId());

        session.getTransaction().commit();

        session.close();

        List<Post> posts = postRepository.getAllPostsForLastDay();

        assertThat(posts).hasSize(2);
        assertThat(posts).containsExactlyInAnyOrder(savedPost1, savedPost2);
    }

    /**
     * Позитивный тест получения всех объектов типа Post с фото
     */
    @Test
    public void whenGetAllPostsWithPhotoIsSuccess() {
        User user = new User();
        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post1 = new Post();
        post1.setDescription("description1");
        post1.setCar(optionalSavedCar.get());
        post1.setUser(optionalSavedUser.get());

        Post post2 = new Post();
        post2.setDescription("description2");
        post2.setCar(optionalSavedCar.get());
        post2.setUser(optionalSavedUser.get());

        postRepository.savePost(post1);
        postRepository.savePost(post2);

        Optional<Post> optionalSavedPost1 = postRepository.getPostById(post1.getId());
        Optional<Post> optionalSavedPost2 = postRepository.getPostById(post2.getId());

        assertThat(optionalSavedPost1).isPresent();
        assertThat(optionalSavedPost2).isPresent();

        Photo photo1 = new Photo();
        photo1.setPath("path/to/photo1.jpg");
        photo1.setPost(optionalSavedPost1.get());

        Photo photo2 = new Photo();
        photo2.setPath("path/to/photo2.jpg");
        photo2.setPost(optionalSavedPost2.get());

        photoRepository.savePhoto(photo1);
        photoRepository.savePhoto(photo2);

        Session session = sf.openSession();
        session.beginTransaction();

        Post savedPost1 = session.get(Post.class, post1.getId());
        Post savedPost2 = session.get(Post.class, post2.getId());

        session.getTransaction().commit();

        session.close();

        List<Post> posts = postRepository.getAllPostsWithPhoto();

        assertThat(posts).hasSize(2);
        assertThat(posts).containsExactlyInAnyOrder(savedPost1, savedPost2);
    }

    /**
     * Негативный тест получения всех объектов типа Post с фото
     */
    @Test
    public void whenGetAllPostsWithPhotoIsFail() {
        User user = new User();
        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> optionalSavedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(optionalSavedBrand).isPresent();

        Car car = new Car();
        car.setName("BMW");
        car.setBrand(optionalSavedBrand.get());

        carRepository.saveCar(car);

        Optional<Car> optionalSavedCar = carRepository.getCarById(car.getId());

        assertThat(optionalSavedCar).isPresent();

        Post post1 = new Post();
        post1.setDescription("description1");
        post1.setCar(optionalSavedCar.get());
        post1.setUser(optionalSavedUser.get());

        Post post2 = new Post();
        post2.setDescription("description2");
        post2.setCar(optionalSavedCar.get());
        post2.setUser(optionalSavedUser.get());

        postRepository.savePost(post1);
        postRepository.savePost(post2);

        Optional<Post> optionalSavedPost1 = postRepository.getPostById(post1.getId());
        Optional<Post> optionalSavedPost2 = postRepository.getPostById(post2.getId());

        assertThat(optionalSavedPost1).isPresent();
        assertThat(optionalSavedPost2).isPresent();

        Photo photo1 = new Photo();
        photo1.setPath("path/to/photo1.jpg");
        photo1.setPost(optionalSavedPost1.get());

        Photo photo2 = new Photo();
        photo2.setPath("path/to/photo2.jpg");
        photo2.setPost(optionalSavedPost2.get());

        photoRepository.savePhoto(photo1);
        photoRepository.savePhoto(photo2);

        List<Post> posts = postRepository.getAllPostsWithPhoto();

        AssertionError error = assertThrows(AssertionError.class, () -> {
            assertThat(posts).hasSize(3);
        });

        assertThat(error.getMessage()).contains("Expected size: 3 but was: 2");
    }

    /**
     * Позитивный тест получения всех объектов типа Post со специфическим брендом автомобиля
     */
    @Test
    public void whenGetAllPhotosByBrandIsSuccess() {
        User user = new User();
        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand1 = new Brand();
        Brand brand2 = new Brand();

        brand1.setName("BMW");
        brand2.setName("Audi");

        brandRepository.saveBrand(brand1);
        brandRepository.saveBrand(brand2);

        Optional<Brand> optionalSavedBrand1 = brandRepository.getBrandById(brand1.getId());
        Optional<Brand> optionalSavedBrand2 = brandRepository.getBrandById(brand2.getId());

        assertThat(optionalSavedBrand1).isPresent();
        assertThat(optionalSavedBrand2).isPresent();

        Car car1 = new Car();
        Car car2 = new Car();

        car1.setName("X5");
        car2.setName("A6");

        car1.setBrand(optionalSavedBrand1.get());
        car2.setBrand(optionalSavedBrand2.get());

        carRepository.saveCar(car1);
        carRepository.saveCar(car2);

        Optional<Car> optionalSavedCar1 = carRepository.getCarById(car1.getId());
        Optional<Car> optionalSavedCar2 = carRepository.getCarById(car2.getId());

        assertThat(optionalSavedCar1).isPresent();
        assertThat(optionalSavedCar2).isPresent();

        Post post1 = new Post();
        post1.setDescription("description1");
        post1.setCar(optionalSavedCar1.get());
        post1.setUser(optionalSavedUser.get());

        Post post2 = new Post();
        post2.setDescription("description2");
        post2.setCar(optionalSavedCar2.get());
        post2.setUser(optionalSavedUser.get());

        postRepository.savePost(post1);
        postRepository.savePost(post2);

        Optional<Post> optionalSavedPost1 = postRepository.getPostById(post1.getId());
        Optional<Post> optionalSavedPost2 = postRepository.getPostById(post2.getId());

        assertThat(optionalSavedPost1).isPresent();
        assertThat(optionalSavedPost2).isPresent();

        Session session = sf.openSession();
        session.beginTransaction();

        Post savedPost1 = session.get(Post.class, post1.getId());

        session.getTransaction().commit();

        session.close();

        List<Post> posts = postRepository.getAllPostsWithSpecificCarBrand("BMW");

        assertThat(posts).hasSize(1);
        assertThat(posts).containsExactlyInAnyOrder(savedPost1);
    }

    /**
     * Негативный тест получения всех объектов типа Post со специфическим брендом автомобиля
     */
    @Test
    public void whenGetAllPhotosByBrandIsFail() {
        User user = new User();
        user.setLogin("test");

        userRepository.create(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();

        Brand brand1 = new Brand();
        Brand brand2 = new Brand();

        brand1.setName("BMW");
        brand2.setName("Audi");

        brandRepository.saveBrand(brand1);
        brandRepository.saveBrand(brand2);

        Optional<Brand> optionalSavedBrand1 = brandRepository.getBrandById(brand1.getId());
        Optional<Brand> optionalSavedBrand2 = brandRepository.getBrandById(brand2.getId());

        assertThat(optionalSavedBrand1).isPresent();
        assertThat(optionalSavedBrand2).isPresent();

        Car car1 = new Car();
        Car car2 = new Car();

        car1.setName("X5");
        car2.setName("A6");

        car1.setBrand(optionalSavedBrand1.get());
        car2.setBrand(optionalSavedBrand2.get());

        carRepository.saveCar(car1);
        carRepository.saveCar(car2);

        Optional<Car> optionalSavedCar1 = carRepository.getCarById(car1.getId());
        Optional<Car> optionalSavedCar2 = carRepository.getCarById(car2.getId());

        assertThat(optionalSavedCar1).isPresent();
        assertThat(optionalSavedCar2).isPresent();

        Post post1 = new Post();
        post1.setDescription("description1");
        post1.setCar(optionalSavedCar1.get());
        post1.setUser(optionalSavedUser.get());

        Post post2 = new Post();
        post2.setDescription("description2");
        post2.setCar(optionalSavedCar2.get());
        post2.setUser(optionalSavedUser.get());

        postRepository.savePost(post1);
        postRepository.savePost(post2);

        Optional<Post> optionalSavedPost1 = postRepository.getPostById(post1.getId());
        Optional<Post> optionalSavedPost2 = postRepository.getPostById(post2.getId());

        assertThat(optionalSavedPost1).isPresent();
        assertThat(optionalSavedPost2).isPresent();

        List<Post> posts = postRepository.getAllPostsWithSpecificCarBrand("BMW");

        AssertionError error = assertThrows(AssertionError.class, () -> {
            assertThat(posts).hasSize(2);
        });

        assertThat(error.getMessage()).contains("Expected size: 2 but was: 1");
    }
}