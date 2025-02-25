package com.lmerynda.spring_play;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.lmerynda.spring_play.model.Todo;
import com.lmerynda.spring_play.repository.TodoRepository;

@Component
public class Initializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Initializer.class);

    private final TodoRepository todoRepository;

    public Initializer(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Initializing data");
        todoRepository.save(new Todo("example", false));
        log.info("Data initialization completed");
    }
}
