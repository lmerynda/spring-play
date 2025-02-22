package com.lmerynda.spring_play.controller;

import com.lmerynda.spring_play.model.Comment;
import com.lmerynda.spring_play.model.Person;
import com.lmerynda.spring_play.model.Todo;
import com.lmerynda.spring_play.repository.CommentRepository;
import com.lmerynda.spring_play.repository.PersonRepository;
import com.lmerynda.spring_play.repository.TodoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private TodoRepository todoRepository;
    private CommentRepository commentRepository;
    private PersonRepository personRepository;

    public TodoController(TodoRepository todoRepository, CommentRepository commentRepository,
            PersonRepository personRepository) {
        this.todoRepository = todoRepository;
        this.commentRepository = commentRepository;
        this.personRepository = personRepository;
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable("id") UUID id) {
        Optional<Todo> todo = todoRepository.findById(id);
        return todo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") UUID id, @RequestBody Todo todoDetails) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (!optionalTodo.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Todo todo = optionalTodo.get();
        todo.update(todoDetails);
        return ResponseEntity.ok(todoRepository.save(todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable("id") UUID id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Comment> createComment(@PathVariable("id") UUID todoId, @RequestBody Comment comment) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if (!optionalTodo.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        comment.setTodo(optionalTodo.get());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Todo> assignTodoToPerson(@PathVariable("id") UUID todoId, @RequestBody TodoAssignDto data) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if (!optionalTodo.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Todo todo = optionalTodo.get();

        Optional<Person> optionalPerson = personRepository.findById(data.email());
        if (!optionalPerson.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        todo.setAssignee(optionalPerson.get());
        todo = todoRepository.save(todo);
        return ResponseEntity.ok(todo);
    }
}
