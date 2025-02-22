package com.lmerynda.spring_play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmerynda.spring_play.controller.TodoAssignDto;
import com.lmerynda.spring_play.model.Comment;
import com.lmerynda.spring_play.model.Person;
import com.lmerynda.spring_play.model.Todo;
import com.lmerynda.spring_play.repository.CommentRepository;
import com.lmerynda.spring_play.repository.PersonRepository;
import com.lmerynda.spring_play.repository.TodoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class TodoAppTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void setup() {
        var todo = todoRepository.save(new Todo("Prepopulated Todo 1", false));
        todoRepository.save(new Todo("Prepopulated Todo 2", false));

        commentRepository.save(new Comment("Prepopulated Comment 1", todo));
        commentRepository.save(new Comment("Prepopulated Comment 2", todo));
        commentRepository.save(new Comment("Prepopulated Comment 3", todo));

        personRepository.save(new Person("person1"));
    }

    @Test
    public void contextLoads() throws Exception {
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateTodo() throws Exception {
        Todo inputTodo = new Todo("Test Todo", false);
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    public void testGetAllTodos() throws Exception {
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetTodoById_Found() throws Exception {
        Todo inputTodo = new Todo("Test Todo", false);
        var result = mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputTodo)))
                .andExpect(status().isOk())
                .andReturn();
        Todo createdTodo = objectMapper.readValue(result.getResponse().getContentAsString(), Todo.class);

        mockMvc.perform(get("/todos/" + createdTodo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    public void testGetTodoByMalformedId_BadRequest() throws Exception {
        mockMvc.perform(get("/todos/1")).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetTodoById_NotFound() throws Exception {
        mockMvc.perform(get("/todos/0000-000-000-000-0000")).andExpect(status().isNotFound());
    }

    @Test
    public void testAssignTodo() throws Exception {
        var todo = todoRepository.findAll().get(0);
        var person = personRepository.findAll().get(0);

        mockMvc.perform(post("/todos/" + todo.getId() + "/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new TodoAssignDto(person.getEmail()))))
                .andExpect(status().isOk());

        var updatedTodo = todoRepository.findById(todo.getId()).get();
        assertEquals(person.getEmail(), updatedTodo.getAssignee().get().getEmail());
    }
}
