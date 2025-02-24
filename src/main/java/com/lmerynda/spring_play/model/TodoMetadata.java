package com.lmerynda.spring_play.model;

import java.util.EnumMap;

import jakarta.persistence.Embeddable;

@Embeddable
public class TodoMetadata {
    public EnumMap<TodoMetadataName, String> metadata = new EnumMap<>(TodoMetadataName.class);

    public TodoMetadata() {
    }

    public TodoMetadata(EnumMap<TodoMetadataName, String> metadata) {
        this.metadata = metadata;
    }

    public void addMetadata(TodoMetadataName name, String value) {
        metadata.put(name, value);
    }
}
