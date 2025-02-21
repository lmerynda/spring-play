package com.lmerynda.spring_play.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String content;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    public Comment() {
    }

    public Comment(String content, Todo todo) {
        this.content = content;
        this.todo = todo;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }
}
