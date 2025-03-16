package ru.job4j.cars.repository.brand;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.hibernate.cfg.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BrandRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        brandRepository = new BrandRepository(crudRepository);
    }

    @AfterEach
    void cleanUp() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Brand").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Позитивный тест создания объекта типа Brand
     */
    @Test
    public void whenSaveABrandThenReturnTheSameBrand() {
        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);
        Optional<Brand> savedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(savedBrand).isPresent();
        assertThat(savedBrand.get().getName()).isEqualTo("BMW");
    }

    /**
     * Негативный тест создания объекта типа Brand
     */
    @Test
    public void whenSaveABrandThenReturnTheWrongBrand() {
        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> savedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(savedBrand).isPresent();
        assertThat(savedBrand.get().getName()).isNotEqualTo("AUDI");
    }

    /**
     * Позитивный тест поиска объекта типа Brand по ID
     */
    @Test
    public void whenFindBrandByIdThenReturnTheSameBrand() {
        Brand brand = new Brand();
        brand.setName("BMW");

        brandRepository.saveBrand(brand);

        Optional<Brand> savedBrand = brandRepository.getBrandById(brand.getId());

        assertThat(savedBrand).isPresent();

        Optional<Brand> foundBrand = brandRepository.getBrandById(savedBrand.get().getId());

        assertThat(foundBrand).isPresent();
        assertThat(foundBrand.get().getName()).isEqualTo("BMW");
    }

    /**
     * Негативный тест поиска объекта типа Brand по ID
     */
    @Test
    public void whenFindBrandByIdThenReturnTheWrongBrand() {
        Brand brand = new Brand();
        brand.setName("BMW");
        brandRepository.saveBrand(brand);

        Integer nonExistentId = 999;
        Optional<Brand> foundBrand = brandRepository.getBrandById(nonExistentId);

        assertThat(foundBrand).isEmpty();
    }

    /**
     * Позитивный тест вывода всех объектов типа Brand
     */
    @Test
    public void whenGetAllBrandsIsSuccess() {
        Brand brand1 = new Brand();
        Brand brand2 = new Brand();
        Brand brand3 = new Brand();

        brand1.setName("BMW");
        brand2.setName("AUDI");
        brand3.setName("VW");

        brandRepository.saveBrand(brand1);
        brandRepository.saveBrand(brand2);
        brandRepository.saveBrand(brand3);

        List<Brand> brands = brandRepository.getAllBrands();

        assertThat(brands).hasSize(3);
        assertThat(brands).containsExactlyInAnyOrder(brand1, brand2, brand3);
    }

    /**
     * Негативный тест вывода всех объектов типа Brand
     */
    @Test
    public void whenGetAllBrandsIsFail() {
        Brand brand1 = new Brand();
        Brand brand2 = new Brand();
        Brand brand3 = new Brand();

        brand1.setName("BMW");
        brand2.setName("AUDI");
        brand3.setName("VW");

        brandRepository.saveBrand(brand1);
        brandRepository.saveBrand(brand2);
        brandRepository.saveBrand(brand3);

        List<Brand> brands = brandRepository.getAllBrands();

        AssertionError error = assertThrows(AssertionError.class, () -> {
            assertThat(brands).hasSize(2);
        });

        assertThat(error.getMessage()).contains("Expected size: 2 but was: 3");
    }

    /**
     * Позитивный тест обновления объекта типа Brand
     */
    @Test
    public void whenUpdateBrandIsSuccess() {
        Brand brand = new Brand();
        brand.setName("BMW");
        brandRepository.saveBrand(brand);

        Optional<Brand> savedBrandOptional = brandRepository.getBrandById(brand.getId());
        assertThat(savedBrandOptional).isPresent();

        Brand savedBrand = savedBrandOptional.get();

        savedBrand.setName("AUDI");

        brandRepository.updateBrand(savedBrand);

        Optional<Brand> updatedBrandOptional = brandRepository.getBrandById(savedBrand.getId());

        assertThat(updatedBrandOptional).isPresent();
        Brand updatedBrand = updatedBrandOptional.get();
        assertThat(updatedBrand.getName()).isEqualTo("AUDI");
    }

    /**
     * Негативный тест обновления объекта типа Brand
     */
    @Test
    public void whenUpdateBrandIsFail() {
        Brand brand = new Brand();
        brand.setName("BMW");
        brandRepository.saveBrand(brand);

        Optional<Brand> savedBrandOptional = brandRepository.getBrandById(brand.getId());
        assertThat(savedBrandOptional).isPresent();

        Brand savedBrand = savedBrandOptional.get();

        Brand nonExistentBrand = new Brand();
        nonExistentBrand.setId(999);
        nonExistentBrand.setName("AUDI");

        brandRepository.updateBrand(nonExistentBrand);

        Optional<Brand> updatedBrandOptional = brandRepository.getBrandById(savedBrand.getId());

        assertThat(updatedBrandOptional).isPresent();
        Brand updatedBrand = updatedBrandOptional.get();
        assertThat(updatedBrand.getName()).isEqualTo("BMW");
    }

    /**
     * Позитивный тест удаления объекта типа Brand
     */
    @Test
    public void whenDeleteBrandIsSuccess() {
        Brand brand = new Brand();
        brand.setName("BMW");
        brandRepository.saveBrand(brand);

        Optional<Brand> savedBrandOptional = brandRepository.getBrandById(brand.getId());
        assertThat(savedBrandOptional).isPresent();

        Brand savedBrand = savedBrandOptional.get();

        brandRepository.deleteBrandById(savedBrand.getId());

        Optional<Brand> deletedBrandOptional = brandRepository.getBrandById(savedBrand.getId());

        assertThat(deletedBrandOptional).isEmpty();
    }

    /**
     * Негативный тест удаления объекта типа Brand
     */
    @Test
    public void whenDeleteBrandIsFail() {
        Brand brand = new Brand();
        brand.setName("BMW");
        brandRepository.saveBrand(brand);

        Optional<Brand> savedBrandOptional = brandRepository.getBrandById(brand.getId());
        assertThat(savedBrandOptional).isPresent();

        Brand savedBrand = savedBrandOptional.get();

        Integer nonExistentId = 999;
        brandRepository.deleteBrandById(nonExistentId);

        Optional<Brand> remainingBrandOptional = brandRepository.getBrandById(savedBrand.getId());
        assertThat(remainingBrandOptional).isPresent();
    }
}