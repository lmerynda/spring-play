package com.lmerynda.spring_play.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lmerynda.spring_play.model.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, UUID> {
}
