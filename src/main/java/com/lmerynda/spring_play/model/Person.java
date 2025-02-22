package com.lmerynda.spring_play.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Person {
    @Id
    private String email;

    @OneToMany(mappedBy = "assignee")
    private List<Todo> assignedTodos;

    public Person() {
    }

    public Person(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public List<Todo> getAssignedTodos() {
        return assignedTodos;
    }
}
