package ru.job4j.cars.repository.pricehistory;

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
import ru.job4j.cars.repository.post.PostRepository;
import ru.job4j.cars.repository.user.UserRepository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PriceHistoryRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private PriceHistoryRepository priceHistoryRepository;
    private PostRepository postRepository;
    private CarRepository carRepository;
    private BrandRepository brandRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        priceHistoryRepository = new PriceHistoryRepository(crudRepository);
        postRepository = new PostRepository(crudRepository);
        carRepository = new CarRepository(crudRepository);
        brandRepository = new BrandRepository(crudRepository);
        userRepository = new UserRepository(crudRepository);
    }

    @AfterEach
    void cleanUp() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from PriceHistory").executeUpdate();
        session.createQuery("delete from Post").executeUpdate();
        session.createQuery("delete from Car").executeUpdate();
        session.createQuery("delete from Brand").executeUpdate();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Позитивный тест создания объекта типа PriceHistory
     */
    @Test
    public void whenCreatePriceHistoryThenSuccess() {
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

        PriceHistory priceHistory = new PriceHistory();

        priceHistory.setBefore(10000L);
        priceHistory.setAfter(100L);
        priceHistory.setPost(optionalSavedPost.get());

        priceHistoryRepository.savePriceHistory(priceHistory);

        Session session = sf.openSession();
        session.beginTransaction();

        PriceHistory savedPriceHistory = session.get(PriceHistory.class, priceHistory.getId());

        session.getTransaction().commit();
        session.close();

        assertNotNull(savedPriceHistory, "PriceHistory should not be null");
        assertEquals(priceHistory.getId(), savedPriceHistory.getId(), "PriceHistory id should match");
    }

    /**
     * Негативный тест создания объекта типа PriceHistory
     */
    @Test
    public void whenCreatePriceHistoryThenFail() {
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

        PriceHistory priceHistory = new PriceHistory();

        priceHistory.setBefore(10000L);
        priceHistory.setAfter(100L);
        priceHistory.setPost(optionalSavedPost.get());

        priceHistoryRepository.savePriceHistory(priceHistory);

        Session session = sf.openSession();
        session.beginTransaction();

        PriceHistory savedPriceHistory = session.get(PriceHistory.class, priceHistory.getId());

        session.getTransaction().commit();
        session.close();

        assertNotNull(savedPriceHistory, "PriceHistory should not be null");
        assertNotEquals("description2",
                savedPriceHistory.getPost().getDescription(),
                "PriceHistory id should match");
    }

    /**
     * Позитивный тест получения списка всех историй цены автомобилей.
     */
    @Test
    public void whenGetAllPriceHistoryThenSuccess() {
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

        PriceHistory priceHistory1 = new PriceHistory();
        PriceHistory priceHistory2 = new PriceHistory();
        PriceHistory priceHistory3 = new PriceHistory();

        priceHistory1.setBefore(10000L);
        priceHistory2.setBefore(20000L);
        priceHistory3.setBefore(30000L);

        priceHistory1.setAfter(100L);
        priceHistory2.setAfter(200L);
        priceHistory3.setAfter(300L);

        priceHistory1.setPost(optionalSavedPost.get());
        priceHistory2.setPost(optionalSavedPost.get());
        priceHistory3.setPost(optionalSavedPost.get());

        priceHistoryRepository.savePriceHistory(priceHistory1);
        priceHistoryRepository.savePriceHistory(priceHistory2);
        priceHistoryRepository.savePriceHistory(priceHistory3);

        List<PriceHistory> savedPriceHistories = priceHistoryRepository.getAllPriceHistory();

        assertThat(savedPriceHistories).hasSize(3);
        assertThat(savedPriceHistories).containsExactlyInAnyOrder(priceHistory1, priceHistory2, priceHistory3);
    }

    /**
     * Негативный тест получения списка всех историй цены автомобилей.
     */
    @Test
    public void whenGetAllPriceHistoryThenFail() {
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

        PriceHistory priceHistory1 = new PriceHistory();
        PriceHistory priceHistory2 = new PriceHistory();
        PriceHistory priceHistory3 = new PriceHistory();

        priceHistory1.setBefore(10000L);
        priceHistory2.setBefore(20000L);
        priceHistory3.setBefore(30000L);

        priceHistory1.setAfter(100L);
        priceHistory2.setAfter(200L);
        priceHistory3.setAfter(300L);

        priceHistory1.setPost(optionalSavedPost.get());
        priceHistory2.setPost(optionalSavedPost.get());
        priceHistory3.setPost(optionalSavedPost.get());

        priceHistoryRepository.savePriceHistory(priceHistory1);
        priceHistoryRepository.savePriceHistory(priceHistory2);
        priceHistoryRepository.savePriceHistory(priceHistory3);

        List<PriceHistory> savedPriceHistories = priceHistoryRepository.getAllPriceHistory();

        AssertionError error = assertThrows(AssertionError.class, () -> {
            assertThat(savedPriceHistories).hasSize(2);
        });

        assertThat(error.getMessage()).contains("Expected size: 2 but was: 3");
    }

    /**
     * Позитивный тест получения истории цены автомобиля по ID.
     */
    @Test
    public void whenGetPriceHistoryByIdThenSuccess() {
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

        PriceHistory priceHistory = new PriceHistory();

        priceHistory.setBefore(10000L);
        priceHistory.setAfter(100L);
        priceHistory.setPost(optionalSavedPost.get());

        priceHistoryRepository.savePriceHistory(priceHistory);

        Optional<PriceHistory> optionalSavedPriceHistory =
                priceHistoryRepository.getPriceHistoryById(priceHistory.getId());

        assertThat(optionalSavedPriceHistory).isPresent();

        Optional<PriceHistory> optionalPriceHistory =
                priceHistoryRepository.getPriceHistoryById(optionalSavedPriceHistory.get().getId());

        assertThat(optionalPriceHistory).isPresent();
        assertThat(optionalPriceHistory.get().getPost().getDescription()).isEqualTo("description");
    }

    /**
     * Негативный тест получения истории цены автомобиля по ID.
     */
    @Test
    public void whenGetPriceHistoryByIdThenFail() {
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

        PriceHistory priceHistory = new PriceHistory();

        priceHistory.setBefore(10000L);
        priceHistory.setAfter(100L);
        priceHistory.setPost(optionalSavedPost.get());

        priceHistoryRepository.savePriceHistory(priceHistory);

        Long nonExistingId = 999L;
        Optional<PriceHistory> foundOptionalPriceHistory =
                priceHistoryRepository.getPriceHistoryById(nonExistingId);

        assertThat(foundOptionalPriceHistory).isEmpty();
    }
}