package es.urjc.code.daw.library.unitary;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.javafaker.Faker;
import es.urjc.code.daw.library.book.Book;
import es.urjc.code.daw.library.book.BookService;
import es.urjc.code.daw.library.rest.BookRestController;
import es.urjc.code.daw.library.security.UserRepositoryAuthProvider;
import es.urjc.code.daw.library.user.UserComponent;
import es.urjc.code.daw.library.user.UserRepository;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = BookRestController.class)
@Import(UserRepositoryAuthProvider.class)
public class BookControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    protected BookService bookService;
    @MockBean
    protected UserRepository userRepository;
    @MockBean
    protected UserComponent userComponent;

    Faker faker = new Faker();


    @Test
    @DisplayName("should return empty list when no books")
    void shouldReturnEmptyListWhenNoBooks() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/books/")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", Matchers.is(0)))
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("should return a list of books")
    void shouldReturnAListOfBooks() throws Exception {
        Book book = new Book(faker.book().title(), faker.backToTheFuture().quote());
        Book book2 = new Book(faker.book().title(), faker.backToTheFuture().quote());

        when(bookService.findAll()).thenReturn(List.of(book, book2));

        this.mockMvc
            .perform(MockMvcRequestBuilders.get("/api/books/")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(2)))
            .andExpect(jsonPath("$[0].title", is(book.getTitle())))
            .andExpect(jsonPath("$[0].description", is(book.getDescription())))
            .andExpect(jsonPath("$[1].title", is(book2.getTitle())))
            .andExpect(jsonPath("$[1].description", is(book2.getDescription())));
    }

    @Test
    @DisplayName("should add a new book")
    @WithMockUser
    void shouldAddANewBook() throws Exception {
        String requestBody = """
            {
                "title": "Clean Code: A Handbook of Agile Software Craftsmanship",
                "description": "Even bad code can function. But if code isn’t clean, it can bring a development organization to its knees. Every year, countless hours and significant resources are lost because of poorly written code. But it doesn’t have to be that way. Noted software expert Robert C. Martin, presents a revolutionary paradigm with Clean Code: A Handbook of Agile Software Craftsmanship. Martin, who has helped bring agile principles from a practitioner’s point of view to tens of thousands of programmers, has teamed up with his colleagues from Object Mentor to distill their best agile practice of cleaning code “on the fly” into a book that will instill within you the values of software craftsman, and make you a better programmer―but only if you work at it. What kind of work will you be doing? You’ll be reading code―lots of code. And you will be challenged to think about what’s right about that code, and what’s wrong with it. More importantly you will be challenged to reassess your professional values and your commitment to your craft.   Clean Code is divided into three parts. The first describes the principles, patterns, and practices of writing clean code. The second part consists of several case studies of increasing complexity. Each case study is an exercise in cleaning up code―of transforming a code base that has some problems into one that is sound and efficient. The third part is the payoff: a single chapter containing a list of heuristics and “smells” gathered while creating the case studies. The result is a knowledge base that describes the way we think when we write, read, and clean code.   Readers will come away from this book understanding How to tell the difference between good and bad code How to write good code and how to transform bad code into good code How to create good names, good functions, good objects, and good classes How to format code for maximum readability How to implement complete error handling without obscuring code logic How to unit test and practice test-driven development What “smells” and heuristics can help you identify bad code This book is a must for any developer, software engineer, project manager, team lead, or systems analyst with an interest in producing better code."
            }""";
        Book book = new Book();
        book.setId(1L);
        when(bookService.save(any())).thenReturn(book);

        this
            .mockMvc
            .perform(post("/api/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("should fail to add a new book if not user")
    @WithMockUser(username = "user", password = "password", roles = "ANONYMOUS")
    void shouldFailToAddANewBookIfNotUser() throws Exception {
        String requestBody = """
            {
                "title": "Clean Code: A Handbook of Agile Software Craftsmanship",
                "description": "Even bad code can function. But if code isn’t clean, it can bring a development organization to its knees. Every year, countless hours and significant resources are lost because of poorly written code. But it doesn’t have to be that way. Noted software expert Robert C. Martin, presents a revolutionary paradigm with Clean Code: A Handbook of Agile Software Craftsmanship. Martin, who has helped bring agile principles from a practitioner’s point of view to tens of thousands of programmers, has teamed up with his colleagues from Object Mentor to distill their best agile practice of cleaning code “on the fly” into a book that will instill within you the values of software craftsman, and make you a better programmer―but only if you work at it. What kind of work will you be doing? You’ll be reading code―lots of code. And you will be challenged to think about what’s right about that code, and what’s wrong with it. More importantly you will be challenged to reassess your professional values and your commitment to your craft.   Clean Code is divided into three parts. The first describes the principles, patterns, and practices of writing clean code. The second part consists of several case studies of increasing complexity. Each case study is an exercise in cleaning up code―of transforming a code base that has some problems into one that is sound and efficient. The third part is the payoff: a single chapter containing a list of heuristics and “smells” gathered while creating the case studies. The result is a knowledge base that describes the way we think when we write, read, and clean code.   Readers will come away from this book understanding How to tell the difference between good and bad code How to write good code and how to transform bad code into good code How to create good names, good functions, good objects, and good classes How to format code for maximum readability How to implement complete error handling without obscuring code logic How to unit test and practice test-driven development What “smells” and heuristics can help you identify bad code This book is a must for any developer, software engineer, project manager, team lead, or systems analyst with an interest in producing better code."
            }""";
        Book book = new Book();
        book.setId(1L);
        when(bookService.save(any())).thenReturn(book);

        this
            .mockMvc
            .perform(post("/api/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("should delete a book")
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    void shouldDeleteABook() throws Exception {
        this.mockMvc
            .perform(delete("/api/books/{id}", 1L)
            )
            .andExpect(status().isOk());

        verify(bookService).delete(1L);
    }

    @Test
    @DisplayName("should fail to delete a book if not admin")
    @WithMockUser
    void shouldFailToDeleteABookIfNotAdmin() throws Exception {
        this.mockMvc
            .perform(delete("/api/books/{id}", 1L)
            )
            .andExpect(status().isForbidden());
    }

}
