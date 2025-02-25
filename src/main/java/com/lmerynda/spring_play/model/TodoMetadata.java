package com.lmerynda.spring_play.model;

import java.util.EnumMap;
import java.util.Map;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;

@Embeddable
@Access(AccessType.FIELD)
public class TodoMetadata {
    String priority;
    String deadline;
    String estimatedTime;

    @Transient
    EnumMap<TodoMetadataName, String> data = new EnumMap<>(TodoMetadataName.class);

    public TodoMetadata() {
    }

    public void loadMap(Map<TodoMetadataName, String> resourceMap) {
        this.priority = resourceMap.get(TodoMetadataName.PRIORITY);
        this.deadline = resourceMap.get(TodoMetadataName.DEADLINE);
        this.estimatedTime = resourceMap.get(TodoMetadataName.ESTIMATED_TIME);
    }

    public EnumMap<TodoMetadataName, String> toEnumMap() {
        var map = new EnumMap<TodoMetadataName, String>(TodoMetadataName.class);
        map.put(TodoMetadataName.PRIORITY, priority);
        map.put(TodoMetadataName.DEADLINE, deadline);
        map.put(TodoMetadataName.ESTIMATED_TIME, estimatedTime);
        return map;
    }

    @PrePersist
    @PreUpdate
    public void syncMetadata() {
        loadMap(data);
    }

    @PostLoad
    public void loadMetadata() {
        data = toEnumMap();
    }
}
