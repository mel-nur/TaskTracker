package com.mel.TaskTracker.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mel.TaskTracker.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final ObjectMapper objectMapper;
    private final String filePath;

    public TaskRepository(@Value("${task.file.path:tasks.json}") String filePath) {
        this.filePath = filePath;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public List<Task> findAll() {
        File file = new File(filePath);
        if(!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Task>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Görevler okunamadı: " + e.getMessage(), e);
        }
    }

    public Optional<Task> findById(Long id) {
        return findAll().stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public Task save(Task task) {
        List<Task> tasks = findAll();
        if (task.getId() == null) {
            long nextId = tasks.stream().mapToLong(Task::getId).max().orElse(0L) + 1;
            task.setId(nextId);
            tasks.add(task);
        } else {
            boolean found = false;
            for(int i = 0; i < tasks.size(); i++) {
                if(tasks.get(i).getId().equals(task.getId())) {
                    tasks.set(i, task);
                    found = true;
                    break;
                }
            }
            if(!found) {
                throw new RuntimeException("Görev bulunamadı: ID " + task.getId());
            }
        }
        writeAll(tasks);
        return task;
    }

    public void deleteById(Long id) {
        List<Task> tasks = findAll();
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));
        if(!removed) {
            throw new RuntimeException("Görev bulunamadı: ID " + id);
        }
        writeAll(tasks);
    }

    private void writeAll(List<Task> tasks) {
        try {
            File file = new File(filePath);
            if(file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, tasks);
        } catch (IOException e) {
            throw new RuntimeException("Görevler kaydedilemedi: " + e.getMessage(), e);
        }
    }
    public String getFilePath() {
        return filePath;
    }
}