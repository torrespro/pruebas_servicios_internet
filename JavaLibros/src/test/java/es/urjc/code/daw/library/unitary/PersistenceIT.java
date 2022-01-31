package es.urjc.code.daw.library.unitary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import com.github.javafaker.Faker;
import es.urjc.code.daw.library.book.Book;
import es.urjc.code.daw.library.book.BookRepository;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("mssql")
class PersistenceIT extends DockerizedTest {

    @Autowired
    private BookRepository repository;
    private Book savedEntity;

    Faker faker = new Faker();

    @BeforeEach
    public void setupDb() {

        repository.deleteAll();

        Book entity = new Book(faker.book().title(), faker.backToTheFuture().quote());
        savedEntity = repository.save(entity);

        assertEquals(entity, savedEntity);
    }

    @Test
    void create() {
        Book newEntity = new Book(faker.book().title(), faker.backToTheFuture().quote());
        repository.save(newEntity);

        Book foundEntity = repository.findById(newEntity.getId()).get();

        assertEqualsReview(newEntity, foundEntity);

        assertEquals(2, repository.count());

    }

    @Test
    void update() {
        savedEntity.setTitle("title 2");
        repository.save(savedEntity);

        Book foundEntity = repository.findById(savedEntity.getId()).get();

        assertEquals("title 2", foundEntity.getTitle());
    }

    @Test
    void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    void getById() {
        Book foundEntity = repository.findById(savedEntity.getId()).get();

        assertEqualsReview(savedEntity, foundEntity);
    }

    /**
     * Validates the equality of this Entity class in different states and with different data. Due to the definition of
     * our Entity, 2 suppress warnings are required in order to validate it as explained in <a
     * href="https://jqno.nl/equalsverifier/manual/jpa-entities/">here</a>.
     */
    @Test
    void validateEntityEqualityAmongStates() {
        EqualsVerifier.forClass(Book.class)
            .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY)
            .suppress(Warning.STRICT_HASHCODE)
            .verify();
    }

    private void assertEqualsReview(Book expectedEntity, Book actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getTitle(), actualEntity.getTitle());
        assertEquals(expectedEntity.getDescription(), actualEntity.getDescription());
    }

}
