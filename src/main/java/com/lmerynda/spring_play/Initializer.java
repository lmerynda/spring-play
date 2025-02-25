package com.lmerynda.spring_play;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.lmerynda.spring_play.model.Todo;
import com.lmerynda.spring_play.model.TodoMetadataName;
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
        var todo = new Todo("example", false);
        todo.addMetadata(TodoMetadataName.DEADLINE, "yesterday");
        todo.addMetadata(TodoMetadataName.ESTIMATED_TIME, "tomorrow");
        todo.addMetadata(TodoMetadataName.PRIORITY, "High");
        todoRepository.save(todo);
        log.info("Data initialization completed");
    }
}
