package com.lmerynda.spring_play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lmerynda.spring_play.model.Comment;
import com.lmerynda.spring_play.model.Person;
import com.lmerynda.spring_play.model.Todo;
import com.lmerynda.spring_play.model.TodoMetadataName;
import com.lmerynda.spring_play.repository.CommentRepository;
import com.lmerynda.spring_play.repository.PersonRepository;
import com.lmerynda.spring_play.repository.TodoRepository;
import com.lmerynda.spring_play.service.TodoService;

@SpringBootTest
public class TodoAppTestSimple {

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void setup() {
        // Clear repositories
        commentRepository.deleteAll();
        todoRepository.deleteAll();
        personRepository.deleteAll();

        // Prepopulate data similar to the controller test setup
        Todo preTodo = todoRepository.save(new Todo("Prepopulated Todo 1", false));
        todoRepository.save(new Todo("Prepopulated Todo 2", false));

        commentRepository.save(new Comment("Prepopulated Comment 1", preTodo));
        commentRepository.save(new Comment("Prepopulated Comment 2", preTodo));
        commentRepository.save(new Comment("Prepopulated Comment 3", preTodo));

        personRepository.save(new Person("person1"));
    }

    @Test
    public void testGetAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        assertFalse(todos.isEmpty());
    }

    @Test
    public void testCreateTodo() {
        Todo inputTodo = new Todo("Test Todo", false);
        inputTodo.addMetadata(TodoMetadataName.DEADLINE, "value");
        Todo createdTodo = todoService.createTodo(inputTodo);
        assertEquals("Test Todo", createdTodo.getTitle());
        assertEquals(false, createdTodo.isCompleted());
    }

    @Test
    public void testGetTodoById_Found() {
        Todo inputTodo = new Todo("Test Todo", false);
        Todo createdTodo = todoService.createTodo(inputTodo);
        Optional<Todo> fetchedOptional = todoService.getTodoById(createdTodo.getId());
        assertTrue(fetchedOptional.isPresent());
        Todo fetchedTodo = fetchedOptional.get();
        assertEquals("Test Todo", fetchedTodo.getTitle());
        assertEquals(false, fetchedTodo.isCompleted());
    }

    @Test
    public void testGetTodoById_NotFound() {
        // Use a random UUID that is not in the repository
        Optional<Todo> fetchedOptional = todoService.getTodoById(UUID.randomUUID());
        assertFalse(fetchedOptional.isPresent());
    }

    @Test
    public void testAssignTodo() {
        // Retrieve an existing todo and person from the repository
        Todo todo = todoRepository.findAll().stream().findFirst().orElseThrow();
        Person person = personRepository.findAll().stream().findFirst().orElseThrow();

        // Call the service method to assign the todo
        Optional<Todo> updatedOptional = todoService.assignTodoToPerson(todo.getId(), person.getEmail());
        assertTrue(updatedOptional.isPresent());
        Todo updatedTodo = updatedOptional.get();
        assertNotNull(updatedTodo.getAssignee());
        assertEquals(person.getEmail(), updatedTodo.getAssignee().get().getEmail());
    }
}
