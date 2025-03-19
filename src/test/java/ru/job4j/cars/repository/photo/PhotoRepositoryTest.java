package ru.job4j.cars.repository.photo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.brand.BrandRepository;
import ru.job4j.cars.repository.car.CarRepository;
import ru.job4j.cars.repository.post.PostRepository;
import ru.job4j.cars.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PhotoRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private PhotoRepository photoRepository;
    private PostRepository postRepository;
    private CarRepository carRepository;
    private BrandRepository brandRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        photoRepository = new PhotoRepository(crudRepository);
        postRepository = new PostRepository(crudRepository);
        carRepository = new CarRepository(crudRepository);
        brandRepository = new BrandRepository(crudRepository);
        userRepository = new UserRepository(crudRepository);
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
     * Позитивный тест создания объекта типа Photo
     */
    @Test
    public void whenSaveAPhotoIsSuccess() {
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

        Photo photo = new Photo();
        photo.setPath("path/to/photo.jpg");
        photo.setPost(optionalSavedPost.get());

        photoRepository.savePhoto(photo);

        Session session = sf.openSession();
        session.beginTransaction();

        Photo savedPhoto = session.get(Photo.class, photo.getId());

        session.getTransaction().commit();

        session.close();

        assertNotNull(savedPhoto, "Photo should not be null");
        assertEquals(photo.getPath(), savedPhoto.getPath(), "Photo path should match");
    }

    /**
     * Негативный тест создания объекта типа Photo
     */
    @Test
    public void whenSaveAPhotoIsFail() {
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

        Photo photo = new Photo();
        photo.setPath("path/to/photo.jpg");
        photo.setPost(optionalSavedPost.get());

        photoRepository.savePhoto(photo);

        Session session = sf.openSession();
        session.beginTransaction();

        Photo savedPhoto = session.get(Photo.class, photo.getId());

        session.getTransaction().commit();

        session.close();

        assertNotNull(savedPhoto, "Photo should not be null");
        assertNotEquals("path/to/photo2.jpg", savedPhoto.getPath(), "Photo path should not match");
    }

    /**
     * Позитивный тест вывода всех объектов типа Photo
     */
    @Test
    public void whenGetAllPhotosIsSuccess() {
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

        Photo photo1 = new Photo();
        Photo photo2 = new Photo();

        photo1.setPath("path/to/photo1.jpg");
        photo2.setPath("path/to/photo2.jpg");

        photo1.setPost(optionalSavedPost.get());
        photo2.setPost(optionalSavedPost.get());

        photoRepository.savePhoto(photo1);
        photoRepository.savePhoto(photo2);

        Session session = sf.openSession();
        session.beginTransaction();

        Photo savedPhoto1 = session.get(Photo.class, photo1.getId());
        Photo savedPhoto2 = session.get(Photo.class, photo2.getId());

        session.getTransaction().commit();

        session.close();

        List<Photo> photos = photoRepository.getAllPhotos();

        assertThat(photos).hasSize(2);
        assertThat(photos).containsExactlyInAnyOrder(savedPhoto1, savedPhoto2);
    }

    /**
     * Негативный тест вывода объектов типа Photo
     */
    @Test
    public void whenGetAllPhotosIsFail() {
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

        Photo photo1 = new Photo();
        Photo photo2 = new Photo();

        photo1.setPath("path/to/photo1.jpg");
        photo2.setPath("path/to/photo2.jpg");

        photo1.setPost(optionalSavedPost.get());
        photo2.setPost(optionalSavedPost.get());

        photoRepository.savePhoto(photo1);
        photoRepository.savePhoto(photo2);

        List<Photo> photos = photoRepository.getAllPhotos();

        AssertionError error = assertThrows(AssertionError.class, () -> {
            assertThat(photos).hasSize(3);
        });

        assertThat(error.getMessage()).contains("Expected size: 3 but was: 2");
    }

    /**
     * Позитивный тест поиска объекта типа Photo по ID
     */
    @Test
    public void whenFindPhotoByIdIsSuccess() {
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

        Photo photo = new Photo();

        photo.setPath("path/to/photo.jpg");

        photo.setPost(optionalSavedPost.get());

        photoRepository.savePhoto(photo);

        Session session = sf.openSession();
        session.beginTransaction();
        Photo savedPhoto = session.get(Photo.class, photo.getId());
        session.getTransaction().commit();
        session.close();

        Optional<Photo> foundPhoto = photoRepository.getPhotoById(savedPhoto.getId());

        assertThat(foundPhoto).isPresent();
        assertThat(foundPhoto.get().getPath()).isEqualTo("path/to/photo.jpg");
    }

    /**
     * Негативный тест поиска объекта типа Photo по ID
     */
    @Test
    public void whenFindPhotoByIdIsFail() {
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

        Photo photo = new Photo();

        photo.setPath("path/to/photo.jpg");

        photo.setPost(optionalSavedPost.get());

        photoRepository.savePhoto(photo);

        Session session = sf.openSession();
        session.beginTransaction();
        Photo savedPhoto = session.get(Photo.class, photo.getId());
        session.getTransaction().commit();
        session.close();

        Optional<Photo> optionalFoundPhoto = photoRepository.getPhotoById(savedPhoto.getId());

        assertThat(optionalFoundPhoto).isPresent();
        assertThat(optionalFoundPhoto.get().getPath()).isNotEqualTo("path/to/photo2.jpg");
    }

    /**
     * Позитивный тест обновления объекта типа Photo
     */
    @Test
    public void whenUpdatePhotoIsSuccess() {
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

        Photo photo = new Photo();

        photo.setPath("path/to/photo.jpg");

        photo.setPost(optionalSavedPost.get());

        photoRepository.savePhoto(photo);

        Optional<Photo> optionalSavedPhoto = photoRepository.getPhotoById(photo.getId());
        assertThat(optionalSavedPhoto).isPresent();

        Photo savedPhoto = optionalSavedPhoto.get();

        savedPhoto.setPath("path/to/photo2.jpg");

        photoRepository.updatePhoto(savedPhoto);

        Optional<Photo> optionalUpdatedPhoto = photoRepository.getPhotoById(savedPhoto.getId());

        assertThat(optionalUpdatedPhoto).isPresent();
        Photo updatedPhoto = optionalUpdatedPhoto.get();
        assertThat(updatedPhoto.getPath()).isEqualTo("path/to/photo2.jpg");
    }

    /**
     * Негативный тест обновления объекта типа Photo
     */
    @Test
    public void whenUpdatePhotoIsFail() {
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

        Photo photo = new Photo();

        photo.setPath("path/to/photo.jpg");

        photo.setPost(optionalSavedPost.get());

        photoRepository.savePhoto(photo);

        Optional<Photo> optionalSavedPhoto = photoRepository.getPhotoById(photo.getId());
        assertThat(optionalSavedPhoto).isPresent();

        Photo savedPhoto = optionalSavedPhoto.get();

        Photo nonExistentPhoto = new Photo();
        nonExistentPhoto.setId(999);
        nonExistentPhoto.setPath("path/to/photo2.jpg");
        nonExistentPhoto.setPost(optionalSavedPost.get());

        photoRepository.updatePhoto(nonExistentPhoto);

        Optional<Photo> optionalUpdatedPhoto = photoRepository.getPhotoById(savedPhoto.getId());

        assertThat(optionalUpdatedPhoto).isPresent();
        Photo updatedPhoto = optionalUpdatedPhoto.get();
        assertThat(updatedPhoto.getPath()).isEqualTo("path/to/photo.jpg");
    }

    /**
     * Позитивный тест удаления объекта типа Photo
     */
    @Test
    public void whenDeletePhotoIsSuccess() {
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

        Photo photo = new Photo();

        photo.setPath("path/to/photo.jpg");

        photo.setPost(optionalSavedPost.get());

        photoRepository.savePhoto(photo);

        Optional<Photo> optionalSavedPhoto = photoRepository.getPhotoById(photo.getId());
        assertThat(optionalSavedPhoto).isPresent();

        Photo savedPhoto = optionalSavedPhoto.get();

        photoRepository.deletePhotoById(savedPhoto.getId());

        Optional<Photo> optionalDeletedPhoto = photoRepository.getPhotoById(savedPhoto.getId());

        assertThat(optionalDeletedPhoto).isEmpty();
    }

    /**
     * Негативный тест удаления объекта типа Photo
     */
    @Test
    public void whenDeletePhotoIsFail() {
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

        Photo photo = new Photo();

        photo.setPath("path/to/photo.jpg");

        photo.setPost(optionalSavedPost.get());

        photoRepository.savePhoto(photo);

        Optional<Photo> optionalSavedPhoto = photoRepository.getPhotoById(photo.getId());
        assertThat(optionalSavedPhoto).isPresent();

        Photo savedPhoto = optionalSavedPhoto.get();

        Integer nonExistentId = 999;
        photoRepository.deletePhotoById(nonExistentId);

        Optional<Photo> remainingOptionalPhoto = photoRepository.getPhotoById(savedPhoto.getId());
        assertThat(remainingOptionalPhoto).isPresent();
    }
}