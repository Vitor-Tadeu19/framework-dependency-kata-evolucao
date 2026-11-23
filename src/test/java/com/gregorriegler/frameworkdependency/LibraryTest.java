package com.gregorriegler.frameworkdependency;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregorriegler.frameworkdependency.infrastructure.persistence.BookEntity;
import com.gregorriegler.frameworkdependency.infrastructure.persistence.SpringDataBookRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringDataBookRepository springDataBookRepository;

    @BeforeEach
    public void clearRepository() {
        springDataBookRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void starts_empty() throws Exception {
        mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser(roles = "NOT_A_USER")
    void non_user_cant_access_library() throws Exception {
        mockMvc.perform(get("/books"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void finds_created_book() throws Exception {
        mockMvc.perform(put("/books/123")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JSONObject()
                .put("title", "Name of Book")
                .put("author", "Name of Author")
                .toString()
            )
        ).andExpect(status().isNoContent());

        mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[" +
                new JSONObject()
                    .put("isbn", "123")
                    .put("title", "Name of Book")
                    .put("author", "Name of Author")
                    .toString() +
                "]"));
    }

    @Test
    @WithMockUser("guest")
    void guest_cant_create_book() throws Exception {
        mockMvc.perform(put("/books/123")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JSONObject()
                .put("title", "Name of Book")
                .put("author", "Name of Author")
                .toString())
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "max", roles = "USER")
    void user_can_rate_a_book() throws Exception {
        springDataBookRepository.save(new BookEntity("123", "irrelevant", "irrelevant"));

        mockMvc.perform(post("/books/123/ratings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JSONObject()
                .put("stars", "FIVE")
                .put("comment", "Rating")
                .toString()))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[" +
                new JSONObject()
                    .put("isbn", "123")
                    .put("title", "irrelevant")
                    .put("author", "irrelevant")
                    .put("ratings", new JSONArray()
                        .put(new JSONObject()
                            .put("user", "max")
                            .put("stars", "FIVE")
                            .put("comment", "Rating")
                        ))
                    .toString() +
                "]"));
    }

    @Test
    @WithMockUser(username = "manager", roles = {"ADMIN", "USER"})
    void updatingBookThroughHttpPreservesBothRatings() throws Exception {
        mockMvc.perform(put("/books/123")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title":"Original","author":"Original Author"}
                """))
            .andExpect(status().isNoContent());
        mockMvc.perform(post("/books/123/ratings")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"stars":"FIVE","comment":"First"}
                """))
            .andExpect(status().isCreated());
        mockMvc.perform(post("/books/123/ratings")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"stars":"FOUR","comment":"Second"}
                """))
            .andExpect(status().isCreated());

        JsonNode before = listBooks().get(0).get("ratings");
        assertThat(before).hasSize(2);
        assertThat(before).extracting(rating -> rating.path("id").asLong())
            .doesNotContain(0L).doesNotHaveDuplicates();
        assertThat(before).extracting(rating -> rating.path("comment").asText())
            .containsExactlyInAnyOrder("First", "Second");

        mockMvc.perform(put("/books/123")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title":"Updated","author":"Updated Author"}
                """))
            .andExpect(status().isNoContent());

        assertThat(listBooks()).singleElement().satisfies(book -> {
            assertThat(book.path("isbn").asText()).isEqualTo("123");
            assertThat(book.path("title").asText()).isEqualTo("Updated");
            assertThat(book.path("author").asText()).isEqualTo("Updated Author");
            assertThat(book.get("ratings")).containsExactlyInAnyOrderElementsOf(before);
        });
    }

    private JsonNode listBooks() throws Exception {
        String response = mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response);
    }

    @Test
    @WithMockUser(username = "max", roles = "USER")
    void rating_a_missing_book_returns_created_without_creating_a_book() throws Exception {
        mockMvc.perform(post("/books/missing/ratings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JSONObject()
                .put("stars", "FIVE")
                .put("comment", "Rating")
                .toString()))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser(roles = "NON_USER")
    void non_user_cant_rate_a_book() throws Exception {
        springDataBookRepository.save(new BookEntity("123", "irrelevant", "irrelevant"));

        mockMvc.perform(post("/books/123/ratings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JSONObject()
                .put("stars", "FIVE")
                .put("comment", "Rating")
                .toString()))
            .andExpect(status().isForbidden());
    }
}
