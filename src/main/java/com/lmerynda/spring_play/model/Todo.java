package com.lmerynda.spring_play.model;

import java.util.Optional;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Todo {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String title;
    private boolean completed;

    @JsonManagedReference
    @OneToMany(mappedBy = "todo")
    @JsonIgnore
    private List<Comment> comments;

    @ManyToOne()
    @JoinColumn(name = "assignee_email", nullable = true)
    private Person assignee;

    public Todo() {
    }

    public Todo(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    // TODO delete?
    public Todo(UUID id, String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Optional<Person> getAssignee() {
        return Optional.ofNullable(assignee);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void update(Todo todoDetails) {
        title = todoDetails.title;
        completed = todoDetails.completed;
    }

    public void setAssignee(Person assignee) {
        this.assignee = assignee;
    }
}
