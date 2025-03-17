package ru.job4j.cars.repository.engine;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EngineRepositoryTest {

    private final SessionFactory sf
            = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private EngineRepository engineRepository;

    @BeforeEach
    void setUp() {
        CrudRepository crudRepository = new CrudRepository(sf);

        engineRepository = new EngineRepository(crudRepository);
    }

    @AfterEach
    void cleanUp() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Engine").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Позитивный тест создания объекта типа Engine
     */
    @Test
    void whenCreateEngineThenSuccess() {
        Engine engine = new Engine();
        engine.setName("V8");

        engineRepository.saveEngine(engine);
        Optional<Engine> savedEngine = engineRepository.getEngineById(engine.getId());

        assertThat(savedEngine).isPresent();
        assertThat(savedEngine.get().getName()).isEqualTo("V8");
    }

    /**
     * Негативный тест создания объекта типа Engine
     */
    @Test
    public void whenCreateEngineThenFail() {
        Engine engine = new Engine();
        engine.setName("V8");

        engineRepository.saveEngine(engine);

        Optional<Engine> savedEngine = engineRepository.getEngineById(engine.getId());

        assertThat(savedEngine).isPresent();
        assertThat(savedEngine.get().getName()).isNotEqualTo("V12");
    }

    /**
     * Позитивный тест получения объекта типа Engine по id
     */
    @Test
    public void whenGetEngineByIdThenSuccess() {
        Engine engine = new Engine();
        engine.setName("V8");

        engineRepository.saveEngine(engine);

        Optional<Engine> savedEngine = engineRepository.getEngineById(engine.getId());

        assertThat(savedEngine).isPresent();

        Optional<Engine> foundEngine = engineRepository.getEngineById(savedEngine.get().getId());

        assertThat(foundEngine).isPresent();
        assertThat(foundEngine.get().getName()).isEqualTo("V8");
    }

    /**
     * Негативный тест получения объекта типа Engine по id
     */
    @Test
    public void whenGetEngineByIdThenFail() {
        Engine engine = new Engine();
        engine.setName("V8");
        engineRepository.saveEngine(engine);

        Integer nonExistentId = 999;
        Optional<Engine> foundEngine = engineRepository.getEngineById(nonExistentId);

        assertThat(foundEngine).isEmpty();
    }

    /**
     * Позитивный тест вывода всех объектов типа Engine
     */
    @Test
    public void whenGetAllEnginesThenSuccess() {
        Engine engine1 = new Engine();
        Engine engine2 = new Engine();
        Engine engine3 = new Engine();

        engine1.setName("V8");
        engine2.setName("V12");
        engine3.setName("V16");

        engineRepository.saveEngine(engine1);
        engineRepository.saveEngine(engine2);
        engineRepository.saveEngine(engine3);

        List<Engine> engines = engineRepository.getAllEngines();

        assertThat(engines).hasSize(3);
        assertThat(engines).containsExactlyInAnyOrder(engine1, engine2, engine3);
    }

    /**
     * Негативный тест вывода всех объектов типа Engine
     */
    @Test
    public void whenGetAllEnginesThenFail() {
        Engine engine1 = new Engine();
        Engine engine2 = new Engine();
        Engine engine3 = new Engine();

        engine1.setName("V8");
        engine2.setName("V12");
        engine3.setName("V16");

        engineRepository.saveEngine(engine1);
        engineRepository.saveEngine(engine2);
        engineRepository.saveEngine(engine3);

        List<Engine> engines = engineRepository.getAllEngines();

        AssertionError error = assertThrows(AssertionError.class, () -> {
            assertThat(engines).hasSize(2);
        });

        assertThat(error.getMessage()).contains("Expected size: 2 but was: 3");
    }

    /**
     * Позитивный тест обновления объекта типа Engine
     */
    @Test
    public void whenUpdateEngineThenSuccess() {
        Engine engine = new Engine();
        engine.setName("V8");
        engineRepository.saveEngine(engine);

        Optional<Engine> savedEngineOptional = engineRepository.getEngineById(engine.getId());
        assertThat(savedEngineOptional).isPresent();

        Engine savedEngine = savedEngineOptional.get();

        savedEngine.setName("V12");

        engineRepository.update(savedEngine);

        Optional<Engine> updatedEngineOptional = engineRepository.getEngineById(savedEngine.getId());

        assertThat(updatedEngineOptional).isPresent();
        Engine updatedEngine = updatedEngineOptional.get();
        assertThat(updatedEngine.getName()).isEqualTo("V12");
    }

    /**
     * Негативный тест обновления объекта типа Engine
     */
    @Test
    public void whenUpdateEngineThenFail() {
        Engine engine = new Engine();
        engine.setName("V8");
        engineRepository.saveEngine(engine);

        Optional<Engine> savedEngineOptional = engineRepository.getEngineById(engine.getId());
        assertThat(savedEngineOptional).isPresent();

        Engine savedEngine = savedEngineOptional.get();

        Engine nonExistentEngine = new Engine();
        nonExistentEngine.setId(999);
        nonExistentEngine.setName("V12");

        engineRepository.update(nonExistentEngine);

        Optional<Engine> updatedEngineOptional = engineRepository.getEngineById(savedEngine.getId());

        assertThat(updatedEngineOptional).isPresent();
        Engine updatedEngine = updatedEngineOptional.get();
        assertThat(updatedEngine.getName()).isEqualTo("V8");
    }

    /**
     * Позитивный тест удаления объекта типа Engine
     */
    @Test
    public void whenDeleteEngineThenSuccess() {
        Engine engine = new Engine();
        engine.setName("V8");
        engineRepository.saveEngine(engine);

        Optional<Engine> savedEngineOptional = engineRepository.getEngineById(engine.getId());
        assertThat(savedEngineOptional).isPresent();

        Engine savedEngine = savedEngineOptional.get();

        engineRepository.deleteEngineById(savedEngine.getId());

        Optional<Engine> deletedEngineOptional = engineRepository.getEngineById(savedEngine.getId());

        assertThat(deletedEngineOptional).isEmpty();
    }

    /**
     * Негативный тест удаления объекта типа Engine
     */
    @Test
    public void whenDeleteEngineThenFail() {
        Engine engine = new Engine();
        engine.setName("V8");
        engineRepository.saveEngine(engine);

        Optional<Engine> savedEngineOptional = engineRepository.getEngineById(engine.getId());
        assertThat(savedEngineOptional).isPresent();

        Engine savedEngine = savedEngineOptional.get();

        Integer nonExistentId = 999;
        engineRepository.deleteEngineById(nonExistentId);

        Optional<Engine> deletedEngineOptional = engineRepository.getEngineById(savedEngine.getId());
        assertThat(deletedEngineOptional).isPresent();
    }
}