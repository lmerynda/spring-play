package com.lmerynda.spring_play.service;

import com.lmerynda.spring_play.model.Comment;
import com.lmerynda.spring_play.model.Person;
import com.lmerynda.spring_play.model.Todo;
import com.lmerynda.spring_play.repository.CommentRepository;
import com.lmerynda.spring_play.repository.PersonRepository;
import com.lmerynda.spring_play.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;
    private final PersonRepository personRepository;

    public TodoService(TodoRepository todoRepository,
            CommentRepository commentRepository,
            PersonRepository personRepository) {
        this.todoRepository = todoRepository;
        this.commentRepository = commentRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Transactional(readOnly = true)
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Todo> getTodoById(UUID id) {
        return todoRepository.findById(id);
    }

    @Transactional
    public Optional<Todo> updateTodo(UUID id, Todo todoDetails) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty()) {
            return Optional.empty();
        }
        Todo todo = optionalTodo.get();
        todo.update(todoDetails);
        return Optional.of(todoRepository.save(todo));
    }

    @Transactional
    public boolean deleteTodo(UUID id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Optional<Comment> createComment(UUID todoId, Comment comment) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if (optionalTodo.isEmpty()) {
            return Optional.empty();
        }
        comment.setTodo(optionalTodo.get());
        return Optional.of(commentRepository.save(comment));
    }

    @Transactional
    public Optional<Todo> assignTodoToPerson(UUID todoId, String email) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if (optionalTodo.isEmpty()) {
            return Optional.empty();
        }
        Todo todo = optionalTodo.get();

        Optional<Person> optionalPerson = personRepository.findById(email);
        if (optionalPerson.isEmpty()) {
            return Optional.empty();
        }
        todo.setAssignee(optionalPerson.get());
        return Optional.of(todoRepository.save(todo));
    }

    @Transactional
    public void setPriority(UUID todoId, String priority) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if (optionalTodo.isEmpty()) {
            return;
        }
        Todo todo = optionalTodo.get();
        todo.setPriority(priority);
        todoRepository.save(todo);
    }
}
