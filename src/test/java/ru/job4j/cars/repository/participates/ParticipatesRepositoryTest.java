package ru.job4j.cars.repository.participates;

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
class ParticipatesRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private ParticipatesRepository participatesRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private BrandRepository brandRepository;
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        participatesRepository = new ParticipatesRepository(crudRepository);
        postRepository = new PostRepository(crudRepository);
        userRepository = new UserRepository(crudRepository);
        brandRepository = new BrandRepository(crudRepository);
        carRepository = new CarRepository(crudRepository);
    }

    @AfterEach
    void cleanUp() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Participates").executeUpdate();
        session.createQuery("delete from Post").executeUpdate();
        session.createQuery("delete from Car").executeUpdate();
        session.createQuery("delete from Brand").executeUpdate();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Позитивный тест создания объекта типа Participates
     */
    @Test
    void whenCreateParticipatesIsSuccess() {
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

        Participates participates = new Participates();
        participates.setUser(optionalSavedUser.get());
        participates.setPost(optionalSavedPost.get());

        participatesRepository.saveParticipates(participates);

        Optional<Participates> optionalSavedParticipates = participatesRepository.getParticipatesById(
                participates.getId());

        assertThat(optionalSavedParticipates).isPresent();

        Participates savedParticipates = optionalSavedParticipates.get();

        assertNotNull(savedParticipates, "Participates should not be null");
        assertEquals(savedParticipates.getId(), participates.getId(), "Participates id should match");
        assertEquals(savedParticipates.getUser().getLogin(), participates.getUser().getLogin());
        assertEquals(savedParticipates.getPost().getDescription(), participates.getPost().getDescription());
    }

    /**
     * Негативный тест создания объекта типа Participates
     */
    @Test
    public void whenCreateParticipatesIsFail() {
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

        Participates participates = new Participates();
        participates.setUser(optionalSavedUser.get());
        participates.setPost(optionalSavedPost.get());

        participatesRepository.saveParticipates(participates);

        int nonExistentId = 999;

        Optional<Participates> optionalSavedParticipates = participatesRepository.getParticipatesById(nonExistentId);

        assertThat(optionalSavedParticipates).isEmpty();
    }

    /**
     * Позитивный тест получения объекта типа Participates по его id
     */
    @Test
    void whenGetParticipatesByIdIsSuccess() {
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

        Participates participates = new Participates();
        participates.setUser(optionalSavedUser.get());
        participates.setPost(optionalSavedPost.get());

        participatesRepository.saveParticipates(participates);

        Optional<Participates> optionalSavedParticipates =
                participatesRepository.getParticipatesById(participates.getId());

        assertThat(optionalSavedParticipates).isPresent();

        Optional<Participates> optionalParticipates =
                participatesRepository.getParticipatesById(optionalSavedParticipates.get().getId());

        assertThat(optionalParticipates).isPresent();
        assertThat(optionalParticipates.get().getUser().getLogin()).isEqualTo("test");
    }

    /**
     * Негативный тест получения объекта типа Participates по его id
     */
    @Test
    void whenGetParticipatesByIdIsFail() {
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

        Participates participates = new Participates();
        participates.setUser(optionalSavedUser.get());
        participates.setPost(optionalSavedPost.get());

        participatesRepository.saveParticipates(participates);

        int nonExistentId = 999;
        Optional<Participates> optionalSavedParticipates =
                participatesRepository.getParticipatesById(nonExistentId);

        assertThat(optionalSavedParticipates).isEmpty();
    }

    /**
     * Позитивный тест вывода всех объектов типа Participates
     */
    @Test
    void whenGetAllParticipatesIsSuccess() {
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
        Post post3 = new Post();

        post1.setDescription("description1");
        post2.setDescription("description2");
        post3.setDescription("description3");

        post1.setCar(optionalSavedCar.get());
        post2.setCar(optionalSavedCar.get());
        post3.setCar(optionalSavedCar.get());

        post1.setUser(optionalSavedUser.get());
        post2.setUser(optionalSavedUser.get());
        post3.setUser(optionalSavedUser.get());

        postRepository.savePost(post1);
        postRepository.savePost(post2);
        postRepository.savePost(post3);

        Optional<Post> optionalSavedPost1 = postRepository.getPostById(post1.getId());
        Optional<Post> optionalSavedPost2 = postRepository.getPostById(post2.getId());
        Optional<Post> optionalSavedPost3 = postRepository.getPostById(post3.getId());

        assertThat(optionalSavedPost1).isPresent();
        assertThat(optionalSavedPost2).isPresent();
        assertThat(optionalSavedPost3).isPresent();

        Participates participates1 = new Participates();
        Participates participates2 = new Participates();
        Participates participates3 = new Participates();

        participates1.setUser(optionalSavedUser.get());
        participates2.setUser(optionalSavedUser.get());
        participates3.setUser(optionalSavedUser.get());

        participates1.setPost(optionalSavedPost1.get());
        participates2.setPost(optionalSavedPost2.get());
        participates3.setPost(optionalSavedPost3.get());

        participatesRepository.saveParticipates(participates1);
        participatesRepository.saveParticipates(participates2);
        participatesRepository.saveParticipates(participates3);

        List<Participates> participatesList = participatesRepository.getAllParticipates();

        assertThat(participatesList).hasSize(3);
        assertThat(participatesList).containsExactlyInAnyOrder(participates1, participates2, participates3);
    }
}