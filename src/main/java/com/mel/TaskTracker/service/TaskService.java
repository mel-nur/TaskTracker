package com.mel.TaskTracker.service;

import com.mel.TaskTracker.model.Task;
import com.mel.TaskTracker.model.TaskStatus;
import com.mel.TaskTracker.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task addTask(String description) {
        if(description == null || description.isBlank()) {
            throw new IllegalArgumentException("Görev açıklaması boş olamaz.");
        }
        Task task = new Task(null, description.trim());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Görev açıklaması boş olamaz.");
        }
        Task task = findOrThrow(id);
        task.setDescription(description.trim());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        findOrThrow(id); // Varsa sil, yoksa hata fırlat
        taskRepository.deleteById(id);
    }

    public Task markInProgress(Long id) {
        return changeStatus(id, TaskStatus.IN_PROGRESS);
    }

    public Task markDone(Long id) {
        return changeStatus(id, TaskStatus.DONE);
    }

    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    private Task changeStatus(Long id, TaskStatus newStatus) {
        Task task = findOrThrow(id);
        task.setStatus(newStatus);
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    private Task findOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Görev bulunamadı: ID=" + id + ". Mevcut görevleri 'list' komutu ile görebilirsiniz."));
    }

    public List<Task> listByStatus(TaskStatus status) {
        return taskRepository.findAll().stream().filter(t -> t.getStatus() == status).toList();
    }
}
